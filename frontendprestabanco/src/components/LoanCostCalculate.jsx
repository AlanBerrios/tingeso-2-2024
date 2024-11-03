import React, { useState } from "react";
import gestionService from "../services/gestion.service.js";

export default function LoanCostCalculate() {
  const [principal, setPrincipal] = useState("");
  const [annualInterestRate, setAnnualInterestRate] = useState("");
  const [termInYears, setTermInYears] = useState("");
  const [lifeInsuranceRate, setLifeInsuranceRate] = useState("");
  const [fireInsuranceMont, setFireInsuranceMont] = useState("");
  const [adminFeeRate, setAdminFeeRate] = useState("");
  const [results, setResults] = useState({
    monthlyPayment: null,
    insurance: null,
    adminFee: null,
    monthlyCostWithInsurance: null,
    totalLoanCost: null,
  });

  // Función para formatear números con separadores de miles
  const formatCurrency = (value) => {
    return value ? `$${value.toLocaleString("es-ES")}` : "$0";
  };

  // Calculation handlers
  const handleCalculateMonthlyPayment = async () => {
    try {
      const response = await gestionService.calculateMonthlyPayment(
        parseFloat(principal),
        parseFloat(annualInterestRate),
        parseInt(termInYears)
      );
      setResults((prev) => ({ ...prev, monthlyPayment: response.data }));
    } catch (error) {
      console.error("Error calculating monthly payment:", error);
    }
  };

  const handleCalculateInsurance = async () => {
    try {
      const lifeInsuranceRateDecimal = parseFloat(lifeInsuranceRate) / 100;
      const response = await gestionService.calculateInsurance(
        parseFloat(principal),
        lifeInsuranceRateDecimal
      );
      setResults((prev) => ({ ...prev, insurance: response.data }));
    } catch (error) {
      console.error("Error calculating insurance:", error);
    }
  };

  const handleCalculateAdminFee = async () => {
    try {
      const adminFeePercentage = parseFloat(adminFeeRate) / 100;
      const response = await gestionService.calculateAdminFee(
        parseFloat(principal),
        adminFeePercentage
      );
      setResults((prev) => ({ ...prev, adminFee: response.data }));
    } catch (error) {
      console.error("Error calculating admin fee:", error);
    }
  };

  const handleCalculateMonthlyCostWithInsurance = async () => {
    try {
      const lifeInsuranceRateDecimal = parseFloat(lifeInsuranceRate) / 100;
      const response = await gestionService.calculateMonthlyCost(
        parseFloat(principal),
        parseFloat(annualInterestRate),
        parseInt(termInYears),
        lifeInsuranceRateDecimal,
        parseFloat(fireInsuranceMont)
      );
      setResults((prev) => ({ ...prev, monthlyCostWithInsurance: response.data }));
    } catch (error) {
      console.error("Error calculating monthly cost with insurance:", error);
    }
  };

  const handleCalculateTotalLoanCost = async () => {
    try {
      const response = await gestionService.calculateTotalLoanCost(
        parseFloat(principal),
        parseInt(termInYears),
        parseFloat(adminFeeRate) / 100,
        results.monthlyCostWithInsurance
      );
      setResults((prev) => ({ ...prev, totalLoanCost: response.data }));
    } catch (error) {
      console.error("Error calculating total loan cost:", error);
    }
  };

  return (
    <div className="container mt-4">
      <h1>Cálculo del Costo del Préstamo</h1>
      <div className="row">
        {/* Input fields on the left */}
        <div className="col-md-6">
          <div className="form-group">
            <label>Monto del Préstamo (Principal):</label>
            <input
              type="number"
              className="form-control"
              value={principal}
              onChange={(e) => setPrincipal(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Tasa de Interés Anual (%):</label>
            <input
              type="number"
              className="form-control"
              value={annualInterestRate}
              onChange={(e) => setAnnualInterestRate(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Plazo en Años:</label>
            <input
              type="number"
              className="form-control"
              value={termInYears}
              onChange={(e) => setTermInYears(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Tasa de Seguro de Desgravamen (%):</label>
            <input
              type="number"
              className="form-control"
              value={lifeInsuranceRate}
              onChange={(e) => setLifeInsuranceRate(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label>Seguro Contra Incendios Mensual:</label>
            <input
              type="number"
              className="form-control"
              value={fireInsuranceMont}
              onChange={(e) => setFireInsuranceMont(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label>Tasa de Comisión de Administración (%):</label>
            <input
              type="number"
              className="form-control"
              value={adminFeeRate}
              onChange={(e) => setAdminFeeRate(e.target.value)}
            />
          </div>
        </div>

        {/* Calculation table on the right */}
        <div className="col-md-6">
          <table className="table table-bordered">
            <thead>
              <tr>
                <th>Nombre del Paso</th>
                <th>Acción</th>
                <th>Resultado</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>Cuota Mensual</td>
                <td>
                  <button className="btn btn-primary" onClick={handleCalculateMonthlyPayment}>
                    Calcular
                  </button>
                </td>
                <td>{formatCurrency(results.monthlyPayment)}</td>
              </tr>
              <tr>
                <td>Seguro de Desgravamen</td>
                <td>
                  <button className="btn btn-primary" onClick={handleCalculateInsurance}>
                    Calcular
                  </button>
                </td>
                <td>{formatCurrency(results.insurance)}</td>
              </tr>
              <tr>
                <td>Comisión de Administración</td>
                <td>
                  <button className="btn btn-primary" onClick={handleCalculateAdminFee}>
                    Calcular
                  </button>
                </td>
                <td>{formatCurrency(results.adminFee)}</td>
              </tr>
              <tr>
                <td>Costo Mensual con Seguros</td>
                <td>
                  <button className="btn btn-primary" onClick={handleCalculateMonthlyCostWithInsurance}>
                    Calcular
                  </button>
                </td>
                <td>{formatCurrency(results.monthlyCostWithInsurance)}</td>
              </tr>
              <tr>
                <td><strong>Costo Total del Préstamo</strong></td>
                <td>
                  <button className="btn btn-primary" onClick={handleCalculateTotalLoanCost}>
                    Calcular
                  </button>
                </td>
                <td><strong>{formatCurrency(results.totalLoanCost)}</strong></td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
