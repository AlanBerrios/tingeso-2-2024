package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.JobsEntity;
import com.example.backendprestabanco.repositories.JobsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class JobsServiceTest {

    @MockBean
    private JobsRepository jobsRepository;

    @Autowired
    private JobsService jobsService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetAllJobs_thenReturnJobList() {
        List<JobsEntity> jobs = List.of(new JobsEntity());
        given(jobsRepository.findAll()).willReturn(jobs);

        List<JobsEntity> result = jobsService.getAllJobs();

        assertThat(result).hasSize(1);
    }
}
