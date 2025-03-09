package com.doorway.Service.Interface;

import com.doorway.Model.Role;
import com.doorway.Payload.RolePayload;

import java.util.List;

public interface RoleService {
    Role getRole(Long id);
    List<Role> getAllRoles();
    Role addRole(RolePayload rolePayload);
    Role updateRole(Long id, RolePayload rolePayload);
    void deleteRole(Long id);
}