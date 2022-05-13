package es.udc.fic.csi2122.baserest.entity;

import javax.persistence.*;

@Entity
public class Employee {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmployee;

    @Column
    private String name;

    @Column
    private String adress;

    @Column
    private Integer age;

    @Column
    private Float salario;

    @Column
    private String ssn;

    @Column
    private Integer department;

    public Employee(){

    }
    public Employee(String name, String adress, Integer age, Float salario, String ssn, Integer department) {
        this.name = name;
        this.adress = adress;
        this.age = age;
        this.salario = salario;
        this.ssn = ssn;
        this.department = department;
    }

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Float getSalario() {
        return salario;
    }

    public void setSalario(Float salario) {
        this.salario = salario;
    }

    public String getSsn() {
        return ssn;
    }

    public void setSsn(String ssn) {
        this.ssn = ssn;
    }

    public Integer getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }


}
