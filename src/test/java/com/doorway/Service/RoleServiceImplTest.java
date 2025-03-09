package com.doorway.Service;

import com.doorway.Exception.ConflictException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.Role;
import com.doorway.Payload.RolePayload;
import com.doorway.Repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getRoleById_ShouldReturnRole_WhenFound() {
        Long id = 1L;
        Role role = new Role();
        when(roleRepository.findById(id)).thenReturn(Optional.of(role));

        Role result = roleService.getRole(id);

        assertNotNull(result);
        assertEquals(role, result);
        verify(roleRepository, times(1)).findById(id);
    }

    @Test
    void getRoleById_ShouldThrowNotFoundException_WhenNotFound() {
        Long id = 1L;
        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> roleService.getRole(id));
        verify(roleRepository, times(1)).findById(id);
    }

    @Test
    void getAllRoles_ShouldReturnAllRoles() {
        List<Role> roles = List.of(new Role(), new Role());
        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> result = roleService.getAllRoles();

        assertNotNull(result);
        assertEquals(roles, result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void addRole_ShouldSaveRole_WhenValid() {
        RolePayload payload = RolePayload.builder().name("Admin").build();
        Role role = new Role();
        when(roleRepository.findByName(payload.getName())).thenReturn(null);
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role result = roleService.addRole(payload);

        assertNotNull(result);
        assertEquals(role, result);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void addRole_ShouldThrowConflictException_WhenRoleExists() {
        RolePayload payload = RolePayload.builder().name("Admin").build();
        Role existingRole = new Role();
        when(roleRepository.findByName(payload.getName())).thenReturn(existingRole);

        assertThrows(ConflictException.class, () -> roleService.addRole(payload));
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void updateRole_ShouldUpdateRole_WhenValid() {
        Long id = 1L;
        RolePayload payload = RolePayload.builder().name("Admin").build();
        Role existingRole = new Role();
        when(roleRepository.findById(id)).thenReturn(Optional.of(existingRole));
        when(roleRepository.findByName(payload.getName())).thenReturn(null);
        when(roleRepository.save(any(Role.class))).thenReturn(existingRole);

        Role result = roleService.updateRole(id, payload);

        assertNotNull(result);
        assertEquals(existingRole, result);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void updateRole_ShouldThrowConflictException_WhenRoleExists() {
        Long id = 1L;
        RolePayload payload = RolePayload.builder().name("Admin").build();
        Role existingRole = new Role();
        existingRole.setName("User"); // Ensure the name is set
        Role conflictingRole = new Role();
        conflictingRole.setName("Admin"); // Ensure the name is set
        when(roleRepository.findById(id)).thenReturn(Optional.of(existingRole));
        when(roleRepository.findByName(payload.getName())).thenReturn(conflictingRole);

        assertThrows(ConflictException.class, () -> roleService.updateRole(id, payload));
        verify(roleRepository, never()).save(any(Role.class));
    }
    @Test
    void deleteRole_ShouldDeleteRole_WhenFound() {
        Long id = 1L;
        Role role = new Role();
        when(roleRepository.findById(id)).thenReturn(Optional.of(role));
        doNothing().when(roleRepository).delete(role);

        roleService.deleteRole(id);

        verify(roleRepository, times(1)).delete(role);
    }

    @Test
    void deleteRole_ShouldThrowNotFoundException_WhenNotFound() {
        Long id = 1L;
        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> roleService.deleteRole(id));
        verify(roleRepository, times(1)).findById(id);
    }
}