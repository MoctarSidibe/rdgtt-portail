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

function Candidats() {
  const [candidats, setCandidats] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    // Mock data for now - will be replaced with actual API calls
    setCandidats([
      {
        id: '1',
        nomFamille: 'Mba',
        prenom: 'Jean',
        telephone: '+241 01 23 45 67',
        nationalite: 'Gabonaise',
        statut: 'EN_ATTENTE',
        autoEcole: 'Auto-École Excellence',
        createdAt: '2024-01-15',
      },
      {
        id: '2',
        nomFamille: 'Nguema',
        prenom: 'Marie',
        telephone: '+241 01 23 45 68',
        nationalite: 'Gabonaise',
        statut: 'EN_FORMATION',
        autoEcole: 'Auto-École Pro',
        createdAt: '2024-01-10',
      },
    ]);
    setLoading(false);
  }, []);

  const getStatusChip = (statut) => {
    const statusMap = {
      EN_ATTENTE: { label: 'En attente', color: 'warning' },
      EN_FORMATION: { label: 'En formation', color: 'info' },
      CODE_EN_COURS: { label: 'Code en cours', color: 'info' },
      CODE_VALIDE: { label: 'Code validé', color: 'success' },
      CRENEAU_EN_COURS: { label: 'Créneau en cours', color: 'info' },
      CRENEAU_VALIDE: { label: 'Créneau validé', color: 'success' },
      PRATIQUE_EN_COURS: { label: 'Pratique en cours', color: 'info' },
      PRATIQUE_VALIDE: { label: 'Pratique validé', color: 'success' },
      ADMIS: { label: 'Admis', color: 'success' },
      SUSPENDU: { label: 'Suspendu', color: 'error' },
      REJETE: { label: 'Rejeté', color: 'error' },
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
        <Typography variant="h4">Gestion des Candidats</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          disabled
        >
          Nouveau Candidat
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
              <TableCell>Téléphone</TableCell>
              <TableCell>Nationalité</TableCell>
              <TableCell>Auto-École</TableCell>
              <TableCell>Statut</TableCell>
              <TableCell>Date d'inscription</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {candidats.map((candidat) => (
              <TableRow key={candidat.id}>
                <TableCell>{candidat.prenom} {candidat.nomFamille}</TableCell>
                <TableCell>{candidat.telephone}</TableCell>
                <TableCell>{candidat.nationalite}</TableCell>
                <TableCell>{candidat.autoEcole}</TableCell>
                <TableCell>{getStatusChip(candidat.statut)}</TableCell>
                <TableCell>{candidat.createdAt}</TableCell>
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

export default Candidats;
