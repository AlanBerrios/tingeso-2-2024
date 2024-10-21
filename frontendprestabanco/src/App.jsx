// src/App.jsx
import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter, Route, Routes, useLocation } from 'react-router-dom';
import Header from './components/Header';
import ClientHeader from './components/ClientHeader';
import EjecHeader from './components/EjecHeader';
import Home from './components/Home';
import ClientHome from './components/ClientHome';
import ClientList from './components/ClientList';
import EjecHome from './components/EjecHome';
import CreditSimulation from './components/CreditSimulation';
import UserDocumentationRegister from './components/UserDocumentationRegister';
import ClientSignUp from './components/ClientSignUp';
import ClientSignIn from './components/ClientSignIn';

function App() {
  const location = useLocation(); // Detecta la ruta actual

  // Lógica para mostrar el encabezado correcto
  const renderHeader = () => {
    if (location.pathname === '/clientView' || location.pathname === '/simulateCredit' || location.pathname === '/clientSingUp') {
      return <ClientHeader />;
    } else if (location.pathname === '/ejecutiveView' || location.pathname === '/clientList' || location.pathname === '/userDocumentationRegister') {
      return <EjecHeader />; 
    }
    return <Header />;
  };

  return (
    <>
      {renderHeader()}
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/clientView" element={<ClientHome />} />
        <Route path="/ejecutiveView" element={<EjecHome />} />
        <Route path="/clientList" element={<ClientList />} />   
        <Route path="/simulateCredit" element={<CreditSimulation />} />
        <Route path="/userDocumentationRegister" element={<UserDocumentationRegister />} />
        <Route path="/clientSingUp" element={<ClientSignUp />} />
        <Route path="/clientSingIn" element={<ClientSignIn />} />
      </Routes>
    </>
  );
}

// Componente envuelto con BrowserRouter
export default function AppWrapper() {
  return (
    <BrowserRouter>
      <App />
    </BrowserRouter>
  );
}
