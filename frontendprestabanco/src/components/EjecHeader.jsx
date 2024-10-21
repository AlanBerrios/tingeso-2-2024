// src/components/EjecHeader.jsx
import React from "react";
import { Link } from "react-router-dom";

export default function EjecHeader() {
  return (
    <nav 
      className="navbar navbar-expand-lg mb-3"
      style={{ 
        position: 'fixed', 
        top: 0, 
        left: 0, 
        width: '100%', 
        zIndex: 1000, 
        backgroundColor: '#FFDD00'  // Amarillo para el ejecutivo
      }}
    >
      <div className="container-fluid">
        <a className="navbar-brand" href="/">PrestaBanco - Ejecutivo</a>
        <div className="collapse navbar-collapse">
          <ul className="navbar-nav">
            <li className="nav-item">
              <Link className="nav-link" to="/ejecutiveView">Inicio</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/clientList">Lista de Clientes</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/userDocumentationRegister">Registrar Documentos de Cliente</Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}
