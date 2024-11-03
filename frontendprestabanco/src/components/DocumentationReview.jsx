import React, { useEffect, useState } from "react";
import gestionService from "../services/gestion.service.js";
import { useParams, useNavigate } from "react-router-dom";

const documentLabels = {
  incomeProof: "Comprobante de Ingresos",
  appraisalCertificate: "Certificado de Avalúo",
  creditHistory: "Historial Crediticio",
  firstPropertyDeed: "Escritura de Primera Vivienda",
  businessFinancialStatement: "Estado Financiero del Negocio",
  businessPlan: "Plan de Negocios",
  remodelingBudget: "Presupuesto de Remodelación",
  updatedAppraisalCertificate: "Certificado de Avalúo Actualizado",
};

export default function DocumentationReview() {
  const { rut } = useParams();
  const navigate = useNavigate();
  const [documentation, setDocumentation] = useState(null);
  const [clientDocuments, setClientDocuments] = useState([]);
  const [reviewStatus, setReviewStatus] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const fetchDocumentation = async () => {
      try {
        const response = await gestionService.getDocumentationByRut(rut);
        if (response.status === 200 && response.data) {
          setDocumentation(response.data);

          const clientDocsResponse = await gestionService.getClientDocumentsByRut(rut);
          setClientDocuments(clientDocsResponse.data);
        } else {
          setError("No se encontró la documentación del cliente.");
        }
      } catch (error) {
        setError("Error al cargar la documentación.");
      }
    };

    fetchDocumentation();
  }, [rut]);

  const handleDownload = async (documentId) => {
    if (!documentId) {
      setError("No se puede descargar el documento: ID de documento no válido.");
      return;
    }
    try {
      const response = await gestionService.downloadClientDocument(documentId);
      const blob = new Blob([response.data], { type: response.headers["content-type"] });
      const link = document.createElement("a");
      link.href = URL.createObjectURL(blob);
      link.download = `document_${documentId}.pdf`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    } catch (err) {
      setError("Documento no encontrado o no entregado.");
    }
  };

  const handleSaveReview = async () => {
    try {
      const updatedDoc = { ...documentation, allDocumentsCompleted: reviewStatus === "Aceptado" };
      await gestionService.updateDocumentation(updatedDoc);
      const creditEvaluationId = localStorage.getItem("creditEvaluationId");
      navigate(`/creditEvaluation/${creditEvaluationId}`);
    } catch (error) {
      setError("Error al guardar el estado de la documentación.");
    }
  };

  return (
    <div className="container mt-4">
      <h1>Revisión de Documentación</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {documentation ? (
        <table className="table">
          <thead>
            <tr>
              <th>Documento</th>
              <th>Estado</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            {Object.entries(documentation)
              .filter(([key]) => key !== "rut" && key !== "allDocumentsCompleted")
              .map(([key, value]) => {
                const document = clientDocuments.find((doc) => doc.documentType === key);
                return (
                  <tr key={key}>
                    <td>{documentLabels[key] || key}</td>
                    <td>{value ? "Entregado" : "No entregado"}</td>
                    <td>
                      <button
                        className="btn btn-primary"
                        onClick={() => handleDownload(document?.id)}
                        disabled={!document}
                      >
                        Descargar
                      </button>
                    </td>
                  </tr>
                );
              })}
          </tbody>
        </table>
      ) : (
        <p>Cargando documentación del cliente...</p>
      )}
      <div className="mt-4">
        <label htmlFor="reviewStatus" className="form-label">
          Estado de Revisión de la Documentación:
        </label>
        <select
          id="reviewStatus"
          value={reviewStatus}
          onChange={(e) => setReviewStatus(e.target.value)}
          className="form-select"
        >
          <option value="">Seleccionar...</option>
          <option value="Aceptado">Aceptado</option>
          <option value="Rechazado">Rechazado</option>
        </select>
        <button className="btn btn-success mt-3" onClick={handleSaveReview} disabled={!reviewStatus}>
          Guardar Elección
        </button>
      </div>
    </div>
  );
}
