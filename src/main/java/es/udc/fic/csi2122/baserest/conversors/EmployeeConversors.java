package es.udc.fic.csi2122.baserest.conversors;

import es.udc.fic.csi2122.baserest.dto.EmployeeDto;
import es.udc.fic.csi2122.baserest.dto.UserDto;
import es.udc.fic.csi2122.baserest.entity.Employee;

import java.util.List;

public class EmployeeConversors {

    private EmployeeConversors() {
    }

    public static EmployeeDto toEmployeeDto(Employee employee) {
        return new EmployeeDto( employee.getName(), employee.getAdress(), employee.getAge(),
                employee.getSalario(), employee.getSsn(),employee.getDepartment());
    }

    public static List<EmployeeDto> toEmployeeDtoList(List<Employee> employees){
        return employees.stream().map(EmployeeConversors::toEmployeeDto).toList();
    }

    public static Employee toEmployee(EmployeeDto employee){
        return new Employee(employee.name(), employee.adress(), employee.age(),
                employee.salario(), employee.ssn(),employee.department());
    }
}
