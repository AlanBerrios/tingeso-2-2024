package com.example.backendprestabanco.services;

import com.example.backendprestabanco.repositories.ClientRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.within;
import static org.mockito.BDDMockito.given;

// @SpringBootTest para cargar el contexto de Spring
@SpringBootTest
@ActiveProfiles("test")
class LoanCostServiceTest {

    // Inyectamos el servicio para probar sus métodos sin necesidad de mocks para los servicios
    @Autowired
    private LoanCostService loanCostService;

    // Test para calcular el pago mensual
    @Test
    public void whenCalculateMonthlyPayment_thenCorrectResult() {
        // Given
        double principal = 100000000; // Monto del préstamo
        double annualInterestRate = 4.5; // Tasa de interés anual
        int termInYears = 20; // Plazo en años

        // When
        double monthlyPayment = loanCostService.calculateMonthlyPayment(principal, annualInterestRate, termInYears);

        // Then
        assertThat(monthlyPayment).isEqualTo(632649.0); // Ajustar el valor esperado a 682649 si este es el valor correcto
    }

    // Test para calcular el seguro
    @Test
    public void whenCalculateInsurance_thenCorrectResult() {
        // Given
        double principal = 100000000;
        double lifeInsuranceRate = 0.0003;

        // When
        double insurance = loanCostService.calculateInsurance(principal, lifeInsuranceRate);

        // Then
        assertThat(insurance).isEqualTo(30000.0);
    }

    // Test para calcular la comisión administrativa
    @Test
    public void whenCalculateAdminFee_thenCorrectResult() {
        // Given
        double principal = 100000000.0;
        double adminFeeRate = 0.01; // 1%

        // When
        double adminFee = loanCostService.calculateAdminFee(principal, adminFeeRate);

        // Then
        assertThat(adminFee).isEqualTo(1000000.0); // 1% of the principal
    }

    // Test para calcular el costo mensual total
    @Test
    public void whenCalculateTotalMonthlyCost_thenCorrectResult() {
        // Given
        double principal = 100000000.0;
        double annualInterestRate = 4.5;
        int termInYears = 20;
        double lifeInsuranceRate = 0.0003;
        double fireInsuranceMont = 20000.0;

        // When
        double totalMonthlyCost = loanCostService.calculateMonthlyPayment(principal, annualInterestRate, termInYears, lifeInsuranceRate, fireInsuranceMont);

        // Then
        assertThat(totalMonthlyCost).isCloseTo(682649.12, within(0.3)); // Permite una diferencia mayor
    }

    // Test para calcular el pago total del préstamo
    @Test
    public void whenCalculateTotalLoanPayment_thenCorrectResult() {
        // Given
        double principal = 100000000.0;
        int termInYears = 20;
        double adminFeeRate = 0.01; // 1%
        double monthlyPayment = 682649; // Cuota mensual calculada anteriormente

        // When
        double totalLoanPayment = loanCostService.calculateTotalLoanPayment(principal, termInYears, adminFeeRate, monthlyPayment);

        // Then
        assertThat(totalLoanPayment).isEqualTo(164835760); // Costo total con comisiones
    }
}
