package ga.rdgtt.usager.controller;

import ga.rdgtt.usager.model.Department;
import ga.rdgtt.usager.repository.DepartmentRepository;
import ga.rdgtt.usager.repository.BureauRepository;
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
@RequestMapping("/api/usager/departments")
@Tag(name = "Departments", description = "API de gestion des départements")
public class DepartmentController {
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private BureauRepository bureauRepository;
    
    @PostMapping
    @Operation(summary = "Créer un département", description = "Crée un nouveau département")
    public ResponseEntity<Department> createDepartment(@Valid @RequestBody Department department) {
        Department savedDepartment = departmentRepository.save(department);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDepartment);
    }
    
    @GetMapping
    @Operation(summary = "Lister tous les départements", description = "Retourne la liste de tous les départements actifs")
    public ResponseEntity<List<Department>> getAllDepartments() {
        List<Department> departments = departmentRepository.findByActifTrue();
        return ResponseEntity.ok(departments);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un département", description = "Retourne les informations d'un département par son ID")
    public ResponseEntity<Department> getDepartmentById(@PathVariable UUID id) {
        Optional<Department> department = departmentRepository.findById(id);
        return department.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/code/{code}")
    @Operation(summary = "Obtenir un département par code", description = "Retourne les informations d'un département par son code")
    public ResponseEntity<Department> getDepartmentByCode(@PathVariable String code) {
        Optional<Department> department = departmentRepository.findByCode(code);
        return department.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un département", description = "Met à jour les informations d'un département")
    public ResponseEntity<Department> updateDepartment(@PathVariable UUID id, @Valid @RequestBody Department department) {
        if (!departmentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        department.setId(id);
        Department updatedDepartment = departmentRepository.save(department);
        return ResponseEntity.ok(updatedDepartment);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un département", description = "Désactive un département (soft delete)")
    public ResponseEntity<Void> deleteDepartment(@PathVariable UUID id) {
        Optional<Department> departmentOpt = departmentRepository.findById(id);
        if (departmentOpt.isPresent()) {
            Department department = departmentOpt.get();
            department.setActif(false);
            departmentRepository.save(department);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/{id}/bureaus")
    @Operation(summary = "Bureaux d'un département", description = "Retourne la liste des bureaux d'un département")
    public ResponseEntity<List<ga.rdgtt.usager.model.Bureau>> getDepartmentBureaus(@PathVariable UUID id) {
        List<ga.rdgtt.usager.model.Bureau> bureaus = bureauRepository.findActiveBureausByDepartment(id);
        return ResponseEntity.ok(bureaus);
    }
    
    @GetMapping("/stats/count")
    @Operation(summary = "Nombre de départements", description = "Retourne le nombre total de départements actifs")
    public ResponseEntity<Long> getDepartmentsCount() {
        long count = departmentRepository.countActiveDepartments();
        return ResponseEntity.ok(count);
    }
}
