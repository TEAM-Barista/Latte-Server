package com.latte.server.Interview.repository;

import com.latte.server.interview.repository.InterviewBookmarkRepository;
import com.latte.server.interview.repository.InterviewLikeRepository;
import com.latte.server.interview.repository.InterviewRepository;
import com.latte.server.interview.service.InterviewService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Donggun on 2021-08-05
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class InterviewTagRepositoryTest {

    @Autowired
    InterviewService interviewService;

    @Autowired
    InterviewRepository interviewRepository;
    @Autowired
    InterviewLikeRepository interviewLikeRepository;
    @Autowired
    InterviewBookmarkRepository interviewBookMarkRepository;
    @PersistenceContext
    EntityManager em;



}