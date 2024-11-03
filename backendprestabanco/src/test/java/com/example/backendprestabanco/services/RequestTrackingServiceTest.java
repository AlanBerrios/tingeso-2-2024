package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.RequestTrackingEntity;
import com.example.backendprestabanco.enums.RequestStatusEnum;
import com.example.backendprestabanco.repositories.RequestTrackingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
class RequestTrackingServiceTest {

    @InjectMocks
    private RequestTrackingService requestTrackingService;

    @MockBean
    private RequestTrackingRepository requestTrackingRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetRequestTracking_thenReturnTrackingList() {
        List<RequestTrackingEntity> trackingList = new ArrayList<>();
        trackingList.add(new RequestTrackingEntity());

        given(requestTrackingRepository.findAll()).willReturn(trackingList);

        List<RequestTrackingEntity> result = requestTrackingService.getRequestTracking();

        assertThat(result).isNotNull().hasSize(1);
    }

    @Test
    void whenSaveRequestTracking_thenReturnSavedTracking() {
        RequestTrackingEntity tracking = new RequestTrackingEntity();
        tracking.setRut("12345678-9");
        tracking.setStatus(RequestStatusEnum.PENDING_DOCUMENTATION);

        given(requestTrackingRepository.save(tracking)).willReturn(tracking);

        RequestTrackingEntity result = requestTrackingService.saveRequestTracking(tracking);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo("12345678-9");
        assertThat(result.getStatus()).isEqualTo(RequestStatusEnum.PENDING_DOCUMENTATION);
    }

    @Test
    void whenGetRequestTrackingByRut_thenReturnTracking() {
        String rut = "12345678-9";
        RequestTrackingEntity tracking = new RequestTrackingEntity();
        tracking.setRut(rut);
        tracking.setStatus(RequestStatusEnum.PENDING_DOCUMENTATION);

        given(requestTrackingRepository.findByRut(rut)).willReturn(tracking);

        RequestTrackingEntity result = requestTrackingService.getRequestTrackingByRut(rut);

        assertThat(result).isNotNull();
        assertThat(result.getRut()).isEqualTo(rut);
    }

    @Test
    void whenUpdateRequestStatus_thenReturnUpdatedTracking() {
        String rut = "12345678-9";
        RequestTrackingEntity tracking = new RequestTrackingEntity();
        tracking.setRut(rut);
        tracking.setStatus(RequestStatusEnum.PENDING_DOCUMENTATION);

        given(requestTrackingRepository.findByRut(rut)).willReturn(tracking);
        when(requestTrackingRepository.save(tracking)).thenReturn(tracking);

        RequestTrackingEntity updatedTracking = requestTrackingService.updateRequestStatus(
                rut, RequestStatusEnum.APPROVED, "All good");

        assertThat(updatedTracking).isNotNull();
        assertThat(updatedTracking.getStatus()).isEqualTo(RequestStatusEnum.APPROVED);
        assertThat(updatedTracking.getComments()).isEqualTo("All good");
    }

    @Test
    void whenDeleteRequestTracking_thenReturnTrue() throws Exception {
        String rut = "12345678-9";
        doNothing().when(requestTrackingRepository).deleteByRut(rut);

        boolean result = requestTrackingService.deleteRequestTracking(rut);

        assertThat(result).isTrue();
        verify(requestTrackingRepository, times(1)).deleteByRut(rut);
    }

    @Test
    void whenDeleteRequestTracking_thenThrowExceptionIfNotFound() {
        String rut = "99999999-9";
        doThrow(new RuntimeException("Request tracking not found")).when(requestTrackingRepository).deleteByRut(rut);

        assertThatThrownBy(() -> requestTrackingService.deleteRequestTracking(rut))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("Request tracking not found");

        verify(requestTrackingRepository, times(1)).deleteByRut(rut);
    }
}
