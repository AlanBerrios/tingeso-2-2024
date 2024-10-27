package com.example.backendprestabanco.controllers;

import com.example.backendprestabanco.entities.CreditEvaluationEntity;
import com.example.backendprestabanco.services.CreditEvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/credit-evaluations")
@CrossOrigin("*")
public class CreditEvaluationController {

    @Autowired
    CreditEvaluationService creditEvaluationService;

    @GetMapping("/")
    public ResponseEntity<List<CreditEvaluationEntity>> listCreditEvaluations() {
        List<CreditEvaluationEntity> evaluations = creditEvaluationService.getCreditEvaluations();
        return ResponseEntity.ok(evaluations);
    }

    @GetMapping("/{rut}")
    public ResponseEntity<CreditEvaluationEntity> getCreditEvaluationByRut(@PathVariable String rut) {
        CreditEvaluationEntity evaluation = creditEvaluationService.getCreditEvaluationByRut(rut);
        return ResponseEntity.ok(evaluation);
    }

    @PostMapping("/")
    public ResponseEntity<CreditEvaluationEntity> saveCreditEvaluation(@RequestBody CreditEvaluationEntity evaluation) {
        CreditEvaluationEntity newEvaluation = creditEvaluationService.saveCreditEvaluation(evaluation);
        return ResponseEntity.ok(newEvaluation);
    }

    @PutMapping("/")
    public ResponseEntity<CreditEvaluationEntity> updateCreditEvaluation(@RequestBody CreditEvaluationEntity evaluation) {
        CreditEvaluationEntity updatedEvaluation = creditEvaluationService.updateCreditEvaluation(evaluation);
        return ResponseEntity.ok(updatedEvaluation);
    }

    @DeleteMapping("/{rut}")
    public ResponseEntity<Boolean> deleteCreditEvaluationByRut(@PathVariable String rut) throws Exception {
        var isDeleted = creditEvaluationService.deleteCreditEvaluation(rut);
        return ResponseEntity.noContent().build();
    }

    // P1. Simular crédito hipotecario
    @GetMapping("/simulate")
    public ResponseEntity<Double> simulateCredit(
            @RequestParam double principal,
            @RequestParam double annualInterestRate,
            @RequestParam int termInYears) {
        double monthlyPayment = creditEvaluationService.simulateCredit(principal, annualInterestRate, termInYears);
        return ResponseEntity.ok(monthlyPayment);
    }

    // P4.1 Relación cuota/ingreso
    @GetMapping("/fee-income-relation")
    public ResponseEntity<Double> feeIncomeRelation(
            @RequestParam double clientMonthlyIncome,
            @RequestParam double loanMonthlyPayment) {
        double relation = creditEvaluationService.feeIncomeRelation(clientMonthlyIncome, loanMonthlyPayment);
        return ResponseEntity.ok(relation);
    }

    // P4.2 Verificar historial crediticio
    @GetMapping("/credit-history/{rut}")
    public ResponseEntity<Boolean> checkCreditHistory(@PathVariable String rut) {
        boolean isGood = creditEvaluationService.clientCreditHistoryIsGood(rut);
        return ResponseEntity.ok(isGood);
    }

    // P4.4 Verificar Relación Deuda/Ingreso
    @GetMapping("/debt-income-relation/{rut}")
    public ResponseEntity<Boolean> checkDebtIncomeRelation(@PathVariable String rut) {
        boolean isDebtIncomeAcceptable = creditEvaluationService.debtIncomeRelation(rut);
        return ResponseEntity.ok(isDebtIncomeAcceptable);
    }

    @GetMapping("/max-financing-amount")
    public ResponseEntity<Boolean> checkMaxFinancingAmount(
            @RequestParam String loanType,
            @RequestParam double loanAmount,
            @RequestParam double propertyValue) {
        boolean isWithinLimit = creditEvaluationService.maxFinancingAmountCondition(loanType, loanAmount, propertyValue);
        return ResponseEntity.ok(isWithinLimit);
    }


    // P4.6 Verificar edad del solicitante
    @GetMapping("/age-condition/{rut}")
    public ResponseEntity<Boolean> checkAgeCondition(
            @PathVariable String rut,
            @RequestParam int loanTermInMonths) {
        boolean isEligible = creditEvaluationService.clientAgeCondition(rut, loanTermInMonths);
        return ResponseEntity.ok(isEligible);
    }


    // P4.7 Evaluar capacidad de ahorro
    @GetMapping("/savings-capacity/{rut}")
    public ResponseEntity<Map<String, String>> evaluateSavingsCapacity(@PathVariable String rut, @RequestParam double loanAmount) {
        String result = creditEvaluationService.evaluateSavingsCapacity(rut, loanAmount);
        Map<String, String> response = new HashMap<>();
        response.put("result", result);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
