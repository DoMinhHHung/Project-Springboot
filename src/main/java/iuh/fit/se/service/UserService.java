package iuh.fit.se.service;

import iuh.fit.se.dto.request.PasswordResetRequest;
import iuh.fit.se.dto.request.UserCreationRequest;
import iuh.fit.se.dto.request.UserUpdateRequest;
import iuh.fit.se.entity.Roles;
import iuh.fit.se.entity.User;
import iuh.fit.se.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(UserCreationRequest request) {
        User user = new User();
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already exists");
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already exists");
        user.setEmail(request.getEmail());

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new RuntimeException("Phone number already exists");
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGender(request.getGender());
        user.setDob(request.getDob());

        HashSet<String> roles = new HashSet<>();
        roles.add(Roles.USER.name());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public User createAdmin(UserCreationRequest request) {
        User user = new User();
        if (userRepository.existsByUsername(request.getUsername()))
            throw new RuntimeException("Username already exists");
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        if (userRepository.existsByEmail(request.getEmail()))
            throw new RuntimeException("Email already exists");
        user.setEmail(request.getEmail());

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber()))
            throw new RuntimeException("Phone number already exists");
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setGender(request.getGender());
        user.setDob(request.getDob());

        HashSet<String> roles = new HashSet<>();
        roles.add(Roles.ADMIN.name());
        roles.add(Roles.USER.name());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public User updateUser(String userId, UserUpdateRequest request) {
        User user = getUser(userId);

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        // user.setPassword(user.getPassword());
        user.setDob(request.getDob());
        user.setGender(request.getGender());

        return userRepository.save(user);
    }

    public void resetPassword(PasswordResetRequest request) {
        var user = userRepository.findByEmail(request.getEmail())
                .or(() -> userRepository.findByUsername(request.getUsername()))
                .orElseThrow(() -> new RuntimeException("User not found!"));
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
