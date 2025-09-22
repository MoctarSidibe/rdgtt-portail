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
  Visibility as VisibilityIcon,
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import axios from 'axios';

function Users() {
  const { user } = useAuth();
  const [users, setUsers] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [bureaus, setBureaus] = useState([]);
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingUser, setEditingUser] = useState(null);
  const [formData, setFormData] = useState({
    nomFamille: '',
    nomJeuneFille: '',
    prenom: '',
    email: '',
    telephone: '',
    roleId: '',
    departmentId: '',
    bureauId: '',
    actif: true,
  });
  const [error, setError] = useState('');

  useEffect(() => {
    fetchUsers();
    fetchDepartments();
    fetchBureaus();
    fetchRoles();
  }, []);

  const fetchUsers = async () => {
    try {
      const response = await axios.get('http://localhost:8081/api/usager/users');
      setUsers(response.data);
    } catch (error) {
      console.error('Erreur lors du chargement des utilisateurs:', error);
      setError('Erreur lors du chargement des utilisateurs');
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

  const fetchBureaus = async () => {
    try {
      const response = await axios.get('http://localhost:8081/api/usager/bureaus');
      setBureaus(response.data);
    } catch (error) {
      console.error('Erreur lors du chargement des bureaux:', error);
    }
  };

  const fetchRoles = async () => {
    // Mock roles for now - will be replaced with actual API call
    setRoles([
      { id: '1', nom: 'Administrateur', code: 'ADMIN' },
      { id: '2', nom: 'Directeur du Centre', code: 'DC' },
      { id: '3', nom: 'Agent SEV', code: 'SEV' },
      { id: '4', nom: 'Agent SAF', code: 'SAF' },
      { id: '5', nom: 'Agent STIAS', code: 'STIAS' },
      { id: '6', nom: 'Directeur Général', code: 'DGTT' },
      { id: '7', nom: 'Administrateur Auto-École', code: 'AUTO_ECOLE_ADMIN' },
      { id: '8', nom: 'Examinateur', code: 'EXAMINATEUR' },
    ]);
  };

  const handleOpenDialog = (user = null) => {
    if (user) {
      setEditingUser(user);
      setFormData({
        nomFamille: user.nomFamille || '',
        nomJeuneFille: user.nomJeuneFille || '',
        prenom: user.prenom || '',
        email: user.email || '',
        telephone: user.telephone || '',
        roleId: user.roleId || '',
        departmentId: user.departmentId || '',
        bureauId: user.bureauId || '',
        actif: user.actif !== false,
      });
    } else {
      setEditingUser(null);
      setFormData({
        nomFamille: '',
        nomJeuneFille: '',
        prenom: '',
        email: '',
        telephone: '',
        roleId: '',
        departmentId: '',
        bureauId: '',
        actif: true,
      });
    }
    setOpenDialog(true);
    setError('');
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingUser(null);
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      if (editingUser) {
        await axios.put(`http://localhost:8081/api/usager/users/${editingUser.id}`, formData);
      } else {
        await axios.post('http://localhost:8081/api/usager/users', formData);
      }
      
      fetchUsers();
      handleCloseDialog();
    } catch (error) {
      console.error('Erreur lors de la sauvegarde:', error);
      setError('Erreur lors de la sauvegarde de l\'utilisateur');
    }
  };

  const handleDelete = async (userId) => {
    if (window.confirm('Êtes-vous sûr de vouloir supprimer cet utilisateur ?')) {
      try {
        await axios.delete(`http://localhost:8081/api/usager/users/${userId}`);
        fetchUsers();
      } catch (error) {
        console.error('Erreur lors de la suppression:', error);
        setError('Erreur lors de la suppression de l\'utilisateur');
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
        <Typography variant="h4">Gestion des Utilisateurs</Typography>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog()}
        >
          Nouvel Utilisateur
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
              <TableCell>Email</TableCell>
              <TableCell>Téléphone</TableCell>
              <TableCell>Rôle</TableCell>
              <TableCell>Département</TableCell>
              <TableCell>Bureau</TableCell>
              <TableCell>Statut</TableCell>
              <TableCell>Actions</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {users.map((user) => (
              <TableRow key={user.id}>
                <TableCell>{user.fullName || `${user.prenom} ${user.nomFamille}`}</TableCell>
                <TableCell>{user.email}</TableCell>
                <TableCell>{user.telephone || '-'}</TableCell>
                <TableCell>{user.roleNom || '-'}</TableCell>
                <TableCell>{user.departmentNom || '-'}</TableCell>
                <TableCell>{user.bureauNom || '-'}</TableCell>
                <TableCell>{getStatusChip(user.actif)}</TableCell>
                <TableCell>
                  <IconButton
                    size="small"
                    onClick={() => handleOpenDialog(user)}
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    size="small"
                    onClick={() => handleDelete(user.id)}
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

      {/* User Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
        <DialogTitle>
          {editingUser ? 'Modifier l\'utilisateur' : 'Nouvel utilisateur'}
        </DialogTitle>
        <form onSubmit={handleSubmit}>
          <DialogContent>
            <Box display="flex" flexDirection="column" gap={2} sx={{ mt: 1 }}>
              <Box display="flex" gap={2}>
                <TextField
                  fullWidth
                  label="Prénom"
                  value={formData.prenom}
                  onChange={(e) => setFormData({ ...formData, prenom: e.target.value })}
                  required
                />
                <TextField
                  fullWidth
                  label="Nom de famille"
                  value={formData.nomFamille}
                  onChange={(e) => setFormData({ ...formData, nomFamille: e.target.value })}
                  required
                />
              </Box>
              <TextField
                fullWidth
                label="Nom de jeune fille"
                value={formData.nomJeuneFille}
                onChange={(e) => setFormData({ ...formData, nomJeuneFille: e.target.value })}
              />
              <TextField
                fullWidth
                label="Email"
                type="email"
                value={formData.email}
                onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                required
              />
              <TextField
                fullWidth
                label="Téléphone"
                value={formData.telephone}
                onChange={(e) => setFormData({ ...formData, telephone: e.target.value })}
              />
              <FormControl fullWidth>
                <InputLabel>Rôle</InputLabel>
                <Select
                  value={formData.roleId}
                  onChange={(e) => setFormData({ ...formData, roleId: e.target.value })}
                  required
                >
                  {roles.map((role) => (
                    <MenuItem key={role.id} value={role.id}>
                      {role.nom}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <FormControl fullWidth>
                <InputLabel>Département</InputLabel>
                <Select
                  value={formData.departmentId}
                  onChange={(e) => setFormData({ ...formData, departmentId: e.target.value })}
                >
                  {departments.map((dept) => (
                    <MenuItem key={dept.id} value={dept.id}>
                      {dept.nom}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
              <FormControl fullWidth>
                <InputLabel>Bureau</InputLabel>
                <Select
                  value={formData.bureauId}
                  onChange={(e) => setFormData({ ...formData, bureauId: e.target.value })}
                >
                  {bureaus.map((bureau) => (
                    <MenuItem key={bureau.id} value={bureau.id}>
                      {bureau.nom}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            </Box>
          </DialogContent>
          <DialogActions>
            <Button onClick={handleCloseDialog}>Annuler</Button>
            <Button type="submit" variant="contained">
              {editingUser ? 'Modifier' : 'Créer'}
            </Button>
          </DialogActions>
        </form>
      </Dialog>
    </Box>
  );
}

export default Users;
