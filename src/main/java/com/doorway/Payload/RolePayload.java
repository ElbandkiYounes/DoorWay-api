package com.doorway.Payload;

import com.doorway.Model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePayload {
    @NotBlank(message = "Name is mandatory")
    private String name;

    public Role toEntity() {
        return Role.builder()
                .name(this.name)
                .build();
    }

    public Role toEntity(Role existingRole) {
        existingRole.setName(this.name);
        return existingRole;
    }
}