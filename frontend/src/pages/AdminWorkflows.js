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
  Fab,
  Paper,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  Switch,
  Tooltip,
} from '@mui/material';
import {
  Add as AddIcon,
  Edit as EditIcon,
  Delete as DeleteIcon,
  PlayArrow as PlayIcon,
  Pause as PauseIcon,
  Settings as SettingsIcon,
  Timeline as WorkflowIcon,
  CheckCircle as ActiveIcon,
  Cancel as InactiveIcon,
  DragIndicator as DragIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

const mockWorkflows = [
  {
    id: 1,
    name: 'Délivrance Permis de Conduire',
    description: 'Processus complet de délivrance du permis de conduire',
    status: 'active',
    steps: 5,
    averageTime: '15 jours',
    lastModified: '2024-01-15',
    createdBy: 'Admin Système'
  },
  {
    id: 2,
    name: 'Validation Auto-École',
    description: 'Processus de validation et agrément des auto-écoles',
    status: 'active',
    steps: 3,
    averageTime: '7 jours',
    lastModified: '2024-01-10',
    createdBy: 'Chef Service'
  },
  {
    id: 3,
    name: 'Duplicata Permis',
    description: 'Processus de demande de duplicata de permis',
    status: 'inactive',
    steps: 2,
    averageTime: '3 jours',
    lastModified: '2024-01-05',
    createdBy: 'Admin Système'
  }
];

function AdminWorkflows() {
  const navigate = useNavigate();
  const [workflows, setWorkflows] = useState(mockWorkflows);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingWorkflow, setEditingWorkflow] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    status: 'active'
  });

  const handleOpenDialog = (workflow = null) => {
    setEditingWorkflow(workflow);
    if (workflow) {
      setFormData({
        name: workflow.name,
        description: workflow.description,
        status: workflow.status
      });
    } else {
      setFormData({
        name: '',
        description: '',
        status: 'active'
      });
    }
    setOpenDialog(true);
  };

  const handleCloseDialog = () => {
    setOpenDialog(false);
    setEditingWorkflow(null);
    setFormData({
      name: '',
      description: '',
      status: 'active'
    });
  };

  const handleSaveWorkflow = () => {
    if (editingWorkflow) {
      // Update existing workflow
      setWorkflows(workflows.map(w => 
        w.id === editingWorkflow.id 
          ? { ...w, ...formData, lastModified: new Date().toISOString().split('T')[0] }
          : w
      ));
    } else {
      // Create new workflow
      const newWorkflow = {
        id: workflows.length + 1,
        ...formData,
        steps: 0,
        averageTime: '0 jours',
        lastModified: new Date().toISOString().split('T')[0],
        createdBy: 'Admin Système'
      };
      setWorkflows([...workflows, newWorkflow]);
    }
    handleCloseDialog();
  };

  const handleToggleStatus = (workflowId) => {
    setWorkflows(workflows.map(w => 
      w.id === workflowId 
        ? { ...w, status: w.status === 'active' ? 'inactive' : 'active' }
        : w
    ));
  };

  const handleDeleteWorkflow = (workflowId) => {
    setWorkflows(workflows.filter(w => w.id !== workflowId));
  };

  const getStatusColor = (status) => {
    return status === 'active' ? 'success' : 'default';
  };

  const getStatusIcon = (status) => {
    return status === 'active' ? <ActiveIcon /> : <InactiveIcon />;
  };

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Header */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Box>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold', mb: 1 }}>
            Configuration des Workflows
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Gérez les processus de validation et d'approbation
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
          Nouveau Workflow
        </Button>
      </Box>

      {/* Workflows Grid */}
      <Grid container spacing={3}>
        {workflows.map((workflow) => (
          <Grid item xs={12} md={6} lg={4} key={workflow.id}>
            <Card 
              sx={{ 
                height: '100%',
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
                      <WorkflowIcon />
                    </Avatar>
                    <Box>
                      <Typography variant="h6" component="h3" sx={{ fontWeight: 'bold' }}>
                        {workflow.name}
                      </Typography>
                      <Chip
                        icon={getStatusIcon(workflow.status)}
                        label={workflow.status === 'active' ? 'Actif' : 'Inactif'}
                        color={getStatusColor(workflow.status)}
                        size="small"
                      />
                    </Box>
                  </Box>
                  <Box>
                    <IconButton
                      size="small"
                      onClick={() => handleToggleStatus(workflow.id)}
                      color={workflow.status === 'active' ? 'primary' : 'default'}
                    >
                      {workflow.status === 'active' ? <PauseIcon /> : <PlayIcon />}
                    </IconButton>
                  </Box>
                </Box>

                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  {workflow.description}
                </Typography>

                <Grid container spacing={2} sx={{ mb: 2 }}>
                  <Grid item xs={6}>
                    <Typography variant="body2" color="text.secondary">
                      Étapes
                    </Typography>
                    <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                      {workflow.steps}
                    </Typography>
                  </Grid>
                  <Grid item xs={6}>
                    <Typography variant="body2" color="text.secondary">
                      Durée Moyenne
                    </Typography>
                    <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                      {workflow.averageTime}
                    </Typography>
                  </Grid>
                </Grid>

                <Typography variant="caption" color="text.secondary" sx={{ mb: 2, display: 'block' }}>
                  Modifié le {workflow.lastModified} par {workflow.createdBy}
                </Typography>

                <Box sx={{ display: 'flex', gap: 1 }}>
                  <Button
                    size="small"
                    startIcon={<SettingsIcon />}
                    onClick={() => navigate(`/admin/workflows/${workflow.id}/steps`)}
                    sx={{ flexGrow: 1 }}
                  >
                    Configurer
                  </Button>
                  <IconButton
                    size="small"
                    onClick={() => handleOpenDialog(workflow)}
                    color="primary"
                  >
                    <EditIcon />
                  </IconButton>
                  <IconButton
                    size="small"
                    onClick={() => handleDeleteWorkflow(workflow.id)}
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
      <Dialog open={openDialog} onClose={handleCloseDialog} maxWidth="sm" fullWidth>
        <DialogTitle>
          {editingWorkflow ? 'Modifier le Workflow' : 'Nouveau Workflow'}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, pt: 1 }}>
            <TextField
              label="Nom du Workflow"
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              fullWidth
              required
            />
            <TextField
              label="Description"
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              fullWidth
              multiline
              rows={3}
              required
            />
            <FormControl fullWidth>
              <InputLabel>Statut</InputLabel>
              <Select
                value={formData.status}
                onChange={(e) => setFormData({ ...formData, status: e.target.value })}
                label="Statut"
              >
                <MenuItem value="active">Actif</MenuItem>
                <MenuItem value="inactive">Inactif</MenuItem>
              </Select>
            </FormControl>
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseDialog}>Annuler</Button>
          <Button 
            onClick={handleSaveWorkflow} 
            variant="contained"
            disabled={!formData.name || !formData.description}
          >
            {editingWorkflow ? 'Modifier' : 'Créer'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default AdminWorkflows;
