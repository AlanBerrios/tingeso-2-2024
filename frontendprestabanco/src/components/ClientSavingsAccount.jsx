import React, { useEffect, useState } from "react";
import gestionService from "../services/gestion.service.js";
import { useParams } from "react-router-dom";
import dayjs from "dayjs";

export default function ClientSavingAccount() {
  const { rut } = useParams();
  const [account, setAccount] = useState(null);
  const [accountHistory, setAccountHistory] = useState([]);
  const [error, setError] = useState("");
  const [transaction, setTransaction] = useState({
    type: "deposit", // or "withdrawal"
    amount: "",
  });

  // Fetch saving account details by client rut
  const fetchSavingAccount = async () => {
    try {
      const response = await gestionService.getSavingsAccountByRut(rut);
      setAccount(response.data);
      fetchAccountHistory();
    } catch (error) {
      setAccount(null);
    }
  };

  // Fetch account history by client rut
  const fetchAccountHistory = async () => {
    try {
      const response = await gestionService.getAccountHistory(rut);
      setAccountHistory(response.data);
    } catch (error) {
      setError("Error al obtener el historial de la cuenta.");
    }
  };

  // Handle account creation
  const handleCreateAccount = async () => {
    try {
      await gestionService.createSavingsAccount({
        rut: rut,
        creationDate: dayjs().format("YYYY-MM-DD"),
        balance: 0,
      });
      fetchSavingAccount(); // Refresh account details
    } catch (error) {
      setError("Error al crear la cuenta de ahorros.");
    }
  };

  // Handle transaction submission
  const handleTransactionSubmit = async (e) => {
    e.preventDefault();
    const amount = parseFloat(transaction.amount);
    if (isNaN(amount) || amount <= 0) {
      setError("Ingrese un monto válido.");
      return;
    }

    const transactionData = {
      rut: rut,
      type: transaction.type,
      amount: transaction.type === "withdrawal" ? -amount : amount,
      date: dayjs().format("YYYY-MM-DD"),
    };

    try {
      // Add the transaction and update account balance
      await gestionService.createAccountHistory(transactionData);
      fetchSavingAccount(); // Refresh the account and history after transaction
      setTransaction({ type: "deposit", amount: "" });
    } catch (error) {
      setError("Error al procesar la transacción.");
    }
  };

  useEffect(() => {
    fetchSavingAccount();
  }, [rut]);

  return (
    <div className="container mt-4">
      <h1>Cuenta de Ahorros</h1>
      {error && <p style={{ color: "red" }}>{error}</p>}

      {account ? (
        <>
          <p>RUT del Cliente: {rut}</p>
          <p>Balance Actual: ${account.balance.toLocaleString()}</p>

          {/* Transaction form */}
          <form onSubmit={handleTransactionSubmit} className="mb-4">
            <div className="form-inline">
              <select
                value={transaction.type}
                onChange={(e) =>
                  setTransaction({ ...transaction, type: e.target.value })
                }
                className="form-select me-2"
              >
                <option value="deposit">Depósito</option>
                <option value="withdrawal">Retiro</option>
              </select>
              <input
                type="number"
                min="0"
                step="0.01"
                value={transaction.amount}
                onChange={(e) =>
                  setTransaction({ ...transaction, amount: e.target.value })
                }
                placeholder="Monto"
                className="form-control me-2"
              />
              <button type="submit" className="btn btn-primary">
                Realizar Transacción
              </button>
            </div>
          </form>

          {/* Transaction history */}
          <h2>Historial de Transacciones</h2>
          <table className="table">
            <thead>
              <tr>
                <th>Fecha</th>
                <th>Tipo</th>
                <th>Monto</th>
              </tr>
            </thead>
            <tbody>
              {accountHistory.length > 0 ? (
                accountHistory.map((history) => (
                  <tr key={history.id}>
                    <td>{dayjs(history.date).format("DD/MM/YYYY")}</td>
                    <td>{history.type === "deposit" ? "Depósito" : "Retiro"}</td>
                    <td style={{ color: history.amount < 0 ? "red" : "green" }}>
                      ${Math.abs(history.amount).toLocaleString()}
                    </td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan="3" className="text-center">
                    No hay transacciones registradas.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </>
      ) : (
        <>
          <p>El cliente {rut} no tiene cuenta de ahorros.</p>
          <button className="btn btn-primary" onClick={handleCreateAccount}>
            Crear cuenta de ahorros
          </button>
        </>
      )}
    </div>
  );
}
