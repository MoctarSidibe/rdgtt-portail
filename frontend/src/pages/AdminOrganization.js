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
  Avatar,
  Tooltip,
  Alert,
  Tabs,
  Tab,
  Divider,
  TreeView,
  TreeItem,
  ExpandMoreIcon,
  ChevronRightIcon,
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Business as DepartmentIcon,
  AccountBalance as BureauIcon,
  Person as PersonIcon,
  Settings as SettingsIcon,
  CheckCircle as CheckIcon,
  Cancel as CancelIcon,
  Visibility as ViewIcon,
  Group as GroupIcon,
  LocationOn as LocationIcon,
  Phone as PhoneIcon,
  Email as EmailIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

const mockDepartments = [
  {
    id: 1,
    name: 'Direction Générale',
    code: 'DGTT',
    description: 'Direction Générale des Transports Terrestres',
    address: 'Libreville, Centre-ville',
    phone: '+241 01 23 45 67',
    email: 'dgtt@transport.gabon.ga',
    director: 'Jean-Pierre Mba',
    isActive: true,
    createdAt: '2024-01-01',
    bureaus: [
      {
        id: 1,
        name: 'Cabinet du Directeur',
        code: 'CD',
        description: 'Cabinet du Directeur Général',
        head: 'Marie Nguema',
        isActive: true,
        userCount: 5
      }
    ]
  },
  {
    id: 2,
    name: 'Service des Examens et Vérifications',
    code: 'SEV',
    description: 'Service chargé des examens et vérifications des véhicules',
    address: 'Libreville, Quartier Montagne Sainte',
    phone: '+241 01 23 45 68',
    email: 'sev@transport.gabon.ga',
    director: 'Paul Mba',
    isActive: true,
    createdAt: '2024-01-02',
    bureaus: [
      {
        id: 2,
        name: 'Bureau des Permis',
        code: 'BP',
        description: 'Bureau de délivrance des permis de conduire',
        head: 'Anna Obame',
        isActive: true,
        userCount: 12
      },
      {
        id: 3,
        name: 'Bureau des Cartes Grises',
        code: 'BCG',
        description: 'Bureau des cartes grises et immatriculations',
        head: 'Pierre Ondo',
        isActive: true,
        userCount: 8
      }
    ]
  },
  {
    id: 3,
    name: 'Service des Affaires Financières',
    code: 'SAF',
    description: 'Service des affaires financières et comptables',
    address: 'Libreville, Quartier Glass',
    phone: '+241 01 23 45 69',
    email: 'saf@transport.gabon.ga',
    director: 'Claire Mba',
    isActive: true,
    createdAt: '2024-01-03',
    bureaus: [
      {
        id: 4,
        name: 'Bureau Comptable',
        code: 'BC',
        description: 'Bureau de la comptabilité générale',
        head: 'Marc Nguema',
        isActive: true,
        userCount: 6
      }
    ]
  },
  {
    id: 4,
    name: 'Direction des Contrôles',
    code: 'DC',
    description: 'Direction des contrôles et inspections',
    address: 'Libreville, Quartier Nombakélé',
    phone: '+241 01 23 45 70',
    email: 'dc@transport.gabon.ga',
    director: 'David Mba',
    isActive: true,
    createdAt: '2024-01-04',
    bureaus: [
      {
        id: 5,
        name: 'Bureau des Licences',
        code: 'BL',
        description: 'Bureau des licences de transport',
        head: 'Sophie Ondo',
        isActive: true,
        userCount: 10
      },
      {
        id: 6,
        name: 'Bureau des Inspections',
        code: 'BI',
        description: 'Bureau des inspections techniques',
        head: 'Jean Mba',
        isActive: true,
        userCount: 15
      }
    ]
  }
];

