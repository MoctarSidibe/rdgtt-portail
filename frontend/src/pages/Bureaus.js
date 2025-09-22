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
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert,
  CircularProgress,
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
} from '@mui/icons-material';
import axios from 'axios';

function Bureaus() {
  const [bureaus, setBureaus] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingBureau, setEditingBureau] = useState(null);
  const [formData, setFormData] = useState({
    nom: '',
    code: '',
    description: '',
    departmentId: '',
    actif: true,
  });
  const [error, setError] = useState('');

  useEffect(() => {
    fetchBureaus();
    fetchDepartments();
  }, []);

  const fetchBureaus = async () => {
    try {
      const response = await axios.get('http://localhost:8081/api/usager/bureaus');
      setBureaus(response.data);
    } catch (error) {
      console.error('Erreur lors du chargement des bureaux:', error);
      setError('Erreur lors du chargement des bureaux');
    } finally {
      setLoading(false);
    }
  };

  const fetchDepartments = async () => {
    try {
      const response = await axios.get('http://localhost:8081/api/usager/departments');
      setDepartments(response.data);
    } catch (error) {
      console.error('Erreur lors du chargement des départements:', error);
    }
  };

  const handleOpenDialog = (bureau = null) => {
    if (bureau) {
      setEditingBureau(bureau);
      setFormData({
        nom: bureau.nom || '',
        code: bureau.code || '',
        description: bureau.description || '',
        departmentId: bureau.department?.id || '',
        actif: bureau.actif !== false,
      });
    } else {
      setEditingBureau(null);
      setFormData({
        nom: '',
        code: '',
        description: '',
        departmentId: '',
        actif: true,
      });
    }
    setOpenDialog(true);
    setError('');
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingBureau(null);
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      const dataToSend = {
        ...formData,
        department: departments.find(d => d.id === formData.departmentId)
      };

      if (editingBureau) {
        await axios.put(`http://localhost:8081/api/usager/bureaus/${editingBureau.id}`, dataToSend);
      } else {
        await axios.post('http://localhost:8081/api/usager/bureaus', dataToSend);
      }
      
      fetchBureaus();
      handleCloseDialog();
    } catch (error) {
      console.error('Erreur lors de la sauvegarde:', error);
      setError('Erreur lors de la sauvegarde du bureau');
    }
  };

  const handleDelete = async (bureauId) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer ce bureau ?')) {
      try {
        await axios.delete(`http://localhost:8081/api/usager/bureaus/${bureauId}`);
        fetchBureaus();
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        setError('Erreur lors de la suppression du bureau');
      }
    }
  };

  const getStatusChip = (actif) => {
    return (
      <Chip
        label={actif ? 'Actif' : 'Inactif'}
        color={actif ? 'success' : 'error'}
        size="small"
      />
    );
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
        <Typography variant="h4">Gestion des Bureaux</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog()}
        >
          Nouveau Bureau
        </Button>
      </Box>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <TableContainer component={Paper}>
        <Table>
          <TableHead>
            <TableRow>
              <TableCell>Nom</TableCell>
              <TableCell>Code</TableCell>
              <TableCell>Département</TableCell>
              <TableCell>Description</TableCell>
              <TableCell>Statut</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {bureaus.map((bureau) => (
              <TableRow key={bureau.id}>
                <TableCell>{bureau.nom}</TableCell>
                <TableCell>{bureau.code}</TableCell>
                <TableCell>{bureau.department?.nom || '-'}</TableCell>
                <TableCell>{bureau.description || '-'}</TableCell>
                <TableCell>{getStatusChip(bureau.actif)}</TableCell>
                <TableCell>
                  <IconButton
                    size="small"
                    onClick={() => handleOpenDialog(bureau)}
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    size="small"
                    onClick={() => handleDelete(bureau.id)}
                    color="error"
                  >
                    <DeleteIcon />
                  </IconButton>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Bureau Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {editingBureau ? 'Modifier le bureau' : 'Nouveau bureau'}
        </DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <Box display="flex" flexDirection="column" gap={2} sx={{ mt: 1 }}>
              <TextField
                fullWidth
                label="Nom du bureau"
                value={formData.nom}
                onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                required
              />
              <TextField
                fullWidth
                label="Code"
                value={formData.code}
                onChange={(e) => setFormData({ ...formData, code: e.target.value })}
                required
              />
              <FormControl fullWidth>
                <InputLabel>Département</InputLabel>
                <Select
                  value={formData.departmentId}
                  onChange={(e) => setFormData({ ...formData, departmentId: e.target.value })}
                  required
                >
                  {departments.map((dept) => (
                    <MenuItem key={dept.id} value={dept.id}>
                      {dept.nom}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <TextField
                fullWidth
                label="Description"
                multiline
                rows={3}
                value={formData.description}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              />
            </Box>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Annuler</Button>
            <Button type="submit" variant="contained">
              {editingBureau ? 'Modifier' : 'Créer'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
}

export default Bureaus;
