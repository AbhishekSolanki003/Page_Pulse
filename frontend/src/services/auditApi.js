import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 30000,
});

export async function auditUrl(url) {
  const response = await api.post('/api/audit', { url });
  return response.data;
}
