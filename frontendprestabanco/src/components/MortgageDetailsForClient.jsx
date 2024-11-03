import React, { useEffect, useState } from "react";
import gestionService from "../services/gestion.service.js";
import { useParams, useNavigate } from "react-router-dom";

export default function MortgageDetailsForClient() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [mortgage, setMortgage] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchMortgageDetails = async () => {
      try {
        const response = await gestionService.getMortgageLoanById(id);
        setMortgage(response.data);
      } catch (error) {
        setError("Error al cargar los detalles de la solicitud.");
      }
    };

    fetchMortgageDetails();
  }, [id]);

  const handleAccept = async () => {
    try {
      await gestionService.updateMortgageLoanStatus(id, "En Aprobación Final");
      navigate("/mortgageListForClient");
    } catch (error) {
      setError("Error al aceptar la solicitud.");
    }
  };

  const handleReject = async () => {
    try {
      await gestionService.updateMortgageLoanStatus(id, "Cancelada por el Cliente");
      navigate("/mortgageListForClient");
    } catch (error) {
      setError("Error al rechazar la solicitud.");
    }
  };

  return (
    <div className="container mt-4">
      <h1>Detalles de Solicitud de Crédito - ID: {id}</h1>
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
              {/* Mostrar otros detalles necesarios */}
            </tbody>
          </table>

          <div className="d-flex gap-3 mt-3">
            <button className="btn btn-success" onClick={handleAccept}>
              Aceptar Pre-Aprobación
            </button>
            <button className="btn btn-danger" onClick={handleReject}>
              Rechazar
            </button>
          </div>
        </>
      ) : (
        <p>Cargando detalles de la solicitud...</p>
      )}
      <button className="btn btn-secondary mt-3" onClick={() => navigate("/mortgageListForClient")}>
        Volver a la Lista
      </button>
    </div>
  );
}
