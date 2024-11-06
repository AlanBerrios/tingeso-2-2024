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
    try {
      await gestionService.updateClient(client);
      alert("Cliente actualizado exitosamente");
      navigate("/client-list"); // Redirige a la lista de clientes
    } catch (error) {
      alert("Error al actualizar el cliente.");
    }
  };

  return (
    <div className="container">
      <h2>Editar Cliente</h2>
      <form onSubmit={handleSubmit}>
        {Object.keys(client).map((key) =>
          key !== "rut" ? (
            <div className="mb-3" key={key}>
              <label className="form-label">{key}</label>
              <input
                type="text"
                className="form-control"
                name={key}
                value={client[key]}
                onChange={handleChange}
              />
            </div>
          ) : null
        )}
        <button type="submit" className="btn btn-success">
          Guardar Cambios
        </button>
      </form>
    </div>
  );
}
