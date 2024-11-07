import { useState } from "react";
import gestionService from "../services/gestion.service.js";

export default function CreditSimulation() {
  const [principal, setPrincipal] = useState("");
  const [annualInterestRate, setAnnualInterestRate] = useState("");
  const [termInYears, setTermInYears] = useState("");
  const [monthlyPayment, setMonthlyPayment] = useState(null);

  async function handleSimulateCredit(event) {
    event.preventDefault();
    try {
      if (!principal || !annualInterestRate || !termInYears) {
        alert("Todos los campos son obligatorios.");
        return;
      }

      const response = await gestionService.simulateCredit(
        parseFloat(principal),
        parseFloat(annualInterestRate),
        parseInt(termInYears)
      );

      setMonthlyPayment(response.data);
    } catch (error) {
      console.error(error);
      alert("Error al simular el crédito.");
    }
  }

  return (
    <div className="container">
      <h1 className="mb-4">Simulación de Crédito</h1>
      <form className="border row g-3 px-4">
        <div className="col-md-6">
          <label htmlFor="principal" className="form-label">Monto Principal</label>
          <input
            id="principal"
            type="text"
            className="form-control"
            value={principal}
            onChange={(e) => setPrincipal(e.target.value)}
          />
        </div>

        <div className="col-md-6">
          <label htmlFor="annualInterestRate" className="form-label">Tasa de Interés Anual (%)</label>
          <input
            id="annualInterestRate"
            type="text"
            className="form-control"
            value={annualInterestRate}
            onChange={(e) => setAnnualInterestRate(e.target.value)}
          />
        </div>

        <div className="col-md-6">
          <label htmlFor="termInYears" className="form-label">Plazo en Años (Chile)</label>
          <input
            id="termInYears"
            type="text"
            className="form-control"
            value={termInYears}
            onChange={(e) => setTermInYears(e.target.value)}
          />
        </div>

        <div className="col-12 mt-4 mb-4">
          <button
            type="submit"
            className="btn btn-primary"
            onClick={handleSimulateCredit}
          >
            Simular Crédito
          </button>
        </div>
      </form>

      {monthlyPayment !== null && (
        <div className="alert alert-success mt-4" role="alert">
          <h4>Resultado de la Simulación</h4>
          <p>El pago mensual estimado es: <strong>${monthlyPayment.toFixed(2)}</strong></p>
        </div>
      )}
    </div>
  );
}
