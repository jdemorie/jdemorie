package com.demo.consultation.domain;

import java.util.List;

public interface DataManager {
  Patient createPatient(String firstName, String lastName);

  Doctor createDoctor(String firstName, String lastName);

  Consultation createConsultation(String consultationName, Patient patient, Doctor doctor) throws DataNotFoundException;

  List<Patient> getPatientList();

  List<Doctor> getDoctorList();

  List<Consultation> getConsultations();

  Doctor getDoctorByFirstAndLastName(String firstName, String lastName) throws DataNotFoundException;

  Patient getPatientByFirstAndLastName(String firstName, String lastName) throws DataNotFoundException;

  void deletePatient(String firstName, String lastName);

  void deleteDoctor(String firstName, String lastName);

  void save(Doctor doctor) throws DataNotFoundException;

  void clear();
}
