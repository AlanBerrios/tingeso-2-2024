import axios from "axios";

const BASE_URL = "http://localhost:8091/api/v1";
const CLIENTS_API_URL = `${BASE_URL}/clients/`;
const SAVINGS_ACCOUNTS_API_URL = `${BASE_URL}/savings-accounts/`;
const ACCOUNT_HISTORY_API_URL = `${BASE_URL}/account-history/`;
const CREDIT_REQUESTS_API_URL = `${BASE_URL}/credit-requests/`;
const DEBTS_API_URL = `${BASE_URL}/debts/`;
const DOCUMENTATION_API_URL = `${BASE_URL}/documentation/`;
const JOBS_API_URL = `${BASE_URL}/jobs/`;
const LOAN_COST_API_URL = `${BASE_URL}/loan-costs/`;
const MORTGAGE_LOANS_API_URL = `${BASE_URL}/mortgage-loans/`;
const REQUEST_TRACKING_API_URL = `${BASE_URL}/request-tracking/`;
const CREDIT_EVALUATIONS_API_URL = `${BASE_URL}/credit-evaluations/`;

// Función para simular crédito hipotecario
function simulateCredit(principal, annualInterestRate, termInYears) {
  return axios.get(`${CREDIT_EVALUATIONS_API_URL}simulate`, {
    params: { principal, annualInterestRate, termInYears },
  });
}

function feeIncomeRelation(clientMonthlyIncome, loanMonthlyPayment) {
  return axios.get(`${CREDIT_EVALUATIONS_API_URL}fee-income-relation`, {
    params: { clientMonthlyIncome, loanMonthlyPayment },
  });
}

// Función para obtener el historial crediticio del cliente por su RUTfunction checkCreditHistory(rut) {
function checkCreditHistory(rut) {
    return axios.get(`${CREDIT_EVALUATIONS_API_URL}credit-history/${rut}`);
}
  
function checkDebtIncomeRelation(rut) {
  return axios.get(`${CREDIT_EVALUATIONS_API_URL}debt-income-relation/${rut}`);
}

function checkMaxFinancingAmount(loanType, loanAmount, propertyValue) {
  return axios.get(`${CREDIT_EVALUATIONS_API_URL}max-financing-amount`, {
    params: { loanType, loanAmount, propertyValue },
  });
}

// Función para verificar la condición de edad
function checkAgeCondition(rut, term) {
  return axios.get(`${CREDIT_EVALUATIONS_API_URL}age-condition/${rut}`, {
    params: { term }
  });
}

// Funciones para clientes
function createClient(client) {
  return axios.post(CLIENTS_API_URL, client);
}

function getClients() {
  return axios.get(CLIENTS_API_URL);
}

function getClientByRut(rut) {
  return axios.get(`${CLIENTS_API_URL}${rut}`);
}

// Funciones para cuentas de ahorro
function createSavingsAccount(account) {
  return axios.post(SAVINGS_ACCOUNTS_API_URL, account);
}

function getSavingsAccounts() {
  return axios.get(SAVINGS_ACCOUNTS_API_URL);
}

function getSavingsAccountByRut(rut) {
  return axios.get(`${SAVINGS_ACCOUNTS_API_URL}${rut}`);
}

function updateSavingsAccount(account) {
  return axios.put(SAVINGS_ACCOUNTS_API_URL, account);
}

function deleteSavingsAccount(id) {
  return axios.delete(`${SAVINGS_ACCOUNTS_API_URL}${id}`);
}

// Funciones para historial de cuentas
function createAccountHistory(history) {
  return axios.post(ACCOUNT_HISTORY_API_URL, history);
}

function getAccountHistory(rut) {
  return axios.get(`${ACCOUNT_HISTORY_API_URL}${rut}`);
}

// Funciones para solicitudes de crédito
function createCreditRequest(request) {
  return axios.post(CREDIT_REQUESTS_API_URL, request);
}

function getCreditRequests() {
  return axios.get(CREDIT_REQUESTS_API_URL);
}

function updateCreditRequest(request) {
  return axios.put(CREDIT_REQUESTS_API_URL, request);
}

function deleteCreditRequest(rut) {
  return axios.delete(`${CREDIT_REQUESTS_API_URL}${rut}`);
}

