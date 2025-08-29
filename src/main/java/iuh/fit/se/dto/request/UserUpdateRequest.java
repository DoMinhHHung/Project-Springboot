package iuh.fit.se.dto.request;

import iuh.fit.se.entity.User;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private String password;
    private LocalDate dob;
    private User.Gender gender;
}
