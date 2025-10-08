import React from 'react';
import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  CardActionArea,
  AppBar,
  Toolbar,
  Button,
  Chip,
  Avatar,
  useTheme,
  useMediaQuery,
} from '@mui/material';
import {
  AdminPanelSettings as AdminIcon,
  School as AutoEcoleIcon,
  CreditCard as PermisIcon,
  Person as CitizenIcon,
  CheckCircle as StatusIcon,
  Business as BureauIcon,
  Assignment as WorkflowIcon,
  Settings as ConfigIcon,
  Timeline as ProcessIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../contexts/AuthContext';

const getServices = (userRole) => {
  const allServices = [
    {
      title: 'Administrateur Système',
      description: 'Configuration des workflows, gestion des rôles et validation des processus administratifs',
      icon: <AdminIcon sx={{ fontSize: 40 }} />,
      path: '/admin',
      color: '#1976d2',
      features: ['Workflows', 'Rôles', 'Validation', 'Configuration'],
      roles: ['ADMIN', 'CHEF_SERVICE']
    },
    {
      title: 'Auto-Écoles',
      description: 'Gestion des auto-écoles, candidats et formation à la conduite',
      icon: <AutoEcoleIcon sx={{ fontSize: 40 }} />,
      path: '/auto-ecole/register',
      color: '#2e7d32',
      features: ['Inscription', 'Candidats', 'Formation', 'Examens'],
      roles: ['ADMIN', 'CHEF_SERVICE', 'AGENT', 'CITOYEN']
    },
    {
      title: 'Permis de Conduire',
      description: 'Délivrance et gestion des permis de conduire',
      icon: <PermisIcon sx={{ fontSize: 40 }} />,
      path: '/permis',
      color: '#ed6c02',
      features: ['Demandes', 'Validation', 'Délivrance', 'Suivi'],
      roles: ['ADMIN', 'CHEF_SERVICE', 'AGENT', 'CITOYEN']
    },
    {
      title: 'Statut Citoyen',
      description: 'Vérification du statut de vos démarches administratives',
      icon: <StatusIcon sx={{ fontSize: 40 }} />,
      path: '/citizen/status',
      color: '#9c27b0',
      features: ['Vérification', 'Statut', 'Suivi', 'Notifications'],
      roles: ['ADMIN', 'CHEF_SERVICE', 'AGENT', 'CITOYEN']
    }
  ];
  
  return allServices.filter(service => 
    !userRole || service.roles.includes(userRole) || service.roles.includes('CITOYEN')
  );
};

const adminFeatures = [
  {
    title: 'Configuration des Workflows',
    description: 'Créer et gérer les processus de validation',
    icon: <WorkflowIcon />,
    path: '/admin/workflows'
  },
  {
    title: 'Gestion des Rôles',
    description: 'Définir les permissions et responsabilités',
    icon: <AdminIcon />,
    path: '/admin/roles'
  },
  {
    title: 'Départements & Bureaux',
    description: 'Organiser la structure administrative',
    icon: <BureauIcon />,
    path: '/admin/organization'
  },
  {
    title: 'Processus Métier',
    description: 'Configurer les étapes de validation',
    icon: <ProcessIcon />,
    path: '/admin/process'
  }
];

function ServiceCard({ service, onClick }) {
  return (
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
      <CardActionArea onClick={onClick} sx={{ height: '100%' }}>
        <CardContent sx={{ p: 3, height: '100%', display: 'flex', flexDirection: 'column' }}>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
            <Avatar sx={{ bgcolor: service.color, mr: 2 }}>
              {service.icon}
            </Avatar>
            <Typography variant="h6" component="h2" sx={{ fontWeight: 'bold' }}>
              {service.title}
            </Typography>
          </Box>
          <Typography variant="body2" color="text.secondary" sx={{ mb: 2, flexGrow: 1 }}>
            {service.description}
          </Typography>
          <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
            {service.features.map((feature, index) => (
              <Chip 
                key={index} 
                label={feature} 
                size="small" 
                sx={{ 
                  bgcolor: `${service.color}20`,
                  color: service.color,
                  fontWeight: 'medium'
                }} 
              />
            ))}
          </Box>
        </CardContent>
      </CardActionArea>
    </Card>
  );
}

function HomePage() {
  const navigate = useNavigate();
  const { user } = useAuth();
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));

  const handleServiceClick = (path) => {
    navigate(path);
  };

  const handleLogin = () => {
    navigate('/login');
  };

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'grey.50' }}>
      {/* Top Navigation Bar */}
      <AppBar position="sticky" elevation={0} sx={{ bgcolor: 'white', color: 'text.primary' }}>
        <Toolbar>
          <Box sx={{ display: 'flex', alignItems: 'center', flexGrow: 1 }}>
            <Avatar 
              src="/dgttlogo.jpg" 
              sx={{ width: 40, height: 40, mr: 2 }}
              alt="DGTT Logo"
            />
            <Box>
              <Typography variant="h6" sx={{ fontWeight: 'bold', color: '#1976d2' }}>
                R-DGTT Portail
              </Typography>
              <Typography variant="caption" color="text.secondary">
                République Gabonaise
              </Typography>
            </Box>
          </Box>
          
          <Box sx={{ display: 'flex', gap: 2, alignItems: 'center' }}>
            <Button
              variant="outlined"
              onClick={() => navigate('/services/related')}
              sx={{ 
                borderColor: '#1976d2',
                color: '#1976d2',
                '&:hover': { borderColor: '#1976d2', bgcolor: '#1976d210' }
              }}
            >
              Services Connexes
            </Button>
            {!user && (
              <Button
                variant="contained"
                onClick={handleLogin}
                sx={{ 
                  bgcolor: '#1976d2',
                  '&:hover': { bgcolor: '#1565c0' }
                }}
              >
                Connexion
              </Button>
            )}
            {user && (
              <Button
                variant="contained"
                onClick={() => navigate('/dashboard')}
                sx={{ 
                  bgcolor: '#1976d2',
                  '&:hover': { bgcolor: '#1565c0' }
                }}
              >
                Tableau de bord
              </Button>
            )}
          </Box>
        </Toolbar>
      </AppBar>

      <Container maxWidth="xl" sx={{ py: 4 }}>
        {/* Hero Section */}
        <Box sx={{ textAlign: 'center', mb: 6 }}>
          <Typography 
            variant={isMobile ? "h4" : "h3"} 
            component="h1" 
            sx={{ 
              fontWeight: 'bold', 
              mb: 2,
              background: 'linear-gradient(45deg, #1976d2, #42a5f5)',
              backgroundClip: 'text',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent'
            }}
          >
            Portail Numérique des Transports
          </Typography>
          <Typography 
            variant="h6" 
            color="text.secondary" 
            sx={{ mb: 4, maxWidth: 800, mx: 'auto' }}
          >
            Simplifiez vos démarches administratives avec notre plateforme numérique 
            dédiée aux services de transport du Gabon
          </Typography>
        </Box>

        {/* Main Services Grid */}
        <Grid container spacing={4} sx={{ mb: 6 }}>
          {getServices(user?.role).map((service, index) => (
            <Grid item xs={12} sm={6} lg={3} key={index}>
              <ServiceCard 
                service={service} 
                onClick={() => handleServiceClick(service.path)}
              />
            </Grid>
          ))}
        </Grid>

        {/* Admin System Section - Only visible to admin users */}
        {user && (user.role === 'ADMIN' || user.role === 'CHEF_SERVICE') && (
          <Box sx={{ mb: 6 }}>
            <Typography 
              variant="h4" 
              component="h2" 
              sx={{ 
                fontWeight: 'bold', 
                mb: 3, 
                textAlign: 'center',
                color: '#1976d2'
              }}
            >
              Administration Système
            </Typography>
            <Typography 
              variant="body1" 
              color="text.secondary" 
              sx={{ textAlign: 'center', mb: 4, maxWidth: 600, mx: 'auto' }}
            >
              Outils avancés pour la configuration et la gestion des workflows administratifs
            </Typography>
            
            <Grid container spacing={3}>
              {adminFeatures.map((feature, index) => (
                <Grid item xs={12} sm={6} md={3} key={index}>
                  <Card 
                    sx={{ 
                      height: '100%',
                      border: '2px solid transparent',
                      transition: 'all 0.3s ease',
                      '&:hover': {
                        borderColor: '#1976d2',
                        transform: 'translateY(-2px)',
                        boxShadow: 4,
                      }
                    }}
                  >
                    <CardActionArea onClick={() => handleServiceClick(feature.path)}>
                      <CardContent sx={{ p: 3, textAlign: 'center' }}>
                        <Avatar sx={{ bgcolor: '#1976d2', mx: 'auto', mb: 2, width: 56, height: 56 }}>
                          {feature.icon}
                        </Avatar>
                        <Typography variant="h6" component="h3" sx={{ fontWeight: 'bold', mb: 1 }}>
                          {feature.title}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                          {feature.description}
                        </Typography>
                      </CardContent>
                    </CardActionArea>
                  </Card>
                </Grid>
              ))}
            </Grid>
          </Box>
        )}

        {/* Quick Stats */}
        <Box sx={{ 
          bgcolor: 'white', 
          borderRadius: 2, 
          p: 4, 
          boxShadow: 1,
          textAlign: 'center'
        }}>
          <Typography variant="h5" sx={{ fontWeight: 'bold', mb: 3, color: '#1976d2' }}>
            Services Disponibles
          </Typography>
          <Grid container spacing={4}>
            <Grid item xs={6} md={3}>
              <Typography variant="h3" sx={{ fontWeight: 'bold', color: '#1976d2' }}>4</Typography>
              <Typography variant="body2" color="text.secondary">Services Principaux</Typography>
            </Grid>
            <Grid item xs={6} md={3}>
              <Typography variant="h3" sx={{ fontWeight: 'bold', color: '#2e7d32' }}>24/7</Typography>
              <Typography variant="body2" color="text.secondary">Disponibilité</Typography>
            </Grid>
            <Grid item xs={6} md={3}>
              <Typography variant="h3" sx={{ fontWeight: 'bold', color: '#ed6c02' }}>100%</Typography>
              <Typography variant="body2" color="text.secondary">Numérisé</Typography>
            </Grid>
            <Grid item xs={6} md={3}>
              <Typography variant="h3" sx={{ fontWeight: 'bold', color: '#9c27b0' }}>✓</Typography>
              <Typography variant="body2" color="text.secondary">Sécurisé</Typography>
            </Grid>
          </Grid>
        </Box>
      </Container>
    </Box>
  );
}

export default HomePage;