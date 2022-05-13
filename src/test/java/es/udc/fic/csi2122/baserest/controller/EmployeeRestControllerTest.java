package es.udc.fic.csi2122.baserest.controller;

import es.udc.fic.csi2122.baserest.dto.EmployeeDto;
import es.udc.fic.csi2122.baserest.entity.Employee;
import es.udc.fic.csi2122.baserest.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static es.udc.fic.csi2122.baserest.utils.TestRestTemplateUtils.getForList;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeRestControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    private void initBaseUrl() {
        baseUrl = "http://localhost:" + port + "/employee";
    }

    //Antes y despues de cada test debemos borrar todo lo que haya en la base de datos para que no dependan unos de otros
    @BeforeEach
    @AfterEach
    private void resetEmployees() {
        employeeRepository.deleteAll();
    }

    //Comprobamos que al principio el bd esta vacia
    @Test
    void getAllEmployees(){
        var response = getForList(restTemplate, baseUrl + "/all", EmployeeDto.class);

        assertThat(response).isEmpty();
    }

    /*Testeamos que al añadir un empleado hay unicamente uno y que concuerda con el añadido*/
    @Test
    void getAllEmployees2() {
        var employee = new EmployeeDto("Pablo", "Fic",40,1700.f, "hola", 10);
        restTemplate.postForObject(baseUrl + "/", employee, Long.class);

        var response = getForList(restTemplate, baseUrl + "/all", EmployeeDto.class);

        assertThat(response).hasSize(1);
        assertThat(response.get(0)).isEqualTo(employee);
    }


    /*Hacemos un test para ver que se añade bien y que el empleado añadido concuerda con el que queria añadir
     * */
    @Test
    void createEmployee() {
        var employee = new EmployeeDto("Krillin", "Facultad economia",22,20.f, "dbz", 16);
        var response = restTemplate.postForEntity(baseUrl + "/", employee, Long.class);

        assertThat(response.getStatusCode()).matches(HttpStatus::is2xxSuccessful);

        var id = response.getBody();
        var response2 = restTemplate.getForObject(baseUrl + "/id=" + id, EmployeeDto.class);
        assertThat(response2).isEqualTo(employee);
    }

    //Testeamos que si se actualiza bien la informacion de un cliente al hacer el update
    @Test
    void updateEmployee(){
        var employee1 = new EmployeeDto("FelipeVI", "Agra de Orzan",40,400000.f, "hola", 32);
        var response = restTemplate.postForEntity(baseUrl + "/", employee1, Long.class);
        var id = response.getBody();

        var employee2 = new EmployeeDto("Krillin", "Facultad economia",22,20.f, "dbz", 16);
        restTemplate.put(baseUrl+ "/id="+ id,employee2,Long.class);

        var response2 = restTemplate.getForObject(baseUrl + "/id=" + id, EmployeeDto.class);
        assertThat(response2).isEqualTo(employee2);

    }

    /*Testeamos que el find by name efectivamente encuentra el empleado que queremos y que concuerda
        Para ello inserto 2 productos y compruebo que efectivamente el que he buscado es el que queria
     */
    @Test
    void findById(){
        var employee1 = new EmployeeDto("FelipeVI", "Agra de Orzan",40,400000.f, "hola", 32);
        var response = restTemplate.postForEntity(baseUrl + "/", employee1, Long.class);

        var employee2 = new EmployeeDto("FelipeVII", "Riazor",42,500000.f, "adios", 33);
        restTemplate.postForEntity(baseUrl + "/", employee2, Long.class);

        var id = response.getBody();
        var response2= restTemplate.getForObject(baseUrl+ "/id=" + id, EmployeeDto.class);
        assertThat(response2).isEqualTo(employee1);

    }


    /*Compruebo que si busco por departamento me devuelve el numero de empleados que deseo y que no me devuelve los de otros departamentos.
     * Ademas compruebo que son los empleados que he introducido y no otros*/

    @Test
    void findByDept(){
        var department= 32;
        var employee1 = new EmployeeDto("FelipeVI", "Agra de Orzan",40,400000.f, "hola", department);
        restTemplate.postForEntity(baseUrl + "/", employee1, Long.class);

        var employee2 = new EmployeeDto("Krillin", "Facultad economia",22,20.f, "dbz", department);
        restTemplate.postForEntity(baseUrl + "/", employee2, Long.class);

        var employee3 = new Employee("Emmanuel", "Puerto Rico", 28, 2400.f, "brrrr", 15);
        restTemplate.postForEntity(baseUrl + "/", employee3, Long.class);

        ResponseEntity<EmployeeDto[]> response =
                restTemplate.getForEntity(baseUrl+ "/department=" + department, EmployeeDto[].class);

        EmployeeDto[] employeeDtos = response.getBody();

        assertThat(employeeDtos.length).isEqualTo(2);
        assertThat(employeeDtos[0]).isEqualTo(employee1);
        assertThat(employeeDtos[1]).isEqualTo(employee2);
    }
}
