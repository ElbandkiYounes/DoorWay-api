package com.doorway.Payload;

import com.doorway.Model.School;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class SchoolPayload {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 1, max = 255)
    private String name;

    public School toEntity() {
        return School.builder()
                .name(name)
                .build();
    }

    public School toEntity(School school) {
        school.setName(name);
        return school;
    }
}
