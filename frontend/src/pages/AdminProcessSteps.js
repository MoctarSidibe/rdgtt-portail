import React, { useState, useCallback, useRef } from 'react';
import {
  Box,
  Container,
  Typography,
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
  Grid,
  Card,
  CardContent,
  Switch,
  FormControlLabel,
  Divider,
} from '@mui/material';
import {
  Add as AddIcon,
  Save as SaveIcon,
  PlayArrow as PlayIcon,
  Settings as SettingsIcon,
  Delete as DeleteIcon,
  Edit as EditIcon,
  Visibility as PreviewIcon,
} from '@mui/icons-material';
import ReactFlow, {
  MiniMap,
  Controls,
  Background,
  useNodesState,
  useEdgesState,
  addEdge,
  ConnectionLineType,
} from 'reactflow';
import 'reactflow/dist/style.css';

const nodeTypes = {
  start: ({ data, selected }) => (
    <div
      style={{
        padding: '10px 15px',
        borderRadius: '8px',
        background: '#4caf50',
        color: 'white',
        border: selected ? '2px solid #1976d2' : '2px solid transparent',
        minWidth: '120px',
        textAlign: 'center',
        fontWeight: 'bold',
      }}
    >
      {data.label}
    </div>
  ),
  process: ({ data, selected }) => (
    <div
      style={{
        padding: '10px 15px',
        borderRadius: '8px',
        background: '#2196f3',
        color: 'white',
        border: selected ? '2px solid #1976d2' : '2px solid transparent',
        minWidth: '150px',
        textAlign: 'center',
        fontWeight: 'bold',
      }}
    >
      {data.label}
    </div>
  ),
  decision: ({ data, selected }) => (
    <div
      style={{
        padding: '10px 15px',
        borderRadius: '8px',
        background: '#ff9800',
        color: 'white',
        border: selected ? '2px solid #1976d2' : '2px solid transparent',
        minWidth: '150px',
        textAlign: 'center',
        fontWeight: 'bold',
        transform: 'rotate(45deg)',
        transformOrigin: 'center',
      }}
    >
      <div style={{ transform: 'rotate(-45deg)' }}>{data.label}</div>
    </div>
  ),
  end: ({ data, selected }) => (
    <div
      style={{
        padding: '10px 15px',
        borderRadius: '50%',
        background: '#f44336',
        color: 'white',
        border: selected ? '2px solid #1976d2' : '2px solid transparent',
        minWidth: '120px',
        textAlign: 'center',
        fontWeight: 'bold',
      }}
    >
      {data.label}
    </div>
  ),
};

const initialNodes = [
  {
    id: '1',
    type: 'start',
    position: { x: 100, y: 100 },
    data: { label: 'Début' },
  },
  {
    id: '2',
    type: 'process',
    position: { x: 300, y: 100 },
    data: { label: 'Réception Demande' },
  },
  {
    id: '3',
    type: 'process',
    position: { x: 500, y: 100 },
    data: { label: 'Vérification Documents' },
  },
  {
    id: '4',
    type: 'decision',
    position: { x: 700, y: 100 },
    data: { label: 'Documents OK?' },
  },
  {
    id: '5',
    type: 'process',
    position: { x: 900, y: 50 },
    data: { label: 'Validation Chef' },
  },
  {
    id: '6',
    type: 'end',
    position: { x: 1100, y: 100 },
    data: { label: 'Fin' },
  },
];

const initialEdges = [
  { id: 'e1-2', source: '1', target: '2', type: 'smoothstep' },
  { id: 'e2-3', source: '2', target: '3', type: 'smoothstep' },
  { id: 'e3-4', source: '3', target: '4', type: 'smoothstep' },
  { id: 'e4-5', source: '4', target: '5', type: 'smoothstep', label: 'Oui' },
  { id: 'e4-6', source: '4', target: '6', type: 'smoothstep', label: 'Non' },
  { id: 'e5-6', source: '5', target: '6', type: 'smoothstep' },
];

