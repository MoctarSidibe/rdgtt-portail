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
  Alert,
  CircularProgress,
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Visibility as VisibilityIcon,
} from '@mui/icons-material';

function Permis() {
  const [permis, setPermis] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    // Mock data for now - will be replaced with actual API calls
    setPermis([
      {
        id: '1',
        numeroPermis: 'PER-2024-001',
        candidat: 'Jean Mba',
        categorie: 'B',
        statut: 'EN_ATTENTE',
        dateEmission: '2024-01-15',
        dateExpiration: '2025-01-15',
      },
      {
        id: '2',
        numeroPermis: 'PER-2024-002',
        candidat: 'Marie Nguema',
        categorie: 'A',
        statut: 'VALIDE',
        dateEmission: '2024-01-10',
        dateExpiration: '2025-01-10',
      },
    ]);
    setLoading(false);
  }, []);

  const getStatusChip = (statut) => {
    const statusMap = {
      EN_ATTENTE: { label: 'En attente', color: 'warning' },
      VALIDE: { label: 'Valide', color: 'success' },
      EXPIRE: { label: 'Expiré', color: 'error' },
      SUSPENDU: { label: 'Suspendu', color: 'error' },
      ANNULE: { label: 'Annulé', color: 'error' },
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
        <Typography variant="h4">Gestion des Permis</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          disabled
        >
          Nouveau Permis
        </Button>
      </Box>

      <Alert severity="info" sx={{ mb: 2 }}>
        Cette fonctionnalité sera disponible une fois le service Permis déployé.
      </Alert>

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Numéro de Permis</TableCell>
              <TableCell>Candidat</TableCell>
              <TableCell>Catégorie</TableCell>
              <TableCell>Statut</TableCell>
              <TableCell>Date d'émission</TableCell>
              <TableCell>Date d'expiration</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {permis.map((permisItem) => (
              <TableRow key={permisItem.id}>
                <TableCell>{permisItem.numeroPermis}</TableCell>
                <TableCell>{permisItem.candidat}</TableCell>
                <TableCell>{permisItem.categorie}</TableCell>
                <TableCell>{getStatusChip(permisItem.statut)}</TableCell>
                <TableCell>{permisItem.dateEmission}</TableCell>
                <TableCell>{permisItem.dateExpiration}</TableCell>
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

export default Permis;
