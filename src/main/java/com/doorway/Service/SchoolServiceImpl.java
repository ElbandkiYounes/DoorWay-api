package com.doorway.Service;

import com.doorway.Exception.ConflictException;
import com.doorway.Exception.NotFoundException;
import com.doorway.Model.School;
import com.doorway.Payload.SchoolPayload;
import com.doorway.Repository.SchoolRepository;
import com.doorway.Service.Interface.SchoolService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchoolServiceImpl implements SchoolService {
    private final SchoolRepository schoolRepository;

    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    //Add School
    public School addSchool(SchoolPayload schoolPayload) {
        School existingSchool = schoolRepository.findByName(schoolPayload.getName());
        if(existingSchool != null) {
            throw new ConflictException("School already exists");
        }
        return schoolRepository.save(schoolPayload.toEntity());
    }

    //Get School
    public School getSchool(Long id) {
        return schoolRepository.findById(id).orElseThrow(() -> new NotFoundException("School not found"));
    }

    //Get All Schools
    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    //Update School
    public School updateSchool(Long id, SchoolPayload schoolPayload) {
        School school = getSchool(id);
        if (school == null) {
            throw new NotFoundException("School not found");
        }
        if (schoolRepository.findByName(schoolPayload.getName()) != null && !school.getName().equals(schoolPayload.getName())) {
            throw new ConflictException("School already exists");
        }
        return schoolRepository.save(schoolPayload.toEntity(school));
    }

    //Delete School
    public void deleteSchool(Long id) {
        schoolRepository.delete(getSchool(id));
    }
}
