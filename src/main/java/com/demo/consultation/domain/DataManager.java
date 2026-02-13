package com.demo.consultation.domain;

import java.util.List;

public interface DataManager {
  Patient createPatient(String firstName, String lastName);

  void deletePatient(String firstName, String lastName);

  Doctor createDoctor(String firstName, String lastName);

  void deleteDoctor(String firstName, String lastName);

  Consultation createConsultation(String consultationName, Patient patient, Doctor doctor) throws DataNotFoundException;

  List<Consultation> getConsultations();

  List<Patient> getPatientList();

  List<Doctor> getDoctorList();

  void clear();

  Doctor getDoctorByFirstAndLastName(String firstName, String lastName) throws DataNotFoundException;

  Patient getPatientByFirstAndLastName(String firstName, String lastName) throws DataNotFoundException;

  List<Consultation> getConsultationsByPatient(Patient patient) throws DataNotFoundException;

  void addPatientToDoctor(Patient patient, Doctor doctor) throws DataNotFoundException;
}
