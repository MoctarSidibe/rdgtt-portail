import React, { useState } from 'react';
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
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Alert,
  Divider,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
} from '@mui/material';
import {
  CreditCard as CreditCardIcon,
  Assignment as AssignmentIcon,
  CheckCircle as CheckIcon,
  Schedule as ScheduleIcon,
  DirectionsCar as CarIcon,
  School as SchoolIcon,
  Person as PersonIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

function RelatedServices() {
  const navigate = useNavigate();
  const [selectedService, setSelectedService] = useState('');

  const services = [
    {
      id: 'duplicata',
      title: 'Duplicata Permis de Conduire',
      description: 'En cas de perte, vol ou détérioration de votre permis',
      icon: <CreditCardIcon />,
      color: '#f57c00',
      requirements: [
        'Déclaration de perte/vol',
        'Pièce d\'identité',
        'Photo d\'identité récente',
        'Justificatif de domicile'
      ],
      fees: '15,000 FCFA',
      duration: '15 jours ouvrés'
    },
    {
      id: 'renouvellement-c',
      title: 'Renouvellement Permis C',
      description: 'Renouvellement du permis de conduire catégorie C',
      icon: <CarIcon />,
      color: '#4CAF50',
      requirements: [
        'Permis de conduire actuel',
        'Certificat médical',
        'Photo d\'identité récente',
        'Justificatif de domicile'
      ],
      fees: '25,000 FCFA',
      duration: '20 jours ouvrés'
    },
    {
      id: 'renouvellement-d',
      title: 'Renouvellement Permis D',
      description: 'Renouvellement du permis de conduire catégorie D',
      icon: <CarIcon />,
      color: '#4CAF50',
      requirements: [
        'Permis de conduire actuel',
        'Certificat médical',
        'Photo d\'identité récente',
        'Justificatif de domicile'
      ],
      fees: '25,000 FCFA',
      duration: '20 jours ouvrés'
    },
    {
      id: 'conversion-etranger',
      title: 'Conversion Permis Étranger',
      description: 'Conversion d\'un permis de conduire étranger',
      icon: <AssignmentIcon />,
      color: '#2196F3',
      requirements: [
        'Permis de conduire étranger',
        'Traduction assermentée',
        'Pièce d\'identité',
        'Justificatif de domicile'
      ],
      fees: '30,000 FCFA',
      duration: '30 jours ouvrés'
    },
    {
      id: 'authenticite',
      title: 'Attestation d\'Authenticité',
      description: 'Vérification de l\'authenticité d\'un permis',
      icon: <CheckIcon />,
      color: '#9C27B0',
      requirements: [
        'Permis de conduire à vérifier',
        'Pièce d\'identité',
        'Justificatif de domicile'
      ],
      fees: '10,000 FCFA',
      duration: '10 jours ouvrés'
    },
    {
      id: 'legalisation',
      title: 'Légalisation Permis',
      description: 'Légalisation d\'un permis de conduire',
      icon: <AssignmentIcon />,
      color: '#FF5722',
      requirements: [
        'Permis de conduire original',
        'Pièce d\'identité',
        'Justificatif de domicile',
        'Motif de la légalisation'
      ],
      fees: '20,000 FCFA',
      duration: '15 jours ouvrés'
    }
  ];

  const handleServiceSelect = (serviceId) => {
    setSelectedService(serviceId);
  };

  const handleRequestService = () => {
    if (selectedService) {
      // Navigate to service request form
      navigate(`/services/request/${selectedService}`);
    }
  };

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'background.default', py: 4 }}>
      <Container maxWidth="lg">
        <Box textAlign="center" mb={4}>
          <Typography variant="h3" component="h1" gutterBottom fontWeight="bold">
            Services Connexes
          </Typography>
          <Typography variant="h6" color="text.secondary">
            Services disponibles pour les titulaires de permis de conduire
          </Typography>
        </Box>

        <Alert severity="info" sx={{ mb: 4 }}>
          <Typography variant="h6" gutterBottom>
            Information importante
          </Typography>
          <Typography variant="body2">
            Ces services sont destinés aux personnes ayant déjà un permis de conduire. 
            Pour obtenir un nouveau permis, vous devez d'abord vous inscrire dans une auto-école.
          </Typography>
        </Alert>

        <Grid container spacing={3}>
          {services.map((service) => (
            <Grid item xs={12} md={6} lg={4} key={service.id}>
              <Card
                sx={{
                  height: '100%',
                  display: 'flex',
                  flexDirection: 'column',
                  transition: 'all 0.3s ease-in-out',
                  '&:hover': {
                    transform: 'translateY(-4px)',
                    boxShadow: 4,
                  },
                  border: selectedService === service.id ? `2px solid ${service.color}` : '1px solid #e0e0e0',
                }}
              >
                <CardContent sx={{ flexGrow: 1 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Box
                      sx={{
                        bgcolor: `${service.color}20`,
                        color: service.color,
                        p: 1,
                        borderRadius: 1,
                        mr: 2,
                      }}
                    >
                      {service.icon}
                    </Box>
                    <Typography variant="h6" component="h3" fontWeight="bold">
                      {service.title}
                    </Typography>
                  </Box>
                  
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    {service.description}
                  </Typography>

                  <Box sx={{ mb: 2 }}>
                    <Typography variant="subtitle2" fontWeight="bold" gutterBottom>
                      Documents requis :
                    </Typography>
                    <List dense>
                      {service.requirements.map((req, index) => (
                        <ListItem key={index} sx={{ py: 0.5 }}>
                          <ListItemIcon sx={{ minWidth: 24 }}>
                            <CheckIcon sx={{ fontSize: 16, color: service.color }} />
                          </ListItemIcon>
                          <ListItemText 
                            primary={req} 
                            primaryTypographyProps={{ variant: 'body2' }}
                          />
                        </ListItem>
                      ))}
                    </List>
                  </Box>

                  <Box sx={{ display: 'flex', gap: 1, mb: 2 }}>
                    <Chip 
                      label={`Frais: ${service.fees}`} 
                      color="primary" 
                      size="small" 
                    />
                    <Chip 
                      label={`Délai: ${service.duration}`} 
                      color="secondary" 
                      size="small" 
                    />
                  </Box>
                </CardContent>

                <CardActions sx={{ p: 2, pt: 0 }}>
                  <Button
                    fullWidth
                    variant={selectedService === service.id ? 'contained' : 'outlined'}
                    onClick={() => handleServiceSelect(service.id)}
                    sx={{
                      bgcolor: selectedService === service.id ? service.color : 'transparent',
                      color: selectedService === service.id ? 'white' : service.color,
                      borderColor: service.color,
                      '&:hover': {
                        bgcolor: selectedService === service.id ? `${service.color}dd` : `${service.color}20`,
                      },
                    }}
                  >
                    {selectedService === service.id ? 'Sélectionné' : 'Sélectionner'}
                  </Button>
                </CardActions>
              </Card>
            </Grid>
          ))}
        </Grid>

        {selectedService && (
          <Paper elevation={2} sx={{ p: 3, mt: 4 }}>
            <Typography variant="h6" gutterBottom>
              Service sélectionné : {services.find(s => s.id === selectedService)?.title}
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              Vous avez sélectionné ce service. Cliquez sur "Demander le service" pour continuer.
            </Typography>
            <Button
              variant="contained"
              size="large"
              onClick={handleRequestService}
              sx={{ mr: 2 }}
            >
              Demander le service
            </Button>
            <Button
              variant="outlined"
              onClick={() => setSelectedService('')}
            >
              Annuler
            </Button>
          </Paper>
        )}

        {/* Quick Actions */}
        <Grid container spacing={3} sx={{ mt: 4 }}>
          <Grid item xs={12} md={4}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  <SchoolIcon color="primary" sx={{ mr: 1, verticalAlign: 'middle' }} />
                  Inscription Auto-École
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Obtenez votre premier permis de conduire
                </Typography>
                <Button variant="outlined" fullWidth onClick={() => navigate('/auto-ecole/register')}>
                  S'inscrire
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          <Grid item xs={12} md={4}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  <PersonIcon color="primary" sx={{ mr: 1, verticalAlign: 'middle' }} />
                  Suivi de Demande
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Vérifiez le statut de votre demande
                </Typography>
                <Button variant="outlined" fullWidth onClick={() => navigate('/citizen/status')}>
                  Vérifier le statut
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          <Grid item xs={12} md={4}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  <ScheduleIcon color="primary" sx={{ mr: 1, verticalAlign: 'middle' }} />
                  Rendez-vous
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Prenez rendez-vous pour vos démarches
                </Typography>
                <Button variant="outlined" fullWidth>
                  Prendre RDV
                </Button>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Container>
    </Box>
  );
}

export default RelatedServices;
