package iuh.fit.se.controller;

import iuh.fit.se.dto.request.UserCreationRequest;
import iuh.fit.se.dto.request.UserUpdateRequest;
import iuh.fit.se.dto.request.PasswordResetRequest;
import iuh.fit.se.entity.User;
import iuh.fit.se.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    User createUser(@RequestBody @Valid UserCreationRequest request) {
        return userService.createUser(request);
    }

    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/myInfo")
    User getMyInfo() {
        return userService.getMyInfo();
    }

    @PutMapping("/{userId}")
    User updateUser(@RequestBody UserUpdateRequest request, @PathVariable String userId) {
        return userService.updateUser(userId, request);
    }

    @PostMapping("/resetpwd")
    public void resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        userService.resetPassword(request);
    }
}
