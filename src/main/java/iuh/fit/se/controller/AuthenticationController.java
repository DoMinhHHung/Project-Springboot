package iuh.fit.se.controller;

import iuh.fit.se.dto.request.AuthenticationRequest;
import iuh.fit.se.dto.response.AuthenticationResponse;
import iuh.fit.se.dto.request.IntrospectRequest;
import iuh.fit.se.dto.response.IntrospectResponse;
import iuh.fit.se.dto.request.UserCreationRequest;
import iuh.fit.se.entity.User;
import iuh.fit.se.service.AuthenticationService;
import iuh.fit.se.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserCreationRequest request) {
        var result = userService.createUser(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/create-admin")
    public ResponseEntity<User> createAdmin(@RequestBody @Valid UserCreationRequest request) {
        var result = userService.createAdmin(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/introspect")
    public ResponseEntity<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
        var result = authenticationService.introspect(request.getToken());
        return ResponseEntity.ok(result);
    }
}
