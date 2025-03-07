package com.doorway.Service.Inteface;


import com.doorway.Model.School;
import com.doorway.Payload.SchoolPayload;

import java.util.List;

public interface SchoolService {
    public School addSchool(SchoolPayload schoolPayload);
    public School getSchool(Long id);
    public List<School> getAllSchools();
    public School updateSchool(Long id, SchoolPayload schoolPayload);
    public void deleteSchool(Long id);
}
