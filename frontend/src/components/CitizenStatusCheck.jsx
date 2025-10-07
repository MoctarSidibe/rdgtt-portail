import React, { useState } from 'react';
import {
  Box,
  Container,
  Typography,
  Card,
  CardContent,
  TextField,
  Button,
  Alert,
  CircularProgress,
  Chip,
  Divider,
  Grid,
  Paper,
  useTheme,
} from '@mui/material';
import {
  Search as SearchIcon,
  CheckCircle as CheckIcon,
  Schedule as ScheduleIcon,
  Error as ErrorIcon,
  Info as InfoIcon,
} from '@mui/icons-material';

const CitizenStatusCheck = () => {
  const theme = useTheme();
  const [demandeNumber, setDemandeNumber] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [applicationData, setApplicationData] = useState(null);

  const getStatusColor = (status) => {
    const statusConfig = {
      'DEPOSEE': { color: 'primary', icon: <InfoIcon /> },
      'EN_COURS': { color: 'warning', icon: <ScheduleIcon /> },
      'DOCUMENTS_MANQUANTS': { color: 'error', icon: <ErrorIcon /> },
      'VALIDATION': { color: 'info', icon: <ScheduleIcon /> },
      'VALIDE': { color: 'success', icon: <CheckIcon /> },
      'REJETEE': { color: 'error', icon: <ErrorIcon /> },
      'TERMINEE': { color: 'success', icon: <CheckIcon /> }
    };
    return statusConfig[status] || { color: 'default', icon: <InfoIcon /> };
  };

  const getStatusText = (status) => {
    const statusTexts = {
      'DEPOSEE': 'Demande déposée',
      'EN_COURS': 'En cours de traitement',
      'DOCUMENTS_MANQUANTS': 'Documents manquants',
      'VALIDATION': 'En validation',
      'VALIDE': 'Validée',
      'REJETEE': 'Rejetée',
      'TERMINEE': 'Terminée'
    };
    return statusTexts[status] || status;
  };

  const handleSearch = async () => {
    if (!demandeNumber.trim()) {
      setError('Veuillez saisir un numéro de demande');
      return;
    }

    setLoading(true);
    setError('');
    setApplicationData(null);

    try {
      // Simulate API call - replace with actual endpoint
      const response = await fetch(`/api/citizen/status/${demandeNumber}`);
      
      if (!response.ok) {
        throw new Error('Demande non trouvée');
      }

      const data = await response.json();
      setApplicationData(data);
    } catch (err) {
      setError(err.message || 'Erreur lors de la recherche');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container maxWidth="md" sx={{ py: 4 }}>
      <Box textAlign="center" mb={4}>
        <Typography variant="h4" component="h1" gutterBottom fontWeight="bold">
          Suivi de Demande
        </Typography>
        <Typography variant="h6" color="text.secondary">
          Vérifiez le statut de votre demande en saisissant votre numéro de référence
        </Typography>
      </Box>

      <Card sx={{ mb: 4 }}>
        <CardContent>
          <Box sx={{ display: 'flex', gap: 2, alignItems: 'flex-start' }}>
            <TextField
              fullWidth
              label="Numéro de demande"
              placeholder="Ex: DEM-2024-001234"
              value={demandeNumber}
              onChange={(e) => setDemandeNumber(e.target.value)}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
              disabled={loading}
            />
            <Button
              variant="contained"
              onClick={handleSearch}
              disabled={loading}
              startIcon={loading ? <CircularProgress size={20} /> : <SearchIcon />}
              sx={{ minWidth: 120 }}
            >
              {loading ? 'Recherche...' : 'Rechercher'}
            </Button>
          </Box>
        </CardContent>
      </Card>

      {error && (
        <Alert severity="error" sx={{ mb: 4 }}>
          {error}
        </Alert>
      )}

      {applicationData && (
        <Card>
          <CardContent>
            <Typography variant="h5" gutterBottom fontWeight="bold">
              Détails de la Demande
            </Typography>
            
            <Grid container spacing={3}>
              <Grid item xs={12} md={6}>
                <Paper sx={{ p: 2, bgcolor: 'grey.50' }}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    Numéro de demande
                  </Typography>
                  <Typography variant="h6" fontWeight="bold">
                    {applicationData.numero_demande}
                  </Typography>
                </Paper>
              </Grid>
              
              <Grid item xs={12} md={6}>
                <Paper sx={{ p: 2, bgcolor: 'grey.50' }}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    Statut actuel
                  </Typography>
                  <Chip
                    icon={getStatusColor(applicationData.statut).icon}
                    label={getStatusText(applicationData.statut)}
                    color={getStatusColor(applicationData.statut).color}
                    variant="filled"
                    sx={{ fontWeight: 'bold' }}
                  />
                </Paper>
              </Grid>

              <Grid item xs={12} md={6}>
                <Paper sx={{ p: 2, bgcolor: 'grey.50' }}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    Type de document
                  </Typography>
                  <Typography variant="body1" fontWeight="medium">
                    {applicationData.document_type?.nom || 'N/A'}
                  </Typography>
                </Paper>
              </Grid>

              <Grid item xs={12} md={6}>
                <Paper sx={{ p: 2, bgcolor: 'grey.50' }}>
                  <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                    Date de dépôt
                  </Typography>
                  <Typography variant="body1" fontWeight="medium">
                    {new Date(applicationData.date_depot).toLocaleDateString('fr-FR')}
                  </Typography>
                </Paper>
              </Grid>

              {applicationData.delai_estime_jours && (
                <Grid item xs={12} md={6}>
                  <Paper sx={{ p: 2, bgcolor: 'grey.50' }}>
                    <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                      Délai estimé
                    </Typography>
                    <Typography variant="body1" fontWeight="medium">
                      {applicationData.delai_estime_jours} jours
                    </Typography>
                  </Paper>
                </Grid>
              )}

              {applicationData.date_traitement && (
                <Grid item xs={12} md={6}>
                  <Paper sx={{ p: 2, bgcolor: 'grey.50' }}>
                    <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                      Date de traitement
                    </Typography>
                    <Typography variant="body1" fontWeight="medium">
                      {new Date(applicationData.date_traitement).toLocaleDateString('fr-FR')}
                    </Typography>
                  </Paper>
                </Grid>
              )}
            </Grid>

            {applicationData.commentaires && (
              <>
                <Divider sx={{ my: 3 }} />
                <Typography variant="subtitle2" color="text.secondary" gutterBottom>
                  Commentaires
                </Typography>
                <Typography variant="body1">
                  {applicationData.commentaires}
                </Typography>
              </>
            )}

            <Divider sx={{ my: 3 }} />
            
            <Box sx={{ bgcolor: 'info.light', p: 2, borderRadius: 1 }}>
              <Typography variant="body2" color="info.contrastText">
                <strong>Information :</strong> Pour toute question concernant votre demande, 
                veuillez contacter le service concerné au +241 01 23 45 67 ou par email à contact@rdgtt.ga
              </Typography>
            </Box>
          </CardContent>
        </Card>
      )}
    </Container>
  );
};

export default CitizenStatusCheck;


