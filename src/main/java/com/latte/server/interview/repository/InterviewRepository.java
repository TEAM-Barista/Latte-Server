package com.latte.server.interview.repository;

import com.latte.server.interview.domain.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Donggun on 2021-08-05
 */

public interface InterviewRepository extends JpaRepository<Interview, Long>, InterviewRepositoryCustom {
}

