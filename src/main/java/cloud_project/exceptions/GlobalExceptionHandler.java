    package cloud_project.exceptions;

    import cloud_project.dtos.ErrorResponseDto;
    import org.springframework.http.HttpHeaders;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.HttpStatusCode;
    import org.springframework.http.ResponseEntity;
    import org.springframework.validation.FieldError;
    import org.springframework.web.bind.MethodArgumentNotValidException;
    import org.springframework.web.bind.annotation.ControllerAdvice;
    import org.springframework.web.bind.annotation.ExceptionHandler;
    import org.springframework.web.context.request.WebRequest;
    import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

    import java.time.LocalDateTime;
    import java.util.HashMap;
    import java.util.Map;
    
    @ControllerAdvice
    public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    
        // Gestion des erreurs de validation
        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
            Map<String, String> validationErrors = new HashMap<>();
            ex.getBindingResult().getAllErrors().forEach(error -> {
                String fieldName = ((FieldError) error).getField();
                String validationMsg = error.getDefaultMessage();
                validationErrors.put(fieldName, validationMsg);
            });
            return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
        }

        // Gestion des exceptions globales
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponseDto> handleGlobalException(Exception exception,
                                                                      WebRequest webRequest) {
            ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                    webRequest.getDescription(false),
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    exception.getMessage(),
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(errorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    
        // Gestion des exceptions de ressource non trouvée
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException exception,
                                                                                WebRequest webRequest) {
            ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                    webRequest.getDescription(false),
                    HttpStatus.NOT_FOUND,
                    exception.getMessage(),
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(errorResponseDTO, HttpStatus.NOT_FOUND);
        }
    
        // Gestion des exceptions d'utilisateur déjà existant
        @ExceptionHandler(UserAlreadyExistsException.class)
        public ResponseEntity<ErrorResponseDto> handleUserAlreadyExistsException(UserAlreadyExistsException exception,
                                                                                 WebRequest webRequest) {
            ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                    webRequest.getDescription(false),
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage(),
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
        }
    
        // Gestion des exceptions de mots de passe non concordants
        @ExceptionHandler(PasswordsDoNotMatchException.class)
        public ResponseEntity<ErrorResponseDto> handlePasswordsDoNotMatchException(PasswordsDoNotMatchException exception,
                                                                                   WebRequest webRequest) {
            ErrorResponseDto errorResponseDTO = new ErrorResponseDto(
                    webRequest.getDescription(false),
                    HttpStatus.BAD_REQUEST,
                    exception.getMessage(),
                    LocalDateTime.now()
            );
            return new ResponseEntity<>(errorResponseDTO, HttpStatus.BAD_REQUEST);
        }
    
    }
