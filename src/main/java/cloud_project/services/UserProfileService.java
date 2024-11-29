package cloud_project.services;

import cloud_project.dtos.ChangePasswordDto;
import cloud_project.dtos.UpdateProfileDto;
import cloud_project.dtos.UserProfileDto;
import cloud_project.entity.*;
import cloud_project.exceptions.ResourceNotFoundException;
import cloud_project.filters.JwtTokenProvider;
import cloud_project.mapper.UserMapper;
import cloud_project.repository.*;
import cloud_project.utils.ImageUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final PendingProfileUpdateRepository pendingProfileUpdateRepository;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final SessionRepository sessionRepository;
    private final ImageRepository imageRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    @Transactional
    public User getUserByToken(String token) {
        String email = jwtTokenProvider.getEmailFromToken(token);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Transactional
    public boolean requestProfileUpdate(UpdateProfileDto updateProfileDto, String token) {

        User user = getUserByToken(token);

        PendingProfileUpdate pendingUpdate = new PendingProfileUpdate();

        pendingUpdate.setUser(user);
        pendingUpdate.setNewEmail(updateProfileDto.getEmail());
        pendingUpdate.setNewName(updateProfileDto.getName());
        pendingUpdate.setNewMobileNumber(updateProfileDto.getMobileNumber());
        pendingUpdate.setExpirationDate(LocalDateTime.now().plusMinutes(15));

        pendingProfileUpdateRepository.save(pendingUpdate);

        if (user.getEmail().equals(pendingUpdate.getNewEmail())) {
            String profileUpdateCode = emailService.generateResetCode();
            generateCode(user, "PROFILE_MODIFICATION", profileUpdateCode);
            emailService.sendEmail(user.getEmail(), "Profile Update Verification", "Your verification code is: " + profileUpdateCode);
            return true;
        } else {
            String emailUpdateCode = emailService.generateResetCode();
            generateCode(user, "EMAIL_VERIFICATION", emailUpdateCode);
            emailService.sendEmail(pendingUpdate.getNewEmail(), "Email Update Verification", "Your verification code is: " + emailUpdateCode);
            return false;
        }

    }

    @Transactional
    public boolean verifyAndApplyProfileUpdate(String token, String verificationCode) {

        User user = getUserByToken(token);

        PendingProfileUpdate pendingUpdate = pendingProfileUpdateRepository.findLatestByUser(user)
                .orElseThrow(() -> new RuntimeException("No pending profile update found"));

        if (user.getEmail().equals(pendingUpdate.getNewEmail())) {
            VerificationCode updateProfileCode = user.getVerificationCodes().stream()
                    .filter(c -> c.getCode().equals(verificationCode) && c.getType().equals("PROFILE_MODIFICATION"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Invalid or expired verification code"));

            if (!emailService.isCodeValid(updateProfileCode.getExpirationDate())) {
                throw new RuntimeException("Verification code has expired");
            }
            updateUserProfile(pendingUpdate, user);
            pendingProfileUpdateRepository.delete(pendingUpdate);
            return true;

        } else {

            VerificationCode updateEmailCode = user.getVerificationCodes().stream()
                    .filter(c -> c.getCode().equals(verificationCode) && c.getType().equals("EMAIL_VERIFICATION"))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Invalid or expired verification code"));

            if (!emailService.isCodeValid(updateEmailCode.getExpirationDate())) {
                throw new RuntimeException("Verification code has expired");
            }

            pendingUpdate.setNewEmailVerified(true);

            String profileUpdateCode = emailService.generateResetCode();
            generateCode(user, "PROFILE_MODIFICATION_AFTER_EMAIL", profileUpdateCode);
            emailService.sendEmail(user.getEmail(), "Profile Update Verification", "Your verification code is: " + profileUpdateCode);
            return false;
        }

    }

    public void applyChangesAfterNewEmailVerification(String token, String verificationCode) {

        User user = getUserByToken(token);

        PendingProfileUpdate pendingUpdate = pendingProfileUpdateRepository.findLatestByUser(user)
                .orElseThrow(() -> new RuntimeException("No pending profile update found"));

        VerificationCode updateProfileCode = user.getVerificationCodes().stream()
                .filter(c -> c.getCode().equals(verificationCode) && c.getType().equals("PROFILE_MODIFICATION_AFTER_EMAIL"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification code"));

        if (!emailService.isCodeValid(updateProfileCode.getExpirationDate())) {
            throw new RuntimeException("Verification code has expired");
        }
        updateUserProfile(pendingUpdate, user);
        pendingProfileUpdateRepository.delete(pendingUpdate);
    }

    @Transactional
    public void changePassword(String token, ChangePasswordDto changePasswordDto) {
        User user = getUserByToken(token);

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);

        sessionRepository.deleteAllByUser(user);

        emailService.sendEmail(user.getEmail(), "Password Changed", "Your password has been changed successfully.");

        SecurityContextHolder.clearContext();
    }

    private void updateUserProfile(PendingProfileUpdate pendingUpdate, User user) {

        user.setName(pendingUpdate.getNewName());
        user.setEmail(pendingUpdate.getNewEmail());
        user.setMobileNumber(pendingUpdate.getNewMobileNumber());
        userRepository.save(user);

    }

    public UserProfileDto getUserProfileInfo(String token) {
        return userMapper.toUserProfileDto(getUserByToken(token));
    }

    private void generateCode(User user, String verificationCodeType, String profileUpdateCode) {
        VerificationCode profileCode = new VerificationCode();
        profileCode.setCode(profileUpdateCode);
        profileCode.setType(verificationCodeType);
        profileCode.setExpirationDate(LocalDateTime.now().plusMinutes(15));
        profileCode.setUser(user);
        user.getVerificationCodes().add(profileCode);
    }


    @Transactional
    public String uploadAndSetProfileImage(String token, MultipartFile file) throws IOException {

        User user = getUserByToken(token);
        if (!ImageUtils.isValidImageExtension(file.getOriginalFilename())) {
            throw new IllegalArgumentException("Invalid image file extension");
        }
        Image image = Image.builder()
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .data(ImageUtils.compressImage(file.getBytes()))
                .user(user)
                .build();

        imageRepository.findByUser(user).ifPresent(existingImage -> imageRepository.delete(existingImage));
        imageRepository.save(image);
        user.setProfileImage(image);
        userRepository.save(user);
        return "File uploaded successfully: " + file.getOriginalFilename();
    }

    @Transactional
    public byte[] downloadImage(String token) throws IOException {
        User user = getUserByToken(token);
        Image image = imageRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Image", "user", user.getEmail()));
        return ImageUtils.decompressImage(image.getData());
    }

    @Transactional
    public void logoutUser(String token) {
        Session session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        sessionRepository.delete(session);
        sessionRepository.flush();  // Force the changes to be committed immediately

        SecurityContextHolder.clearContext();
    }

    @org.springframework.transaction.annotation.Transactional
    public void deleteUserData(String token) {
        Session session = sessionRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        User user = session.getUser();
        // Suppression des codes de vérification
        verificationCodeRepository.deleteAllByUser(user);

        // Suppression des sessions
        sessionRepository.deleteAllByUser(user);

        // Suppression de l'utilisateur
        userRepository.delete(user);

        // Nettoyage du contexte de sécurité
        SecurityContextHolder.clearContext();
    }
}