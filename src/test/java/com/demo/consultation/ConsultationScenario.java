package com.demo.consultation;

import com.demo.consultation.domain.Consultation;
import com.demo.consultation.domain.Doctor;
import com.demo.consultation.domain.Patient;

public interface ConsultationScenario {
  ConsultationScenario givenADocManager();

  ConsultationScenario whenICreateADoctor(String firstName, String lastName) throws Exception;

  ConsultationScenario whenIDeleteADoctor(String firstName, String lastName) throws Exception;

  ConsultationScenario whenICreateAPatient(String firstName, String lastName) throws Exception;

  ConsultationScenario whenIDeleteAPatient(String firstName, String lastName) throws Exception;

  ConsultationScenario whenPatientHaveAConsultationWithADoctor(Patient patient, Doctor doctor, String consultationName) throws Exception;

  ConsultationScenario whenIAssignAPatientToADoctor(Patient patient, Doctor doctor) throws Exception;

  ConsultationScenario thenTheListOfPatientsStoredShouldBe(Patient... patients) throws Exception;

  ConsultationScenario thenTheListOfDoctorsStoredShouldBe(Doctor... doctors) throws Exception;

  ConsultationScenario thenTheListOfConsultationsStoredShouldBe(Consultation... consultations) throws Exception;

  ConsultationScenario thenTheConsultationShouldExistForPatient(Patient patient, String... consultationNames) throws Exception;

  ConsultationScenario thenDoctorShouldKnowPatient(Doctor doctor, Patient patient) throws Exception;

  ConsultationScenario whenICleanAllData() throws Exception;
}
