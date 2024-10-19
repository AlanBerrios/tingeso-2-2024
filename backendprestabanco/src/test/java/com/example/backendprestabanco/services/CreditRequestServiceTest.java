package com.example.backendprestabanco.services;

import com.example.backendprestabanco.entities.CreditRequestEntity;
import com.example.backendprestabanco.repositories.CreditRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CreditRequestServiceTest {

    @MockBean
    private CreditRequestRepository creditRequestRepository;

    @Autowired
    private CreditRequestService creditRequestService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetCreditRequests_thenReturnRequestList() {
        ArrayList<CreditRequestEntity> requests = new ArrayList<>();
        requests.add(new CreditRequestEntity());

        given(creditRequestRepository.findAll()).willReturn(requests);

        ArrayList<CreditRequestEntity> result = creditRequestService.getCreditRequests();

        assertThat(result).hasSize(1);
    }

    @Test
    void whenSaveCreditRequest_thenReturnSavedRequest() {
        CreditRequestEntity request = new CreditRequestEntity();
        given(creditRequestRepository.save(request)).willReturn(request);

        CreditRequestEntity result = creditRequestService.saveCreditRequest(request);

        assertThat(result).isNotNull();
    }

    @Test
    void whenDeleteCreditRequest_thenReturnTrue() throws Exception {
        String rut = "12345678-9";
        doNothing().when(creditRequestRepository).deleteByRut(rut);

        boolean result = creditRequestService.deleteCreditRequest(rut);

        assertThat(result).isTrue();
        verify(creditRequestRepository, times(1)).deleteByRut(rut);
    }
}
