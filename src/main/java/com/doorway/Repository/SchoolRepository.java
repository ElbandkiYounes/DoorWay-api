package com.doorway.Repository;

import com.doorway.Model.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
    School findByName(String name);
}
