package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.JobsEntity;
import com.example.backendprestabanco.repositories.JobsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class JobsServiceTest {

    @InjectMocks
    private JobsService jobsService;

    @MockBean
    private JobsRepository jobsRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetAllJobs_thenReturnJobList() {
        List<JobsEntity> jobs = new ArrayList<>();
        jobs.add(new JobsEntity());

        given(jobsRepository.findAll()).willReturn(jobs);

        List<JobsEntity> result = jobsService.getAllJobs();

        assertThat(result).isNotNull().hasSize(1);
    }

    @Test
    void whenGetJobsByRut_thenReturnJobList() {
        String rut = "12345678-9";
        List<JobsEntity> jobs = new ArrayList<>();
        JobsEntity job = new JobsEntity();
        job.setRut(rut);
        jobs.add(job);

        given(jobsRepository.findByRut(rut)).willReturn(jobs);

        List<JobsEntity> result = jobsService.getJobsByRut(rut);

        assertThat(result).isNotNull().hasSize(1);
        assertThat(result.get(0).getRut()).isEqualTo(rut);
    }

    @Test
    void whenSaveJob_thenReturnSavedJob() {
        JobsEntity job = new JobsEntity();
        job.setRut("12345678-9");

        given(jobsRepository.save(job)).willReturn(job);

        JobsEntity result = jobsService.saveJob(job);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12345678-9");
    }

    @Test
    void whenUpdateJob_thenReturnUpdatedJob() {
        JobsEntity job = new JobsEntity();
        job.setRut("12345678-9");

        given(jobsRepository.save(job)).willReturn(job);

        JobsEntity result = jobsService.updateJob(job);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12345678-9");
    }

    @Test
    void whenDeleteJob_thenReturnTrueIfSuccessful() {
        String rut = "12345678-9";
        doNothing().when(jobsRepository).deleteByRut(rut);

        boolean result = jobsService.deleteJob(rut);

        assertThat(result).isTrue();
        verify(jobsRepository, times(1)).deleteByRut(rut);
    }

    @Test
    void whenDeleteJob_thenReturnFalseIfExceptionOccurs() {
        String rut = "12345678-9";
        doThrow(new RuntimeException("Error deleting job")).when(jobsRepository).deleteByRut(rut);

        boolean result = jobsService.deleteJob(rut);

        assertThat(result).isFalse();
        verify(jobsRepository, times(1)).deleteByRut(rut);
    }
}
