package ga.rdgtt.admin.service;

import ga.rdgtt.admin.model.DocumentType;
import ga.rdgtt.admin.repository.DocumentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class DocumentTypeService {
    
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    
    public List<DocumentType> findAll() {
        return documentTypeRepository.findAll();
    }
    
    public List<DocumentType> findByServiceCode(String serviceCode) {
        return documentTypeRepository.findByServiceCode(serviceCode);
    }
    
    public Optional<DocumentType> findById(UUID id) {
        return documentTypeRepository.findById(id);
    }
    
    public DocumentType create(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }
    
    public Optional<DocumentType> update(UUID id, DocumentType documentType) {
        if (documentTypeRepository.existsById(id)) {
            documentType.setId(id);
            return Optional.of(documentTypeRepository.save(documentType));
        }
        return Optional.empty();
    }
    
    public boolean delete(UUID id) {
        if (documentTypeRepository.existsById(id)) {
            documentTypeRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Optional<DocumentType> activate(UUID id) {
        return documentTypeRepository.findById(id)
                .map(dt -> {
                    dt.setActif(true);
                    return documentTypeRepository.save(dt);
                });
    }
    
    public Optional<DocumentType> deactivate(UUID id) {
        return documentTypeRepository.findById(id)
                .map(dt -> {
                    dt.setActif(false);
                    return documentTypeRepository.save(dt);
                });
    }
}
