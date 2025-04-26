package com.doorway.Controller;

import com.doorway.Model.Interviewer;
import com.doorway.Payload.LoginPayload;
import com.doorway.Payload.LoginResponse;
import com.doorway.Service.AuthenticationService;
import com.doorway.Service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping()
@RestController
public class AuthenticationController {
    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginPayload payload) {
        Interviewer authenticatedUser = authenticationService.authenticate(payload);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        // Get the role name if available
        String roleName = authenticatedUser.getRole() != null ? authenticatedUser.getRole().getName() : null;

        LoginResponse loginResponse = LoginResponse.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .role(roleName)
                .build();

        return ResponseEntity.ok(loginResponse);
    }

    @GetMapping("/api/me")
    public ResponseEntity<Interviewer> getCurrentUser(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(jwtService.getMe(token));
    }
}
