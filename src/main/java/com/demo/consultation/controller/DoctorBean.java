package com.demo.consultation.controller;

public class DoctorBean {
  private String firstName;
  private String lastName;

  public DoctorBean(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public DoctorBean() {
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
