// src/App.jsx
import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { BrowserRouter, Route, Routes, useLocation } from 'react-router-dom';
import Header from './components/Header';
import ClientHeader from './components/ClientHeader';
import EjecHeader from './components/EjecHeader';
import ClientJoinedHeader from './components/ClientJoinedHeader';
import Home from './components/Home';
import ClientHome from './components/ClientHome';
import ClientJoinedHome from './components/ClientJoinedHome';
import ClientList from './components/ClientList';
import EjecHome from './components/EjecHome';
import CreditSimulation from './components/CreditSimulation';
import UserDocumentationRegister from './components/UserDocumentationRegister';
import ClientSignUp from './components/ClientSignUp';
import ClientSignIn from './components/ClientSignIn';
import CreditRequest from './components/CreditRequest';
import CreditEvaluation from './components/CreditEvaluation';
import MortgageList from './components/MortgageList';

function App() {
  const location = useLocation(); // Detecta la ruta actual

  // Lógica para mostrar el encabezado correcto
  const renderHeader = () => {
    if (
      location.pathname === '/clientView' ||
      location.pathname === '/clientSingUp' ||
      location.pathname === '/clientSingIn'
    ) {
      return <ClientHeader />;

    } else if (
      location.pathname === '/ejecutiveView' ||
      location.pathname === '/clientList' ||
      location.pathname === '/userDocumentationRegister' ||
      location.pathname === '/creditRequest' ||
      location.pathname === '/mortgageList' ||
      location.pathname.startsWith('/creditEvaluation/')
    ) {
      return <EjecHeader />;

    } else if (location.pathname === '/clientJoinedView' ||
      location.pathname === '/simulateCredit'
    ) {
      return <ClientJoinedHeader />;

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
        <Route path="/clientJoinedView" element={<ClientJoinedHome />} />
        <Route path="/creditRequest" element={<CreditRequest />} />
        <Route path="/mortgageList" element={<MortgageList />} />
        <Route path="/creditEvaluation/:id" element={<CreditEvaluation />} />
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
