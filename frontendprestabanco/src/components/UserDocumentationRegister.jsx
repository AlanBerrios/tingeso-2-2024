import React, { useState } from "react";
import gestionService from "../services/gestion.service.js";

const documentLabels = {
  incomeProof: "Comprobante de Ingresos",
  appraisalCertificate: "Certificado de Avalúo",
  creditHistory: "Historial Crediticio",
  firstPropertyDeed: "Escritura de Primera Vivienda",
  businessFinancialStatement: "Estado Financiero del Negocio",
  businessPlan: "Plan de Negocios",
  remodelingBudget: "Presupuesto de Remodelación",
  updatedAppraisalCertificate: "Certificado de Avalúo Actualizado",
  allDocumentsCompleted: "Todos los Documentos Completados",
};

export default function UserDocumentationRegister() {
  const [rut, setRut] = useState("");
  const [documentation, setDocumentation] = useState(null);
  const [rutNotFound, setRutNotFound] = useState(false); // Controla si se muestra el mensaje de error

  const handleSelectChange = (e) => {
    const { name, value } = e.target;
    setDocumentation((prevDoc) => ({
      ...prevDoc,
      [name]: value === "true" ? true : value === "false" ? false : null,
    }));
  };

  const handleSearchByRut = async (e) => {
    e.preventDefault();
    try {
      const response = await gestionService.getDocumentationByRut(rut);
      if (response.data) {
        setDocumentation(response.data);
        setRutNotFound(false); // Oculta el mensaje si se encuentra el RUT
      } else {
        setDocumentation(null);
        setRutNotFound(true); // Muestra el mensaje si no se encuentra el RUT
      }
    } catch (error) {
      console.error(error);
      setDocumentation(null);
      setRutNotFound(true); // Asegura mostrar el mensaje en caso de error
    }
  };

  const handleSaveDocumentation = async (e) => {
    e.preventDefault();

    if (Object.values(documentation).some((value) => value === null)) {
      alert("Por favor, asegúrese de que todos los documentos tengan un valor asignado.");
      return;
    }

    try {
      if (documentation.rut) {
        await gestionService.updateDocumentation(documentation);
      } else {
        await gestionService.saveDocumentation(documentation);
      }
      alert("Documentación guardada exitosamente.");
    } catch (error) {
      console.error(error);
      alert("Error al guardar la documentación.");
    }
  };

  return (
    <div className="container mt-4">
      <h1>Registro de Documentación de Cliente</h1>

      <form onSubmit={handleSearchByRut} className="mb-4">
        <div className="mb-3">
          <label htmlFor="rut" className="form-label">
            RUT del Cliente
          </label>
          <input
            id="rut"
            type="text"
            className="form-control"
            value={rut}
            onChange={(e) => setRut(e.target.value)}
          />
        </div>
        <button type="submit" className="btn btn-primary">
          Buscar
        </button>
      </form>

      {rutNotFound && ( // Mostrar mensaje en rojo si no se encuentra el RUT
        <p style={{ color: "red", fontWeight: "bold" }}>RUT no encontrado / no existe</p>
      )}

      {documentation && ( // Mostrar formulario solo si se encuentra la documentación
        <form onSubmit={handleSaveDocumentation} className="border p-4">
          <h2>Documentación del Cliente</h2>

          {Object.keys(documentation).map((key) =>
            key !== "rut" ? (
              <div className="mb-3" key={key}>
                <label htmlFor={key} className="form-label">
                  {documentLabels[key] || key}
                </label>
                <select
                  id={key}
                  name={key}
                  className="form-control"
                  value={
                    documentation[key] === true
                      ? "true"
                      : documentation[key] === false
                      ? "false"
                      : "null"
                  }
                  onChange={handleSelectChange}
                >
                  <option value="null">Nulo</option>
                  <option value="true">Entregado</option>
                  <option value="false">No Entregado</option>
                </select>
              </div>
            ) : null
          )}

          <button type="submit" className="btn btn-primary mt-3">
            Guardar Documentación
          </button>
        </form>
      )}
    </div>
  );
}
