package com.doorway.Conf;

import com.doorway.Model.Interviewer;
import com.doorway.Model.Role;
import com.doorway.Repository.InterviewerRepository;
import com.doorway.Repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(
            InterviewerRepository interviewerRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            // Check if admin user already exists
            Optional<Interviewer> existingAdmin = interviewerRepository.findByEmail("admin@doorway.com");

            if (existingAdmin.isEmpty()) {
                // Find or create admin role
                Role adminRole = roleRepository.findByName("ADMIN");
                if (adminRole == null) {
                    adminRole = Role.builder()
                            .name("ADMIN")
                            .build();

                    roleRepository.save(adminRole);
                }

                // Create admin user
                Interviewer admin = Interviewer.builder()
                        .name("Admin User")
                        .email("admin@doorway.com")
                        .password(passwordEncoder.encode("password"))
                        .phoneNumber("0000000000")  // You might want to set a different phone number
                        .role(adminRole)
                        .build();

                interviewerRepository.save(admin);

                System.out.println("Admin user created successfully");
            } else {
                System.out.println("Admin user already exists");
            }
        };
    }
}