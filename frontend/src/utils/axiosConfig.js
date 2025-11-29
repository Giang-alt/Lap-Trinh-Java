import axios from 'axios';

// Central Axios instance that attaches the JWT and points to the backend
const api = axios.create({
  // Backend runs on 8080 with context path /api (the path is provided in each request)
  baseURL: 'http://localhost:8080',
});

// Automatically add Authorization header when a token is present
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers = config.headers || {};
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Handle unauthorized responses globally
api.interceptors.response.use(
  (response) => response,
  (error) => {
    return Promise.reject(error);
  }
);

export default api;
