package com.latte.server.interview.repository;

import com.latte.server.post.domain.SeniorRequest;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Donggun on 2021-09-13
 */

public interface SeniorRequestRepository extends JpaRepository<SeniorRequest, Long> {
}
