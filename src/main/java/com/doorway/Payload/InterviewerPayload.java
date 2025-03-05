package com.doorway.Payload;

import com.doorway.Model.Interviewer;
import com.doorway.Util.Base64Util;
import jakarta.validation.constraints.*;
import lombok.*;

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

    @NotBlank(message = "Role is mandatory")
    private String role;

    private String profilePicture;

    public Interviewer toEntity() {
        return Interviewer.builder()
                .name(name)
                .email(email)
                .phoneNumber(phoneNumber)
                .password(password)
                .role(role)
                .profilePicture(Base64Util.decode(profilePicture))
                .build();
    }

    public Interviewer toEntity(Interviewer interviewer) {
        interviewer.setName(name);
        interviewer.setEmail(email);
        interviewer.setPhoneNumber(phoneNumber);
        interviewer.setPassword(password);
        interviewer.setRole(role);
        interviewer.setProfilePicture(Base64Util.decode(profilePicture));
        return interviewer;
    }
}
