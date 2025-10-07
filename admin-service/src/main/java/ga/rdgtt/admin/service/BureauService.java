package ga.rdgtt.admin.service;

import ga.rdgtt.admin.model.Bureau;
import ga.rdgtt.admin.repository.BureauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BureauService {

    @Autowired
    private BureauRepository bureauRepository;

    public List<Bureau> getAllBureaus() {
        return bureauRepository.findAll();
    }

    public Optional<Bureau> getBureauById(UUID id) {
        return bureauRepository.findById(id);
    }

    public Bureau createBureau(Bureau bureau) {
        return bureauRepository.save(bureau);
    }

    public Bureau updateBureau(Bureau bureau) {
        return bureauRepository.save(bureau);
    }

    public void deleteBureau(UUID id) {
        bureauRepository.deleteById(id);
    }

    public List<Bureau> getBureausByDepartment(UUID departmentId) {
        return bureauRepository.findByDepartementId(departmentId);
    }

    public List<Bureau> getActiveBureaus() {
        return bureauRepository.findByActifTrue();
    }

    public Optional<Bureau> getBureauByCode(String code) {
        return bureauRepository.findByCode(code);
    }
}
