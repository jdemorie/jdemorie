package com.demo.consultation.controller;

public class ConsultationBean {
  private String consultationName;

  public ConsultationBean() {
  }

  public ConsultationBean(String consultationName) {
    this.consultationName = consultationName;
  }

  public String getConsultationName() {
    return consultationName;
  }

  public void setConsultationName(String consultationName) {
    this.consultationName = consultationName;
  }
}
