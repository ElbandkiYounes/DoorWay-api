package com.doorway.Service;

import com.doorway.Exception.ConflictException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Role;
import com.doorway.Payload.RolePayload;
import com.doorway.Repository.RoleRepository;
import com.doorway.Service.Interface.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Add Role
    public Role addRole(RolePayload rolePayload) {
        Role existingRole = roleRepository.findByName(rolePayload.getName());
        if (existingRole != null) {
            throw new ConflictException("Role already exists");
        }
        return roleRepository.save(rolePayload.toEntity());
    }

    // Get Role
    public Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
    }

    // Get All Roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Update Role
    public Role updateRole(Long id, RolePayload rolePayload) {
        Role role = getRole(id);
        if (role == null) {
            throw new NotFoundException("Role not found");
        }
        if (roleRepository.findByName(rolePayload.getName()) != null && !role.getName().equals(rolePayload.getName())) {
            throw new ConflictException("Role already exists");
        }
        return roleRepository.save(rolePayload.toEntity(role));
    }

    // Delete Role
    public void deleteRole(Long id) {
        roleRepository.delete(getRole(id));
    }
}