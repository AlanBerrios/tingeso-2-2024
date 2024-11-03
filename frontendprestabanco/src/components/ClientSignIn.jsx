import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import gestionService from "../services/gestion.service.js";

export default function ClientSignIn() {
  const navigate = useNavigate();
  const [isLoading, setIsLoading] = useState(false);
  const [credentials, setCredentials] = useState({ rut: "", password: "" });
  const [error, setError] = useState(null);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setCredentials((prev) => ({ ...prev, [name]: value }));
  };

  const handleSignIn = async (e) => {
    e.preventDefault();

    if (!credentials.rut || !credentials.password) {
      setError("Por favor, ingrese RUT y contraseña.");
      return;
    }

    try {
      setIsLoading(true);
      const response = await gestionService.getClientByRut(credentials.rut);
      const client = response.data;

      if (!client || client.password !== credentials.password) {
        setIsLoading(false);
        setError("RUT o contraseña incorrecta.");
        return;
      }

      // Almacenar el RUT en el localStorage
      localStorage.setItem("clientRut", credentials.rut);

      setTimeout(() => {
        setIsLoading(false);
        navigate("/clientJoinedView");
      }, 1000);
    } catch (error) {
      console.error(error);
      setError("Error al iniciar sesión. Por favor, intente nuevamente.");
      setIsLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <h1>Inicio de Sesión de Cliente</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {isLoading ? (
        <div className="loading-container">
          <img
            src="https://media.giphy.com/media/3o7TKtnuHOHHUjR38Y/giphy.gif"
            alt="Cargando..."
            className="loading-gif"
          />
        </div>
      ) : (
        <form onSubmit={handleSignIn} className="border p-4">
          <div className="mb-3">
            <label htmlFor="rut" className="form-label">RUT</label>
            <input
              id="rut"
              name="rut"
              type="text"
              className="form-control"
              value={credentials.rut}
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
              value={credentials.password}
              onChange={handleInputChange}
              required
            />
          </div>

          <button type="submit" className="btn btn-primary mt-3">
            Iniciar Sesión
          </button>
        </form>
      )}
    </div>
  );
}
