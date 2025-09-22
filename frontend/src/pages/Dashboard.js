import React, { useState, useEffect } from 'react';
import {
  Box,
  Grid,
  Card,
  CardContent,
  Typography,
  Paper,
  List,
  ListItem,
  ListItemText,
  ListItemIcon,
  Chip,
  Avatar,
} from '@mui/material';
import {
  People as PeopleIcon,
  Business as BusinessIcon,
  School as SchoolIcon,
  Person as PersonIcon,
  CreditCard as CreditCardIcon,
  TrendingUp as TrendingUpIcon,
  Notifications as NotificationsIcon,
} from '@mui/icons-material';
import { useAuth } from '../contexts/AuthContext';
import axios from 'axios';

function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState({
    totalUsers: 0,
    totalDepartments: 0,
    totalBureaus: 0,
    totalAutoEcoles: 0,
    totalCandidats: 0,
    totalPermis: 0,
  });
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDashboardData();
  }, []);

  const fetchDashboardData = async () => {
    try {
      // Fetch statistics
      const [usersRes, departmentsRes, bureausRes] = await Promise.all([
        axios.get('http://localhost:8081/api/usager/users/stats/count'),
        axios.get('http://localhost:8081/api/usager/departments/stats/count'),
        axios.get('http://localhost:8081/api/usager/bureaus/stats/count'),
      ]);

      setStats({
        totalUsers: usersRes.data,
        totalDepartments: departmentsRes.data,
        totalBureaus: bureausRes.data,
        totalAutoEcoles: 0, // Will be implemented when auto-ecole service is ready
        totalCandidats: 0, // Will be implemented when auto-ecole service is ready
        totalPermis: 0, // Will be implemented when permis service is ready
      });

      // Mock notifications for now
      setNotifications([
        { id: 1, message: 'Nouvelle auto-école en attente d\'approbation', type: 'warning', time: 'Il y a 2 heures' },
        { id: 2, message: '3 nouveaux candidats inscrits aujourd\'hui', type: 'info', time: 'Il y a 4 heures' },
        { id: 3, message: 'Inspection programmée pour demain', type: 'success', time: 'Il y a 6 heures' },
      ]);

    } catch (error) {
      console.error('Erreur lors du chargement des données:', error);
    } finally {
      setLoading(false);
    }
  };

  const statCards = [
    {
      title: 'Utilisateurs',
      value: stats.totalUsers,
      icon: <PeopleIcon />,
      color: '#1976d2',
    },
    {
      title: 'Départements',
      value: stats.totalDepartments,
      icon: <BusinessIcon />,
      color: '#388e3c',
    },
    {
      title: 'Bureaux',
      value: stats.totalBureaus,
      icon: <BusinessIcon />,
      color: '#f57c00',
    },
    {
      title: 'Auto-Écoles',
      value: stats.totalAutoEcoles,
      icon: <SchoolIcon />,
      color: '#7b1fa2',
    },
    {
      title: 'Candidats',
      value: stats.totalCandidats,
      icon: <PersonIcon />,
      color: '#c2185b',
    },
    {
      title: 'Permis',
      value: stats.totalPermis,
      icon: <CreditCardIcon />,
      color: '#5d4037',
    },
  ];

  const getChipColor = (type) => {
    switch (type) {
      case 'warning': return 'warning';
      case 'info': return 'info';
      case 'success': return 'success';
      case 'error': return 'error';
      default: return 'default';
    }
  };

  if (loading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
        <Typography>Chargement du tableau de bord...</Typography>
      </Box>
    );
  }

  return (
    <Box>
      <Typography variant="h4" gutterBottom>
        Tableau de bord
      </Typography>
      <Typography variant="subtitle1" color="text.secondary" sx={{ mb: 3 }}>
        Bienvenue, {user?.prenom} {user?.nomFamille}
      </Typography>

      <Grid container spacing={3}>
        {/* Statistics Cards */}
        {statCards.map((card, index) => (
          <Grid item xs={12} sm={6} md={4} key={index}>
            <Card>
              <CardContent>
                <Box display="flex" alignItems="center" justifyContent="space-between">
                  <Box>
                    <Typography color="text.secondary" gutterBottom>
                      {card.title}
                    </Typography>
                    <Typography variant="h4">
                      {card.value}
                    </Typography>
                  </Box>
                  <Avatar sx={{ bgcolor: card.color, width: 56, height: 56 }}>
                    {card.icon}
                  </Avatar>
                </Box>
              </CardContent>
            </Card>
          </Grid>
        ))}

        {/* Notifications */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Box display="flex" alignItems="center" mb={2}>
              <NotificationsIcon sx={{ mr: 1 }} />
              <Typography variant="h6">Notifications récentes</Typography>
            </Box>
            <List>
              {notifications.map((notification) => (
                <ListItem key={notification.id} divider>
                  <ListItemIcon>
                    <Chip
                      label={notification.type}
                      color={getChipColor(notification.type)}
                      size="small"
                    />
                  </ListItemIcon>
                  <ListItemText
                    primary={notification.message}
                    secondary={notification.time}
                  />
                </ListItem>
              ))}
            </List>
          </Paper>
        </Grid>

        {/* Quick Actions */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Actions rapides
            </Typography>
            <List>
              <ListItem button>
                <ListItemIcon>
                  <PeopleIcon />
                </ListItemIcon>
                <ListItemText primary="Gérer les utilisateurs" />
              </ListItem>
              <ListItem button>
                <ListItemIcon>
                  <SchoolIcon />
                </ListItemIcon>
                <ListItemText primary="Approuver les auto-écoles" />
              </ListItem>
              <ListItem button>
                <ListItemIcon>
                  <PersonIcon />
                </ListItemIcon>
                <ListItemText primary="Examiner les candidats" />
              </ListItem>
              <ListItem button>
                <ListItemIcon>
                  <CreditCardIcon />
                </ListItemIcon>
                <ListItemText primary="Traiter les permis" />
              </ListItem>
            </List>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
}

export default Dashboard;
