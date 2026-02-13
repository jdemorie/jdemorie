package com.demo.consultation;

import com.demo.consultation.domain.Doctor;
import com.demo.consultation.domain.Patient;
import org.junit.jupiter.api.Test;

public abstract class GlobalConsultationTest {
  protected ConsultationScenario scenario;

  @Test
  void givenADocManagerWhenICreateAndDeleteADoctorThenTheDoctorStoredShouldBe() throws Exception {
    scenario.thenTheListOfDoctorsStoredShouldBe()
        .whenICreateADoctor("jerome", "durant")
        .thenTheListOfDoctorsStoredShouldBe(new Doctor("jerome", "durant"))
        .whenIDeleteADoctor("jerome", "durant")
        .thenTheListOfDoctorsStoredShouldBe();
  }

  @Test
  void givenADocManagerWhenIAssignAPatientToADoctorThenDoctorKnowsThePatient() throws Exception {
    Doctor doctor = new Doctor("jerome", "durant");
    Patient patient = new Patient("john", "doe");
    scenario.whenICreateADoctor("jerome", "durant")
        .whenICreateAPatient("john", "doe")
        .whenIAssignAPatientToADoctor(patient, doctor)
        .thenDoctorShouldKnowPatient(doctor, patient);
  }

  @Test
  void givenAPatientWhenThePatientHaveAConsultationWithADoctorThenICanGetTheConsultationSummary() throws Exception {
    Doctor doctor = new Doctor("jerome", "durant");
    Patient patient = new Patient("john", "doe");
    scenario.whenICreateADoctor("jerome", "durant")
        .whenICreateAPatient("john", "doe")
        .whenPatientHaveAConsultationWithADoctor(patient, doctor, "firstConsultation")
        .thenTheConsultationShouldExistForPatient(patient, "firstConsultation")
        .thenDoctorShouldKnowPatient(doctor, patient);
  }

  @Test
  void givenAPatientWhenIWantToKnowAllHisConsultationThenIGetTheListOfConsultation() throws Exception {
    Doctor doctor = new Doctor("jerome", "durant");
    Patient patient = new Patient("john", "doe");
    scenario.whenICreateADoctor("jerome", "durant")
        .whenICreateAPatient("john", "doe")
        .whenPatientHaveAConsultationWithADoctor(patient, doctor, "firstConsultation")
        .whenPatientHaveAConsultationWithADoctor(patient, doctor, "secondConsultation")
        .thenTheConsultationShouldExistForPatient(patient, "firstConsultation", "secondConsultation");
  }

  @Test
  void givenADoctorWhenIAssignSeveralPatientsToADoctorThenDoctorKnowsAllThePatients() throws Exception {
    Doctor doctor = new Doctor("jerome", "durant");
    Patient patient1 = new Patient("john", "doe");
    Patient patient2 = new Patient("jane", "doe");
    scenario.whenICreateADoctor("jerome", "durant")
        .whenICreateAPatient("john", "doe")
        .whenICreateAPatient("jane", "doe")
        .whenIAssignAPatientToADoctor(patient1, doctor)
        .whenIAssignAPatientToADoctor(patient2, doctor)
        .thenDoctorShouldKnowPatient(doctor, patient1)
        .thenDoctorShouldKnowPatient(doctor, patient2);
  }
}
