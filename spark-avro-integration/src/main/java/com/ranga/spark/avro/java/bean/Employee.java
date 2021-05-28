package com.ranga.spark.avro.java.bean;

import java.io.Serializable;

/* rangareddy.avula created on 11/06/20 */

public class Employee implements Serializable {
    private long id;
    private String name;
    private int age;
    private float salary;

    public Employee() {

    }

    public Employee(long id, String name, int age, float salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getSalary() {
        return salary;
    }

    public void setSalary(float salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
