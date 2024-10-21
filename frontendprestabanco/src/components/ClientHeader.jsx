// src/components/ClientHeader.jsx
import React from "react";
import { Link } from "react-router-dom";

export default function ClientHeader() {
  return (
    <nav 
      className="navbar navbar-expand-lg mb-3"
      style={{ 
        position: 'fixed', 
        top: 0, 
        left: 0, 
        width: '100%', 
        zIndex: 1000, 
        backgroundColor: '#00B96B'
      }}
    >
      <div className="container-fluid">
        <a className="navbar-brand" href="/">PrestaBanco - Cliente</a>
        <div className="collapse navbar-collapse">
          <ul className="navbar-nav">
            <li className="nav-item">
              <Link className="nav-link" to="/clientView">Inicio</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/clientSingUp">Registrarse</Link>
            </li>
            <li className="nav-item">
              <Link className="nav-link" to="/clientSingIn">Iniciar Sesión</Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}