function TabPanel({ children, value, index, ...other }) {
  return (
    <div
      role="tabpanel"
      hidden={value !== index}
      id={`simple-tabpanel-${index}`}
      aria-labelledby={`simple-tab-${index}`}
      {...other}
    >
      {value === index && (
        <Box sx={{ p: 3 }}>
          {children}
        </Box>
      )}
    </div>
  );
}

function AdminOrganization() {
  const navigate = useNavigate();
  const [departments, setDepartments] = useState(mockDepartments);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingItem, setEditingItem] = useState(null);
  const [dialogType, setDialogType] = useState('department'); // 'department' or 'bureau'
  const [parentDepartment, setParentDepartment] = useState(null);
  const [tabValue, setTabValue] = useState(0);
  const [formData, setFormData] = useState({
    name: '',
    code: '',
    description: '',
    address: '',
    phone: '',
    email: '',
    director: '',
    head: '',
    isActive: true
  });

  const handleTabChange = (event, newValue) => {
    setTabValue(newValue);
  };

  const handleOpenDialog = (type, item = null, parentDept = null) => {
    setDialogType(type);
    setParentDepartment(parentDept);
    setEditingItem(item);
    
    if (item) {
      setFormData({
        name: item.name,
        code: item.code,
        description: item.description,
        address: item.address || '',
        phone: item.phone || '',
        email: item.email || '',
        director: item.director || '',
        head: item.head || '',
        isActive: item.isActive
      });
    } else {
      setFormData({
        name: '',
        code: '',
        description: '',
        address: '',
        phone: '',
        email: '',
        director: '',
        head: '',
        isActive: true
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingItem(null);
    setParentDepartment(null);
    setFormData({
      name: '',
      code: '',
      description: '',
      address: '',
      phone: '',
      email: '',
      director: '',
      head: '',
      isActive: true
    });
  };

  const handleSave = () => {
    if (dialogType === 'department') {
      if (editingItem) {
        setDepartments(departments.map(d => 
          d.id === editingItem.id 
            ? { ...d, ...formData }
            : d
        ));
      } else {
        const newDepartment = {
          id: departments.length + 1,
          ...formData,
          createdAt: new Date().toISOString().split('T')[0],
          bureaus: []
        };
        setDepartments([...departments, newDepartment]);
      }
    } else {
      // Bureau
      const departmentId = parentDepartment.id;
      const newBureau = {
        id: Date.now(), // Simple ID generation
        ...formData,
        userCount: 0
      };

      setDepartments(departments.map(d => 
        d.id === departmentId 
          ? { 
              ...d, 
              bureaus: editingItem 
                ? d.bureaus.map(b => b.id === editingItem.id ? { ...b, ...formData } : b)
                : [...d.bureaus, newBureau]
            }
          : d
      ));
    }
    handleCloseDialog();
  };

  const handleToggleStatus = (type, itemId, parentId = null) => {
    if (type === 'department') {
      setDepartments(departments.map(d => 
        d.id === itemId 
          ? { ...d, isActive: !d.isActive }
          : d
      ));
    } else {
      setDepartments(departments.map(d => 
        d.id === parentId 
          ? { 
              ...d, 
              bureaus: d.bureaus.map(b => 
                b.id === itemId 
                  ? { ...b, isActive: !b.isActive }
                  : b
              )
            }
          : d
      ));
    }
  };

  const handleDelete = (type, itemId, parentId = null) => {
    if (type === 'department') {
      setDepartments(departments.filter(d => d.id !== itemId));
    } else {
      setDepartments(departments.map(d => 
        d.id === parentId 
          ? { ...d, bureaus: d.bureaus.filter(b => b.id !== itemId) }
          : d
      ));
    }
  };

  const getTotalUsers = () => {
    return departments.reduce((total, dept) => 
      total + dept.bureaus.reduce((bureauTotal, bureau) => bureauTotal + bureau.userCount, 0), 0
    );
  };

  const getTotalBureaus = () => {
    return departments.reduce((total, dept) => total + dept.bureaus.length, 0);
  };

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Header */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Box>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold', mb: 1 }}>
            Organisation Administrative
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Gérez les services et bureaux de l'administration
          </Typography>
        </Box>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button
            variant="outlined"
            startIcon={<AddIcon />}
            onClick={() => handleOpenDialog('department')}
          >
            Nouveau Service
          </Button>
          <Button
            variant="contained"
            startIcon={<AddIcon />}
            onClick={() => handleOpenDialog('bureau')}
            sx={{ 
              bgcolor: '#1976d2',
              '&:hover': { bgcolor: '#1565c0' }
            }}
          >
            Nouveau Bureau
          </Button>
        </Box>
      </Box>

      {/* Stats Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar sx={{ bgcolor: '#1976d2', mr: 2 }}>
                  <DepartmentIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {departments.length}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Services
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
                  <BureauIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {getTotalBureaus()}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Bureaux
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
                  <GroupIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {getTotalUsers()}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Utilisateurs
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
                  <CheckIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {departments.filter(d => d.isActive).length}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Actifs
                  </Typography>
                </Box>
              </Box>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Tabs */}
      <Box sx={{ borderBottom: 1, borderColor: 'divider', mb: 3 }}>
        <Tabs value={tabValue} onChange={handleTabChange} aria-label="organization tabs">
          <Tab label="Vue Hiérarchique" />
          <Tab label="Services" />
          <Tab label="Bureaux" />
        </Tabs>
      </Box>

      {/* Hierarchical View */}
      <TabPanel value={tabValue} index={0}>
        <Paper sx={{ p: 3 }}>
          <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 3 }}>
            Structure Organisationnelle
          </Typography>
          <TreeView
            defaultCollapseIcon={<ExpandMoreIcon />}
            defaultExpandIcon={<ChevronRightIcon />}
            defaultExpanded={['root']}
          >
            {departments.map((department) => (
              <TreeItem
                key={department.id}
                nodeId={department.id.toString()}
                label={
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, py: 1 }}>
                    <Avatar sx={{ bgcolor: '#1976d2', width: 32, height: 32 }}>
                      <DepartmentIcon />
                    </Avatar>
                    <Box sx={{ flexGrow: 1 }}>
                      <Typography variant="body1" sx={{ fontWeight: 'bold' }}>
                        {department.name} ({department.code})
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {department.director} • {department.bureaus.length} bureau(s)
                      </Typography>
                    </Box>
                    <Chip
                      label={department.isActive ? 'Actif' : 'Inactif'}
                      color={department.isActive ? 'success' : 'default'}
                      size="small"
                    />
                  </Box>
                }
              >
                {department.bureaus.map((bureau) => (
                  <TreeItem
                    key={bureau.id}
                    nodeId={`${department.id}-${bureau.id}`}
                    label={
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, py: 1 }}>
                        <Avatar sx={{ bgcolor: '#2e7d32', width: 28, height: 28 }}>
                          <BureauIcon />
                        </Avatar>
                        <Box sx={{ flexGrow: 1 }}>
                          <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                            {bureau.name} ({bureau.code})
                          </Typography>
                          <Typography variant="caption" color="text.secondary">
                            {bureau.head} • {bureau.userCount} utilisateur(s)
                          </Typography>
                        </Box>
                        <Chip
                          label={bureau.isActive ? 'Actif' : 'Inactif'}
                          color={bureau.isActive ? 'success' : 'default'}
                          size="small"
                        />
                      </Box>
                    }
                  />
                ))}
              </TreeItem>
            ))}
          </TreeView>
        </Paper>
      </TabPanel>

      {/* Departments Tab */}
      <TabPanel value={tabValue} index={1}>
        <Grid container spacing={3}>
          {departments.map((department) => (
            <Grid item xs={12} md={6} lg={4} key={department.id}>
              <Card 
                sx={{ 
                  height: '100%',
                  opacity: department.isActive ? 1 : 0.7,
                  transition: 'all 0.3s ease',
                  '&:hover': {
                    transform: 'translateY(-2px)',
                    boxShadow: 4,
                  }
                }}
              >
                <CardContent sx={{ p: 3 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <Avatar sx={{ bgcolor: '#1976d2', mr: 2 }}>
                        <DepartmentIcon />
                      </Avatar>
                      <Box>
                        <Typography variant="h6" component="h3" sx={{ fontWeight: 'bold' }}>
                          {department.name}
                        </Typography>
                        <Chip
                          label={department.code}
                          size="small"
                          sx={{ bgcolor: '#1976d220', color: '#1976d2' }}
                        />
                      </Box>
                    </Box>
                    <IconButton
                      size="small"
                      onClick={() => handleToggleStatus('department', department.id)}
                      color={department.isActive ? 'success' : 'default'}
                    >
                      {department.isActive ? <CheckIcon /> : <CancelIcon />}
                    </IconButton>
                  </Box>

                  <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    {department.description}
                  </Typography>

                  <Box sx={{ mb: 2 }}>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                      Informations de contact
                    </Typography>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 0.5 }}>
                      <LocationIcon sx={{ fontSize: 16, mr: 1, color: 'text.secondary' }} />
                      <Typography variant="caption">{department.address}</Typography>
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center', mb: 0.5 }}>
                      <PhoneIcon sx={{ fontSize: 16, mr: 1, color: 'text.secondary' }} />
                      <Typography variant="caption">{department.phone}</Typography>
                    </Box>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <EmailIcon sx={{ fontSize: 16, mr: 1, color: 'text.secondary' }} />
                      <Typography variant="caption">{department.email}</Typography>
                    </Box>
                  </Box>

                  <Box sx={{ mb: 2 }}>
                    <Typography variant="body2" color="text.secondary">
                      Directeur: {department.director}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Bureaux: {department.bureaus.length}
                    </Typography>
                  </Box>

                  <Box sx={{ display: 'flex', gap: 1 }}>
                    <Button
                      size="small"
                      startIcon={<ViewIcon />}
                      onClick={() => setTabValue(2)}
                      sx={{ flexGrow: 1 }}
                    >
                      Voir Bureaux
                    </Button>
                    <IconButton
                      size="small"
                      onClick={() => handleOpenDialog('department', department)}
                      color="primary"
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton
                      size="small"
                      onClick={() => handleDelete('department', department.id)}
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
      </TabPanel>

      {/* Bureaus Tab */}
      <TabPanel value={tabValue} index={2}>
        <Grid container spacing={3}>
          {departments.flatMap(dept => 
            dept.bureaus.map(bureau => ({ ...bureau, department: dept }))
          ).map((bureau) => (
            <Grid item xs={12} md={6} lg={4} key={bureau.id}>
              <Card 
                sx={{ 
                  height: '100%',
                  opacity: bureau.isActive ? 1 : 0.7,
                  transition: 'all 0.3s ease',
                  '&:hover': {
                    transform: 'translateY(-2px)',
                    boxShadow: 4,
                  }
                }}
              >
                <CardContent sx={{ p: 3 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center' }}>
                      <Avatar sx={{ bgcolor: '#2e7d32', mr: 2 }}>
                        <BureauIcon />
                      </Avatar>
                      <Box>
                        <Typography variant="h6" component="h3" sx={{ fontWeight: 'bold' }}>
                          {bureau.name}
                        </Typography>
                        <Chip
                          label={bureau.code}
                          size="small"
                          sx={{ bgcolor: '#2e7d3220', color: '#2e7d32' }}
                        />
                      </Box>
                    </Box>
                    <IconButton
                      size="small"
                      onClick={() => handleToggleStatus('bureau', bureau.id, bureau.department.id)}
                      color={bureau.isActive ? 'success' : 'default'}
                    >
                      {bureau.isActive ? <CheckIcon /> : <CancelIcon />}
                    </IconButton>
                  </Box>

                  <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    {bureau.description}
                  </Typography>

                  <Box sx={{ mb: 2 }}>
                    <Typography variant="body2" color="text.secondary">
                      Responsable: {bureau.head}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Service: {bureau.department.name}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                      Utilisateurs: {bureau.userCount}
                    </Typography>
                  </Box>

                  <Box sx={{ display: 'flex', gap: 1 }}>
                    <Button
                      size="small"
                      startIcon={<ViewIcon />}
                      sx={{ flexGrow: 1 }}
                    >
                      Voir Détails
                    </Button>
                    <IconButton
                      size="small"
                      onClick={() => handleOpenDialog('bureau', bureau, bureau.department)}
                      color="primary"
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton
                      size="small"
                      onClick={() => handleDelete('bureau', bureau.id, bureau.department.id)}
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
      </TabPanel>

      {/* Create/Edit Dialog */}
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {editingItem ? `Modifier ${dialogType === 'department' ? 'le Service' : 'le Bureau'}` : 
           `Nouveau ${dialogType === 'department' ? 'Service' : 'Bureau'}`}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, pt: 1 }}>
            {dialogType === 'bureau' && !editingItem && (
              <FormControl fullWidth>
                <InputLabel>Service parent</InputLabel>
                <Select
                  value={parentDepartment?.id || ''}
                  onChange={(e) => {
                    const dept = departments.find(d => d.id === e.target.value);
                    setParentDepartment(dept);
                  }}
                  label="Service parent"
                  required
                >
                  {departments.map((dept) => (
                    <MenuItem key={dept.id} value={dept.id}>
                      {dept.name} ({dept.code})
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            )}

            <Grid container spacing={2}>
              <Grid item xs={12} sm={6}>
                <TextField
                  label={`Nom ${dialogType === 'department' ? 'du service' : 'du bureau'}`}
                  value={formData.name}
                  onChange={(e) => setFormData({ ...formData, name: e.target.value })}
                  fullWidth
                  required
                />
              </Grid>
              <Grid item xs={12} sm={6}>
                <TextField
                  label={`Code ${dialogType === 'department' ? 'du service' : 'du bureau'}`}
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

            {dialogType === 'department' && (
              <>
                <TextField
                  label="Adresse"
                  value={formData.address}
                  onChange={(e) => setFormData({ ...formData, address: e.target.value })}
                  fullWidth
                />
                
                <Grid container spacing={2}>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      label="Téléphone"
                      value={formData.phone}
                      onChange={(e) => setFormData({ ...formData, phone: e.target.value })}
                      fullWidth
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      label="Email"
                      type="email"
                      value={formData.email}
                      onChange={(e) => setFormData({ ...formData, email: e.target.value })}
                      fullWidth
                    />
                  </Grid>
                </Grid>

                <TextField
                  label="Directeur"
                  value={formData.director}
                  onChange={(e) => setFormData({ ...formData, director: e.target.value })}
                  fullWidth
                />
              </>
            )}

            {dialogType === 'bureau' && (
              <TextField
                label="Responsable"
                value={formData.head}
                onChange={(e) => setFormData({ ...formData, head: e.target.value })}
                fullWidth
              />
            )}

            <FormControlLabel
              control={
                <Switch
                  checked={formData.isActive}
                  onChange={(e) => setFormData({ ...formData, isActive: e.target.checked })}
                />
              }
              label={`${dialogType === 'department' ? 'Service' : 'Bureau'} actif`}
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Annuler</Button>
          <Button 
            onClick={handleSave} 
            variant="contained"
            disabled={!formData.name || !formData.code || !formData.description || (dialogType === 'bureau' && !editingItem && !parentDepartment)}
          >
            {editingItem ? 'Modifier' : 'Créer'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default AdminOrganization;
