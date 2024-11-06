import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import gestionService from "../services/gestion.service.js";

export default function ClientList() {
  const [clients, setClients] = useState([]);

  async function fetchClients() {
    try {
      const response = await gestionService.getClients();
      setClients(response.data);
    } catch (error) {
      alert("Error al obtener los clientes.");
    }
  }

  useEffect(() => {
    fetchClients();
  }, []);

  const handleDelete = async (rut) => {
    try {
      const response = await gestionService.deleteClientByRut(rut);
      if (response.status === 200) {
        alert("Cliente eliminado exitosamente.");
        // Actualizar la lista de clientes eliminando el cliente borrado
        setClients(clients.filter(client => client.rut !== rut));
      } else {
        alert("Error al eliminar el cliente.");
      }
    } catch (error) {
      alert("Error al eliminar el cliente: " + (error.response ? error.response.data : error.message));
    }
  };

  return (
    <div className="container">
      <h1>Lista de Clientes</h1>
      <table className="table">
        <thead>
          <tr>
            <th scope="col">RUT</th>
            <th scope="col">Nombre</th>
            <th scope="col">Apellido</th>
            <th scope="col">Correo</th>
            <th scope="col">Teléfono</th>
            <th scope="col">Ingresos</th>
            <th scope="col">Historial Crediticio</th>
            <th scope="col">Edad</th>
            <th scope="col">Tipo de Empleo</th>
            <th scope="col">Antigüedad Empleo</th>
            <th scope="col">Estado Historial</th>
            <th scope="col">Deudas Pendientes</th>
            <th scope="col">Acciones</th>
          </tr>
        </thead>
        <tbody>
          {clients.map((client, index) => (
            <tr key={index}>
              <td>{client.rut}</td>
              <td>{client.firstName}</td>
              <td>{client.lastName}</td>
              <td>{client.email}</td>
              <td>{client.phone}</td>
              <td>{client.income}</td>
              <td>{client.creditHistory}</td>
              <td>{client.age}</td>
              <td>{client.employmentType}</td>
              <td>{client.employmentSeniority}</td>
              <td>{client.historyStatus}</td>
              <td>{client.pendingDebts}</td>
              <td>
                <Link to={`/editClient/${client.rut}`} className="btn btn-primary me-2">
                  Editar
                </Link>
                <button onClick={() => handleDelete(client.rut)} className="btn btn-danger">
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
