package com.doorway.Payload;

import com.doorway.Model.Interviewer;

import com.doorway.Model.Role;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewerPayload {
    @NotBlank(message = "Name is mandatory")
    private String name;

    @Email
    @NotBlank(message = "Email is mandatory")
    private String email;

    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;

    @NotBlank(message = "Password is mandatory")
    private String password;

    @NotNull(message = "Role is mandatory")
    private Long roleId;



    public Interviewer toEntity(MultipartFile image, Role role) throws IOException {
         Interviewer interviewer = Interviewer.builder()
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .role(role)
                .build();
        if (image != null){interviewer.setProfilePicture(image.getBytes());}
         return interviewer;
    }

    public Interviewer toEntity(Interviewer interviewer, MultipartFile image, Role role) throws IOException {
        interviewer.setName(name);
        interviewer.setEmail(email);
        interviewer.setPhoneNumber(phoneNumber);
        interviewer.setPassword(password);
        if (image != null){interviewer.setProfilePicture(image.getBytes());}
        interviewer.setRole(role);
        return interviewer;
    }
}