function AdminProcessSteps() {
  const [nodes, setNodes, onNodesChange] = useNodesState(initialNodes);
  const [edges, setEdges, onEdgesChange] = useEdgesState(initialEdges);
  const [openDialog, setOpenDialog] = useState(false);
  const [editingNode, setEditingNode] = useState(null);
  const [formData, setFormData] = useState({
    label: '',
    type: 'process',
    role: '',
    department: '',
    deadline: '',
    required: true,
  });

  const reactFlowWrapper = useRef(null);
  const [reactFlowInstance, setReactFlowInstance] = useState(null);

  const onConnect = useCallback(
    (params) => setEdges((eds) => addEdge(params, eds)),
    [setEdges]
  );

  const onDragOver = useCallback((event) => {
    event.preventDefault();
    event.dataTransfer.dropEffect = 'move';
  }, []);

  const onDrop = useCallback(
    (event) => {
      event.preventDefault();

      const reactFlowBounds = reactFlowWrapper.current.getBoundingClientRect();
      const type = event.dataTransfer.getData('application/reactflow');

      if (typeof type === 'undefined' || !type) {
        return;
      }

      const position = reactFlowInstance.project({
        x: event.clientX - reactFlowBounds.left,
        y: event.clientY - reactFlowBounds.top,
      });

      const newNode = {
        id: `${nodes.length + 1}`,
        type,
        position,
        data: { 
          label: type === 'start' ? 'Début' : 
                type === 'end' ? 'Fin' : 
                type === 'decision' ? 'Décision' : 'Processus'
        },
      };

      setNodes((nds) => nds.concat(newNode));
    },
    [reactFlowInstance, nodes.length, setNodes]
  );

  const onNodeClick = (event, node) => {
    setEditingNode(node);
    setFormData({
      label: node.data.label,
      type: node.type,
      role: node.data.role || '',
      department: node.data.department || '',
      deadline: node.data.deadline || '',
      required: node.data.required !== false,
    });
    setOpenDialog(true);
  };

  const handleSaveNode = () => {
    setNodes((nds) =>
      nds.map((node) =>
        node.id === editingNode.id
          ? {
              ...node,
              data: {
                ...node.data,
                ...formData,
              },
            }
          : node
      )
    );
    setOpenDialog(false);
    setEditingNode(null);
  };

  const handleDeleteNode = () => {
    setNodes((nds) => nds.filter((node) => node.id !== editingNode.id));
    setEdges((eds) => eds.filter((edge) => 
      edge.source !== editingNode.id && edge.target !== editingNode.id
    ));
    setOpenDialog(false);
    setEditingNode(null);
  };

  const onDragStart = (event, nodeType) => {
    event.dataTransfer.setData('application/reactflow', nodeType);
    event.dataTransfer.effectAllowed = 'move';
  };

  const saveWorkflow = () => {
    // Save workflow logic here
    console.log('Saving workflow:', { nodes, edges });
    alert('Workflow sauvegardé avec succès!');
  };

  const previewWorkflow = () => {
    // Preview workflow logic here
    console.log('Previewing workflow:', { nodes, edges });
    alert('Aperçu du workflow généré!');
  };

  return (
    <Container maxWidth="xl" sx={{ py: 4 }}>
      {/* Header */}
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 4 }}>
        <Box>
          <Typography variant="h4" component="h1" sx={{ fontWeight: 'bold', mb: 1 }}>
            Configuration des Étapes de Processus
          </Typography>
          <Typography variant="body1" color="text.secondary">
            Créez et configurez vos workflows avec un éditeur visuel avancé
          </Typography>
        </Box>
        <Box sx={{ display: 'flex', gap: 2 }}>
          <Button
            variant="outlined"
            startIcon={<PreviewIcon />}
            onClick={previewWorkflow}
          >
            Aperçu
          </Button>
          <Button
            variant="contained"
            startIcon={<SaveIcon />}
            onClick={saveWorkflow}
            sx={{ 
              bgcolor: '#1976d2',
              '&:hover': { bgcolor: '#1565c0' }
            }}
          >
            Sauvegarder
          </Button>
        </Box>
      </Box>

      <Grid container spacing={3}>
        {/* Sidebar - Node Types */}
        <Grid item xs={12} md={3}>
          <Paper sx={{ p: 2, height: 'fit-content' }}>
            <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 2 }}>
              Types d'Étapes
            </Typography>
            
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              <Card
                draggable
                onDragStart={(event) => onDragStart(event, 'start')}
                sx={{ cursor: 'grab', '&:active': { cursor: 'grabbing' } }}
              >
                <CardContent sx={{ p: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    <Box
                      sx={{
                        width: 20,
                        height: 20,
                        borderRadius: '4px',
                        bgcolor: '#4caf50',
                      }}
                    />
                    <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                      Début
                    </Typography>
                  </Box>
                </CardContent>
              </Card>

              <Card
                draggable
                onDragStart={(event) => onDragStart(event, 'process')}
                sx={{ cursor: 'grab', '&:active': { cursor: 'grabbing' } }}
              >
                <CardContent sx={{ p: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    <Box
                      sx={{
                        width: 20,
                        height: 20,
                        borderRadius: '4px',
                        bgcolor: '#2196f3',
                      }}
                    />
                    <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                      Processus
                    </Typography>
                  </Box>
                </CardContent>
              </Card>

              <Card
                draggable
                onDragStart={(event) => onDragStart(event, 'decision')}
                sx={{ cursor: 'grab', '&:active': { cursor: 'grabbing' } }}
              >
                <CardContent sx={{ p: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    <Box
                      sx={{
                        width: 20,
                        height: 20,
                        borderRadius: '4px',
                        bgcolor: '#ff9800',
                        transform: 'rotate(45deg)',
                      }}
                    />
                    <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                      Décision
                    </Typography>
                  </Box>
                </CardContent>
              </Card>

              <Card
                draggable
                onDragStart={(event) => onDragStart(event, 'end')}
                sx={{ cursor: 'grab', '&:active': { cursor: 'grabbing' } }}
              >
                <CardContent sx={{ p: 2 }}>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                    <Box
                      sx={{
                        width: 20,
                        height: 20,
                        borderRadius: '50%',
                        bgcolor: '#f44336',
                      }}
                    />
                    <Typography variant="body2" sx={{ fontWeight: 'bold' }}>
                      Fin
                    </Typography>
                  </Box>
                </CardContent>
              </Card>
            </Box>

            <Divider sx={{ my: 3 }} />

            <Typography variant="h6" sx={{ fontWeight: 'bold', mb: 2 }}>
              Instructions
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
              • Glissez les éléments dans l'éditeur
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
              • Cliquez sur une étape pour la configurer
            </Typography>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
              • Connectez les étapes en glissant
            </Typography>
            <Typography variant="body2" color="text.secondary">
              • Utilisez les contrôles pour naviguer
            </Typography>
          </Paper>
        </Grid>

        {/* React Flow Editor */}
        <Grid item xs={12} md={9}>
          <Paper sx={{ height: '600px' }}>
            <Box ref={reactFlowWrapper} sx={{ height: '100%' }}>
              <ReactFlow
                nodes={nodes}
                edges={edges}
                onNodesChange={onNodesChange}
                onEdgesChange={onEdgesChange}
                onConnect={onConnect}
                onInit={setReactFlowInstance}
                onDrop={onDrop}
                onDragOver={onDragOver}
                onNodeClick={onNodeClick}
                nodeTypes={nodeTypes}
                connectionLineType={ConnectionLineType.SmoothStep}
                fitView
              >
                <Controls />
                <MiniMap />
                <Background variant="dots" gap={12} size={1} />
              </ReactFlow>
            </Box>
          </Paper>
        </Grid>
      </Grid>

      {/* Node Configuration Dialog */}
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)} maxWidth="sm" fullWidth>
        <DialogTitle>
          Configuration de l'Étape
        </DialogTitle>
        <DialogContent>
          <Box sx={{ display: 'flex', flexDirection: 'column', gap: 3, pt: 1 }}>
            <TextField
              label="Nom de l'étape"
              value={formData.label}
              onChange={(e) => setFormData({ ...formData, label: e.target.value })}
              fullWidth
              required
            />
            
            <FormControl fullWidth>
              <InputLabel>Type d'étape</InputLabel>
              <Select
                value={formData.type}
                onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                label="Type d'étape"
              >
                <MenuItem value="start">Début</MenuItem>
                <MenuItem value="process">Processus</MenuItem>
                <MenuItem value="decision">Décision</MenuItem>
                <MenuItem value="end">Fin</MenuItem>
              </Select>
            </FormControl>

            {formData.type === 'process' && (
              <>
                <FormControl fullWidth>
                  <InputLabel>Rôle responsable</InputLabel>
                  <Select
                    value={formData.role}
                    onChange={(e) => setFormData({ ...formData, role: e.target.value })}
                    label="Rôle responsable"
                  >
                    <MenuItem value="admin">Administrateur</MenuItem>
                    <MenuItem value="chef">Chef de Service</MenuItem>
                    <MenuItem value="agent">Agent</MenuItem>
                    <MenuItem value="validateur">Validateur</MenuItem>
                  </Select>
                </FormControl>

                <FormControl fullWidth>
                  <InputLabel>Département</InputLabel>
                  <Select
                    value={formData.department}
                    onChange={(e) => setFormData({ ...formData, department: e.target.value })}
                    label="Département"
                  >
                    <MenuItem value="dgtt">DGTT</MenuItem>
                    <MenuItem value="sev">SEV</MenuItem>
                    <MenuItem value="saf">SAF</MenuItem>
                    <MenuItem value="dc">DC</MenuItem>
                  </Select>
                </FormControl>

                <TextField
                  label="Délai (jours)"
                  type="number"
                  value={formData.deadline}
                  onChange={(e) => setFormData({ ...formData, deadline: e.target.value })}
                  fullWidth
                />

                <FormControlLabel
                  control={
                    <Switch
                      checked={formData.required}
                      onChange={(e) => setFormData({ ...formData, required: e.target.checked })}
                    />
                  }
                  label="Étape obligatoire"
                />
              </>
            )}
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Annuler</Button>
          {editingNode && editingNode.type !== 'start' && editingNode.type !== 'end' && (
            <Button onClick={handleDeleteNode} color="error">
              Supprimer
            </Button>
          )}
          <Button 
            onClick={handleSaveNode} 
            variant="contained"
            disabled={!formData.label}
          >
            Sauvegarder
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
}

export default AdminProcessSteps;
