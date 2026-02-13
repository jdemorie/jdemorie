package com.demo.consultation.domain;

public class DataNotFoundException extends Exception {
  public DataNotFoundException(String message, Exception e) {
    super(message, e);
  }

  public DataNotFoundException(String message) {
    super(message);
  }
}
