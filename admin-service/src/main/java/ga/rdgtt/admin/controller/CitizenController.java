package ga.rdgtt.admin.controller;

import ga.rdgtt.admin.model.WorkflowInstance;
import ga.rdgtt.admin.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/citizen")
@CrossOrigin(origins = "*")
public class CitizenController {

    @Autowired
    private WorkflowService workflowService;

    /**
     * Obtenir le statut d'une demande par son numéro
     */
    @GetMapping("/status/{demandeNumber}")
    public ResponseEntity<Map<String, Object>> getDemandeStatus(@PathVariable String demandeNumber) {
        try {
            // Rechercher l'instance de workflow par numéro de demande
            WorkflowInstance workflow = workflowService.getWorkflowByDemandeNumber(demandeNumber);
            
            if (workflow == null) {
                return ResponseEntity.notFound().build();
            }

            // Construire la réponse avec les informations de statut
            Map<String, Object> response = new HashMap<>();
            response.put("numero_demande", workflow.getDemandeId());
            response.put("statut", workflow.getStatut());
            response.put("date_depot", workflow.getDateDebut());
            response.put("date_traitement", workflow.getDateFin());
            response.put("commentaires", workflow.getCommentaires());
            
            // Ajouter les informations du type de document si disponible
            if (workflow.getDocumentType() != null) {
                Map<String, Object> documentType = new HashMap<>();
                documentType.put("nom", workflow.getDocumentType().getNom());
                documentType.put("code", workflow.getDocumentType().getCode());
                response.put("document_type", documentType);
            }

            // Ajouter le délai estimé si disponible
            if (workflow.getDocumentType() != null && workflow.getDocumentType().getDelaiTraitementJours() != null) {
                response.put("delai_estime_jours", workflow.getDocumentType().getDelaiTraitementJours());
            }

            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la récupération du statut",
                "message", e.getMessage()
            ));
        }
    }

    /**
     * Obtenir l'historique d'une demande
     */
    @GetMapping("/history/{demandeNumber}")
    public ResponseEntity<Map<String, Object>> getDemandeHistory(@PathVariable String demandeNumber) {
        try {
            // Rechercher l'instance de workflow par numéro de demande
            WorkflowInstance workflow = workflowService.getWorkflowByDemandeNumber(demandeNumber);
            
            if (workflow == null) {
                return ResponseEntity.notFound().build();
            }

            // Récupérer l'historique des étapes
            var history = workflowService.getWorkflowHistory(workflow.getId());
            
            Map<String, Object> response = new HashMap<>();
            response.put("numero_demande", workflow.getDemandeId());
            response.put("statut_actuel", workflow.getStatut());
            response.put("historique", history);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                "error", "Erreur lors de la récupération de l'historique",
                "message", e.getMessage()
            ));
        }
    }
}


