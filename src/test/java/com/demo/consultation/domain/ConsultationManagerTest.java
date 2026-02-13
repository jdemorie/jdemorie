package com.demo.consultation.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConsultationManagerTest {
  private ConsultationManagerScenario scenario;

  @BeforeEach
  public void setUp() throws Exception {
    scenario = new ConsultationManagerScenario();
    scenario.givenADocManager()
        .thenTheListOfDoctorsStoredShouldBe()
        .thenTheListOfPatientsStoredShouldBe()
        .thenTheListOfConsultationsStoredShouldBe();
  }

  @Test
  void givenADocManagerWhenICreateAndDeleteADoctorThenTheDoctorStoredShouldBe() throws Exception {
    scenario.thenTheListOfDoctorsStoredShouldBe()
        .whenICreateADoctor("jerome", "demorieux")
        .thenTheListOfDoctorsStoredShouldBe(new Doctor("jerome", "demorieux"))
        .whenIDeleteADoctor("jerome", "demorieux")
        .thenTheListOfDoctorsStoredShouldBe();
  }

  @Test
  void givenADocManagerWhenIAssignAPatientToADoctorThenDoctorKnowsThePatient() throws Exception {
    Doctor doctor = new Doctor("jerome", "demorieux");
    Patient patient = new Patient("john", "doe");
    scenario.whenICreateADoctor("jerome", "demorieux")
        .whenICreateAPatient("john", "doe")
        .whenIAssignAPatientToADoctor(patient, doctor)
        .thenDoctorShouldKnowPatient(doctor, patient);
  }

  @Test
  void givenAPatientWhenThePatientHaveAConsultationWithADoctorThenICanGetTheConsultationSummary() throws Exception {
    Doctor doctor = new Doctor("jerome", "demorieux");
    Patient patient = new Patient("john", "doe");
    scenario.whenICreateADoctor("jerome", "demorieux")
        .whenICreateAPatient("john", "doe")
        .whenPatientHaveAConsultationWithADoctor(patient, doctor, "firstConsultation")
        .thenTheConsultationShouldExistForPatient(patient, "firstConsultation")
        .thenDoctorShouldKnowPatient(doctor, patient);
  }

  @Test
  void givenAPatientWhenIWantToKnowAllHisConsultationThenIGetTheListOfConsultation() throws Exception {
    Doctor doctor = new Doctor("jerome", "demorieux");
    Patient patient = new Patient("john", "doe");
    scenario.whenICreateADoctor("jerome", "demorieux")
        .whenICreateAPatient("john", "doe")
        .whenPatientHaveAConsultationWithADoctor(patient, doctor, "firstConsultation")
        .whenPatientHaveAConsultationWithADoctor(patient, doctor, "secondConsultation")
        .thenTheConsultationShouldExistForPatient(patient, "firstConsultation", "secondConsultation");
  }
}
