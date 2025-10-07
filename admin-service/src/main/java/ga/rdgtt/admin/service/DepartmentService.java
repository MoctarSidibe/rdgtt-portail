package ga.rdgtt.admin.service;

import ga.rdgtt.admin.model.Department;
import ga.rdgtt.admin.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(UUID id) {
        return departmentRepository.findById(id);
    }

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public Department updateDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public void deleteDepartment(UUID id) {
        departmentRepository.deleteById(id);
    }

    public List<Department> getActiveDepartments() {
        return departmentRepository.findByActifTrue();
    }

    public Optional<Department> getDepartmentByCode(String code) {
        return departmentRepository.findByCode(code);
    }
}