// Funciones para deudas
function createDebt(debt) {
  return axios.post(DEBTS_API_URL, debt);
}

function getAllDebts() {
  return axios.get(DEBTS_API_URL);
}

function getDebtsByRut(rut) {
  return axios.get(`${DEBTS_API_URL}${rut}`);
}

function updateDebt(debt) {
  return axios.put(DEBTS_API_URL, debt);
}

function deleteDebt(id) {
  return axios.delete(`${DEBTS_API_URL}${id}`);
}

// Funciones para documentación
function createDocumentation(doc) {
  return axios.post(DOCUMENTATION_API_URL, doc);
}

function getDocumentation() {
  return axios.get(DOCUMENTATION_API_URL);
}

function getDocumentationByRut(rut) {
  return axios.get(`${DOCUMENTATION_API_URL}${rut}`);
}

function updateDocumentation(doc) {
  return axios.put(DOCUMENTATION_API_URL, doc);
}

function deleteDocumentation(rut) {
  return axios.delete(`${DOCUMENTATION_API_URL}${rut}`);
}

// Funciones para trabajos
function createJob(job) {
  return axios.post(JOBS_API_URL, job);
}

function getJobs() {
  return axios.get(JOBS_API_URL);
}

function getJobsByRut(rut) {
  return axios.get(`${JOBS_API_URL}${rut}`);
}

function updateJob(job) {
  return axios.put(JOBS_API_URL, job);
}

function deleteJob(rut) {
  return axios.delete(`${JOBS_API_URL}${rut}`);
}

// Funciones para costos de préstamo
function getLoanCosts() {
  return axios.get(LOAN_COST_API_URL);
}

// Funciones para préstamos hipotecarios
function createMortgageLoan(loan) {
  return axios.post(MORTGAGE_LOANS_API_URL, loan);
}

function getMortgageLoans() {
  return axios.get(MORTGAGE_LOANS_API_URL);
}

function getMortgageLoanByRut(rut) {
  return axios.get(`${MORTGAGE_LOANS_API_URL}rut/${rut}`);
}

function getMortgageLoanById(id) {
  return axios.get(`${MORTGAGE_LOANS_API_URL}id/${id}`);}

function updateMortgageLoan(loan) {
  return axios.put(MORTGAGE_LOANS_API_URL, loan);
}

function deleteMortgageLoan(rut) {
  return axios.delete(`${MORTGAGE_LOANS_API_URL}${rut}`);
}

// Funciones para seguimiento de solicitudes
function createRequestTracking(tracking) {
  return axios.post(REQUEST_TRACKING_API_URL, tracking);
}

function getRequestTracking() {
  return axios.get(REQUEST_TRACKING_API_URL);
}

function updateRequestStatus(rut, status, comments) {
  return axios.put(`${REQUEST_TRACKING_API_URL}${rut}`, { status, comments });
}

function deleteRequestTracking(rut) {
  return axios.delete(`${REQUEST_TRACKING_API_URL}${rut}`);
}

// Exportación predeterminada como un objeto
export default {
  simulateCredit,
  createClient,
  getClients,
  getClientByRut,
  createSavingsAccount,
  getSavingsAccounts,
  getSavingsAccountByRut,
  updateSavingsAccount,
  deleteSavingsAccount,
  createAccountHistory,
  getAccountHistory,
  createCreditRequest,
  getCreditRequests,
  updateCreditRequest,
  deleteCreditRequest,
  createDebt,
  getAllDebts,
  getDebtsByRut,
  updateDebt,
  deleteDebt,
  createDocumentation,
  getDocumentation,
  getDocumentationByRut,
  updateDocumentation,
  deleteDocumentation,
  createJob,
  getJobs,
  getJobsByRut,
  updateJob,
  deleteJob,
  getLoanCosts,
  createMortgageLoan,
  getMortgageLoans,
  getMortgageLoanByRut,
  getMortgageLoanById,
  updateMortgageLoan,
  deleteMortgageLoan,
  createRequestTracking,
  getRequestTracking,
  updateRequestStatus,
  deleteRequestTracking,
  feeIncomeRelation,
  checkCreditHistory,
  checkDebtIncomeRelation,
  checkMaxFinancingAmount,
  checkAgeCondition,
};
