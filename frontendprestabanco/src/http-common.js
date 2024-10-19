import axios from 'axios'

const payrollBackendServer = import.meta.env.VITE_APP_BACKEND_SERVER;
const payrollBackendPort = import.meta.env.VITE_APP_BACKEND_PORT;

console.log(payrollBackendServer, payrollBackendServer);

export default axios.create({
    baseURL: `http://${payrollBackendServer}:${payrollBackendPort}`,
})