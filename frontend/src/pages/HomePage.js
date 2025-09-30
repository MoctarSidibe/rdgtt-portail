import React from 'react';
import {
  Box,
  Container,
  Typography,
  Grid,
  Card,
  CardContent,
  CardActions,
  Button,
  Chip,
  Paper,
  Avatar,
  useTheme,
  useMediaQuery,
} from '@mui/material';
import {
  School as SchoolIcon,
  CreditCard as CreditCardIcon,
  Assignment as AssignmentIcon,
  DirectionsCar as CarIcon,
  LocalShipping as ShippingIcon,
  Person as PersonIcon,
  CheckCircle as CheckIcon,
  Schedule as ScheduleIcon,
  Construction as ConstructionIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

function HomePage() {
  const theme = useTheme();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const navigate = useNavigate();

  const services = [
    {
      id: 'auto-ecole',
      title: 'Auto-École',
      description: 'Gestion des auto-écoles, inscription des candidats et suivi de formation',
      icon: <SchoolIcon sx={{ fontSize: 60 }} />,
      status: 'available',
      statusText: 'Disponible',
      color: '#1a237e',
      features: [
        'Inscription des auto-écoles',
        'Gestion des candidats',
        'Suivi de formation',
        'Processus d\'inspection',
        'Génération des permis d\'exploitation'
      ],
      action: () => navigate('/auto-ecoles')
    },
    {
      id: 'permis-conduire',
      title: 'Permis de Conduire',
      description: 'Gestion complète des permis de conduire et services connexes',
      icon: <CreditCardIcon sx={{ fontSize: 60 }} />,
      status: 'development',
      statusText: 'En développement',
      color: '#388e3c',
      features: [
        'Permis de conduire',
        'Duplicata permis',
        'Renouvellement permis',
        'Catégorie C et D',
        'Conversion et échange',
        'Attestation d\'authenticité'
      ],
      action: () => navigate('/permis')
    },
    {
      id: 'fiche-enregistrement',
      title: 'Fiche d\'Enregistrement',
      description: 'Gestion des fiches d\'enregistrement des véhicules',
      icon: <AssignmentIcon sx={{ fontSize: 60 }} />,
      status: 'coming-soon',
      statusText: 'Bientôt disponible',
      color: '#f57c00',
      features: [
        'Fiche d\'enregistrement',
        'Duplicata fiche',
        'Renouvellement fiche'
      ],
      action: null
    },
    {
      id: 'carte-grise',
      title: 'Certificat d\'Immatriculation',
      description: 'Gestion des cartes grises et certificats d\'immatriculation',
      icon: <CarIcon sx={{ fontSize: 60 }} />,
      status: 'coming-soon',
      statusText: 'Bientôt disponible',
      color: '#7b1fa2',
      features: [
        'Certificat d\'immatriculation',
        'Carte grise',
        'Certificat administratif'
      ],
      action: null
    },
    {
      id: 'licence-transport',
      title: 'Licence de Transport',
      description: 'Gestion des licences de transport de marchandises',
      icon: <ShippingIcon sx={{ fontSize: 60 }} />,
      status: 'coming-soon',
      statusText: 'Bientôt disponible',
      color: '#c2185b',
      features: [
        'Licence de transport',
        'Renouvellement licence',
        'Gestion des transporteurs'
      ],
      action: null
    },
    {
      id: 'portail-citoyen',
      title: 'Portail Citoyen',
      description: 'Interface citoyen pour le suivi des demandes',
      icon: <PersonIcon sx={{ fontSize: 60 }} />,
      status: 'coming-soon',
      statusText: 'Bientôt disponible',
      color: '#5d4037',
      features: [
        'Suivi des demandes',
        'Notifications',
        'Historique des démarches',
        'Paiements en ligne'
      ],
      action: null
    }
  ];

  const getStatusChip = (status) => {
    const statusConfig = {
      available: { color: 'success', icon: <CheckIcon /> },
      development: { color: 'warning', icon: <ConstructionIcon /> },
      'coming-soon': { color: 'info', icon: <ScheduleIcon /> }
    };
    
    const config = statusConfig[status] || statusConfig['coming-soon'];
    
    return (
      <Chip
        icon={config.icon}
        label={status === 'available' ? 'Disponible' : status === 'development' ? 'En développement' : 'Bientôt disponible'}
        color={config.color}
        variant="filled"
        size="small"
      />
    );
  };

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'background.default' }}>
      {/* Header Section */}
      <Paper
        elevation={0}
        sx={{
          background: 'linear-gradient(135deg, #1a237e 0%, #283593 100%)',
          color: 'white',
          py: 8,
          mb: 4,
          position: 'relative',
          '&::after': {
            content: '""',
            position: 'absolute',
            bottom: 0,
            left: 0,
            right: 0,
            height: '8px',
            background: 'linear-gradient(90deg, #00a651 0%, #00a651 33.33%, #fcd116 33.33%, #fcd116 66.66%, #1a237e 66.66%, #1a237e 100%)',
          }
        }}
      >
        <Container maxWidth="lg">
          <Box textAlign="center">
            <Box
              sx={{
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
                gap: 3,
                mb: 3,
                flexWrap: 'wrap',
              }}
            >
              <Box
                component="img"
                src="/dgttlogo.jpg"
                alt="DGTT Logo"
                sx={{
                  width: 120,
                  height: 120,
                  objectFit: 'contain',
                  borderRadius: 2,
                  bgcolor: 'rgba(255,255,255,0.1)',
                  p: 1,
                }}
              />
              <Box
                component="img"
                src="/emblemgabon.png"
                alt="Emblème du Gabon"
                sx={{
                  width: 100,
                  height: 100,
                  objectFit: 'contain',
                  borderRadius: 2,
                  bgcolor: 'rgba(255,255,255,0.1)',
                  p: 1,
                }}
              />
              <Box
                component="img"
                src="/rengus-logo.jpg"
                alt="Rengus Logo"
                sx={{
                  width: 120,
                  height: 120,
                  objectFit: 'contain',
                  borderRadius: 2,
                  bgcolor: 'rgba(255,255,255,0.1)',
                  p: 1,
                }}
              />
            </Box>
            <Typography variant="h2" component="h1" gutterBottom fontWeight="bold">
              R-DGTT Portail
            </Typography>
            <Typography variant="h5" component="h2" sx={{ mb: 2, opacity: 0.9 }}>
              Ministère des Transports, de la Marine Marchande et de la Logistique du Gabon
            </Typography>
            <Typography variant="h6" sx={{ opacity: 0.8, maxWidth: 600, mx: 'auto' }}>
              Plateforme numérique pour la gestion des services de transport
            </Typography>
          </Box>
        </Container>
      </Paper>

      {/* Services Section */}
      <Container maxWidth="lg" sx={{ pb: 8 }}>
        <Box textAlign="center" mb={6}>
          <Typography variant="h3" component="h2" gutterBottom fontWeight="bold">
            Services Disponibles
          </Typography>
          <Typography variant="h6" color="text.secondary" sx={{ maxWidth: 600, mx: 'auto' }}>
            Choisissez le service dont vous avez besoin pour vos démarches administratives
          </Typography>
        </Box>

        <Grid container spacing={4}>
          {services.map((service) => (
            <Grid item xs={12} md={6} lg={4} key={service.id}>
              <Card
                sx={{
                  height: '100%',
                  display: 'flex',
                  flexDirection: 'column',
                  transition: 'all 0.3s ease-in-out',
                  '&:hover': {
                    transform: 'translateY(-8px)',
                    boxShadow: theme.shadows[8],
                  },
                  border: `2px solid ${service.color}20`,
                  position: 'relative',
                  overflow: 'visible',
                }}
              >
                {/* Status Badge */}
                <Box
                  sx={{
                    position: 'absolute',
                    top: -12,
                    right: 16,
                    zIndex: 1,
                  }}
                >
                  {getStatusChip(service.status)}
                </Box>

                <CardContent sx={{ flexGrow: 1, pt: 3 }}>
                  <Box
                    sx={{
                      display: 'flex',
                      flexDirection: 'column',
                      alignItems: 'center',
                      textAlign: 'center',
                      mb: 3,
                    }}
                  >
                    <Avatar
                      sx={{
                        bgcolor: `${service.color}20`,
                        color: service.color,
                        width: 100,
                        height: 100,
                        mb: 2,
                      }}
                    >
                      {service.icon}
                    </Avatar>
                    <Typography variant="h5" component="h3" gutterBottom fontWeight="bold">
                      {service.title}
                    </Typography>
                    <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
                      {service.description}
                    </Typography>
                  </Box>

                  {/* Features List */}
                  <Box sx={{ mb: 3 }}>
                    <Typography variant="subtitle2" fontWeight="bold" gutterBottom>
                      Fonctionnalités :
                    </Typography>
                    {service.features.map((feature, index) => (
                      <Box
                        key={index}
                        sx={{
                          display: 'flex',
                          alignItems: 'center',
                          mb: 1,
                          fontSize: '0.875rem',
                        }}
                      >
                        <CheckIcon
                          sx={{
                            fontSize: 16,
                            color: service.color,
                            mr: 1,
                          }}
                        />
                        <Typography variant="body2">{feature}</Typography>
                      </Box>
                    ))}
                  </Box>
                </CardContent>

                <CardActions sx={{ p: 2, pt: 0 }}>
                  <Button
                    fullWidth
                    variant={service.status === 'available' ? 'contained' : 'outlined'}
                    size="large"
                    onClick={service.action}
                    disabled={service.status !== 'available'}
                    sx={{
                      bgcolor: service.status === 'available' ? service.color : 'transparent',
                      color: service.status === 'available' ? 'white' : service.color,
                      borderColor: service.color,
                      '&:hover': {
                        bgcolor: service.status === 'available' ? `${service.color}dd` : `${service.color}20`,
                      },
                    }}
                  >
                    {service.status === 'available' 
                      ? 'Accéder au service' 
                      : service.status === 'development' 
                        ? 'En cours de développement' 
                        : 'Bientôt disponible'
                    }
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>

        {/* Footer Info */}
        <Box textAlign="center" mt={8}>
          <Paper
            elevation={0}
            sx={{
              p: 4,
              bgcolor: 'grey.50',
              border: '1px solid',
              borderColor: 'grey.200',
            }}
          >
            <Typography variant="h6" gutterBottom fontWeight="bold">
              Besoin d'aide ?
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mb: 2 }}>
              Notre équipe est là pour vous accompagner dans vos démarches
            </Typography>
            <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, flexWrap: 'wrap' }}>
              <Chip
                label="📞 +241 01 23 45 67"
                variant="outlined"
                color="primary"
              />
              <Chip
                label="✉️ contact@rdgtt.ga"
                variant="outlined"
                color="primary"
              />
              <Chip
                label="🕒 Lun-Ven: 8h-17h"
                variant="outlined"
                color="primary"
              />
            </Box>
          </Paper>
        </Box>
      </Container>
    </Box>
  );
}

export default HomePage;
