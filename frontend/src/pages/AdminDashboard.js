import React, { useState, useEffect } from 'react';
import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  CardActionArea,
  Avatar,
  Chip,
  Button,
  Paper,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  Divider,
  LinearProgress,
} from '@mui/material';
import {
  Settings as ConfigIcon,
  Timeline as WorkflowIcon,
  People as RoleIcon,
  Business as DeptIcon,
  Assignment as ProcessIcon,
  CheckCircle as ValidationIcon,
  Dashboard as DashboardIcon,
  TrendingUp as StatsIcon,
  Notifications as NotificationIcon,
  Schedule as PendingIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const adminFeatures = [
  {
    title: 'Configuration des Workflows',
    description: 'Créer et gérer les processus de validation',
    icon: <WorkflowIcon sx={{ fontSize: 40 }} />,
    path: '/admin/workflows',
    color: '#1976d2',
    stats: '12 workflows actifs'
  },
  {
    title: 'Gestion des Rôles',
    description: 'Définir les permissions et responsabilités',
    icon: <RoleIcon sx={{ fontSize: 40 }} />,
    path: '/admin/roles',
    color: '#2e7d32',
    stats: '8 rôles configurés'
  },
  {
    title: 'Départements & Bureaux',
    description: 'Organiser la structure administrative',
    icon: <DeptIcon sx={{ fontSize: 40 }} />,
    path: '/admin/organization',
    color: '#ed6c02',
    stats: '4 départements, 12 bureaux'
  },
  {
    title: 'Processus Métier',
    description: 'Configurer les étapes de validation',
    icon: <ProcessIcon sx={{ fontSize: 40 }} />,
    path: '/admin/process',
    color: '#9c27b0',
    stats: '25 processus configurés'
  }
];

const recentActivities = [
  { action: 'Nouveau workflow créé', user: 'Admin Système', time: 'Il y a 2 min', type: 'workflow' },
  { action: 'Rôle mis à jour', user: 'Chef Service', time: 'Il y a 15 min', type: 'role' },
  { action: 'Bureau ajouté', user: 'Admin Système', time: 'Il y a 1h', type: 'dept' },
  { action: 'Processus validé', user: 'Agent', time: 'Il y a 2h', type: 'process' },
  { action: 'Workflow activé', user: 'Chef Service', time: 'Il y a 3h', type: 'workflow' },
];

const systemStats = {
  totalWorkflows: 12,
  activeProcesses: 45,
  pendingValidations: 8,
  completedToday: 23
};

function AdminDashboard() {
  const navigate = useNavigate();
  const { user } = useAuth();
  const [stats, setStats] = useState(systemStats);

  const handleFeatureClick = (path) => {
    navigate(path);
  };

  const getActivityIcon = (type) => {
    switch (type) {
      case 'workflow': return <WorkflowIcon />;
      case 'role': return <RoleIcon />;
      case 'dept': return <DeptIcon />;
      case 'process': return <ProcessIcon />;
      default: return <NotificationIcon />;
    }
  };

  const getActivityColor = (type) => {
    switch (type) {
      case 'workflow': return '#1976d2';
      case 'role': return '#2e7d32';
      case 'dept': return '#ed6c02';
      case 'process': return '#9c27b0';
      default: return '#666';
    }
  };

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold', mb: 1 }}>
          Administration Système
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Gérez les workflows, rôles et processus administratifs
        </Typography>
      </Box>

      {/* Quick Stats */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar sx={{ bgcolor: '#1976d2', mr: 2 }}>
                  <WorkflowIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {stats.totalWorkflows}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Workflows Actifs
                  </Typography>
                </Box>
              </Box>
              <LinearProgress 
                variant="determinate" 
                value={75} 
                sx={{ height: 6, borderRadius: 3 }}
              />
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar sx={{ bgcolor: '#2e7d32', mr: 2 }}>
                  <ProcessIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {stats.activeProcesses}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Processus Actifs
                  </Typography>
                </Box>
              </Box>
              <LinearProgress 
                variant="determinate" 
                value={60} 
                sx={{ height: 6, borderRadius: 3, color: '#2e7d32' }}
              />
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar sx={{ bgcolor: '#ed6c02', mr: 2 }}>
                  <PendingIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {stats.pendingValidations}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    En Attente
                  </Typography>
                </Box>
              </Box>
              <LinearProgress 
                variant="determinate" 
                value={30} 
                sx={{ height: 6, borderRadius: 3, color: '#ed6c02' }}
              />
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                <Avatar sx={{ bgcolor: '#9c27b0', mr: 2 }}>
                  <ValidationIcon />
                </Avatar>
                <Box>
                  <Typography variant="h6" sx={{ fontWeight: 'bold' }}>
                    {stats.completedToday}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    Terminés Aujourd'hui
                  </Typography>
                </Box>
              </Box>
              <LinearProgress 
                variant="determinate" 
                value={90} 
                sx={{ height: 6, borderRadius: 3, color: '#9c27b0' }}
              />
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Main Features Grid */}
      <Grid container spacing={4} sx={{ mb: 4 }}>
        {adminFeatures.map((feature, index) => (
          <Grid item xs={12} sm={6} md={3} key={index}>
            <Card 
              sx={{ 
                height: '100%',
                transition: 'all 0.3s ease',
                '&:hover': {
                  transform: 'translateY(-4px)',
                  boxShadow: 6,
                }
              }}
            >
              <CardActionArea onClick={() => handleFeatureClick(feature.path)} sx={{ height: '100%' }}>
                <CardContent sx={{ p: 3, height: '100%', display: 'flex', flexDirection: 'column' }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Avatar sx={{ bgcolor: feature.color, mr: 2 }}>
                      {feature.icon}
                    </Avatar>
                    <Box sx={{ flexGrow: 1 }}>
                      <Typography variant="h6" component="h3" sx={{ fontWeight: 'bold' }}>
                        {feature.title}
                      </Typography>
                    </Box>
                  </Box>
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 2, flexGrow: 1 }}>
                    {feature.description}
                  </Typography>
                  <Chip 
                    label={feature.stats} 
                    size="small" 
                    sx={{ 
                      bgcolor: `${feature.color}20`,
                      color: feature.color,
                      fontWeight: 'medium',
                      alignSelf: 'flex-start'
                    }} 
                  />
                </CardContent>
              </CardActionArea>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Recent Activities */}
      <Grid container spacing={4}>
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 3 }}>
              Activités Récentes
            </Typography>
            <List>
              {recentActivities.map((activity, index) => (
                <React.Fragment key={index}>
                  <ListItem sx={{ px: 0 }}>
                    <ListItemIcon sx={{ minWidth: 40 }}>
                      <Avatar sx={{ bgcolor: getActivityColor(activity.type), width: 32, height: 32 }}>
                        {getActivityIcon(activity.type)}
                      </Avatar>
                    </ListItemIcon>
                    <ListItemText
                      primary={activity.action}
                      secondary={`${activity.user} • ${activity.time}`}
                      primaryTypographyProps={{ fontWeight: 'medium' }}
                    />
                  </ListItem>
                  {index < recentActivities.length - 1 && <Divider />}
                </React.Fragment>
              ))}
            </List>
          </Paper>
        </Grid>

        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 3 }}>
              Actions Rapides
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <Button 
                variant="contained" 
                startIcon={<WorkflowIcon />}
                onClick={() => navigate('/admin/workflows')}
                sx={{ justifyContent: 'flex-start' }}
              >
                Créer un Workflow
              </Button>
              <Button 
                variant="outlined" 
                startIcon={<RoleIcon />}
                onClick={() => navigate('/admin/roles')}
                sx={{ justifyContent: 'flex-start' }}
              >
                Gérer les Rôles
              </Button>
              <Button 
                variant="outlined" 
                startIcon={<DeptIcon />}
                onClick={() => navigate('/admin/organization')}
                sx={{ justifyContent: 'flex-start' }}
              >
                Ajouter un Bureau
              </Button>
              <Button 
                variant="outlined" 
                startIcon={<ProcessIcon />}
                onClick={() => navigate('/admin/process')}
                sx={{ justifyContent: 'flex-start' }}
              >
                Configurer un Processus
              </Button>
            </Box>
          </Paper>
        </Grid>
      </Grid>
    </Container>
  );
}

export default AdminDashboard;
