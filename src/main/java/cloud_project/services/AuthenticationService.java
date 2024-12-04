package cloud_project.services;

import cloud_project.constants.ApplicationConstants;
import cloud_project.dtos.*;
import cloud_project.entity.Session;
import cloud_project.entity.User;
import cloud_project.entity.VerificationCode;
import cloud_project.exceptions.ResourceNotFoundException;
import cloud_project.exceptions.UserAlreadyExistsException;
import cloud_project.mapper.UserMapper;
import cloud_project.repository.SessionRepository;
import cloud_project.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Environment env;
    private final EmailService emailService;
    private final SessionRepository sessionRepository;
    private final UserMapper userMapper;

    @Transactional
    public User registerUser(RegisterUserDto registerUserDto) {
        userRepository.findByEmail(registerUserDto.getEmail())
                .ifPresent(existingUser -> {
                    throw new UserAlreadyExistsException("User already exists with email: " + registerUserDto.getEmail());
                });
        String verificationCode = emailService.generateResetCode();
        emailService.sendEmail(registerUserDto.getEmail(), "Verification Code", "Your verification code is: " + verificationCode);
        User user = userMapper.toEntity(registerUserDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        VerificationCode codeEntity = new VerificationCode();
        codeEntity.setCode(verificationCode);
        codeEntity.setType("EMAIL_VERIFICATION");
        codeEntity.setExpirationDate(LocalDateTime.now().plusMinutes(15));
        codeEntity.setUser(user);

        if (user.getVerificationCodes() == null) {
            user.setVerificationCodes(new ArrayList<>());
        }
        user.getVerificationCodes().add(codeEntity);
        return userRepository.save(user);
    }

    public LoginResponseDto loginUser(LoginRequestDTO loginRequest) {
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found: " + loginRequest.getEmail()));

            if (!user.isEmailVerified()) {
                throw new RuntimeException("Email not verified. Please verify your email before logging in.");
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                String jwt = Jwts.builder()
                        .setIssuer("CloudProject")
                        .setSubject(authentication.getName())
                        .claim("authorities", authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.joining(",")))
                        .setIssuedAt(new Timestamp(System.currentTimeMillis()))
                        .setExpiration(new Timestamp(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())) // 1 jour
                        .signWith(secretKey, SignatureAlgorithm.HS256)
                        .compact();

                Session session = new Session();
                session.setToken(jwt);
                session.setUser(user);
                session.setExpirationDate(new Timestamp(LocalDateTime.now().plusDays(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())); // 1 jour
                sessionRepository.save(session);

                return new LoginResponseDto("Success", jwt);
            } else {
                throw new RuntimeException("Authentication failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    public void initiatePasswordReset(ForgotPasswordDto forgotPasswordDto) {
        User user = userRepository.findByEmail(forgotPasswordDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", forgotPasswordDto.getEmail()));

        String resetCode = emailService.generateResetCode();
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setCode(resetCode);
        verificationCode.setType("PASSWORD_RESET");
        verificationCode.setExpirationDate(LocalDateTime.now().plusMinutes(15));
        verificationCode.setUser(user);
        user.getVerificationCodes().add(verificationCode);
        userRepository.save(user);

        emailService.sendEmail(user.getEmail(), "Password Reset Code", "Your password reset code is: " + resetCode);
    }

    public void verifyResetCode(ResetCodeDto resetCodeDto) {
        User user = userRepository.findByEmail(resetCodeDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", resetCodeDto.getEmail()));

        VerificationCode verificationCode = user.getVerificationCodes().stream()
                .filter(code -> code.getCode().equals(resetCodeDto.getCode()) && code.getType().equals("PASSWORD_RESET"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid reset code"));

        if (!emailService.isCodeValid(verificationCode.getExpirationDate())) {
            throw new RuntimeException("Reset code has expired");
        }
        verificationCode.setVerified(true);
        userRepository.save(user);
    }


    @Transactional
    public void updatePassword(NewPasswordDto newPasswordDto) {
        User user = userRepository.findByEmail(newPasswordDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", newPasswordDto.getEmail()));

        VerificationCode verificationCode = user.getVerificationCodes().stream()
                .filter(code -> code.getType().equals("PASSWORD_RESET") && code.isVerified() && code.getExpirationDate().isAfter(LocalDateTime.now()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Password reset code is not verified or has expired"));

        if (!newPasswordDto.getPassword().equals(newPasswordDto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(newPasswordDto.getPassword()));
        userRepository.save(user);

        sessionRepository.deleteAllByUser(user);

        SecurityContextHolder.clearContext();
    }


    public void verifyEmail(EmailVerificationDto emailVerificationDto) {
        User user = userRepository.findByEmail(emailVerificationDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", emailVerificationDto.getEmail()));

        VerificationCode verificationCode = user.getVerificationCodes().stream()
                .filter(code -> code.getCode().equals(emailVerificationDto.getVerificationCode())
                        && code.getType().equals("EMAIL_VERIFICATION"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification code"));

        if (verificationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code has expired");
        }

        user.setEmailVerified(true);
        userRepository.save(user);
    }

    public void resendVerificationCode(ResendVerificationCodeDto resendVerificationCodeDto) {
        User user = userRepository.findByEmail(resendVerificationCodeDto.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", resendVerificationCodeDto.getEmail()));

        if (user.isEmailVerified()) {
            throw new RuntimeException("Email is already verified");
        }

        String newCode = emailService.generateResetCode();
        emailService.sendEmail(user.getEmail(), "New Verification Code", "Your new verification code is: " + newCode);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setCode(newCode);
        verificationCode.setType("EMAIL_VERIFICATION");
        verificationCode.setExpirationDate(LocalDateTime.now().plusMinutes(15));
        verificationCode.setUser(user);

        user.getVerificationCodes().add(verificationCode);
        userRepository.save(user);
    }
}
