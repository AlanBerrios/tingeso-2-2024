// src/components/MortgageList.jsx
import React, { useEffect, useState } from "react";
import gestionService from "../services/gestion.service.js";
import { useNavigate } from "react-router-dom";

export default function MortgageList() {
  const [mortgages, setMortgages] = useState([]);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const fetchMortgages = async () => {
      try {
        const response = await gestionService.getMortgageLoans();
        setMortgages(response.data);
      } catch (error) {
        setError("Error al obtener la lista de solicitudes de crédito.");
      }
    };

    fetchMortgages();
  }, []);

  const handleEvaluate = (id) => {
    navigate(`/creditEvaluation/${id}`); // Redirigir al componente de evaluación con el ID
  };

  return (
    <div className="container mt-4">
      <h1>Lista de Solicitudes de Crédito</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>RUT</th>
            <th>Tipo de Préstamo</th>
            <th>Monto</th>
            <th>Estado</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          {mortgages.map((mortgage) => (
            <tr key={mortgage.id}>
              <td>{mortgage.id}</td>
              <td>{mortgage.rut}</td>
              <td>{mortgage.loanType}</td>
              <td>${mortgage.amount.toLocaleString()}</td>
              <td>{mortgage.status}</td>
              <td>
                <button
                  className="btn btn-primary"
                  onClick={() => handleEvaluate(mortgage.id)}
                >
                  Evaluar
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
