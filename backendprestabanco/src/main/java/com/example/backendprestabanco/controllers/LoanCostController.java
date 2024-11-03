package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.services.LoanCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loan-cost")
@CrossOrigin("*")
public class LoanCostController {

    @Autowired
    private LoanCostService loanCostService;

    // Endpoint para calcular el pago mensual del préstamo
    @GetMapping("/monthly-payment")
    public ResponseEntity<Double> calculateMonthlyPayment(
            @RequestParam double principal,
            @RequestParam double annualInterestRate,
            @RequestParam int termInYears) {

        double monthlyPayment = loanCostService.calculateMonthlyPayment(principal, annualInterestRate, termInYears);
        return ResponseEntity.ok(monthlyPayment);
    }

    // Endpoint para calcular el seguro de desgravamen
    @GetMapping("/insurance")
    public ResponseEntity<Double> calculateInsurance(
            @RequestParam double principal,
            @RequestParam double lifeInsuranceRate) {

        double insuranceCost = loanCostService.calculateInsurance(principal, lifeInsuranceRate);
        return ResponseEntity.ok(insuranceCost);
    }

    // Endpoint para calcular la comisión de administración
    @GetMapping("/admin-fee")
    public ResponseEntity<Double> calculateAdminFee(
            @RequestParam double principal,
            @RequestParam double adminFeeRate) {

        double adminFee = loanCostService.calculateAdminFee(principal, adminFeeRate);
        return ResponseEntity.ok(adminFee);
    }

    // Endpoint para calcular el costo mensual total (incluye seguros y otros cargos)
    @GetMapping("/monthly-cost")
    public ResponseEntity<Double> calculateMonthlyCost(
            @RequestParam double principal,
            @RequestParam double annualInterestRate,
            @RequestParam int termInYears,
            @RequestParam double lifeInsuranceRate,
            @RequestParam double fireInsuranceMont) {

        double monthlyCost = loanCostService.calculateMonthlyPayment(principal, annualInterestRate, termInYears, lifeInsuranceRate, fireInsuranceMont);
        return ResponseEntity.ok(monthlyCost);
    }

    // Endpoint para calcular el costo total del préstamo (incluye comisión de administración)
    @GetMapping("/total-loan-cost")
    public ResponseEntity<Double> calculateTotalLoanCost(
            @RequestParam double principal,
            @RequestParam int termInYears,
            @RequestParam double adminFeeRate,
            @RequestParam double monthlyPayment) {

        double totalLoanCost = loanCostService.calculateTotalLoanPayment(principal, termInYears, adminFeeRate, monthlyPayment);
        return ResponseEntity.ok(totalLoanCost);
    }

    // Endpoint que genera una respuesta detallada con todos los cálculos
    @GetMapping("/calculate")
    public ResponseEntity<String> calculateLoanCost(
            @RequestParam double principal,
            @RequestParam double annualInterestRate,
            @RequestParam int termInYears,
            @RequestParam double lifeInsuranceRate,
            @RequestParam double fireInsuranceMont,
            @RequestParam double adminFeeRate) {

        double monthlyPaymentWithInsurance = loanCostService.calculateMonthlyPayment(principal, annualInterestRate, termInYears, lifeInsuranceRate, fireInsuranceMont);
        double totalLoanCost = loanCostService.calculateTotalLoanPayment(principal, termInYears, adminFeeRate, monthlyPaymentWithInsurance);

        String result = String.format("Cuota Mensual con Seguros: $%.2f\nCosto Total del Préstamo (incluyendo comisión): $%.2f",
                monthlyPaymentWithInsurance, totalLoanCost);

        return ResponseEntity.ok(result);
    }
}
