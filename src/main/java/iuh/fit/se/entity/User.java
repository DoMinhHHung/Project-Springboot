package iuh.fit.se.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(unique = true)
    private String username;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String phoneNumber;
    @Column(unique = true)
    private String email;
    private String password;
    private LocalDate dob;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> roles;

    public enum Gender {
        MALE,
        FEMALE
    }
}
