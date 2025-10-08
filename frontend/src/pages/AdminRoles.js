import React, { useState, useEffect } from 'react';
import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
  IconButton,
  Paper,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  Switch,
  FormControlLabel,
  Accordion,
  AccordionSummary,
  AccordionDetails,
  Avatar,
  Tooltip,
  Alert,
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  ExpandMore as ExpandMoreIcon,
  Security as SecurityIcon,
  Person as PersonIcon,
  AdminPanelSettings as AdminIcon,
  Business as BusinessIcon,
  CheckCircle as CheckIcon,
  Cancel as CancelIcon,
  Settings as SettingsIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

const mockRoles = [
  {
    id: 1,
    name: 'Administrateur Système',
    code: 'ADMIN',
    description: 'Accès complet au système, gestion des utilisateurs et configuration',
    permissions: [
      'users.create', 'users.read', 'users.update', 'users.delete',
      'workflows.create', 'workflows.read', 'workflows.update', 'workflows.delete',
      'roles.create', 'roles.read', 'roles.update', 'roles.delete',
      'departments.create', 'departments.read', 'departments.update', 'departments.delete',
      'bureaus.create', 'bureaus.read', 'bureaus.update', 'bureaus.delete',
      'processes.create', 'processes.read', 'processes.update', 'processes.delete',
      'reports.read', 'settings.update'
    ],
    userCount: 3,
    isActive: true,
    createdAt: '2024-01-01',
    color: '#d32f2f'
  },
  {
    id: 2,
    name: 'Chef de Service',
    code: 'CHEF_SERVICE',
    description: 'Gestion des processus et validation des demandes importantes',
    permissions: [
      'workflows.read', 'workflows.update',
      'processes.create', 'processes.read', 'processes.update',
      'departments.read', 'bureaus.read',
      'reports.read'
    ],
    userCount: 8,
    isActive: true,
    createdAt: '2024-01-02',
    color: '#1976d2'
  },
  {
    id: 3,
    name: 'Agent',
    code: 'AGENT',
    description: 'Traitement des demandes et saisie des données',
    permissions: [
      'processes.read', 'processes.update',
      'candidats.create', 'candidats.read', 'candidats.update',
      'permis.create', 'permis.read', 'permis.update'
    ],
    userCount: 25,
    isActive: true,
    createdAt: '2024-01-03',
    color: '#2e7d32'
  },
  {
    id: 4,
    name: 'Validateur',
    code: 'VALIDATEUR',
    description: 'Validation des documents et approbation des demandes',
    permissions: [
      'processes.read', 'processes.update',
      'documents.validate', 'demandes.approve'
    ],
    userCount: 12,
    isActive: true,
    createdAt: '2024-01-04',
    color: '#ed6c02'
  },
  {
    id: 5,
    name: 'Citoyen',
    code: 'CITOYEN',
    description: 'Accès limité pour consultation et dépôt de demandes',
    permissions: [
      'demandes.create', 'demandes.read',
      'status.read', 'documents.upload'
    ],
    userCount: 1500,
    isActive: true,
    createdAt: '2024-01-05',
    color: '#9c27b0'
  }
];

const permissionCategories = {
  'Gestion des Utilisateurs': [
    { key: 'users.create', label: 'Créer des utilisateurs' },
    { key: 'users.read', label: 'Lire les utilisateurs' },
    { key: 'users.update', label: 'Modifier les utilisateurs' },
    { key: 'users.delete', label: 'Supprimer les utilisateurs' }
  ],
  'Gestion des Workflows': [
    { key: 'workflows.create', label: 'Créer des workflows' },
    { key: 'workflows.read', label: 'Lire les workflows' },
    { key: 'workflows.update', label: 'Modifier les workflows' },
    { key: 'workflows.delete', label: 'Supprimer les workflows' }
  ],
  'Gestion des Rôles': [
    { key: 'roles.create', label: 'Créer des rôles' },
    { key: 'roles.read', label: 'Lire les rôles' },
    { key: 'roles.update', label: 'Modifier les rôles' },
    { key: 'roles.delete', label: 'Supprimer les rôles' }
  ],
  'Gestion des Départements': [
    { key: 'departments.create', label: 'Créer des départements' },
    { key: 'departments.read', label: 'Lire les départements' },
    { key: 'departments.update', label: 'Modifier les départements' },
    { key: 'departments.delete', label: 'Supprimer les départements' }
  ],
  'Gestion des Bureaux': [
    { key: 'bureaus.create', label: 'Créer des bureaux' },
    { key: 'bureaus.read', label: 'Lire les bureaux' },
    { key: 'bureaus.update', label: 'Modifier les bureaux' },
    { key: 'bureaus.delete', label: 'Supprimer les bureaux' }
  ],
  'Gestion des Processus': [
    { key: 'processes.create', label: 'Créer des processus' },
    { key: 'processes.read', label: 'Lire les processus' },
    { key: 'processes.update', label: 'Modifier les processus' },
    { key: 'processes.delete', label: 'Supprimer les processus' }
  ],
  'Gestion des Candidats': [
    { key: 'candidats.create', label: 'Créer des candidats' },
    { key: 'candidats.read', label: 'Lire les candidats' },
    { key: 'candidats.update', label: 'Modifier les candidats' },
    { key: 'candidats.delete', label: 'Supprimer les candidats' }
  ],
  'Gestion des Permis': [
    { key: 'permis.create', label: 'Créer des permis' },
    { key: 'permis.read', label: 'Lire les permis' },
    { key: 'permis.update', label: 'Modifier les permis' },
    { key: 'permis.delete', label: 'Supprimer les permis' }
  ],
  'Validation et Approbation': [
    { key: 'documents.validate', label: 'Valider les documents' },
    { key: 'demandes.approve', label: 'Approuver les demandes' }
  ],
  'Rapports et Consultation': [
    { key: 'reports.read', label: 'Lire les rapports' },
    { key: 'status.read', label: 'Consulter le statut' }
  ],
  'Dépôt de Demandes': [
    { key: 'demandes.create', label: 'Créer des demandes' },
    { key: 'demandes.read', label: 'Lire ses demandes' },
    { key: 'documents.upload', label: 'Télécharger des documents' }
  ],
  'Configuration Système': [
    { key: 'settings.update', label: 'Modifier les paramètres' }
  ]
};

