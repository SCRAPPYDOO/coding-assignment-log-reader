package com.log.repository;

import com.log.model.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LogRepository extends JpaRepository<LogEntity, String> {

    Optional<LogEntity> findFirstById(String id);
}
