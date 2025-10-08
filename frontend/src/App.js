import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';
import { Box } from '@mui/material';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import Layout from './components/Layout';
import HomePage from './pages/HomePage';
import Login from './pages/Login';
import Dashboard from './pages/Dashboard';
import Users from './pages/Users';
import Departments from './pages/Departments';
import Bureaus from './pages/Bureaus';
import AutoEcoles from './pages/AutoEcoles';
import Candidats from './pages/Candidats';
import Permis from './pages/Permis';
import Profile from './pages/Profile';
import CitizenStatusCheck from './components/CitizenStatusCheck';
import AutoEcolePortal from './pages/AutoEcolePortal';
import RelatedServices from './pages/RelatedServices';
import AdminDashboard from './pages/AdminDashboard';
import AdminWorkflows from './pages/AdminWorkflows';
import AdminProcessSteps from './pages/AdminProcessSteps';
import AdminDocumentTypes from './pages/AdminDocumentTypes';
import AdminRoles from './pages/AdminRoles';
import AdminOrganization from './pages/AdminOrganization';

function ProtectedRoute({ children }) {
  const { user, loading } = useAuth();
  
  if (loading) {
    return <div>Chargement...</div>;
  }
  
  return user ? children : <Navigate to="/login" />;
}

function AppRoutes() {
  const { user } = useAuth();
  
  if (!user) {
    return (
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/citizen/status" element={<CitizenStatusCheck />} />
        <Route path="/auto-ecole/register" element={<AutoEcolePortal />} />
        <Route path="/services/related" element={<RelatedServices />} />
        <Route path="*" element={<Navigate to="/" />} />
      </Routes>
    );
  }
  
  return (
    <Layout>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/users" element={<Users />} />
        <Route path="/departments" element={<Departments />} />
        <Route path="/bureaus" element={<Bureaus />} />
        <Route path="/auto-ecoles" element={<AutoEcoles />} />
        <Route path="/candidats" element={<Candidats />} />
        <Route path="/permis" element={<Permis />} />
        <Route path="/profile" element={<Profile />} />
        
        {/* Admin Routes */}
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="/admin/workflows" element={<AdminWorkflows />} />
        <Route path="/admin/workflows/:id/steps" element={<AdminProcessSteps />} />
        <Route path="/admin/roles" element={<AdminRoles />} />
        <Route path="/admin/organization" element={<AdminOrganization />} />
        <Route path="/admin/document-types" element={<AdminDocumentTypes />} />
        <Route path="/admin/process" element={<div>Admin Process - Coming Soon</div>} />
        
        <Route path="/login" element={<Navigate to="/dashboard" />} />
        <Route path="*" element={<Navigate to="/dashboard" />} />
      </Routes>
    </Layout>
  );
}

function App() {
  return (
    <AuthProvider>
      <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
        <AppRoutes />
      </Box>
    </AuthProvider>
  );
}

export default App;
