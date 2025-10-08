import React, { useState } from 'react';
import {
  Box,
  Paper,
  Typography,
  Button,
  Grid,
  Card,
  CardContent,
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
  Switch,
  FormControlLabel,
  Alert,
  Fab,
  Tooltip,
  Divider,
  Badge,
  Avatar,
  Stack
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  Description as DocumentIcon,
  Visibility as ViewIcon,
  Settings as SettingsIcon,
  CheckCircle as ActiveIcon,
  Cancel as InactiveIcon,
  Article as ArticleIcon,
  Assignment as AssignmentIcon,
  Folder as FolderIcon,
  InsertDriveFile as FileIcon,
  Assessment as ReportIcon
} from '@mui/icons-material';

const AdminDocumentTypes = () => {
  const [documentTypes, setDocumentTypes] = useState([
    {
      id: 1,
      nom: 'Demande de Permis de Conduire',
      code: 'PERMIS_CONDUITE',
      description: 'Demande initiale de permis de conduire pour toutes catégories',
      typeDocument: 'DEMANDE',
      delaiTraitementJours: 30,
      documentsRequis: [
        'Pièce d\'identité',
        'Certificat médical',
        'Photo d\'identité',
        'Justificatif de domicile'
      ],
      etapes: 6,
      actif: true,
      couleur: '#1976d2',
      icone: 'Assignment',
      demandesEnCours: 156,
      demandesTraitees: 1247,
      tauxReussite: 89.5
    },
    {
      id: 2,
      nom: 'Renouvellement de Permis',
      code: 'RENOUVELLEMENT_PERMIS',
      description: 'Renouvellement de permis de conduire existant',
      typeDocument: 'RENOUVELLEMENT',
      delaiTraitementJours: 15,
      documentsRequis: [
        'Permis actuel',
        'Pièce d\'identité',
        'Certificat médical'
      ],
      etapes: 4,
      actif: true,
      couleur: '#2e7d32',
      icone: 'CheckCircle',
      demandesEnCours: 89,
      demandesTraitees: 892,
      tauxReussite: 94.2
    },
    {
      id: 3,
      nom: 'Duplicata de Permis',
      code: 'DUPLICATA_PERMIS',
      description: 'Émission d\'un duplicata en cas de perte ou vol',
      typeDocument: 'DUPLICATA',
      delaiTraitementJours: 10,
      documentsRequis: [
        'Déclaration de perte/vol',
        'Pièce d\'identité',
        'Photo d\'identité'
      ],
      etapes: 3,
      actif: true,
      couleur: '#f57c00',
      icone: 'FileIcon',
      demandesEnCours: 45,
      demandesTraitees: 234,
      tauxReussite: 97.8
    },
    {
      id: 4,
      nom: 'Changement de Catégorie',
      code: 'CHANGEMENT_CATEGORIE',
      description: 'Ajout ou modification de catégorie de permis',
      typeDocument: 'MODIFICATION',
      delaiTraitementJours: 20,
      documentsRequis: [
        'Permis actuel',
        'Certificat médical',
        'Formation complémentaire'
      ],
      etapes: 5,
      actif: true,
      couleur: '#7b1fa2',
      icone: 'Settings',
      demandesEnCours: 23,
      demandesTraitees: 167,
      tauxReussite: 91.6
    },
    {
      id: 5,
      nom: 'Permis International',
      code: 'PERMIS_INTERNATIONAL',
      description: 'Demande de permis de conduire international',
      typeDocument: 'INTERNATIONAL',
      delaiTraitementJours: 7,
      documentsRequis: [
        'Permis national',
        'Pièce d\'identité',
        'Photo d\'identité',
        'Justificatif de voyage'
      ],
      etapes: 4,
      actif: true,
      couleur: '#d32f2f',
      icone: 'Assessment',
      demandesEnCours: 12,
      demandesTraitees: 89,
      tauxReussite: 96.6
    },
    {
      id: 6,
      nom: 'Certificat de Conduite',
      code: 'CERTIFICAT_CONDUITE',
      description: 'Certificat de capacité de conduite',
      typeDocument: 'CERTIFICAT',
      delaiTraitementJours: 5,
      documentsRequis: [
        'Examen de conduite',
        'Pièce d\'identité'
      ],
      etapes: 2,
      actif: false,
      couleur: '#616161',
      icone: 'Article',
      demandesEnCours: 0,
      demandesTraitees: 45,
      tauxReussite: 88.9
    }
  ]);

  const [selectedType, setSelectedType] = useState(null);
  const [openDialog, setOpenDialog] = useState(false);
  const [dialogMode, setDialogMode] = useState('create');
  const [formData, setFormData] = useState({});

  const getIcon = (iconName) => {
    const icons = {
      Assignment: <AssignmentIcon />,
      CheckCircle: <ActiveIcon />,
      FileIcon: <FileIcon />,
      Settings: <SettingsIcon />,
      Assessment: <ReportIcon />,
      Article: <ArticleIcon />
    };
    return icons[iconName] || <DocumentIcon />;
  };

  const handleCreate = () => {
    setDialogMode('create');
    setFormData({
      nom: '',
      code: '',
      description: '',
      typeDocument: 'DEMANDE',
      delaiTraitementJours: 30,
      documentsRequis: [],
      couleur: '#1976d2',
      icone: 'Assignment',
      actif: true
    });
    setOpenDialog(true);
  };

  const handleEdit = (type) => {
    setDialogMode('edit');
    setFormData({ ...type });
    setSelectedType(type);
    setOpenDialog(true);
  };

  const handleDelete = (type) => {
    if (window.confirm(`Êtes-vous sûr de vouloir supprimer "${type.nom}" ?`)) {
      setDocumentTypes(prev => prev.filter(dt => dt.id !== type.id));
    }
  };

  const handleSave = () => {
    if (dialogMode === 'create') {
      const newType = {
        ...formData,
        id: Math.max(...documentTypes.map(dt => dt.id)) + 1,
        demandesEnCours: 0,
        demandesTraitees: 0,
        tauxReussite: 0
      };
      setDocumentTypes(prev => [...prev, newType]);
    } else {
      setDocumentTypes(prev => prev.map(dt => 
        dt.id === selectedType.id ? { ...formData, ...dt } : dt
      ));
    }
    setOpenDialog(false);
    setSelectedType(null);
  };

  const toggleActive = (type) => {
    setDocumentTypes(prev => prev.map(dt => 
      dt.id === type.id ? { ...dt, actif: !dt.actif } : dt
    ));
  };

  const getTypeColor = (type) => {
    const colors = {
      DEMANDE: '#1976d2',
      RENOUVELLEMENT: '#2e7d32',
      DUPLICATA: '#f57c00',
      MODIFICATION: '#7b1fa2',
      INTERNATIONAL: '#d32f2f',
      CERTIFICAT: '#616161'
    };
    return colors[type] || '#1976d2';
  };

  const getTypeLabel = (type) => {
    const labels = {
      DEMANDE: 'Demande',
      RENOUVELLEMENT: 'Renouvellement',
      DUPLICATA: 'Duplicata',
      MODIFICATION: 'Modification',
      INTERNATIONAL: 'International',
      CERTIFICAT: 'Certificat'
    };
    return labels[type] || type;
  };

  const totalDemandes = documentTypes.reduce((sum, dt) => sum + dt.demandesEnCours + dt.demandesTraitees, 0);
  const totalActifs = documentTypes.filter(dt => dt.actif).length;
  const tauxMoyenReussite = documentTypes.length > 0 ? 
    (documentTypes.reduce((sum, dt) => sum + dt.tauxReussite, 0) / documentTypes.length).toFixed(1) : 0;

  return (
    <Box sx={{ p: 3 }}>
      {/* Header */}
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" component="h1" gutterBottom sx={{ fontWeight: 'bold', color: '#1976d2' }}>
            Gestion des Types de Documents
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            Configuration des types de documents et processus administratifs
          </Typography>
        </Box>
        <Button
          variant="contained"
          startIcon={<AddIcon />}
          onClick={handleCreate}
          sx={{ 
            bgcolor: '#1976d2',
            '&:hover': { bgcolor: '#1565c0' },
            px: 3,
            py: 1.5
          }}
        >
          Nouveau Type
        </Button>
      </Box>

      {/* Statistics Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card sx={{ bgcolor: '#e3f2fd', border: '1px solid #bbdefb' }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                <DocumentIcon sx={{ color: '#1976d2', mr: 1 }} />
                <Typography variant="h6" color="#1976d2">
                  Types Configurés
                </Typography>
              </Box>
              <Typography variant="h4" color="#1976d2" sx={{ fontWeight: 'bold' }}>
                {documentTypes.length}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card sx={{ bgcolor: '#e8f5e8', border: '1px solid #c8e6c9' }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                <ActiveIcon sx={{ color: '#2e7d32', mr: 1 }} />
                <Typography variant="h6" color="#2e7d32">
                  Types Actifs
                </Typography>
              </Box>
              <Typography variant="h4" color="#2e7d32" sx={{ fontWeight: 'bold' }}>
                {totalActifs}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card sx={{ bgcolor: '#fff3e0', border: '1px solid #ffcc02' }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                <AssignmentIcon sx={{ color: '#f57c00', mr: 1 }} />
                <Typography variant="h6" color="#f57c00">
                  Total Demandes
                </Typography>
              </Box>
              <Typography variant="h4" color="#f57c00" sx={{ fontWeight: 'bold' }}>
                {totalDemandes.toLocaleString()}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6} md={3}>
          <Card sx={{ bgcolor: '#f3e5f5', border: '1px solid #e1bee7' }}>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                <ReportIcon sx={{ color: '#7b1fa2', mr: 1 }} />
                <Typography variant="h6" color="#7b1fa2">
                  Taux Réussite
                </Typography>
              </Box>
              <Typography variant="h4" color="#7b1fa2" sx={{ fontWeight: 'bold' }}>
                {tauxMoyenReussite}%
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Document Types Grid */}
      <Grid container spacing={3}>
        {documentTypes.map((type) => (
          <Grid item xs={12} sm={6} lg={4} key={type.id}>
            <Card 
              sx={{ 
                height: '100%',
                transition: 'all 0.3s ease',
                '&:hover': {
                  transform: 'translateY(-4px)',
                  boxShadow: 4
                },
                border: type.actif ? `2px solid ${type.couleur}` : '2px solid #e0e0e0',
                opacity: type.actif ? 1 : 0.7
              }}
            >
              <CardContent sx={{ p: 3 }}>
                {/* Header */}
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Avatar sx={{ bgcolor: type.couleur, mr: 2, width: 48, height: 48 }}>
                      {getIcon(type.icone)}
                    </Avatar>
                    <Box>
                      <Typography variant="h6" sx={{ fontWeight: 'bold', color: type.couleur }}>
                        {type.nom}
                      </Typography>
                      <Chip 
                        label={getTypeLabel(type.typeDocument)}
                        size="small"
                        sx={{ 
                          bgcolor: getTypeColor(type.typeDocument),
                          color: 'white',
                          fontWeight: 'bold'
                        }}
                      />
                    </Box>
                  </Box>
                  <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-end' }}>
                    <Switch
                      checked={type.actif}
                      onChange={() => toggleActive(type)}
                      color="primary"
                      size="small"
                    />
                    <Box sx={{ display: 'flex', gap: 0.5, mt: 1 }}>
                      <Tooltip title="Modifier">
                        <IconButton size="small" onClick={() => handleEdit(type)}>
                          <EditIcon fontSize="small" />
                        </IconButton>
                      </Tooltip>
                      <Tooltip title="Supprimer">
                        <IconButton size="small" onClick={() => handleDelete(type)} color="error">
                          <DeleteIcon fontSize="small" />
                        </IconButton>
                      </Tooltip>
                    </Box>
                  </Box>
                </Box>

                {/* Description */}
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2, minHeight: 40 }}>
                  {type.description}
                </Typography>

                {/* Code */}
                <Box sx={{ mb: 2 }}>
                  <Typography variant="caption" color="text.secondary">
                    Code:
                  </Typography>
                  <Typography variant="body2" sx={{ fontFamily: 'monospace', fontWeight: 'bold' }}>
                    {type.code}
                  </Typography>
                </Box>

                {/* Stats */}
                <Grid container spacing={2} sx={{ mb: 2 }}>
                  <Grid item xs={4}>
                    <Box sx={{ textAlign: 'center' }}>
                      <Typography variant="h6" color={type.couleur} sx={{ fontWeight: 'bold' }}>
                        {type.etapes}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        Étapes
                      </Typography>
                    </Box>
                  </Grid>
                  <Grid item xs={4}>
                    <Box sx={{ textAlign: 'center' }}>
                      <Typography variant="h6" color={type.couleur} sx={{ fontWeight: 'bold' }}>
                        {type.delaiTraitementJours}
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        Jours
                      </Typography>
                    </Box>
                  </Grid>
                  <Grid item xs={4}>
                    <Box sx={{ textAlign: 'center' }}>
                      <Typography variant="h6" color={type.couleur} sx={{ fontWeight: 'bold' }}>
                        {type.tauxReussite}%
                      </Typography>
                      <Typography variant="caption" color="text.secondary">
                        Réussite
                      </Typography>
                    </Box>
                  </Grid>
                </Grid>

                <Divider sx={{ my: 2 }} />

                {/* Demandes Info */}
                <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                  <Box>
                    <Typography variant="caption" color="text.secondary">
                      Demandes
                    </Typography>
                    <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                      {type.demandesEnCours} en cours
                    </Typography>
                  </Box>
                  <Box sx={{ textAlign: 'right' }}>
                    <Typography variant="caption" color="text.secondary">
                      Traitées
                    </Typography>
                    <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                      {type.demandesTraitees}
                    </Typography>
                  </Box>
                </Box>

                {/* Documents Requis */}
                <Box sx={{ mt: 2 }}>
                  <Typography variant="caption" color="text.secondary" sx={{ mb: 1, display: 'block' }}>
                    Documents requis:
                  </Typography>
                  <Stack direction="row" spacing={0.5} flexWrap="wrap" useFlexGap>
                    {type.documentsRequis.slice(0, 2).map((doc, index) => (
                      <Chip 
                        key={index}
                        label={doc}
                        size="small"
                        variant="outlined"
                        sx={{ fontSize: '0.7rem', height: 20 }}
                      />
                    ))}
                    {type.documentsRequis.length > 2 && (
                      <Chip 
                        label={`+${type.documentsRequis.length - 2}`}
                        size="small"
                        variant="outlined"
                        sx={{ fontSize: '0.7rem', height: 20 }}
                      />
                    )}
                  </Stack>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Create/Edit Dialog */}
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="md" fullWidth>
        <DialogTitle>
          {dialogMode === 'create' ? 'Nouveau Type de Document' : 'Modifier le Type de Document'}
        </DialogTitle>
        <DialogContent>
          <Grid container spacing={2} sx={{ mt: 1 }}>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Nom du type"
                value={formData.nom || ''}
                onChange={(e) => setFormData({ ...formData, nom: e.target.value })}
                required
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Code"
                value={formData.code || ''}
                onChange={(e) => setFormData({ ...formData, code: e.target.value })}
                required
                helperText="Code unique pour identifier le type"
              />
            </Grid>
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Description"
                value={formData.description || ''}
                onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                multiline
                rows={3}
              />
            </Grid>
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <InputLabel>Type de document</InputLabel>
                <Select
                  value={formData.typeDocument || ''}
                  onChange={(e) => setFormData({ ...formData, typeDocument: e.target.value })}
                  label="Type de document"
                >
                  <MenuItem value="DEMANDE">Demande</MenuItem>
                  <MenuItem value="RENOUVELLEMENT">Renouvellement</MenuItem>
                  <MenuItem value="DUPLICATA">Duplicata</MenuItem>
                  <MenuItem value="MODIFICATION">Modification</MenuItem>
                  <MenuItem value="INTERNATIONAL">International</MenuItem>
                  <MenuItem value="CERTIFICAT">Certificat</MenuItem>
                </Select>
              </FormControl>
            </Grid>
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Délai de traitement (jours)"
                type="number"
                value={formData.delaiTraitementJours || ''}
                onChange={(e) => setFormData({ ...formData, delaiTraitementJours: parseInt(e.target.value) })}
                required
              />
            </Grid>
            <Grid item xs={12}>
              <FormControlLabel
                control={
                  <Switch
                    checked={formData.actif || false}
                    onChange={(e) => setFormData({ ...formData, actif: e.target.checked })}
                  />
                }
                label="Type actif"
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Annuler</Button>
          <Button onClick={handleSave} variant="contained">
            {dialogMode === 'create' ? 'Créer' : 'Modifier'}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default AdminDocumentTypes;
