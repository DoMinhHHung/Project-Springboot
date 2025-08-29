package iuh.fit.se.dto.request;

import iuh.fit.se.entity.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;
@Data
public class UserCreationRequest {
    @Column(unique = true)
    @Size(min = 3, message = "Username must be at 3 charaters")
    private String username;
    private String firstName;
    private String lastName;

    @Column(unique = true)
    @Pattern(regexp = "^[0-9]{10}$", message ="Phone number must have 10 digits")
    private String phoneNumber;
    private User.Gender gender;
    @Column(unique = true)
    @Email(message = "Invalid email address")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long, including uppercase, lowercase, numbers and special characters.")
    private String password;
    private LocalDate dob;
}
