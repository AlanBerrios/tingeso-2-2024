// src/components/ClientJoinedHeader.jsx
import React from "react";
import { Link } from "react-router-dom";

export default function ClientJoinedHeader() {
  const clientRut = localStorage.getItem("clientRut");

  return (
    <nav
      className="navbar navbar-expand-lg mb-3"
      style={{
        position: 'fixed',
        top: 0,
        left: 0,
        width: '100%',
        zIndex: 1000,
        backgroundColor: '#008CBA', // Color azul para clientes autenticados
      }}
    >
      <div className="container-fluid">
        <a className="navbar-brand" href="/">PrestaBanco - Mi Cuenta</a>
        <div className="collapse navbar-collapse">
          <ul className="navbar-nav">
            <li className="nav-item">
              <Link className="nav-link" to="/clientJoinedView">Inicio</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/simulateCredit">Simular Crédito</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/creditRequest">Solicitar Crédito</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/clientDocument">Mis Documentos</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/mortgageListForClient">Mis Solicitudes de Crédito</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to={`/clientSavingAccount/${clientRut}`}>Mis Cuentas de Ahorros</Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}
