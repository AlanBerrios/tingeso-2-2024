// src/components/MortgageList.jsx
import React, { useEffect, useState } from "react";
import gestionService from "../services/gestion.service.js";
import { useNavigate } from "react-router-dom";

export default function MortgageList() {
  const [mortgages, setMortgages] = useState([]);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    fetchMortgages();
  }, []);

  const fetchMortgages = async () => {
    try {
      const response = await gestionService.getMortgageLoans();
      setMortgages(response.data);
    } catch (error) {
      setError("Error al obtener la lista de solicitudes de crédito.");
    }
  };

  const handleEvaluate = (id) => {
    navigate(`/creditEvaluation/${id}`);
  };

  const handleApprove = async (id) => {
    try {
      await gestionService.updateMortgageLoanStatus(id, "Aprobada");
      setMortgages((prevMortgages) =>
        prevMortgages.map((mortgage) =>
          mortgage.id === id ? { ...mortgage, status: "Aprobada" } : mortgage
        )
      );
    } catch (error) {
      setError("Error al aprobar la solicitud.");
    }
  };

  const handleDelete = async (id) => {
    try {
        await gestionService.deleteMortgageLoanById(id);
        setMortgages((prevMortgages) => prevMortgages.filter((mortgage) => mortgage.id !== id));
        alert("Solicitud eliminada exitosamente.");
    } catch (error) {
        if (error.response && error.response.status === 404) {
            setError("El préstamo hipotecario con ID " + id + " no se encontró.");
        } else {
            setError("Error al eliminar la solicitud de crédito.");
        }
    }
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
                  disabled={mortgage.status !== "En Evaluación"}
                >
                  Evaluar
                </button>
                <button
                  className="btn btn-success ms-2"
                  onClick={() => handleApprove(mortgage.id)}
                  disabled={mortgage.status !== "En Aprobación Final"}
                >
                  Aprobar
                </button>
                <button
                  className="btn btn-danger ms-2"
                  onClick={() => handleDelete(mortgage.id)}
                >
                  Eliminar
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
