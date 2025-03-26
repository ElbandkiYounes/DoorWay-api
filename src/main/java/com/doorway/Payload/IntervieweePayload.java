package com.doorway.Payload;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import com.doorway.Model.Interviewee;
import com.doorway.Model.School;

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
    @Email(message = "Invalid email format")
    private String email;

    @Past(message = "Date of birth must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfBirth;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^(\\+\\d{1,3})?\\d{10}$", message = "Invalid phone number format. Expected format: +<country_code>XXXXXXXXXX or XXXXXXXXXX.")
    private String phoneNumber;

    @NotNull(message = "School is mandatory")
    private Long schoolId;

    public Interviewee toEntity(MultipartFile image, MultipartFile resume, School school) throws IOException {
        return Interviewee.builder()
                .name(name)
                .email(email)
                .dateOfBirth(dateOfBirth)
                .phoneNumber(phoneNumber)
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
        interviewee.setSchool(school);
        interviewee.setProfilePicture(image.getBytes());
        interviewee.setResume(resume.getBytes());
        return interviewee;
    }
}
