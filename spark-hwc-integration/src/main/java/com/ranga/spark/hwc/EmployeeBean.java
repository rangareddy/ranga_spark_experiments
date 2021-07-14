package com.ranga.spark.hwc;

import java.io.Serializable;

/**
 * @author Ranga Reddy
 * Version: 1.0
 * Created : 07/14/2021
 */

public class EmployeeBean implements Serializable {

    private Long id;
    private String name;
    private Integer age;
    private Float salary;

    public EmployeeBean(Long id, String name, Integer age, Float salary) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", salary=" + salary +
                '}';
    }
}