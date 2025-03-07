package com.doorway.Service;

import com.doorway.Exception.ConflictException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.School;
import com.doorway.Payload.SchoolPayload;
import com.doorway.Repository.SchoolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchoolServiceTest {

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private SchoolServiceImpl schoolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addSchool_ShouldAddSchool_WhenSchoolDoesNotExist() {
        SchoolPayload schoolPayload = SchoolPayload.builder().name("Test School").build();
        School school = schoolPayload.toEntity();

        when(schoolRepository.findByName("Test School")).thenReturn(null);
        when(schoolRepository.save(any(School.class))).thenReturn(school);

        School result = schoolService.addSchool(schoolPayload);

        assertNotNull(result);
        assertEquals("Test School", result.getName());
        verify(schoolRepository, times(1)).findByName("Test School");
        verify(schoolRepository, times(1)).save(any(School.class));
    }

    @Test
    void addSchool_ShouldThrowConflictException_WhenSchoolExists() {
        SchoolPayload schoolPayload = SchoolPayload.builder().name("Test School").build();
        School existingSchool = schoolPayload.toEntity();

        when(schoolRepository.findByName("Test School")).thenReturn(existingSchool);

        assertThrows(ConflictException.class, () -> schoolService.addSchool(schoolPayload));
        verify(schoolRepository, times(1)).findByName("Test School");
        verify(schoolRepository, never()).save(any(School.class));
    }

    @Test
    void getSchool_ShouldReturnSchool_WhenSchoolExists() {
        School school = School.builder().id(1L).name("Test School").build();

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school));

        School result = schoolService.getSchool(1L);

        assertNotNull(result);
        assertEquals("Test School", result.getName());
        verify(schoolRepository, times(1)).findById(1L);
    }

    @Test
    void getSchool_ShouldThrowNotFoundException_WhenSchoolDoesNotExist() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> schoolService.getSchool(1L));
        verify(schoolRepository, times(1)).findById(1L);
    }

    @Test
    void updateSchool_ShouldUpdateSchool_WhenSchoolExists() {
        SchoolPayload schoolPayload = SchoolPayload.builder().name("Updated School").build();
        School existingSchool = School.builder().id(1L).name("Test School").build();

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(existingSchool));
        when(schoolRepository.findByName("Updated School")).thenReturn(null);
        when(schoolRepository.save(any(School.class))).thenReturn(existingSchool);

        School result = schoolService.updateSchool(1L, schoolPayload);

        assertNotNull(result);
        assertEquals("Updated School", result.getName());
        verify(schoolRepository, times(1)).findById(1L);
        verify(schoolRepository, times(1)).findByName("Updated School");
        verify(schoolRepository, times(1)).save(any(School.class));
    }

    @Test
    void updateSchool_ShouldThrowConflictException_WhenSchoolNameExists() {
        SchoolPayload schoolPayload = SchoolPayload.builder().name("Updated School").build();
        School existingSchool = School.builder().id(1L).name("Test School").build();
        School conflictingSchool = School.builder().id(2L).name("Updated School").build();

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(existingSchool));
        when(schoolRepository.findByName("Updated School")).thenReturn(conflictingSchool);

        assertThrows(ConflictException.class, () -> schoolService.updateSchool(1L, schoolPayload));
        verify(schoolRepository, times(1)).findById(1L);
        verify(schoolRepository, times(1)).findByName("Updated School");
        verify(schoolRepository, never()).save(any(School.class));
    }

    @Test
    void deleteSchool_ShouldDeleteSchool_WhenSchoolExists() {
        School existingSchool = School.builder().id(1L).name("Test School").build();

        when(schoolRepository.findById(1L)).thenReturn(Optional.of(existingSchool));

        schoolService.deleteSchool(1L);

        verify(schoolRepository, times(1)).findById(1L);
        verify(schoolRepository, times(1)).delete(existingSchool);
    }

    @Test
    void deleteSchool_ShouldThrowNotFoundException_WhenSchoolDoesNotExist() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> schoolService.deleteSchool(1L));
        verify(schoolRepository, times(1)).findById(1L);
        verify(schoolRepository, never()).delete(any(School.class));
    }
}