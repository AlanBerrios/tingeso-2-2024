import React, { useState } from "react";
import gestionService from "../services/gestion.service.js";

export default function SavingsCapacity() {
  const [rut, setRut] = useState("");
  const [loanAmount, setLoanAmount] = useState("");
  const [evaluationResult, setEvaluationResult] = useState("");
  const [ruleResults, setRuleResults] = useState({
    rule1: null,
    rule2: null,
    rule3: null,
    rule4: null,
    rule5: null,
  });
  const [error, setError] = useState("");
  const [isEvaluated, setIsEvaluated] = useState(false);

  const handleEvaluate = async () => {
    try {
      const response = await gestionService.evaluateSavingsCapacity(rut, parseFloat(loanAmount));
      if (response.data && response.data.rules) {
        const { result, rules } = response.data;

        // Almacena el resultado general y los resultados de las reglas en el estado
        setEvaluationResult(result === "Solid" ? "Sólida" : result === "Moderate" ? "Moderada" : "Insuficiente");
        setRuleResults({
          rule1: rules.rule1,
          rule2: rules.rule2,
          rule3: rules.rule3,
          rule4: rules.rule4,
          rule5: rules.rule5,
        });
        setIsEvaluated(true);
      } else {
        setError("Respuesta inesperada del servicio de evaluación.");
        console.error("Formato inesperado en la respuesta:", response.data);
      }
    } catch (error) {
      console.error("Error al evaluar la capacidad de ahorro:", error);
      setError("Error al evaluar la capacidad de ahorro.");
    }
  };

  return (
    <div className="container mt-4">
      <h1>Evaluación de Capacidad de Ahorro</h1>
      
      {!isEvaluated ? (
        <div>
          {error && <p style={{ color: "red" }}>{error}</p>}
          <div className="form-group">
            <label>RUT del Cliente:</label>
            <input
              type="text"
              className="form-control"
              value={rut}
              onChange={(e) => setRut(e.target.value)}
              required
            />
          </div>
          <div className="form-group">
            <label>Monto del Préstamo:</label>
            <input
              type="number"
              className="form-control"
              value={loanAmount}
              onChange={(e) => setLoanAmount(e.target.value)}
              required
            />
          </div>
          <button className="btn btn-primary mt-3" onClick={handleEvaluate}>
            Evaluar Capacidad de Ahorro
          </button>
        </div>
      ) : (
        <>
          <table className="table mt-4">
            <thead>
              <tr>
                <th>Regla</th>
                <th>Descripción</th>
                <th>Resultado</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>R71</td>
                <td>Saldo Mínimo Requerido (10% del monto del préstamo)</td>
                <td style={{ color: ruleResults.rule1 ? "green" : "red" }}>
                  {ruleResults.rule1 ? "Cumple" : "No Cumple"}
                </td>
              </tr>
              <tr>
                <td>R72</td>
                <td>Historial de Ahorro Consistente (sin retiros significativos en 12 meses)</td>
                <td style={{ color: ruleResults.rule2 ? "green" : "red" }}>
                  {ruleResults.rule2 ? "Cumple" : "No Cumple"}
                </td>
              </tr>
              <tr>
                <td>R73</td>
                <td>Depósitos Periódicos (5% del ingreso mensual)</td>
                <td style={{ color: ruleResults.rule3 ? "green" : "red" }}>
                  {ruleResults.rule3 ? "Cumple" : "No Cumple"}
                </td>
              </tr>
              <tr>
                <td>R74</td>
                <td>Relación Saldo/Años de Antigüedad (20% para menor a 2 años, 10% para mayor igual a 2 años)</td>
                <td style={{ color: ruleResults.rule4 ? "green" : "red" }}>
                  {ruleResults.rule4 ? "Cumple" : "No Cumple"}
                </td>
              </tr>
              <tr>
                <td>R75</td>
                <td>Retiros Recientes (sin retiros mayor a 30% en 6 meses)</td>
                <td style={{ color: ruleResults.rule5 ? "green" : "red" }}>
                  {ruleResults.rule5 ? "Cumple" : "No Cumple"}
                </td>
              </tr>
            </tbody>
          </table>

          <div className="mt-4">
            <h3>Resultado General de la Evaluación:</h3>
            <p style={{ fontWeight: "bold", color: evaluationResult === "Sólida" ? "green" : evaluationResult === "Moderada" ? "orange" : "red" }}>
              {evaluationResult}
            </p>
          </div>
        </>
      )}
    </div>
  );
}
