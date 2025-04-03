package com.doorway.Service;


import com.doorway.Model.Interviewer;
import com.doorway.Payload.LoginPayload;
import com.doorway.Repository.InterviewerRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final InterviewerRepository userRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            InterviewerRepository userRepository,
            AuthenticationManager authenticationManager
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
    }

    public Interviewer authenticate(LoginPayload payload) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        payload.getEmail(),
                        payload.getPassword()
                )
        );

        return userRepository.findByEmail(payload.getEmail())
                .orElseThrow();
    }
}
