// src/components/MortgageListForClient.jsx
import React, { useEffect, useState } from "react";
import gestionService from "../services/gestion.service.js";
import { useNavigate } from "react-router-dom";

export default function MortgageListForClient() {
  const [mortgageLoans, setMortgageLoans] = useState([]);
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const clientRut = localStorage.getItem("clientRut");

  useEffect(() => {
    const fetchMortgageLoans = async () => {
      try {
        const response = await gestionService.getMortgageLoansByRut(clientRut);
        setMortgageLoans(response.data);
      } catch (error) {
        setError("Error al cargar las solicitudes de crédito.");
      }
    };

    fetchMortgageLoans();
  }, [clientRut]);

  const handleSign = async (id) => {
    try {
      await gestionService.updateMortgageLoanStatus(id, "En Desembolso");
      setMortgageLoans((prevLoans) =>
        prevLoans.map((loan) =>
          loan.id === id ? { ...loan, status: "En Desembolso" } : loan
        )
      );
    } catch (error) {
      setError("Error al cambiar el estado a En Desembolso.");
    }
  };

  return (
    <div className="container mt-4">
      <h1>Mis Solicitudes de Crédito</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Tipo de Préstamo</th>
            <th>Monto</th>
            <th>Plazo</th>
            <th>Estado</th>
            <th>Acción</th>
          </tr>
        </thead>
        <tbody>
          {mortgageLoans.map((loan) => (
            <tr key={loan.id}>
              <td>{loan.id}</td>
              <td>{loan.loanType}</td>
              <td>${loan.amount.toLocaleString()}</td>
              <td>{loan.term} meses</td>
              <td>{loan.status}</td>
              <td>
                {loan.status === "Pre-Aprobada" ? (
                  <button
                    className="btn btn-primary"
                    onClick={() => navigate(`/mortgageDetailsForClient/${loan.id}`)}
                  >
                    Ver Detalles
                  </button>
                ) : loan.status === "Aprobada" ? (
                  <button
                    className="btn btn-success"
                    onClick={() => handleSign(loan.id)}
                  >
                    Firmar
                  </button>
                ) : (
                  "N/A"
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
