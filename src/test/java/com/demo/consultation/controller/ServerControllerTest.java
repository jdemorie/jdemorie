package com.demo.consultation.controller;

import com.demo.consultation.application.ServerApplication;
import com.demo.consultation.domain.Doctor;
import com.demo.consultation.domain.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ServerApplication.class)
public class ServerControllerTest {
  @Autowired
  private WebApplicationContext wac;
  private ServerConsultationManagerScenario scenario;

  @BeforeEach
  public void setUp() throws Exception {
    scenario = new ServerConsultationManagerScenario(wac);
    scenario.givenADocManager()
        .whenICleanAllData()
        .thenTheListOfPatientsStoredShouldBe();
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
