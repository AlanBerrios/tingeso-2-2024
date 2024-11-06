import React, { useState, useEffect } from "react";
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
};

export default function ClientDocument() {
  const [documentation, setDocumentation] = useState(null);
  const [clientDocuments, setClientDocuments] = useState([]);
  const [error, setError] = useState("");
  const [uploadError, setUploadError] = useState("");
  const [uploadSuccess, setUploadSuccess] = useState("");

  const clientRut = localStorage.getItem("clientRut");

  useEffect(() => {
    if (clientRut) {
      fetchDocumentationStatus(clientRut);
      fetchClientDocuments(clientRut);
    } else {
      setError("RUT de cliente no encontrado. Inicie sesión nuevamente.");
    }
  }, [clientRut]);

  const fetchDocumentationStatus = async (rut) => {
    try {
      const response = await gestionService.getDocumentationByRut(rut);
      setDocumentation(response.data);
    } catch (err) {
      setError("Error al obtener el estado de los documentos.");
    }
  };

  const fetchClientDocuments = async (rut) => {
    try {
      const response = await gestionService.getClientDocumentsByRut(rut);
      setClientDocuments(response.data);
    } catch (err) {
      setError("Error al obtener los documentos del cliente.");
    }
  };

  const handleDownload = async (documentId) => {
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

  const handleUpload = async (e, documentType) => {
    const file = e.target.files[0];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("clientRut", clientRut);
    formData.append("documentType", documentType);

    try {
      setUploadError("");
      setUploadSuccess("");

      // Buscar y eliminar el documento existente antes de subir uno nuevo
      const existingDocument = clientDocuments.find((doc) => doc.documentType === documentType);
      if (existingDocument) {
        await gestionService.deleteClientDocument(existingDocument.id);
      }

      const response = await gestionService.uploadClientDocument(formData);

      if (response.status === 200) {
        setUploadSuccess("Documento subido exitosamente.");
        
        fetchDocumentationStatus(clientRut);
        fetchClientDocuments(clientRut);
      } else {
        setUploadError("Error al subir el documento.");
      }
    } catch (err) {
      setUploadError("Error al subir el documento.");
    }
  };

  const handleDelete = async (documentId, documentType) => {
    try {
      const response = await gestionService.deleteClientDocument(documentId);
      if (response.status === 200) {
        setClientDocuments((prevDocuments) =>
          prevDocuments.filter((doc) => doc.id !== documentId)
        );

        const isDocumentTypeEmpty = clientDocuments.filter((doc) => doc.documentType === documentType).length === 1;
        if (isDocumentTypeEmpty) {
          // Actualizar el estado en `Documentation` en la base de datos a `false`
          const updatedDocumentation = { ...documentation, [documentType]: false };
          await gestionService.updateDocumentation(updatedDocumentation);
          
          // Refrescar el estado local de la documentación
          setDocumentation(updatedDocumentation);
        }
        setError("");
      } else {
        setError("Error al eliminar el documento.");
      }
    } catch (err) {
      setError("Error al eliminar el documento.");
    }
  };

  return (
    <div className="container mt-4">
      <h1>Documentos del Cliente</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}
      {uploadSuccess && <p style={{ color: "green" }}>{uploadSuccess}</p>}
      {uploadError && <p style={{ color: "red" }}>{uploadError}</p>}

      {documentation && (
        <table className="table">
          <thead>
            <tr>
              <th>Documentosss</th>
              <th>Estado</th>
              <th>Nombre de Archivo</th>
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
                    <td>{document ? document.documentName : "No hay archivo"}</td>
                    <td>
                      <label className="btn btn-info me-2">
                        {document ? "Actualizar archivo" : "Subir archivo"}
                        <input
                          type="file"
                          accept="application/pdf"
                          onChange={(e) => handleUpload(e, key)}
                          style={{ display: "none" }}
                        />
                      </label>
                      <button
                        className="btn btn-primary me-2"
                        onClick={() => handleDownload(document?.id)}
                        disabled={!value || !document}
                      >
                        Descargar
                      </button>
                      <button
                        className="btn btn-danger"
                        onClick={() => handleDelete(document?.id, key)}
                        disabled={!value || !document}
                      >
                        Eliminar
                      </button>
                    </td>
                  </tr>
                );
              })}
          </tbody>
        </table>
      )}
    </div>
  );
}
