import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import gestionService from "../services/gestion.service.js";

export default function EditClient() {
  const { rut } = useParams();
  const navigate = useNavigate();

  const [client, setClient] = useState({
    rut: "",
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    phone: "",
    income: 0,
    creditHistory: "",
    age: 0,
    employmentType: "",
    employmentSeniority: 0,
    historyStatus: "",
    pendingDebts: 0,
  });

  useEffect(() => {
    const fetchClientData = async () => {
      try {
        const response = await gestionService.getClientByRut(rut);
        setClient(response.data);
      } catch (error) {
        alert("Error al obtener los datos del cliente.");
      }
    };
    fetchClientData();
  }, [rut]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setClient({ ...client, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    // Validar campos obligatorios
    for (const [key, value] of Object.entries(client)) {
      if (value === "" && key !== "income" && key !== "pendingDebts") {
        alert("Este cuadro no puede estar vacío");
        return;
      }
    }

    // Establecer valores en 0 si están vacíos para `income` y `pendingDebts`
    setClient((prevClient) => ({
      ...prevClient,
      income: prevClient.income || 0,
      pendingDebts: prevClient.pendingDebts || 0,
    }));

    try {
      await gestionService.updateClient(client);
      alert("Cliente actualizado exitosamente");
      navigate("/clientList");
    } catch (error) {
      alert("Error al actualizar el cliente.");
    }
  };

  return (
    <div className="container">
      <h2>Editar Cliente - Rut: {client.rut}</h2>
      <form onSubmit={handleSubmit}>
        <div className="row">
          {Object.keys(client).map((key) => {
            if (key === "rut") return null; // No permitir editar el RUT

            // Mapeo de etiquetas en español
            const labels = {
              firstName: "Nombre",
              lastName: "Apellido",
              email: "Correo",
              password: "Contraseña",
              phone: "Teléfono",
              income: "Ingresos",
              creditHistory: "Historial Crediticio",
              age: "Edad",
              employmentType: "Tipo de Empleo",
              employmentSeniority: "Antigüedad en Empleo",
              historyStatus: "Estado del Historial",
              pendingDebts: "Deudas Pendientes",
            };

            return (
              <div className="col-md-6 mb-3" key={key}>
                <label className="form-label">{labels[key]}</label>

                {key === "creditHistory" ? (
                  <select
                    className="form-select"
                    name={key}
                    value={client[key]}
                    onChange={handleChange}
                    required
                  >
                    <option value="">Seleccione</option>
                    <option value="Good">Bueno</option>
                    <option value="Moderate">Moderado</option>
                    <option value="Weak">Débil</option>
                  </select>
                ) : key === "historyStatus" ? (
                  <select
                    className="form-select"
                    name={key}
                    value={client[key]}
                    onChange={handleChange}
                    required
                  >
                    <option value="">Seleccione</option>
                    <option value="Good">Bueno</option>
                    <option value="Moderate">Moderado</option>
                    <option value="Bad">Malo</option>
                  </select>
                ) : key === "employmentType" ? (
                  <select
                    className="form-select"
                    name={key}
                    value={client[key]}
                    onChange={handleChange}
                    required
                  >
                    <option value="">Seleccione</option>
                    <option value="Dependent">Dependiente</option>
                    <option value="Independent">Independiente</option>
                  </select>
                ) : (
                  <input
                    type={key === "age" || key === "income" || key === "pendingDebts" ? "number" : "text"}
                    className="form-control"
                    name={key}
                    value={client[key]}
                    onChange={handleChange}
                    onBlur={() => {
                      if ((key === "income" || key === "pendingDebts") && client[key] === "") {
                        setClient((prevClient) => ({ ...prevClient, [key]: 0 }));
                      }
                    }}
                    required
                  />
                )}
              </div>
            );
          })}
        </div>
        <div className="d-flex">
          <button type="submit" className="btn btn-success me-2">
            Guardar Cambios
          </button>
          <button
            type="button"
            className="btn btn-secondary"
            onClick={() => navigate("/clientList")}
          >
            Cancelar Edición
          </button>
        </div>
      </form>
    </div>
  );
}
