package com.akatsuki.pioms.log.repository;

import com.akatsuki.pioms.log.aggregate.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
}
