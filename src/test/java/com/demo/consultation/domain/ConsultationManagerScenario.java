package com.demo.consultation.domain;

import com.demo.consultation.ConsultationScenario;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConsultationManagerScenario implements ConsultationScenario {
  private ConsultationManager consultationManager;

  public ConsultationScenario givenADocManager() {
    consultationManager = new ConsultationManager(new FakeDocManager());
    return this;
  }

  public ConsultationScenario whenICreateADoctor(String firstName, String lastName) {
    consultationManager.createDoctor(firstName, lastName);
    return this;
  }

  public ConsultationScenario whenIDeleteADoctor(String firstName, String lastName) {
    consultationManager.deleteDoctor(firstName, lastName);
    return this;
  }

  public ConsultationScenario whenICreateAPatient(String firstName, String lastName) {
    consultationManager.createPatient(firstName, lastName);
    return this;
  }

  @Override
  public ConsultationScenario whenIDeleteAPatient(String firstName, String lastName) throws Exception {
    consultationManager.deletePatient(firstName, lastName);
    return this;
  }

  public ConsultationScenario whenPatientHaveAConsultationWithADoctor(PatientId patientId, DoctorId doctorId, String consultationName)
      throws Exception {
    consultationManager.createConsultation(patientId, doctorId, consultationName);
    return this;
  }

  public ConsultationScenario whenIAssignAPatientToADoctor(PatientId patientId, DoctorId doctorId) throws Exception {
    consultationManager.addPatientToDoctor(patientId, doctorId);
    return this;
  }

  public ConsultationScenario thenTheListOfPatientsStoredShouldBe(PatientId... patients) {
    assertArrayEquals(patients, consultationManager.getPatientList().toArray());
    return this;
  }

  public ConsultationScenario thenTheListOfDoctorsStoredShouldBe(DoctorId... doctors) {
    assertArrayEquals(doctors, consultationManager.getDoctorList().stream().map(Doctor::doctorId).toArray());
    return this;
  }

  public ConsultationScenario thenTheListOfConsultationsStoredShouldBe(ConsultationId... consultationIds) {
    assertArrayEquals(consultationIds, consultationManager.getConsultations().stream().map(Consultation::consultationId).toArray());
    return this;
  }

  public ConsultationScenario thenTheConsultationShouldExistForPatient(PatientId patientId, String... consultationNames) throws Exception {
    List<Consultation> consultationForPatient = consultationManager.getConsultationsByPatient(patientId.firstName(), patientId.lastName());
    assertArrayEquals(consultationNames,
                      consultationForPatient.stream().map(consultation -> consultation.consultationId().consultationName()).toArray());
    return this;
  }

  public ConsultationScenario thenDoctorShouldKnowPatient(DoctorId doctorId, PatientId patientId) throws Exception {
    Patient patient = consultationManager.getPatientByFirstAndLastName(patientId.firstName(), patientId.lastName());
    Doctor doctor = consultationManager.getDoctorByFirstAndLastName(doctorId.firstName(), doctorId.lastName());
    assertTrue(doctor.knowsPatient(patient));
    return this;
  }

  @Override
  public ConsultationScenario whenICleanAllData() {
    consultationManager.clear();
    return this;
  }
}
