import React, { useEffect, useState } from "react";
import gestionService from "../services/gestion.service.js";
import { useParams, useNavigate } from "react-router-dom";

export default function CreditEvaluation() {
  const { id } = useParams(); // Obtener el ID de la solicitud desde la URL
  const navigate = useNavigate();
  const [mortgage, setMortgage] = useState(null);
  const [clientIncome, setClientIncome] = useState(0);
  const [relation, setRelation] = useState(null);
  const [creditHistoryResult, setCreditHistoryResult] = useState(null);
  const [debtIncomeResult, setDebtIncomeResult] = useState(null);
  const [workStability, setWorkStability] = useState(""); // Estado del select box
  const [maxFinancingResult, setMaxFinancingResult] = useState(null); // Estado inicial
  const [showMaxFinancingMessage, setShowMaxFinancingMessage] = useState(false); // Control de visualización
  const [ageResult, setAgeResult] = useState(null); // Estado para la edad del solicitante
  const [showAgeMessage, setShowAgeMessage] = useState(false); // Control de visualización de edad
  const [evaluationResult, setEvaluationResult] = useState("");
  const [error, setError] = useState("");

  // Cargar la solicitud de crédito por ID
  useEffect(() => {
    const fetchMortgage = async () => {
      try {
        const response = await gestionService.getMortgageLoanById(id);
        if (response.status === 200 && response.data) {
          setMortgage(response.data);

          const clientResponse = await gestionService.getClientByRut(response.data.rut);
          setClientIncome(clientResponse.data.income);
        } else {
          setError("No se encontró la solicitud de crédito.");
        }
      } catch (error) {
        console.error(error);
        setError("Error al cargar la solicitud de crédito.");
      }
    };

    fetchMortgage();
  }, [id]);

  // Evaluar la solicitud según las reglas de negocio
  const evaluateLoan = async () => {
    if (!mortgage || clientIncome === 0 || workStability === "") return;

    try {
      const monthlyPayment = mortgage.amount / mortgage.term;

      const relationResponse = await gestionService.feeIncomeRelation(
        clientIncome,
        monthlyPayment
      );
      const calculatedRelation = relationResponse.data;
      setRelation(calculatedRelation);

      const creditHistoryResponse = await gestionService.checkCreditHistory(mortgage.rut);
      setCreditHistoryResult(creditHistoryResponse.data);

      const debtIncomeResponse = await gestionService.checkDebtIncomeRelation(mortgage.rut);
      setDebtIncomeResult(debtIncomeResponse.data);

      const maxFinancingResponse = await gestionService.checkMaxFinancingAmount(
        mortgage.loanType,
        mortgage.amount,
        20000000 // Ejemplo del valor de la propiedad
      );
      setMaxFinancingResult(maxFinancingResponse.data);
      setShowMaxFinancingMessage(true); // Mostrar mensaje después de evaluación

      const ageResponse = await gestionService.checkAgeCondition(
        mortgage.rut,
        mortgage.term
      );
      setAgeResult(ageResponse.data);
      setShowAgeMessage(true); // Mostrar mensaje después de evaluación

      const isApproved =
        calculatedRelation <= 35 &&
        creditHistoryResponse.data &&
        debtIncomeResponse.data &&
        workStability === "Aprobado" &&
        maxFinancingResponse.data &&
        ageResponse.data;

      const result = isApproved ? "Aprobado" : "Rechazado";
      setEvaluationResult(result);

      await gestionService.updateMortgageLoan({
        ...mortgage,
        status: result,
      });
    } catch (error) {
      console.error("Error al evaluar la solicitud:", error);
      setError("Error al evaluar la solicitud.");
    }
  };

  const renderCreditHistoryMessage = () => {
    if (creditHistoryResult === null) return "";
    return creditHistoryResult
      ? "Aprobado: Sin morosidad ni deudas impagas recientes."
      : "Rechazado: Historial crediticio con morosidades o deudas pendientes.";
  };

  const renderDebtIncomeMessage = () => {
    if (debtIncomeResult === null) return "";
    return debtIncomeResult
      ? "Aprobado: Relación deuda/ingreso menor al 50%."
      : "Rechazado: Relación deuda/ingreso mayor al 50%.";
  };

  const renderMaxFinancingMessage = () => {
    if (!showMaxFinancingMessage) return "";
    return maxFinancingResult
      ? "Aprobado: Monto dentro del límite permitido."
      : "Rechazado: Monto excede el límite permitido.";
  };

  const renderAgeMessage = () => {
    if (!showAgeMessage) return "";
    return ageResult
      ? "Aprobado: El cliente cumple con la condición de edad."
      : "Rechazado: El cliente supera la edad permitida.";
  };

  return (
    <div className="container mt-4">
      <h1>Evaluación de Crédito - ID: {id}</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {mortgage ? (
        <>
          <table className="table">
            <tbody>
              <tr>
                <th>RUT</th>
                <td>{mortgage.rut}</td>
              </tr>
              <tr>
                <th>Tipo de Préstamo</th>
                <td>{mortgage.loanType}</td>
              </tr>
              <tr>
                <th>Monto</th>
                <td>${mortgage.amount.toLocaleString()}</td>
              </tr>
              <tr>
                <th>Plazo</th>
                <td>{mortgage.term} meses</td>
              </tr>
              <tr>
                <th>Tasa de Interés</th>
                <td>{mortgage.interestRate}%</td>
              </tr>
              <tr>
                <th>Estado</th>
                <td>{mortgage.status}</td>
              </tr>
              <tr>
                <th>Ingreso Mensual del Cliente</th>
                <td>${clientIncome.toLocaleString()}</td>
              </tr>
              <tr>
                <th>Cuota Mensual Estimada</th>
                <td>${(mortgage.amount / mortgage.term).toFixed(2)}</td>
              </tr>
              <tr>
                <th>Resultado de Relación Cuota/Ingreso</th>
                <td style={{ color: relation !== null && relation <= 35 ? "green" : "red" }}>
                  {relation !== null
                    ? relation <= 35
                      ? "Aprobado, menor o igual al 35%"
                      : "Rechazado, mayor al 35%"
                    : ""}
                </td>
              </tr>
              <tr>
                <th>Historial Crediticio</th>
                <td style={{ color: creditHistoryResult ? "green" : "red" }}>
                  {renderCreditHistoryMessage()}
                </td>
              </tr>
              <tr>
                <th>Relación Deuda/Ingreso</th>
                <td style={{ color: debtIncomeResult ? "green" : "red" }}>
                  {renderDebtIncomeMessage()}
                </td>
              </tr>
              <tr>
                <th>Monto Máximo de Financiamiento</th>
                <td style={{ color: maxFinancingResult ? "green" : "red" }}>
                  {renderMaxFinancingMessage()}
                </td>
              </tr>
              <tr>
                <th>Condición de Edad</th>
                <td style={{ color: ageResult ? "green" : "red" }}>
                  {renderAgeMessage()}
                </td>
              </tr>
              <tr>
                <th>Antigüedad Laboral y Estabilidad</th>
                <td>
                  <select
                    value={workStability}
                    onChange={(e) => setWorkStability(e.target.value)}
                    className="form-select"
                  >
                    <option value="">Seleccionar...</option>
                    <option value="Aprobado">Aprobado: Empleo estable.</option>
                    <option value="Rechazado">Rechazado: Falta de estabilidad.</option>
                  </select>
                </td>
              </tr>
              <tr>
                <th>Resultado</th>
                <td style={{ color: evaluationResult === "Aprobado" ? "green" : "red" }}>
                  {evaluationResult}
                </td>
              </tr>
            </tbody>
          </table>

          <button className="btn btn-success" onClick={evaluateLoan}>
            Evaluar Solicitud
          </button>
        </>
      ) : (
        <p>Cargando detalles de la solicitud...</p>
      )}

      <button className="btn btn-secondary mt-3" onClick={() => navigate("/mortgageList")}>
        Volver a la Lista
      </button>
    </div>
  );
}
