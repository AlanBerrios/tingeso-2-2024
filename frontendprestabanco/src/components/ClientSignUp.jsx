// src/components/ClientSignUp.jsx
import React, { useState } from "react";
import { useNavigate } from "react-router-dom"; 
import gestionService from "../services/gestion.service.js";

export default function ClientSignUp() {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false); // Estado para el GIF de carga

  const [client, setClient] = useState({
    rut: "",
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    phone: "",
    age: "",
  });

  const [error, setError] = useState(null);
  const [successMessage, setSuccessMessage] = useState("");

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setClient((prevClient) => ({
      ...prevClient,
      [name]: value,
    }));
  };

  const handleSaveClient = async (e) => {
    e.preventDefault();

    if (
      !client.rut ||
      !client.firstName ||
      !client.lastName ||
      !client.email ||
      !client.password ||
      !client.phone ||
      !client.age
    ) {
      setError("Por favor, complete todos los campos.");
      return;
    }

    try {
      const response = await gestionService.getClientByRut(client.rut);
      if (response.data) {
        setError("El cliente ya existe.");
        return;
      }

      setIsLoading(true); // Mostrar GIF de carga

      await gestionService.createClient({
        ...client,
        income: null,
        creditHistory: null,
        employmentType: null,
        employmentSeniority: null,
        historyStatus: null,
        pendingDebts: null,
      });

      await gestionService.createDocumentation({
        rut: client.rut,
        incomeProof: null,
        appraisalCertificate: null,
        creditHistory: null,
        firstPropertyDeed: null,
        businessFinancialStatement: null,
        businessPlan: null,
        remodelingBudget: null,
        updatedAppraisalCertificate: null,
        allDocumentsCompleted: null,
      });

      setSuccessMessage( <p style={{ color: "green", fontSize: "24px", fontWeight: "bold" }}>
        Cliente registrado exitosamente.
      </p> );
      setError(null);

      setTimeout(() => {
        setIsLoading(false); // Ocultar GIF de carga
        navigate("/clientView"); // Redirigir después de 1 segundo
      }, 3000);
    } catch (error) {
      console.error(error);
      setError("Error al guardar el cliente.");
      setIsLoading(false); // Asegurarse de ocultar el GIF de carga en caso de error
    }
  };

  return (
    <div className="container mt-4">
      <h1>Registro de Cliente</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {successMessage && <p style={{ color: "green" }}>{successMessage}</p>}

      {isLoading ? (
        <div className="loading-container">
          <img
            src="https://media.giphy.com/media/3o7TKtnuHOHHUjR38Y/giphy.gif"
            alt="Cargando..."
            className="loading-gif"
          />
        </div>
      ) : (
        <form onSubmit={handleSaveClient} className="border p-4">
          <div className="mb-3">
            <label htmlFor="rut" className="form-label">RUT</label>
            <input
              id="rut"
              name="rut"
              type="text"
              className="form-control"
              value={client.rut}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="mb-3">
            <label htmlFor="firstName" className="form-label">Nombre</label>
            <input
              id="firstName"
              name="firstName"
              type="text"
              className="form-control"
              value={client.firstName}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="mb-3">
            <label htmlFor="lastName" className="form-label">Apellido</label>
            <input
              id="lastName"
              name="lastName"
              type="text"
              className="form-control"
              value={client.lastName}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="mb-3">
            <label htmlFor="email" className="form-label">Correo Electrónico</label>
            <input
              id="email"
              name="email"
              type="email"
              className="form-control"
              value={client.email}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="mb-3">
            <label htmlFor="password" className="form-label">Contraseña</label>
            <input
              id="password"
              name="password"
              type="password"
              className="form-control"
              value={client.password}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="mb-3">
            <label htmlFor="phone" className="form-label">Teléfono</label>
            <input
              id="phone"
              name="phone"
              type="text"
              className="form-control"
              value={client.phone}
              onChange={handleInputChange}
              required
            />
          </div>

          <div className="mb-3">
            <label htmlFor="age" className="form-label">Edad</label>
            <input
              id="age"
              name="age"
              type="number"
              className="form-control"
              value={client.age}
              onChange={handleInputChange}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary mt-3">
            Registrar Cliente
          </button>
        </form>
      )}
    </div>
  );
}
