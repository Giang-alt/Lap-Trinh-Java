import React, { createContext, useContext, useState, useEffect } from 'react';
import api from '../utils/axiosConfig';

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      setLoading(false);
      return;
    }

    // Attach token so the first verification call includes it
    api.defaults.headers.common.Authorization = `Bearer ${token}`;

    api
      .get('/api/auth/me')
      .then((response) => {
        const { id, username, role } = response.data;
        setUser({ id, username, role });
      })
      .catch((error) => {
        localStorage.removeItem('token');
        setUser(null);
        console.error('Token verification failed:', error);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);

  const login = async (username, password) => {
    try {
      const response = await api.post('/api/auth/login', {
        username,
        password,
      });

      const { token } = response.data;

      // Persist token and set default header immediately
      localStorage.setItem('token', token);
      api.defaults.headers.common.Authorization = `Bearer ${token}`;

      // Fetch current user details using the fresh token
      const meResponse = await api.get('/api/auth/me');
      const { id, username: userUsername, role: userRole } = meResponse.data;
      setUser({ id, username: userUsername, role: userRole });

      return { success: true };
    } catch (error) {
      localStorage.removeItem('token');
      setUser(null);
      return {
        success: false,
        error: error.response?.data?.error || 'Login failed',
      };
    }
  };

  const register = async (userData, userType) => {
    try {
      const endpoint = userType === 'consumer' ? '/api/auth/register/consumer' : '/api/auth/register/provider';
      await api.post(endpoint, userData);
      return { success: true };
    } catch (error) {
      return { 
        success: false, 
        error: error.response?.data?.error || 'Registration failed' 
      };
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    delete api.defaults.headers.common.Authorization;
    setUser(null);
  };

  const value = {
    user,
    login,
    register,
    logout,
    loading
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};
