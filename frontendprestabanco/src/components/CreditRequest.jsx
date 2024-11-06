// src/components/CreditRequest.jsx
import React, { useState, useEffect } from "react";
import gestionService from "../services/gestion.service.js";
import { useNavigate } from "react-router-dom";

export default function CreditRequest() {
  const navigate = useNavigate();
  const [loan, setLoan] = useState({
    rut: "",
    loanType: "",
    amount: "",
    term: "",
    interestRate: "",
    maxFinancingAmount: "",
    status: "En Evaluación",
    creationDate: new Date().toISOString().slice(0, 10),
    documentRequirementsCompleted: false,
  });

  const [documentation, setDocumentation] = useState(null);
  const [error, setError] = useState("");
  const [missingDocuments, setMissingDocuments] = useState([]);
  const [isLoading, setIsLoading] = useState(false);

  const loanTypes = [
    { type: "Primera Vivienda", term: 30, financing: 80, interest: [3.5, 5] },
    { type: "Segunda Vivienda", term: 20, financing: 70, interest: [4, 6] },
    { type: "Propiedades Comerciales", term: 25, financing: 60, interest: [5, 7] },
    { type: "Remodelación", term: 15, financing: 50, interest: [4.5, 6] },
  ];

  const documentRequirements = {
    "Primera Vivienda": ["incomeProof", "appraisalCertificate", "creditHistory"],
    "Segunda Vivienda": ["incomeProof", "appraisalCertificate", "firstPropertyDeed", "creditHistory"],
    "Propiedades Comerciales": ["businessFinancialStatement", "incomeProof", "appraisalCertificate", "businessPlan"],
    "Remodelación": ["incomeProof", "remodelingBudget", "updatedAppraisalCertificate"],
  };

  useEffect(() => {
    const clientRut = localStorage.getItem("clientRut");
    if (clientRut) {
      setLoan((prevLoan) => ({ ...prevLoan, rut: clientRut }));
      fetchDocumentation(clientRut);
    } else {
      setError("RUT de cliente no encontrado. Inicie sesión nuevamente.");
    }
  }, []);

  const handleLoanTypeChange = (e) => {
    const selectedType = loanTypes.find((type) => type.type === e.target.value);
    if (e.target.value === "Primera Vivienda" && documentation?.firstPropertyDeed === true) {
      setError("No puedes solicitar un préstamo para Primera Vivienda si ya tienes una escritura registrada.");
      setLoan((prevLoan) => ({ ...prevLoan, loanType: "" }));
      return;
    }
    setError("");
    setLoan((prevLoan) => ({
      ...prevLoan,
      loanType: selectedType.type,
      term: selectedType.term,
      maxFinancingAmount: selectedType.financing,
      interestRate: "",
    }));
  };

  const fetchDocumentation = async (rut) => {
    try {
      const response = await gestionService.getDocumentationByRut(rut);
      setDocumentation(response.data);
    } catch (error) {
      setError("No se encontró la documentación para este RUT.");
      setDocumentation(null);
    }
  };

  const handleInterestRateChange = (e) => {
    setLoan((prevLoan) => ({ ...prevLoan, interestRate: parseFloat(e.target.value) }));
  };

  const handleSaveLoan = async (e) => {
    e.preventDefault();

    if (!documentation) {
      setError("Documentos no encontrados. Verifique el RUT.");
      return;
    }

    const requiredDocs = documentRequirements[loan.loanType];
    const missing = requiredDocs.filter((doc) => !documentation[doc]);

    if (missing.length > 0) {
      setMissingDocuments(missing);
      setError("Faltan documentos requeridos para este tipo de préstamo.");
      return;
    }

    try {
      setIsLoading(true);
      await gestionService.createMortgageLoan({
        ...loan,
        documentRequirementsCompleted: true,
        documentRequirementsCompletionDate: new Date().toISOString().slice(0, 10),
      });

      setError("");
      setTimeout(() => {
        setIsLoading(false);
        navigate("/mortgageListForClient");
      }, 3000);
    } catch (error) {
      console.error(error);
      setError("Error al guardar la solicitud de crédito.");
      setIsLoading(false);
    }
  };

  return (
    <div className="container mt-4 d-flex">
      <div className="w-50">
        <h1>Solicitud de Crédito Hipotecario</h1>

        {error && (
          <div>
            <p style={{ color: "red" }}>{error}</p>
            {missingDocuments.length > 0 && (
              <ul>
                {missingDocuments.map((doc) => (
                  <li key={doc} style={{ color: "red" }}>
                    {doc === "incomeProof" && "Comprobante de Ingresos"}
                    {doc === "appraisalCertificate" && "Certificado de Avalúo"}
                    {doc === "creditHistory" && "Historial Crediticio"}
                    {doc === "firstPropertyDeed" && "Escritura de la Primera Vivienda"}
                    {doc === "businessFinancialStatement" && "Estado Financiero del Negocio"}
                    {doc === "businessPlan" && "Plan de Negocios"}
                    {doc === "remodelingBudget" && "Presupuesto de Remodelación"}
                    {doc === "updatedAppraisalCertificate" && "Certificado de Avalúo Actualizado"}
                  </li>
                ))}
              </ul>
            )}
          </div>
        )}

        {isLoading ? (
          <div className="loading-container text-center">
            <p style={{ color: "green", fontSize: "24px", fontWeight: "bold" }}>
              Solicitud creada exitosamente.
            </p>
            <img
              src="https://media.giphy.com/media/3o7TKtnuHOHHUjR38Y/giphy.gif"
              alt="Cargando..."
              className="loading-gif"
            />
          </div>
        ) : (
          <form onSubmit={handleSaveLoan} className="border p-4">
            <div className="mb-3">
              <label htmlFor="rut" className="form-label">RUT</label>
              <input
                id="rut"
                name="rut"
                type="text"
                className="form-control"
                value={loan.rut}
                readOnly
              />
            </div>

            <div className="mb-3">
              <label htmlFor="loanType" className="form-label">Tipo de Préstamo</label>
              <select
                id="loanType"
                name="loanType"
                className="form-control"
                value={loan.loanType}
                onChange={handleLoanTypeChange}
                required
              >
                <option value="">Seleccione un tipo de préstamo</option>
                {loanTypes.map((type) => (
                  <option key={type.type} value={type.type}>
                    {type.type}
                  </option>
                ))}
              </select>
            </div>

            <div className="mb-3">
              <label htmlFor="amount" className="form-label">Monto del Préstamo</label>
              <input
                id="amount"
                name="amount"
                type="number"
                className="form-control"
                value={loan.amount}
                onChange={(e) => setLoan((prevLoan) => ({ ...prevLoan, amount: e.target.value }))}
                required
              />
            </div>

            <div className="mb-3">
              <label htmlFor="term" className="form-label">Plazo (años)</label>
              <input
                id="term"
                name="term"
                type="number"
                className="form-control"
                value={loan.term}
                disabled
              />
            </div>

            <div className="mb-3">
              <label htmlFor="interestRate" className="form-label">Tasa de Interés</label>
              <input
                id="interestRate"
                name="interestRate"
                type="number"
                step="0.01"
                min={loan.loanType ? loanTypes.find((type) => type.type === loan.loanType).interest[0] : 0}
                max={loan.loanType ? loanTypes.find((type) => type.type === loan.loanType).interest[1] : 0}
                className="form-control"
                value={loan.interestRate}
                onChange={handleInterestRateChange}
                required
              />
            </div>

            <button type="submit" className="btn btn-primary mt-3">
              Solicitar Crédito
            </button>
          </form>
        )}
      </div>

      <div className="w-50 p-4">
        {documentation && (
          <table className="table">
            <thead>
              <tr>
                <th>Documento</th>
                <th>Entregado</th>
              </tr>
            </thead>
            <tbody>
              {Object.entries(documentation)
                .filter(([key]) => key !== "rut" && key !== "allDocumentsCompleted")
                .map(([key, value]) => (
                  <tr key={key}>
                    <td>
                      {key === "incomeProof" && "Comprobante de Ingresos"}
                      {key === "appraisalCertificate" && "Certificado de Avalúo"}
                      {key === "creditHistory" && "Historial Crediticio"}
                      {key === "firstPropertyDeed" && "Escritura de la Primera Vivienda"}
                      {key === "businessFinancialStatement" && "Estado Financiero del Negocio"}
                      {key === "businessPlan" && "Plan de Negocios"}
                      {key === "remodelingBudget" && "Presupuesto de Remodelación"}
                      {key === "updatedAppraisalCertificate" && "Certificado de Avalúo Actualizado"}
                    </td>
                    <td>{value ? "Sí" : "No"}</td>
                  </tr>
                ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}
