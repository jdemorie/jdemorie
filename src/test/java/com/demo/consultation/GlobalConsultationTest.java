package com.demo.consultation;

import com.demo.consultation.domain.Doctor;
import com.demo.consultation.domain.DoctorId;
import com.demo.consultation.domain.Patient;
import com.demo.consultation.domain.PatientId;
import org.junit.jupiter.api.Test;

public abstract class GlobalConsultationTest {
  protected ConsultationScenario scenario;

  @Test
  void givenADocManagerWhenICreateAndDeleteADoctorThenTheDoctorStoredShouldBe() throws Exception {
    DoctorId doctorId = new DoctorId("jerome", "durant");
    scenario.thenTheListOfDoctorsStoredShouldBe()
        .whenICreateADoctor("jerome", "durant")
        .thenTheListOfDoctorsStoredShouldBe(doctorId)
        .whenIDeleteADoctor("jerome", "durant")
        .thenTheListOfDoctorsStoredShouldBe();
  }

  @Test
  void givenADocManagerWhenIAssignAPatientToADoctorThenDoctorKnowsThePatient() throws Exception {
    DoctorId doctor = new DoctorId("jerome", "durant");
    PatientId patient = new PatientId("john", "doe");
    scenario.whenICreateADoctor("jerome", "durant")
        .whenICreateAPatient("john", "doe")
        .whenIAssignAPatientToADoctor(patient, doctor)
        .thenDoctorShouldKnowPatient(doctor, patient);
  }

  @Test
  void givenAPatientWhenThePatientHaveAConsultationWithADoctorThenICanGetTheConsultationSummary() throws Exception {
    DoctorId doctor = new DoctorId("jerome", "durant");
    PatientId patient = new PatientId("john", "doe");
    scenario.whenICreateADoctor("jerome", "durant")
        .whenICreateAPatient("john", "doe")
        .whenPatientHaveAConsultationWithADoctor(patient, doctor, "firstConsultation")
        .thenTheConsultationShouldExistForPatient(patient, "firstConsultation")
        .thenDoctorShouldKnowPatient(doctor, patient);
  }

  @Test
  void givenAPatientWhenIWantToKnowAllHisConsultationThenIGetTheListOfConsultation() throws Exception {
    DoctorId doctor = new DoctorId("jerome", "durant");
    PatientId patient = new PatientId("john", "doe");
    scenario.whenICreateADoctor("jerome", "durant")
        .whenICreateAPatient("john", "doe")
        .whenPatientHaveAConsultationWithADoctor(patient, doctor, "firstConsultation")
        .whenPatientHaveAConsultationWithADoctor(patient, doctor, "secondConsultation")
        .thenTheConsultationShouldExistForPatient(patient, "firstConsultation", "secondConsultation");
  }

  @Test
  void givenADoctorWhenIAssignSeveralPatientsToADoctorThenDoctorKnowsAllThePatients() throws Exception {
    DoctorId doctor = new DoctorId("jerome", "durant");
    PatientId patient1 = new PatientId("john", "doe");
    PatientId patient2 = new PatientId("jane", "doe");
    scenario.whenICreateADoctor("jerome", "durant")
        .whenICreateAPatient("john", "doe")
        .whenICreateAPatient("jane", "doe")
        .whenIAssignAPatientToADoctor(patient1, doctor)
        .whenIAssignAPatientToADoctor(patient2, doctor)
        .thenDoctorShouldKnowPatient(doctor, patient1)
        .thenDoctorShouldKnowPatient(doctor, patient2);
  }
}
