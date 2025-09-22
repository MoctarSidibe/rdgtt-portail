package ga.rdgtt.admin.controller;

import ga.rdgtt.admin.model.ProcessStep;
import ga.rdgtt.admin.service.ProcessStepService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/process-steps")
@Tag(name = "Process Steps", description = "Gestion des étapes de processus")
public class ProcessStepController {
    
    @Autowired
    private ProcessStepService processStepService;
    
    @GetMapping
    @Operation(summary = "Lister toutes les étapes de processus")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DGTT')")
    public ResponseEntity<List<ProcessStep>> getAllProcessSteps() {
        List<ProcessStep> processSteps = processStepService.findAll();
        return ResponseEntity.ok(processSteps);
    }
    
    @GetMapping("/document-type/{documentTypeId}")
    @Operation(summary = "Lister les étapes par type de document")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DGTT')")
    public ResponseEntity<List<ProcessStep>> getProcessStepsByDocumentType(@PathVariable UUID documentTypeId) {
        List<ProcessStep> processSteps = processStepService.findByDocumentTypeId(documentTypeId);
        return ResponseEntity.ok(processSteps);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir une étape de processus par ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DGTT')")
    public ResponseEntity<ProcessStep> getProcessStep(@PathVariable UUID id) {
        return processStepService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Créer une nouvelle étape de processus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProcessStep> createProcessStep(@Valid @RequestBody ProcessStep processStep) {
        ProcessStep created = processStepService.create(processStep);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une étape de processus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProcessStep> updateProcessStep(@PathVariable UUID id, @Valid @RequestBody ProcessStep processStep) {
        return processStepService.update(id, processStep)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une étape de processus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProcessStep(@PathVariable UUID id) {
        if (processStepService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/reorder")
    @Operation(summary = "Réorganiser l'ordre des étapes")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProcessStep> reorderProcessStep(@PathVariable UUID id, @RequestParam Integer newOrder) {
        return processStepService.reorder(id, newOrder)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/activate")
    @Operation(summary = "Activer une étape de processus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProcessStep> activateProcessStep(@PathVariable UUID id) {
        return processStepService.activate(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Désactiver une étape de processus")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProcessStep> deactivateProcessStep(@PathVariable UUID id) {
        return processStepService.deactivate(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
