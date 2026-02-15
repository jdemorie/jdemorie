package com.demo.consultation;

import com.demo.consultation.domain.ConsultationId;
import com.demo.consultation.domain.DoctorId;
import com.demo.consultation.domain.PatientId;

public interface ConsultationScenario {
  ConsultationScenario givenADocManager();

  ConsultationScenario whenICreateADoctor(String firstName, String lastName) throws Exception;

  ConsultationScenario whenIDeleteADoctor(String firstName, String lastName) throws Exception;

  ConsultationScenario whenICreateAPatient(String firstName, String lastName) throws Exception;

  ConsultationScenario whenIDeleteAPatient(String firstName, String lastName) throws Exception;

  ConsultationScenario whenPatientHaveAConsultationWithADoctor(PatientId patientId, DoctorId doctorId, String consultationName) throws Exception;

  ConsultationScenario whenIAssignAPatientToADoctor(PatientId patientId, DoctorId doctorId) throws Exception;

  ConsultationScenario thenTheListOfPatientsStoredShouldBe(PatientId... patientIds) throws Exception;

  ConsultationScenario thenTheListOfDoctorsStoredShouldBe(DoctorId... doctorIds) throws Exception;

  ConsultationScenario thenTheListOfConsultationsStoredShouldBe(ConsultationId... consultationIds) throws Exception;

  ConsultationScenario thenTheConsultationShouldExistForPatient(PatientId patientId, ConsultationId... consultationIds) throws Exception;

  ConsultationScenario thenDoctorShouldKnowPatient(DoctorId doctorId, PatientId patientId) throws Exception;

  ConsultationScenario thenDoctorShouldHaveTheListOfPatients(DoctorId doctorId, PatientId... patientIds) throws Exception;

  ConsultationScenario whenICleanAllData() throws Exception;
}
