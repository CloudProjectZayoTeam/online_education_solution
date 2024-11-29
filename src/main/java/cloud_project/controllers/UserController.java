package cloud_project.controllers;

import cloud_project.dtos.ChangePasswordDto;
import cloud_project.dtos.EmailVerifyUpdateDto;
import cloud_project.dtos.UpdateProfileDto;
import cloud_project.dtos.UserProfileDto;
import cloud_project.exceptions.PasswordsDoNotMatchException;
import cloud_project.services.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService userProfileService;

    @PostMapping("/verify-update")
    public ResponseEntity<String> verifyAndApplyProfileUpdate(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid EmailVerifyUpdateDto emailVerifyUpdateDto) {

        try {
            String jwtToken = token.replace("Bearer ", "");
            if (userProfileService.verifyAndApplyProfileUpdate(jwtToken, emailVerifyUpdateDto.getVerificationCode()))
                return ResponseEntity.ok("Profile updated successfully.");
            else
                return ResponseEntity.ok("Now your new email had verified , please verify the profile update by using a code sent to your current email");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An exception occurred: " + e.getMessage());
        }
    }

    @PostMapping("/verify-update-with-email")
    public ResponseEntity<String> verifyAndApplyProfileUpdateWithEmail(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid EmailVerifyUpdateDto emailVerifyUpdateDto) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            userProfileService.applyChangesAfterNewEmailVerification(jwtToken, emailVerifyUpdateDto.getVerificationCode());
            return ResponseEntity.ok("Profile updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An exception occurred: " + e.getMessage());
            }
    }

    @PostMapping("/request-update")
    public ResponseEntity<String> requestProfileUpdate(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid UpdateProfileDto updateProfileDto) {

        try {
            String jwtToken = token.replace("Bearer ", "");
            if (userProfileService.requestProfileUpdate(updateProfileDto, jwtToken))
                return ResponseEntity.ok("PROFILE_MODIFICATION");
            else
                return ResponseEntity.ok("EMAIL_VERIFICATION");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An exception occurred: " + e.getMessage());
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid ChangePasswordDto changePasswordDto) {

        try {
            String jwtToken = token.replace("Bearer ", "");
            userProfileService.changePassword(jwtToken, changePasswordDto);
            return ResponseEntity.ok("Password changed successfully. An email has been sent to you for this infos !");
        } catch (PasswordsDoNotMatchException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An exception occurred: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<UserProfileDto> getUserProfileInfo(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            UserProfileDto userProfile = userProfileService.getUserProfileInfo(jwtToken);
            return ResponseEntity.ok(userProfile);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestHeader("Authorization") String token,
                                              @RequestParam("file") MultipartFile file) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            String response = userProfileService.uploadAndSetProfileImage(jwtToken, file);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/download-image")
    public ResponseEntity<byte[]> downloadImage(@RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            byte[] image = userProfileService.downloadImage(jwtToken);
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.substring(7); // Suppression de "Bearer " pour obtenir le jeton
        userProfileService.logoutUser(token);
        return ResponseEntity.ok("User logged out successfully");
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteUserData(@RequestHeader("Authorization") String authorizationHeader) {
        // Extraction du token à partir du header Authorization
        String token = authorizationHeader.replace("Bearer ", "");

        // Appel au service pour supprimer les données utilisateur
        userProfileService.deleteUserData(token);

        return ResponseEntity.ok("User data deleted successfully");
    }

}
