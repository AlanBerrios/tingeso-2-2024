// src/App.jsx
import React from 'react';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import { HashRouter, Route, Routes, useLocation } from 'react-router-dom';
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
import ClientSignUp from './components/ClientSignUp';
import ClientSignIn from './components/ClientSignIn';
import CreditRequest from './components/CreditRequest';
import CreditEvaluation from './components/CreditEvaluation';
import MortgageList from './components/MortgageList';
import ClientDocument from './components/ClientDocument';
import DocumentationReview from './components/DocumentationReview';
import MortgageDetailsForClient from './components/MortgageDetailsForClient';
import MortgageListForClient from './components/MortgageListForClient';
import SavingsCapacity from './components/SavingCapacity';
import LoanCostCalculate from './components/LoanCostCalculate';
import EditClient from './components/EditClient';
import ClientSavingAccount from './components/ClientSavingsAccount';

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
      location.pathname === '/mortgageList' ||
      location.pathname.startsWith('/creditEvaluation/') ||
      location.pathname.startsWith('/documentationReview/') || 
      location.pathname === '/savingCapacity' ||
      location.pathname === '/loanCostCalculate' ||
      location.pathname.startsWith('/editClient/')
    ) {
      return <EjecHeader />;

    } else if (location.pathname === '/clientJoinedView' ||
      location.pathname === '/simulateCredit' ||
      location.pathname === '/creditRequest' ||
      location.pathname === '/clientDocument' ||
      location.pathname === '/mortgageListForClient' ||
      location.pathname.startsWith('/mortgageDetailsForClient/') ||
      location.pathname.startsWith('/clientSavingAccount/')
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
        <Route path="/clientSingUp" element={<ClientSignUp />} />
        <Route path="/clientSingIn" element={<ClientSignIn />} />
        <Route path="/clientJoinedView" element={<ClientJoinedHome />} />
        <Route path="/creditRequest" element={<CreditRequest />} />
        <Route path="/mortgageList" element={<MortgageList />} />
        <Route path="/creditEvaluation/:id" element={<CreditEvaluation />} />
        <Route path="/clientDocument" element={<ClientDocument />} />
        <Route path="/documentationReview/:rut" element={<DocumentationReview />} />
        <Route path="/mortgageDetailsForClient/:id" element={<MortgageDetailsForClient />} />
        <Route path="/mortgageListForClient" element={<MortgageListForClient />} />
        <Route path="/savingCapacity" element={<SavingsCapacity />} />
        <Route path="/loanCostCalculate" element={<LoanCostCalculate />} />
        <Route path="/editClient/:rut" element={<EditClient />} />
        <Route path="/clientSavingAccount/:rut" element={<ClientSavingAccount />} />
      </Routes>
    </>
  );
}

// Componente envuelto con HashRouter
export default function AppWrapper() {
  return (
    <HashRouter>
      <App />
    </HashRouter>
  );
}
