package iuh.fit.se.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordResetRequest {
    private String username;
    private String email;
    @NotBlank(message = "New password is required")
    private String newPassword;
}