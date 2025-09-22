package ga.rdgtt.admin.service;

import ga.rdgtt.admin.model.ProcessStep;
import ga.rdgtt.admin.repository.ProcessStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ProcessStepService {
    
    @Autowired
    private ProcessStepRepository processStepRepository;
    
    public List<ProcessStep> findAll() {
        return processStepRepository.findAll();
    }
    
    public List<ProcessStep> findByDocumentTypeId(UUID documentTypeId) {
        return processStepRepository.findByDocumentTypeIdOrderByOrdreAsc(documentTypeId);
    }
    
    public Optional<ProcessStep> findById(UUID id) {
        return processStepRepository.findById(id);
    }
    
    public ProcessStep create(ProcessStep processStep) {
        return processStepRepository.save(processStep);
    }
    
    public Optional<ProcessStep> update(UUID id, ProcessStep processStep) {
        if (processStepRepository.existsById(id)) {
            processStep.setId(id);
            return Optional.of(processStepRepository.save(processStep));
        }
        return Optional.empty();
    }
    
    public boolean delete(UUID id) {
        if (processStepRepository.existsById(id)) {
            processStepRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Optional<ProcessStep> reorder(UUID id, Integer newOrder) {
        return processStepRepository.findById(id)
                .map(ps -> {
                    ps.setOrdre(newOrder);
                    return processStepRepository.save(ps);
                });
    }
    
    public Optional<ProcessStep> activate(UUID id) {
        return processStepRepository.findById(id)
                .map(ps -> {
                    ps.setActif(true);
                    return processStepRepository.save(ps);
                });
    }
    
    public Optional<ProcessStep> deactivate(UUID id) {
        return processStepRepository.findById(id)
                .map(ps -> {
                    ps.setActif(false);
                    return processStepRepository.save(ps);
                });
    }
}
