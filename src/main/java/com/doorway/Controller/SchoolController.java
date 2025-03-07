package com.doorway.Controller;

import com.doorway.Model.School;
import com.doorway.Payload.SchoolPayload;
import com.doorway.Service.Inteface.SchoolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {
    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping
    public ResponseEntity<School> addSchool(@Valid @RequestBody SchoolPayload schoolPayload) {
        School newSchool = schoolService.addSchool(schoolPayload);
        return ResponseEntity.ok(newSchool);
    }

    @GetMapping("/{id}")
    public ResponseEntity<School> getSchool(@PathVariable Long id) {
        School school = schoolService.getSchool(id);
        return ResponseEntity.ok(school);
    }

    @GetMapping
    public ResponseEntity<List<School>> getAllSchools() {
        List<School> schools = schoolService.getAllSchools();
        return ResponseEntity.ok(schools);
    }

    @PutMapping("/{id}")
    public ResponseEntity<School> updateSchool(@PathVariable Long id, @Valid @RequestBody SchoolPayload schoolPayload) {
        School updatedSchool = schoolService.updateSchool(id, schoolPayload);
        return ResponseEntity.ok(updatedSchool);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.noContent().build();
    }
}