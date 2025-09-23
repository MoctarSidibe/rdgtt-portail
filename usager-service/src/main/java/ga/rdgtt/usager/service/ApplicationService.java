package ga.rdgtt.usager.service;

import ga.rdgtt.usager.model.*;
import ga.rdgtt.usager.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class ApplicationService {

    @Autowired
    private UserApplicationRepository userApplicationRepository;

    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    // @Autowired
    // private WorkflowService workflowService; // Will be implemented later

    /**
     * Créer une nouvelle demande
     */
    public UserApplication createApplication(UUID userId, UUID documentTypeId, 
                                           Map<String, Object> applicationData) {
        
        // Vérifier que l'utilisateur existe
        userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        DocumentType documentType = documentTypeRepository.findById(documentTypeId)
            .orElseThrow(() -> new RuntimeException("Type de document non trouvé"));

        ApplicationStatus initialStatus = applicationStatusRepository.findByCode("DEPOSEE")
            .orElseThrow(() -> new RuntimeException("Statut initial non trouvé"));

        // Générer un numéro de demande unique
        String numeroDemande = generateApplicationNumber(documentType);

        UserApplication application = new UserApplication();
        application.setUserId(userId);
        application.setDocumentTypeId(documentTypeId);
        application.setNumeroDemande(numeroDemande);
        application.setStatutId(initialStatus.getId());
        application.setMontantTotal(documentType.getPrix());
        application.setMontantPaye(0.0);
        application.setDateDepot(LocalDateTime.now());
        application.setDelaiEstimeJours(documentType.getDelaiTraitementJours());
        application.setDonneesDemande(applicationData.toString()); // JSON string

        UserApplication savedApplication = userApplicationRepository.save(application);

        // Créer une instance de workflow
        // workflowService.createWorkflowInstance(savedApplication); // Will be implemented later

        // Envoyer une notification
        notificationService.notifyDemandeDeposee(userId, numeroDemande, documentType.getNom());

        return savedApplication;
    }

    /**
     * Mettre à jour le statut d'une demande
     */
    public void updateApplicationStatus(UUID applicationId, String newStatusCode, 
                                      String commentaire, UUID userId) {
        
        UserApplication application = userApplicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Demande non trouvée"));

        ApplicationStatus newStatus = applicationStatusRepository.findByCode(newStatusCode)
            .orElseThrow(() -> new RuntimeException("Statut non trouvé"));

        ApplicationStatus oldStatus = applicationStatusRepository.findById(application.getStatutId())
            .orElseThrow(() -> new RuntimeException("Ancien statut non trouvé"));

        // Enregistrer l'historique
        ApplicationHistory history = new ApplicationHistory();
        history.setApplicationId(applicationId);
        history.setAncienStatutId(application.getStatutId());
        history.setNouveauStatutId(newStatus.getId());
        history.setCommentaire(commentaire);
        history.setUtilisateurId(userId);
        history.setCreatedAt(LocalDateTime.now());

        // Mettre à jour la demande
        application.setStatutId(newStatus.getId());
        application.setUpdatedAt(LocalDateTime.now());

        if (newStatusCode.equals("TERMINEE")) {
            application.setDateFin(LocalDateTime.now());
        }

        userApplicationRepository.save(application);

        // Envoyer une notification
        notificationService.notifyStatutChange(application.getUserId(), 
            application.getNumeroDemande(), 
            oldStatus.getNom(), 
            newStatus.getNom());

        // Mettre à jour le workflow
        // workflowService.updateWorkflowStatus(application.getWorkflowInstanceId(), newStatusCode); // Will be implemented later
    }

    /**
     * Obtenir les demandes d'un utilisateur
     */
    public List<UserApplication> getUserApplications(UUID userId) {
        return userApplicationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    /**
     * Obtenir une demande par ID
     */
    public UserApplication getApplicationById(UUID applicationId) {
        return userApplicationRepository.findById(applicationId)
            .orElseThrow(() -> new RuntimeException("Demande non trouvée"));
    }

    /**
     * Obtenir les demandes par statut
     */
    public List<UserApplication> getApplicationsByStatus(String statusCode) {
        // Vérifier que le statut existe
        applicationStatusRepository.findByCode(statusCode)
            .orElseThrow(() -> new RuntimeException("Statut non trouvé"));
        
        return userApplicationRepository.findByStatutCodeOrderByCreatedAtDesc(statusCode);
    }

    /**
     * Obtenir les demandes en attente de paiement
     */
    public List<UserApplication> getApplicationsPendingPayment() {
        return getApplicationsByStatus("EN_ATTENTE_PAIEMENT");
    }

    /**
     * Obtenir les demandes avec documents manquants
     */
    public List<UserApplication> getApplicationsWithMissingDocuments() {
        return getApplicationsByStatus("DOCUMENTS_MANQUANTS");
    }

    /**
     * Marquer une demande comme payée
     */
    public void markAsPaid(UUID applicationId, Double amount, String paymentReference) {
        UserApplication application = getApplicationById(applicationId);
        
        application.setMontantPaye(amount);
        application.setUpdatedAt(LocalDateTime.now());

        // Si le montant payé correspond au montant total, passer au statut suivant
        if (amount >= application.getMontantTotal()) {
            updateApplicationStatus(applicationId, "EN_COURS", 
                "Paiement reçu: " + paymentReference, null);
        }

        userApplicationRepository.save(application);

        // Envoyer une notification
        notificationService.notifyPaiementRecu(application.getUserId(), 
            application.getNumeroDemande(), amount);
    }

    /**
     * Annuler une demande
     */
    public void cancelApplication(UUID applicationId, String reason, UUID userId) {
        updateApplicationStatus(applicationId, "ANNULEE", reason, userId);
    }

    /**
     * Rejeter une demande
     */
    public void rejectApplication(UUID applicationId, String reason, UUID userId) {
        updateApplicationStatus(applicationId, "REJETEE", reason, userId);
    }

    /**
     * Obtenir les statistiques des demandes
     */
    public Map<String, Long> getApplicationStatistics() {
        return Map.of(
            "total", userApplicationRepository.count(),
            "en_attente", userApplicationRepository.countByStatutCode("DEPOSEE"),
            "en_cours", userApplicationRepository.countByStatutCode("EN_COURS"),
            "terminees", userApplicationRepository.countByStatutCode("TERMINEE"),
            "rejetees", userApplicationRepository.countByStatutCode("REJETEE")
        );
    }

    /**
     * Générer un numéro de demande unique
     */
    private String generateApplicationNumber(DocumentType documentType) {
        String prefix = documentType.getCode().substring(0, 3).toUpperCase();
        String year = String.valueOf(LocalDateTime.now().getYear());
        String month = String.format("%02d", LocalDateTime.now().getMonthValue());
        
        // Compter les demandes du mois pour générer un numéro séquentiel
        long count = userApplicationRepository.countByCreatedAtAfter(
            LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
        );
        
        String sequence = String.format("%04d", count + 1);
        
        return prefix + year + month + sequence;
    }

    /**
     * Obtenir les demandes en retard
     */
    public List<UserApplication> getOverdueApplications() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        return userApplicationRepository.findByDateDepotBeforeAndStatutCodeNotIn(
            cutoffDate, List.of("TERMINEE", "REJETEE", "ANNULEE")
        );
    }

    /**
     * Envoyer des rappels pour les demandes en retard
     */
    public void sendOverdueReminders() {
        List<UserApplication> overdueApplications = getOverdueApplications();
        
        for (UserApplication application : overdueApplications) {
            notificationService.notifyRappelDocument(
                application.getUserId(),
                application.getNumeroDemande(),
                "Votre demande est en retard. Veuillez contacter le service."
            );
        }
    }
}
