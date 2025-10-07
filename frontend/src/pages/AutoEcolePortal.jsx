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
  Stepper,
  Step,
  StepLabel,
  StepContent,
  Paper,
  TextField,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  Chip,
  Alert,
  useTheme,
} from '@mui/material';
import {
  School as SchoolIcon,
  Person as PersonIcon,
  Assignment as AssignmentIcon,
  CheckCircle as CheckIcon,
  Schedule as ScheduleIcon,
  DirectionsCar as CarIcon,
} from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';

function AutoEcolePortal() {
  const theme = useTheme();
  const navigate = useNavigate();
  const [activeStep, setActiveStep] = useState(0);
  const [formData, setFormData] = useState({
    nom: '',
    prenom: '',
    email: '',
    telephone: '',
    autoEcole: '',
    typePermis: '',
  });

  const steps = [
    {
      label: 'Inscription Auto-École',
      description: 'Choisissez votre auto-école et inscrivez-vous',
      icon: <SchoolIcon />,
      content: (
        <Box>
          <Typography variant="h6" gutterBottom>
            Sélectionnez votre auto-école
          </Typography>
          <FormControl fullWidth sx={{ mb: 2 }}>
            <InputLabel>Auto-École</InputLabel>
            <Select
              value={formData.autoEcole}
              onChange={(e) => setFormData({ ...formData, autoEcole: e.target.value })}
            >
              <MenuItem value="auto-ecole-1">Auto-École Centrale</MenuItem>
              <MenuItem value="auto-ecole-2">Auto-École Excellence</MenuItem>
              <MenuItem value="auto-ecole-3">Auto-École Pro</MenuItem>
            </Select>
          </FormControl>
          <Alert severity="info" sx={{ mb: 2 }}>
            Vous devez vous inscrire dans une auto-école agréée pour obtenir un permis de conduire
          </Alert>
        </Box>
      ),
    },
    {
      label: 'Formation',
      description: 'Suivez votre formation théorique et pratique',
      icon: <AssignmentIcon />,
      content: (
        <Box>
          <Typography variant="h6" gutterBottom>
            Programme de formation
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} md={6}>
              <Card>
                <CardContent>
                  <Typography variant="h6" color="primary" gutterBottom>
                    Formation Théorique
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    • Code de la route<br/>
                    • Règles de circulation<br/>
                    • Signalisation<br/>
                    • Sécurité routière
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={6}>
              <Card>
                <CardContent>
                  <Typography variant="h6" color="primary" gutterBottom>
                    Formation Pratique
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    • Conduite en ville<br/>
                    • Conduite sur route<br/>
                    • Manœuvres<br/>
                    • Conduite autonome
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Box>
      ),
    },
    {
      label: 'Examens',
      description: 'Passez vos examens théorique et pratique',
      icon: <CheckIcon />,
      content: (
        <Box>
          <Typography variant="h6" gutterBottom>
            Processus d'examen
          </Typography>
          <Grid container spacing={2}>
            <Grid item xs={12} md={6}>
              <Card sx={{ border: '2px solid #4CAF50' }}>
                <CardContent>
                  <Typography variant="h6" color="success.main" gutterBottom>
                    Examen Théorique
                  </Typography>
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    • 40 questions à choix multiples<br/>
                    • Durée : 30 minutes<br/>
                    • Note minimale : 35/40
                  </Typography>
                  <Chip label="Obligatoire" color="success" size="small" />
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={6}>
              <Card sx={{ border: '2px solid #2196F3' }}>
                <CardContent>
                  <Typography variant="h6" color="primary" gutterBottom>
                    Examen Pratique
                  </Typography>
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                    • Conduite avec examinateur<br/>
                    • Durée : 32 minutes<br/>
                    • Note minimale : 20/30
                  </Typography>
                  <Chip label="Obligatoire" color="primary" size="small" />
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Box>
      ),
    },
    {
      label: 'Permis',
      description: 'Obtenez votre permis de conduire',
      icon: <CarIcon />,
      content: (
        <Box>
          <Typography variant="h6" gutterBottom>
            Obtention du permis
          </Typography>
          <Alert severity="success" sx={{ mb: 2 }}>
            Félicitations ! Vous avez réussi tous les examens. Votre permis de conduire sera délivré.
          </Alert>
          <Grid container spacing={2}>
            <Grid item xs={12} md={6}>
              <Card>
                <CardContent>
                  <Typography variant="h6" color="primary" gutterBottom>
                    Permis de Conduire
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    • Délivrance sous 15 jours<br/>
                    • Valide 5 ans<br/>
                    • Récupération en préfecture
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
            <Grid item xs={12} md={6}>
              <Card>
                <CardContent>
                  <Typography variant="h6" color="primary" gutterBottom>
                    Services Connexes
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    • Duplicata en cas de perte<br/>
                    • Renouvellement<br/>
                    • Conversion permis étranger
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          </Grid>
        </Box>
      ),
    },
  ];

  const handleNext = () => {
    setActiveStep((prevActiveStep) => prevActiveStep + 1);
  };

  const handleBack = () => {
    setActiveStep((prevActiveStep) => prevActiveStep - 1);
  };

  const handleSubmit = () => {
    // Handle form submission
    console.log('Form submitted:', formData);
    navigate('/citizen/status');
  };

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: 'background.default', py: 4 }}>
      <Container maxWidth="lg">
        <Box textAlign="center" mb={4}>
          <Typography variant="h3" component="h1" gutterBottom fontWeight="bold">
            Inscription Auto-École
          </Typography>
          <Typography variant="h6" color="text.secondary">
            Obtenez votre permis de conduire en suivant le processus complet
          </Typography>
        </Box>

        <Paper elevation={2} sx={{ p: 3, mb: 4 }}>
          <Stepper activeStep={activeStep} orientation="vertical">
            {steps.map((step, index) => (
              <Step key={step.label}>
                <StepLabel
                  optional={
                    index === steps.length - 1 ? (
                      <Typography variant="caption">Dernière étape</Typography>
                    ) : null
                  }
                  icon={step.icon}
                >
                  <Typography variant="h6">{step.label}</Typography>
                  <Typography variant="body2" color="text.secondary">
                    {step.description}
                  </Typography>
                </StepLabel>
                <StepContent>
                  {step.content}
                  <Box sx={{ mb: 2, mt: 2 }}>
                    <div>
                      <Button
                        variant="contained"
                        onClick={index === steps.length - 1 ? handleSubmit : handleNext}
                        sx={{ mt: 1, mr: 1 }}
                      >
                        {index === steps.length - 1 ? 'Terminer' : 'Continuer'}
                      </Button>
                      <Button
                        disabled={index === 0}
                        onClick={handleBack}
                        sx={{ mt: 1, mr: 1 }}
                      >
                        Retour
                      </Button>
                    </div>
                  </Box>
                </StepContent>
              </Step>
            ))}
          </Stepper>
        </Paper>

        {/* Quick Actions */}
        <Grid container spacing={3}>
          <Grid item xs={12} md={4}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  <ScheduleIcon color="primary" sx={{ mr: 1, verticalAlign: 'middle' }} />
                  Suivi de Progression
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Consultez l'avancement de votre formation
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
                  <PersonIcon color="primary" sx={{ mr: 1, verticalAlign: 'middle' }} />
                  Mon Profil
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Gérez vos informations personnelles
                </Typography>
                <Button variant="outlined" fullWidth>
                  Modifier le profil
                </Button>
              </CardContent>
            </Card>
          </Grid>
          
          <Grid item xs={12} md={4}>
            <Card>
              <CardContent>
                <Typography variant="h6" gutterBottom>
                  <CarIcon color="primary" sx={{ mr: 1, verticalAlign: 'middle' }} />
                  Services Connexes
                </Typography>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
                  Duplicata, renouvellement, etc.
                </Typography>
                <Button variant="outlined" fullWidth onClick={() => navigate('/services/related')}>
                  Voir les services
                </Button>
              </CardContent>
            </Card>
          </Grid>
        </Grid>
      </Container>
    </Box>
  );
}

export default AutoEcolePortal;
