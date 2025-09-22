package ga.rdgtt.admin.controller;

import ga.rdgtt.admin.model.DocumentType;
import ga.rdgtt.admin.service.DocumentTypeService;
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
@RequestMapping("/api/admin/document-types")
@Tag(name = "Document Types", description = "Gestion des types de documents")
public class DocumentTypeController {
    
    @Autowired
    private DocumentTypeService documentTypeService;
    
    @GetMapping
    @Operation(summary = "Lister tous les types de documents")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DGTT')")
    public ResponseEntity<List<DocumentType>> getAllDocumentTypes() {
        List<DocumentType> documentTypes = documentTypeService.findAll();
        return ResponseEntity.ok(documentTypes);
    }
    
    @GetMapping("/service/{serviceCode}")
    @Operation(summary = "Lister les types de documents par service")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DGTT')")
    public ResponseEntity<List<DocumentType>> getDocumentTypesByService(@PathVariable String serviceCode) {
        List<DocumentType> documentTypes = documentTypeService.findByServiceCode(serviceCode);
        return ResponseEntity.ok(documentTypes);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un type de document par ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('DGTT')")
    public ResponseEntity<DocumentType> getDocumentType(@PathVariable UUID id) {
        return documentTypeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(summary = "Créer un nouveau type de document")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocumentType> createDocumentType(@Valid @RequestBody DocumentType documentType) {
        DocumentType created = documentTypeService.create(documentType);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un type de document")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocumentType> updateDocumentType(@PathVariable UUID id, @Valid @RequestBody DocumentType documentType) {
        return documentTypeService.update(id, documentType)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un type de document")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDocumentType(@PathVariable UUID id) {
        if (documentTypeService.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @PutMapping("/{id}/activate")
    @Operation(summary = "Activer un type de document")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocumentType> activateDocumentType(@PathVariable UUID id) {
        return documentTypeService.activate(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}/deactivate")
    @Operation(summary = "Désactiver un type de document")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocumentType> deactivateDocumentType(@PathVariable UUID id) {
        return documentTypeService.deactivate(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
