package es.udc.fic.csi2122.baserest.controller;

import es.udc.fic.csi2122.baserest.conversors.EmployeeConversors;
import es.udc.fic.csi2122.baserest.conversors.UserConversors;
import es.udc.fic.csi2122.baserest.dto.EmployeeDto;
import es.udc.fic.csi2122.baserest.dto.UserDto;
import es.udc.fic.csi2122.baserest.entity.Employee;
import es.udc.fic.csi2122.baserest.entity.User;
import es.udc.fic.csi2122.baserest.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@Transactional
@RequestMapping("employee")
public class EmployeeRestController {

    /*DUDAS TUTORIA
     * APLICATION.PROPERTIES
     * EL GET INDIVIDUAL SI HAY DOS CON MISMO NOMBRE ROMPE*/
    private static final Logger logger = LoggerFactory.getLogger(BaseRestController.class);

    @PersistenceContext
    private EntityManager em;

    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeRestController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //Devuelve la lista de todos los empleados, para comprobaciones
    @GetMapping(value = "all")
    public List<EmployeeDto> getEmployeeList() {
        return EmployeeConversors.toEmployeeDtoList(employeeRepository.findAll());
    }

    //Crea un nuevo employee con url /employees
    @PostMapping(value = "")
    public Long newEmployee(@RequestBody EmployeeDto employeeDto) {
        logger.info("Creating new employee: {}", employeeDto);
        var newEmployee = em.merge(EmployeeConversors.toEmployee(employeeDto));
        logger.info("Created employee: {}", newEmployee);
        return newEmployee.getIdEmployee();
    }

    //Modifica un employe a URL /employees/id=?
    @PutMapping(value = "id={id}")
    public void updateEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable Long id){
        Employee employee = employeeRepository.getById(id);
        logger.info("Updating employee: {}", employee);
        employee.setAdress(employeeDto.adress());
        employee.setAge(employeeDto.age());
        employee.setDepartment(employeeDto.department());
        employee.setSalario(employeeDto.salario());
        employee.setName(employeeDto.name());
        employee.setSsn(employeeDto.ssn());
        Employee updatedEmployee = employeeRepository.save(employee);
        logger.info("Updated employee: {}", updatedEmployee);
    }

    //Devuelve el/los empleados con id ? a URL /employees/id=?
    @GetMapping(value = "/id={idEmployee}")
    public  ResponseEntity<EmployeeDto>getByName(@PathVariable Long idEmployee) {

        logger.info("Fetching employee with id: {}", idEmployee);
        var employee = employeeRepository.findById(idEmployee);

        return ResponseEntity.of(employee.map(u -> {
            logger.info("Found employee with id {}: {}", idEmployee, u);
            return EmployeeConversors.toEmployeeDto(u);
        }));
    }


    //Devuelve el/los empleados con departamento ? a URL /employees/department=?
    @GetMapping(value="/department={department}")
    public ResponseEntity<List<EmployeeDto>> getByDepartment(@PathVariable Integer department){

        final List<Employee> employees;

        logger.info("Fetching employee with department: {}", department);

        employees = employeeRepository.findByDepartment(department);

        return ResponseEntity.ok(EmployeeConversors.toEmployeeDtoList(employees));
    }


}
