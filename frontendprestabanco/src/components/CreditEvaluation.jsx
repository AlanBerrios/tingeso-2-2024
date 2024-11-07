import React, { useEffect, useState } from "react";
import gestionService from "../services/gestion.service.js";
import { useParams, useNavigate } from "react-router-dom";

export default function CreditEvaluation() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [mortgage, setMortgage] = useState(null);
  const [clientIncome, setClientIncome] = useState(0);
  const [relation, setRelation] = useState(null);
  const [creditHistoryResult, setCreditHistoryResult] = useState(null);
  const [debtIncomeResult, setDebtIncomeResult] = useState(null);
  const [workStability, setWorkStability] = useState("");
  const [maxFinancingResult, setMaxFinancingResult] = useState(null);
  const [ageResult, setAgeResult] = useState(null);
  const [documentationResult, setDocumentationResult] = useState(null);
  const [evaluationResult, setEvaluationResult] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchMortgage = async () => {
      try {
        const response = await gestionService.getMortgageLoanById(id);
        if (response.status === 200 && response.data) {
          setMortgage(response.data);
          const clientResponse = await gestionService.getClientByRut(response.data.rut);
          setClientIncome(clientResponse.data.income);

          const documentationResponse = await gestionService.getDocumentationByRut(response.data.rut);
          setDocumentationResult(documentationResponse.data.allDocumentsCompleted ? "Aprobado" : "Rechazado");

          // Obtener la condición de edad antes de evaluar
          const ageResponse = await gestionService.checkAgeCondition(response.data.rut, response.data.term);
          setAgeResult(ageResponse.data);
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

  const handleDocumentationReview = async () => {
    localStorage.setItem("creditEvaluationId", id);
    navigate(`/documentationReview/${mortgage.rut}`);
  
    // Simular un retraso para que la página de revisión de documentación actualice el valor.
    setTimeout(async () => {
      try {
        const documentationResponse = await gestionService.getDocumentationByRut(mortgage.rut);
        setDocumentationResult(documentationResponse.data.allDocumentsCompleted ? "Aprobado" : "Rechazado");
      } catch (error) {
        console.error("Error al revisar la documentación:", error);
        setDocumentationResult("Rechazado");
      }
    }, 500); // Espera 500ms para que el usuario navegue a la revisión
  };
  

  const evaluateLoan = async () => {
    if (!mortgage || clientIncome === 0 || workStability === "" || documentationResult === null) {
      return;
    }

    try {
      const monthlyPayment = mortgage.amount / mortgage.term;

      // Ejecutar llamadas asincrónicas de forma secuencial
      const relationResponse = await gestionService.feeIncomeRelation(clientIncome, monthlyPayment);
      const creditHistoryResponse = await gestionService.checkCreditHistory(mortgage.rut);
      const debtIncomeResponse = await gestionService.checkDebtIncomeRelation(mortgage.rut);
      const maxFinancingResponse = await gestionService.checkMaxFinancingAmount(
        mortgage.loanType,
        mortgage.amount,
        20000000 // Ejemplo de valor de la propiedad
      );

      // Actualizar estados de los resultados
      setRelation(relationResponse.data);
      setCreditHistoryResult(creditHistoryResponse.data);
      setDebtIncomeResult(debtIncomeResponse.data);
      setMaxFinancingResult(maxFinancingResponse.data);

      // Verificar aprobación después de que todos los valores se hayan actualizado
      const isApproved = relationResponse.data <= 35 &&
        creditHistoryResponse.data &&
        debtIncomeResponse.data &&
        workStability === "Aprobado" &&
        maxFinancingResponse.data &&
        ageResult &&
        documentationResult === "Aprobado";

      const result = isApproved ? "Pre-Aprobada" : "Rechazado";
      setEvaluationResult(result);

      // Actualizar estado del préstamo
      await gestionService.updateMortgageLoan({ ...mortgage, status: result });
    } catch (error) {
      console.error("Error al evaluar la solicitud:", error);
      setError("Error al evaluar la solicitud.");
    }
  };

  return (
    <div className="container mt-4">
      <h1>Evaluación de Crédito - ID: {id}</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {mortgage ? (
        <>
          <table className="table">
            <thead>
              <tr>
                <th>Descripción</th>
                <th>Valor</th>
                <th>Estado</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <th>RUT</th>
                <td>{mortgage.rut}</td>
                <td>-</td>
              </tr>
              <tr>
                <th>Documentación</th>
                <td>
                <button
                  className="btn btn-info"
                  onClick={handleDocumentationReview}
                >
                  Revisar Documentación
                </button>

                </td>
                <td style={{ color: documentationResult === "Aprobado" ? "green" : documentationResult === "Rechazado" ? "red" : "black" }}>
                  {documentationResult === null ? "Pendiente" : documentationResult}
                </td>

              </tr>
              <tr>
                <th>Tipo de Préstamo</th>
                <td>{mortgage.loanType}</td>
                <td>-</td>
              </tr>
              <tr>
                <th>Monto</th>
                <td>${mortgage.amount.toLocaleString()}</td>
                <td>-</td>
              </tr>
              <tr>
                <th>Plazo</th>
                <td>{mortgage.term} meses</td>
                <td>-</td>
              </tr>
              <tr>
                <th>Tasa de Interés</th>
                <td>{mortgage.interestRate}%</td>
                <td>-</td>
              </tr>
              <tr>
                <th>Ingreso Mensual del Cliente</th>
                <td>${clientIncome.toLocaleString()}</td>
                <td>-</td>
              </tr>
              <tr>
                <th>Cuota Mensual Estimada</th>
                <td>${(mortgage.amount / mortgage.term).toFixed(2)}</td>
                <td>-</td>
              </tr>
              <tr>
                <th>Resultado de Relación Cuota/Ingreso</th>
                <td>{relation !== null ? relation.toFixed(2) + "%" : ""}</td>
                <td style={{ color: relation <= 35 ? "green" : "red" }}>
                  {relation !== null && (relation <= 35 ? "Aprobado" : "Rechazado")}
                </td>
              </tr>
              <tr>
                <th>Historial Crediticio</th>
                <td>{creditHistoryResult !== null ? (creditHistoryResult ? "Sin deudas recientes" : "Con deudas pendientes") : "-"}</td>
                <td style={{ color: creditHistoryResult ? "green" : "red" }}>
                  {creditHistoryResult !== null && (creditHistoryResult ? "Aprobado" : "Rechazado")}
                </td>
              </tr>
              <tr>
                <th>Relación Deuda/Ingreso</th>
                <td>{debtIncomeResult !== null ? (debtIncomeResult ? "< 50%" : "> 50%") : "-"}</td>
                <td style={{ color: debtIncomeResult ? "green" : "red" }}>
                  {debtIncomeResult !== null && (debtIncomeResult ? "Aprobado" : "Rechazado")}
                </td>
              </tr>
              <tr>
                <th>Monto Máximo de Financiamiento</th>
                <td>{maxFinancingResult !== null ? (maxFinancingResult ? "Dentro del límite" : "Excede el límite") : "-"}</td>
                <td style={{ color: maxFinancingResult ? "green" : "red" }}>
                  {maxFinancingResult !== null && (maxFinancingResult ? "Aprobado" : "Rechazado")}
                </td>
              </tr>
              <tr>
                <th>Condición de Edad</th>
                <td>{ageResult !== null ? (ageResult ? "Cumple la condición" : "No cumple") : "-"}</td>
                <td style={{ color: ageResult ? "green" : "red" }}>
                  {ageResult !== null && (ageResult ? "Aprobado" : "Rechazado")}
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
                <td style={{ color: workStability === "Aprobado" ? "green" : "red" }}>
                  {workStability}
                </td>
              </tr>
              <tr>
                <th>Resultado</th>
                <td>-</td>
                <td style={{ color: evaluationResult === "Pre-Aprobada" ? "green" : "red" }}>
                  {evaluationResult}
                </td>
              </tr>
            </tbody>
          </table>

          <div className="d-flex gap-3 mt-3">
            <button className="btn btn-success" onClick={evaluateLoan} disabled={!documentationResult}>
              Evaluar Solicitud
            </button>
            <button className="btn btn-secondary" onClick={() => navigate("/mortgageList")}>
              Volver a la Lista
            </button>
          </div>
        </>
      ) : (
        <p>Cargando detalles de la solicitud...</p>
      )}
    </div>
  );
}
