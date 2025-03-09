package com.doorway.Payload;

import com.doorway.Model.Interviewee;
import com.doorway.Model.School;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntervieweePayload {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is invalid")
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    @NotBlank(message = "Phone number is mandatory")
    private String phoneNumber;
    @NotBlank(message = "Password is mandatory")
    private String password;
    @NotNull(message = "School is mandatory")
    private Long schoolId;

    public Interviewee toEntity(MultipartFile image, MultipartFile resume, School school) throws IOException {
        return Interviewee.builder()
                .name(name)
                .email(email)
                .dateOfBirth(dateOfBirth)
                .phoneNumber(phoneNumber)
                .password(password)
                .school(school)
                .profilePicture(image.getBytes())
                .resume(resume.getBytes())
                .build();
    }

    public Interviewee toEntity(Interviewee interviewee, MultipartFile image, MultipartFile resume, School school) throws IOException {
        interviewee.setName(name);
        interviewee.setEmail(email);
        interviewee.setDateOfBirth(dateOfBirth);
        interviewee.setPhoneNumber(phoneNumber);
        interviewee.setPassword(password);
        interviewee.setSchool(school);
        interviewee.setProfilePicture(image.getBytes());
        interviewee.setResume(resume.getBytes());
        return interviewee;
    }
}