function AdminRoles() {
  const navigate = useNavigate();
  const [roles, setRoles] = useState(mockRoles);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingRole, setEditingRole] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    code: '',
    description: '',
    permissions: [],
    isActive: true
  });

  const handleOpenDialog = (role = null) => {
    setEditingRole(role);
    if (role) {
      setFormData({
        name: role.name,
        code: role.code,
        description: role.description,
        permissions: role.permissions,
        isActive: role.isActive
      });
    } else {
      setFormData({
        name: '',
        code: '',
        description: '',
        permissions: [],
        isActive: true
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingRole(null);
    setFormData({
      name: '',
      code: '',
      description: '',
      permissions: [],
      isActive: true
    });
  };

  const handleSaveRole = () => {
    if (editingRole) {
      setRoles(roles.map(r => 
        r.id === editingRole.id 
          ? { ...r, ...formData }
          : r
      ));
    } else {
      const newRole = {
        id: roles.length + 1,
        ...formData,
        userCount: 0,
        createdAt: new Date().toISOString().split('T')[0],
        color: `hsl(${Math.floor(Math.random() * 360)}, 70%, 50%)`
      };
      setRoles([...roles, newRole]);
    }
    handleCloseDialog();
  };

  const handleTogglePermission = (permission) => {
    setFormData(prev => ({
      ...prev,
      permissions: prev.permissions.includes(permission)
        ? prev.permissions.filter(p => p !== permission)
        : [...prev.permissions, permission]
    }));
  };

  const handleToggleRoleStatus = (roleId) => {
    setRoles(roles.map(r => 
      r.id === roleId 
        ? { ...r, isActive: !r.isActive }
        : r
    ));
  };

  const handleDeleteRole = (roleId) => {
    setRoles(roles.filter(r => r.id !== roleId));
  };

  const getRoleIcon = (code) => {
    switch (code) {
      case 'ADMIN': return <AdminIcon />;
      case 'CHEF_SERVICE': return <BusinessIcon />;
      case 'AGENT': return <PersonIcon />;
      case 'VALIDATEUR': return <CheckIcon />;
      case 'CITOYEN': return <PersonIcon />;
      default: return <SecurityIcon />;
    }
  };

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Header */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Box>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold', mb: 1 }}>
            Gestion des Rôles
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Configurez les rôles et permissions des utilisateurs
          </Typography>
        </Box>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={() => handleOpenDialog()}
          sx={{ 
            bgcolor: '#1976d2',
            '&:hover': { bgcolor: '#1565c0' }
          }}
        >
          Nouveau Rôle
        </Button>
      </Box>

      {/* Stats Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar sx={{ bgcolor: '#1976d2', mr: 2 }}>
                  <SecurityIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {roles.length}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Rôles Configurés
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar sx={{ bgcolor: '#2e7d32', mr: 2 }}>
                  <CheckIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {roles.filter(r => r.isActive).length}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Rôles Actifs
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar sx={{ bgcolor: '#ed6c02', mr: 2 }}>
                  <PersonIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {roles.reduce((sum, r) => sum + r.userCount, 0)}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Utilisateurs Assignés
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar sx={{ bgcolor: '#9c27b0', mr: 2 }}>
                  <SettingsIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {Object.values(permissionCategories).flat().length}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Permissions Disponibles
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Roles Grid */}
      <Grid container spacing={3}>
        {roles.map((role) => (
          <Grid item xs={12} md={6} lg={4} key={role.id}>
            <Card 
              sx={{ 
                height: '100%',
                transition: 'all 0.3s ease',
                opacity: role.isActive ? 1 : 0.7,
                '&:hover': {
                  transform: 'translateY(-2px)',
                  boxShadow: 4,
                }
              }}
            >
              <CardContent sx={{ p: 3 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Avatar sx={{ bgcolor: role.color, mr: 2 }}>
                      {getRoleIcon(role.code)}
                    </Avatar>
                    <Box>
                      <Typography variant="h6" component="h3" sx={{ fontWeight: 'bold' }}>
                        {role.name}
                      </Typography>
                      <Chip
                        label={role.code}
                        size="small"
                        sx={{ bgcolor: `${role.color}20`, color: role.color }}
                      />
                    </Box>
                  </Box>
                  <Box>
                    <IconButton
                      size="small"
                      onClick={() => handleToggleRoleStatus(role.id)}
                      color={role.isActive ? 'success' : 'default'}
                    >
                      {role.isActive ? <CheckIcon /> : <CancelIcon />}
                    </IconButton>
                  </Box>
                </Box>

                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  {role.description}
                </Typography>

                <Box sx={{ mb: 2 }}>
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                    Permissions ({role.permissions.length})
                  </Typography>
                  <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 0.5 }}>
                    {role.permissions.slice(0, 3).map((permission) => (
                      <Chip
                        key={permission}
                        label={permission.split('.')[0]}
                        size="small"
                        variant="outlined"
                      />
                    ))}
                    {role.permissions.length > 3 && (
                      <Chip
                        label={`+${role.permissions.length - 3}`}
                        size="small"
                        variant="outlined"
                      />
                    )}
                  </Box>
                </Box>

                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                  <Typography variant="body2" color="text.secondary">
                    {role.userCount} utilisateur(s)
                  </Typography>
                  <Chip
                    label={role.isActive ? 'Actif' : 'Inactif'}
                    color={role.isActive ? 'success' : 'default'}
                    size="small"
                  />
                </Box>

                <Box sx={{ display: 'flex', gap: 1 }}>
                  <Button
                    size="small"
                    startIcon={<EditIcon />}
                    onClick={() => handleOpenDialog(role)}
                    sx={{ flexGrow: 1 }}
                  >
                    Modifier
                  </Button>
                  <IconButton
                    size="small"
                    onClick={() => handleDeleteRole(role.id)}
                    color="error"
                  >
                    <DeleteIcon />
                  </IconButton>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Create/Edit Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="md" fullWidth>
        <DialogTitle>
          {editingRole ? 'Modifier le Rôle' : 'Nouveau Rôle'}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, pt: 1 }}>
            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  label="Nom du rôle"
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  fullWidth
                  required
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  label="Code du rôle"
                  value={formData.code}
                  onChange={(e) => setFormData({ ...formData, code: e.target.value.toUpperCase() })}
                  fullWidth
                  required
                />
              </Grid>
            </Grid>
            
            <TextField
              label="Description"
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              fullWidth
              multiline
              rows={3}
              required
            />

            <FormControlLabel
              control={
                <Switch
                  checked={formData.isActive}
                  onChange={(e) => setFormData({ ...formData, isActive: e.target.checked })}
                />
              }
              label="Rôle actif"
            />

            <Divider />

            <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
              Permissions
            </Typography>

            {Object.entries(permissionCategories).map(([category, permissions]) => (
              <Accordion key={category}>
                <AccordionSummary expandIcon={<ExpandMoreIcon />}>
                  <Typography variant="subtitle1" sx={{ fontWeight: 'bold' }}>
                    {category}
                  </Typography>
                </AccordionSummary>
                <AccordionDetails>
                  <Grid container spacing={1}>
                    {permissions.map((permission) => (
                      <Grid item xs={12} sm={6} md={4} key={permission.key}>
                        <FormControlLabel
                          control={
                            <Switch
                              checked={formData.permissions.includes(permission.key)}
                              onChange={() => handleTogglePermission(permission.key)}
                              size="small"
                            />
                          }
                          label={permission.label}
                        />
                      </Grid>
                    ))}
                  </Grid>
                </AccordionDetails>
              </Accordion>
            ))}
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Annuler</Button>
          <Button 
            onClick={handleSaveRole} 
            variant="contained"
            disabled={!formData.name || !formData.code || !formData.description}
          >
            {editingRole ? 'Modifier' : 'Créer'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default AdminRoles;
