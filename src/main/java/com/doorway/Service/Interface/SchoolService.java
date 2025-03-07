package com.doorway.Service.Interface;


import com.doorway.Model.School;
import com.doorway.Payload.SchoolPayload;

import java.util.List;

public interface SchoolService {
     School addSchool(SchoolPayload schoolPayload);
     School getSchool(Long id);
     List<School> getAllSchools();
     School updateSchool(Long id, SchoolPayload schoolPayload);
     void deleteSchool(Long id);
}
