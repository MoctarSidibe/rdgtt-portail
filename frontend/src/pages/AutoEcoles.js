import React, { useState, useEffect } from 'react';
import {
  Box,
  Typography,
  Button,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  Chip,
  IconButton,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Alert,
  CircularProgress,
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
} from '@mui/icons-material';

function AutoEcoles() {
  const [autoEcoles, setAutoEcoles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    // Mock data for now - will be replaced with actual API calls
    setAutoEcoles([
      {
        id: '1',
        nom: 'Auto-École Excellence',
        adresse: 'Libreville, Gabon',
        telephone: '+241 01 23 45 67',
        email: 'contact@excellence.ga',
        directeurNom: 'Jean Mba',
        statut: 'EN_ATTENTE',
        createdAt: '2024-01-15',
      },
      {
        id: '2',
        nom: 'Auto-École Pro',
        adresse: 'Port-Gentil, Gabon',
        telephone: '+241 01 23 45 68',
        email: 'contact@pro.ga',
        directeurNom: 'Marie Nguema',
        statut: 'APPROUVE',
        createdAt: '2024-01-10',
      },
    ]);
    setLoading(false);
  }, []);

  const getStatusChip = (statut) => {
    const statusMap = {
      EN_ATTENTE: { label: 'En attente', color: 'warning' },
      EN_REVUE: { label: 'En revue', color: 'info' },
      INSPECTION_PREVUE: { label: 'Inspection prévue', color: 'info' },
      INSPECTION_EFFECTUEE: { label: 'Inspection effectuée', color: 'info' },
      APPROUVE: { label: 'Approuvé', color: 'success' },
      REJETE: { label: 'Rejeté', color: 'error' },
      SUSPENDU: { label: 'Suspendu', color: 'error' },
    };
    
    const status = statusMap[statut] || { label: statut, color: 'default' };
    return <Chip label={status.label} color={status.color} size="small" />;
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box>
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Typography variant="h4">Gestion des Auto-Écoles</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          disabled
        >
          Nouvelle Auto-École
        </Button>
      </Box>

      <Alert severity="info" sx={{ mb: 2 }}>
        Cette fonctionnalité sera disponible une fois le service Auto-École déployé.
      </Alert>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Nom</TableCell>
              <TableCell>Adresse</TableCell>
              <TableCell>Téléphone</TableCell>
              <TableCell>Directeur</TableCell>
              <TableCell>Statut</TableCell>
              <TableCell>Date de création</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {autoEcoles.map((autoEcole) => (
              <TableRow key={autoEcole.id}>
                <TableCell>{autoEcole.nom}</TableCell>
                <TableCell>{autoEcole.adresse}</TableCell>
                <TableCell>{autoEcole.telephone}</TableCell>
                <TableCell>{autoEcole.directeurNom}</TableCell>
                <TableCell>{getStatusChip(autoEcole.statut)}</TableCell>
                <TableCell>{autoEcole.createdAt}</TableCell>
                <TableCell>
                  <IconButton size="small">
                    <VisibilityIcon />
                  </IconButton>
                  <IconButton size="small">
                    <EditIcon />
                  </IconButton>
                  <IconButton size="small" color="error">
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
    </Box>
  );
}

export default AutoEcoles;
