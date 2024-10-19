package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.RequestTrackingEntity;
import com.example.backendprestabanco.enums.RequestStatusEnum;
import com.example.backendprestabanco.repositories.RequestTrackingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@SpringBootTest
class RequestTrackingServiceTest {

    @MockBean
    private RequestTrackingRepository requestTrackingRepository;

    @Autowired
    private RequestTrackingService requestTrackingService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenUpdateRequestStatus_thenReturnUpdatedTracking() {
        // Arrange: Crear un objeto de prueba
        String rut = "12345678-9";
        RequestTrackingEntity tracking = new RequestTrackingEntity();
        tracking.setRut(rut);
        tracking.setStatus(RequestStatusEnum.PENDING_DOCUMENTATION);

        // Configurar el comportamiento del mock para findByRut y save
        given(requestTrackingRepository.findByRut(rut)).willReturn(tracking);
        when(requestTrackingRepository.save(tracking)).thenReturn(tracking);

        // Act: Llamar al método que se está probando
        RequestTrackingEntity updatedTracking = requestTrackingService.updateRequestStatus(
                rut, RequestStatusEnum.APPROVED, "All good");

        // Assert: Verificar el resultado esperado
        assertThat(updatedTracking).isNotNull();
        assertThat(updatedTracking.getStatus()).isEqualTo(RequestStatusEnum.APPROVED);
        assertThat(updatedTracking.getComments()).isEqualTo("All good");
    }
}
