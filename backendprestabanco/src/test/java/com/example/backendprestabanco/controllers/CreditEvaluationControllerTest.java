package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.CreditEvaluationEntity;
import com.example.backendprestabanco.services.CreditEvaluationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreditEvaluationController.class)
public class CreditEvaluationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreditEvaluationService creditEvaluationService;

    @Test
    public void listCreditEvaluations_ShouldReturnCreditEvaluations() throws Exception {
        CreditEvaluationEntity evaluation1 = new CreditEvaluationEntity(1L, "12345678-9", 0.3, "Good", 5, 0.2, true, "Approved", null);
        CreditEvaluationEntity evaluation2 = new CreditEvaluationEntity(2L, "98765432-1", 0.4, "Bad", 2, 0.3, false, "Rejected", null);

        // Crear una lista de tipo ArrayList en lugar de Arrays.asList
        List<CreditEvaluationEntity> evaluations = new ArrayList<>(Arrays.asList(evaluation1, evaluation2));

        // Asegurarse de que el mock está correctamente configurado
        given(creditEvaluationService.getCreditEvaluations()).willReturn((ArrayList<CreditEvaluationEntity>) evaluations);

        mockMvc.perform(get("/api/v1/credit-evaluations/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].rut", is("12345678-9")))
                .andExpect(jsonPath("$[1].rut", is("98765432-1")));
    }


    @Test
    public void simulateCredit_ShouldReturnMonthlyPayment() throws Exception {
        given(creditEvaluationService.simulateCredit(100000, 5, 30)).willReturn(536.82);

        mockMvc.perform(get("/api/v1/credit-evaluations/simulate")
                        .param("principal", "100000")
                        .param("annualInterestRate", "5")
                        .param("termInYears", "30"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(536.82)));
    }

    @Test
    public void getCreditEvaluationByRut_ShouldReturnEvaluation() throws Exception {
        CreditEvaluationEntity evaluation = new CreditEvaluationEntity(1L, "12345678-9", 0.3, "Good", 5, 0.2, true, "Approved", null);
        given(creditEvaluationService.getCreditEvaluationByRut("12345678-9")).willReturn(evaluation);

        mockMvc.perform(get("/api/v1/credit-evaluations/{rut}", "12345678-9"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rut", is("12345678-9")));
    }

    @Test
    public void feeIncomeRelation_ShouldReturnRelation() throws Exception {
        given(creditEvaluationService.feeIncomeRelation(5000, 1500)).willReturn(33.33);

        mockMvc.perform(get("/api/v1/credit-evaluations/fee-income-relation")
                        .param("clientMonthlyIncome", "5000")
                        .param("loanMonthlyPayment", "1500"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(33.33)));
    }

    @Test
    public void checkCreditHistory_ShouldReturnBoolean() throws Exception {
        given(creditEvaluationService.clientCreditHistoryIsGood("12345678-9")).willReturn(true);

        mockMvc.perform(get("/api/v1/credit-evaluations/credit-history/{rut}", "12345678-9"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(true)));
    }

    @Test
    public void evaluateSavingsCapacity_ShouldReturnEvaluationResult() throws Exception {
        given(creditEvaluationService.evaluateSavingsCapacity("12345678-9", 100000)).willReturn("Solid");

        mockMvc.perform(get("/api/v1/credit-evaluations/savings-capacity/{rut}", "12345678-9")
                        .param("loanAmount", "100000"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.result", is("Solid")));
    }

}
