package com.demo.consultation.domain;

import com.demo.consultation.ConsultationScenario;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsultationManagerScenario implements ConsultationScenario {
  private ConsultationManager consultationManager;
  private Doctor doctor;
  private Patient patient;
  private Consultation consultation;

  public ConsultationScenario givenADocManager() {
    consultationManager = new ConsultationManager(new FakeDocManager());
    return this;
  }

  public ConsultationScenario whenICreateADoctor(String firstName, String lastName) {
    doctor = consultationManager.createDoctor(firstName, lastName);
    return this;
  }

  public ConsultationScenario whenIDeleteADoctor(String firstName, String lastName) {
    consultationManager.deleteDoctor(firstName, lastName);
    return this;
  }

  public ConsultationScenario whenICreateAPatient(String firstName, String lastName) {
    patient = consultationManager.createPatient(firstName, lastName);
    return this;
  }

  @Override
  public ConsultationScenario whenIDeleteAPatient(String firstName, String lastName) throws Exception {
    consultationManager.deletePatient(firstName, lastName);
    return this;
  }

  public ConsultationScenario whenPatientHaveAConsultationWithADoctor(Patient patient, Doctor doctor, String consultationName) throws Exception {
    consultation = consultationManager.createConsultation(patient, doctor, consultationName);
    return this;
  }

  public ConsultationScenario whenIAssignAPatientToADoctor(Patient patient, Doctor doctor) {
    doctor.addPatient(patient);
    return this;
  }

  public ConsultationScenario thenTheListOfPatientsStoredShouldBe(Patient... patients) {
    assertArrayEquals(patients, consultationManager.getPatientList().toArray());
    return this;
  }

  public ConsultationScenario thenTheListOfDoctorsStoredShouldBe(Doctor... doctors) {
    assertArrayEquals(doctors, consultationManager.getDoctorList().toArray());
    return this;
  }

  public ConsultationScenario thenTheListOfConsultationsStoredShouldBe(Consultation... consultations) {
    assertArrayEquals(consultations, consultationManager.getConsultations().toArray());
    return this;
  }

  public ConsultationScenario thenTheConsultationShouldExistForPatient(Patient patient, String... consultationNames) throws Exception {
    List<Consultation> consultationForPatient = consultationManager.getConsultationsByPatient(patient);
    assertArrayEquals(consultationNames, consultationForPatient.stream().map(Consultation::consultationName).toArray());
    return this;
  }

  public ConsultationScenario thenDoctorShouldKnowPatient(Doctor doctor, Patient patient) {
    assertTrue(doctor.knowsPatient(patient));
    return this;
  }

  @Override
  public ConsultationScenario whenICleanAllData() {
    consultationManager.clear();
    return this;
  }
}
