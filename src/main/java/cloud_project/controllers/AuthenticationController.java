package cloud_project.controllers;

import cloud_project.dtos.*;
import cloud_project.exceptions.UserAlreadyExistsException;
import cloud_project.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    private final AuthenticationService authService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto) {
        try {
            authService.registerUser(registerUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto(HttpStatus.CREATED, "User successfully registered"));
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, "An exception occurred: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> loginUser(@RequestBody @Valid LoginRequestDTO loginRequest) {
        try {
            LoginResponseDto response = authService.loginUser(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponseDto("Failure", null));
        }
    }

    @PostMapping("/password-reset/initiate")
    public ResponseEntity<String> initiatePasswordReset(@RequestBody @Valid ForgotPasswordDto forgotPasswordDto) {
        try {
            authService.initiatePasswordReset(forgotPasswordDto);
            return ResponseEntity.ok("Password reset code sent");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An exception occurred: " + e.getMessage());
        }
    }

    @PostMapping("/password-reset/verify")
    public ResponseEntity<String> verifyResetCode(@RequestBody @Valid ResetCodeDto resetCodeDto) {
        try {
            authService.verifyResetCode(resetCodeDto);
            return ResponseEntity.ok("Reset code verified");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset code");
        }
    }

    @PostMapping("/password-update")
    public ResponseEntity<String> updatePassword(@RequestBody @Valid NewPasswordDto newPasswordDto) {
        try {
            authService.updatePassword(newPasswordDto);
            return ResponseEntity.ok("Password updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An exception occurred: " + e.getMessage());
        }
    }

    @PostMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@Validated @RequestBody EmailVerificationDto emailVerificationDto) {
        authService.verifyEmail(emailVerificationDto);
        return ResponseEntity.ok("Email successfully verified");
    }

    @PostMapping("/resend-verification-code")
    public ResponseEntity<String> resendVerificationCode(@RequestBody ResendVerificationCodeDto resendVerificationCodeDto) {
        authService.resendVerificationCode(resendVerificationCodeDto);
        return ResponseEntity.ok("Verification code resent successfully");
    }
}
