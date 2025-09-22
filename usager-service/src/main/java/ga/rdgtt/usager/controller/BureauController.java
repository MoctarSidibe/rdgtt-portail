package ga.rdgtt.usager.controller;

import ga.rdgtt.usager.model.Bureau;
import ga.rdgtt.usager.repository.BureauRepository;
import ga.rdgtt.usager.repository.DepartmentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/usager/bureaus")
@Tag(name = "Bureaus", description = "API de gestion des bureaux")
public class BureauController {
    
    @Autowired
    private BureauRepository bureauRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @PostMapping
    @Operation(summary = "Créer un bureau", description = "Crée un nouveau bureau")
    public ResponseEntity<Bureau> createBureau(@Valid @RequestBody Bureau bureau) {
        // Verify department exists
        if (bureau.getDepartment() != null && bureau.getDepartment().getId() != null) {
            if (!departmentRepository.existsById(bureau.getDepartment().getId())) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        Bureau savedBureau = bureauRepository.save(bureau);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBureau);
    }
    
    @GetMapping
    @Operation(summary = "Lister tous les bureaux", description = "Retourne la liste de tous les bureaux actifs")
    public ResponseEntity<List<Bureau>> getAllBureaus() {
        List<Bureau> bureaus = bureauRepository.findByActifTrue();
        return ResponseEntity.ok(bureaus);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un bureau", description = "Retourne les informations d'un bureau par son ID")
    public ResponseEntity<Bureau> getBureauById(@PathVariable UUID id) {
        Optional<Bureau> bureau = bureauRepository.findById(id);
        return bureau.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    @Operation(summary = "Obtenir un bureau par code", description = "Retourne les informations d'un bureau par son code")
    public ResponseEntity<Bureau> getBureauByCode(@PathVariable String code) {
        Optional<Bureau> bureau = bureauRepository.findByCode(code);
        return bureau.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/department/{departmentId}")
    @Operation(summary = "Bureaux d'un département", description = "Retourne la liste des bureaux d'un département")
    public ResponseEntity<List<Bureau>> getBureausByDepartment(@PathVariable UUID departmentId) {
        List<Bureau> bureaus = bureauRepository.findActiveBureausByDepartment(departmentId);
        return ResponseEntity.ok(bureaus);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un bureau", description = "Met à jour les informations d'un bureau")
    public ResponseEntity<Bureau> updateBureau(@PathVariable UUID id, @Valid @RequestBody Bureau bureau) {
        if (!bureauRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        // Verify department exists if provided
        if (bureau.getDepartment() != null && bureau.getDepartment().getId() != null) {
            if (!departmentRepository.existsById(bureau.getDepartment().getId())) {
                return ResponseEntity.badRequest().build();
            }
        }
        
        bureau.setId(id);
        Bureau updatedBureau = bureauRepository.save(bureau);
        return ResponseEntity.ok(updatedBureau);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un bureau", description = "Désactive un bureau (soft delete)")
    public ResponseEntity<Void> deleteBureau(@PathVariable UUID id) {
        Optional<Bureau> bureauOpt = bureauRepository.findById(id);
        if (bureauOpt.isPresent()) {
            Bureau bureau = bureauOpt.get();
            bureau.setActif(false);
            bureauRepository.save(bureau);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/stats/count")
    @Operation(summary = "Nombre de bureaux", description = "Retourne le nombre total de bureaux actifs")
    public ResponseEntity<Long> getBureausCount() {
        long count = bureauRepository.countActiveBureaus();
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/stats/count/department/{departmentId}")
    @Operation(summary = "Nombre de bureaux par département", description = "Retourne le nombre de bureaux actifs pour un département")
    public ResponseEntity<Long> getBureausCountByDepartment(@PathVariable UUID departmentId) {
        long count = bureauRepository.countActiveBureausByDepartment(departmentId);
        return ResponseEntity.ok(count);
    }
}
