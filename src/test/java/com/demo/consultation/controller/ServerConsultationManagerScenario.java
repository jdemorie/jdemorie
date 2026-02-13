package com.demo.consultation.controller;

import com.demo.consultation.ConsultationScenario;
import com.demo.consultation.domain.Consultation;
import com.demo.consultation.domain.Doctor;
import com.demo.consultation.domain.Patient;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ServerConsultationManagerScenario implements ConsultationScenario {
  private final WebApplicationContext applicationContext;
  private final ObjectMapper objectMapper;
  private MockMvc mockMvc;

  public ServerConsultationManagerScenario(WebApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    this.objectMapper = new ObjectMapper();
  }

  @Override
  public ConsultationScenario givenADocManager() {
    DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(applicationContext);
    mockMvc = builder.build();
    return this;
  }

  @Override
  public ConsultationScenario whenICreateADoctor(String firstName, String lastName) throws Exception {
    DoctorBean doctorBean = new DoctorBean(firstName, lastName);
    String jsonContent = objectMapper.writeValueAsString(doctorBean);
    MvcResult mvcResult =
        mockMvc.perform(post("/server/doctor").content(jsonContent).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();
    String responseContent = mvcResult.getResponse().getContentAsString();
    objectMapper.readValue(responseContent, DoctorBean.class);
    return this;
  }

  @Override
  public ConsultationScenario whenIDeleteADoctor(String firstName, String lastName) throws Exception {
    DoctorBean doctorBean = new DoctorBean(firstName, lastName);
    String jsonContent = objectMapper.writeValueAsString(doctorBean);
    mockMvc.perform(delete("/server/doctor").content(jsonContent).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Doctor deleted successfully"));
    return this;
  }

  public ConsultationScenario whenIDeleteAPatient(String firstName, String lastName) throws Exception {
    PatientBean patientBean = new PatientBean(firstName, lastName);
    String jsonContent = objectMapper.writeValueAsString(patientBean);
    mockMvc.perform(
            delete("/server/patient").content(jsonContent).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Patient deleted successfully"));
    return this;
  }

  @Override
  public ConsultationScenario whenICreateAPatient(String firstName, String lastName) throws Exception {
    PatientBean patientBean = new PatientBean(firstName, lastName);
    String jsonContent = objectMapper.writeValueAsString(patientBean);
    MvcResult mvcResult = mockMvc.perform(
            post("/server/patient").content(jsonContent).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String responseContent = mvcResult.getResponse().getContentAsString();
    objectMapper.readValue(responseContent, PatientBean.class);
    return this;
  }

  @Override
  public ConsultationScenario whenPatientHaveAConsultationWithADoctor(Patient patient, Doctor doctor, String consultationName) throws Exception {
    DoctorBean doctorBean = new DoctorBean(doctor.firstName(), doctor.lastName());
    String jsonContent = objectMapper.writeValueAsString(doctorBean);
    MvcResult mvcResult = mockMvc.perform(
            post("/server/consultation/{consultationName}/patient/{patientFirstName}/{patientLastName}/add",
                 consultationName, patient.firstName(), patient.lastName())
                .content(jsonContent).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String responseContent = mvcResult.getResponse().getContentAsString();
    objectMapper.readValue(responseContent, ConsultationBean.class);
    return this;
  }

  @Override
  public ConsultationScenario whenIAssignAPatientToADoctor(Patient patient, Doctor doctor) throws Exception {
    DoctorBean doctorBean = new DoctorBean(doctor.firstName(), doctor.lastName());
    PatientBean patientBean = new PatientBean(patient.firstName(), patient.lastName());
    mockMvc.perform(put("/server/doctor/{doctorFirstName}/{doctorLastName}/patient/{patientFirstName}/{patientLastName}/assign",
                        doctorBean.getFirstName(), doctorBean.getLastName(), patientBean.getFirstName(), patientBean.getLastName())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Patient assigned to a doctor"));
    return this;
  }

  @Override
  public ConsultationScenario thenTheListOfPatientsStoredShouldBe(Patient... patients) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/server/patient").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();
    PatientBean[] returnedPatientBeans = objectMapper.readValue(jsonResponse, PatientBean[].class);
    assertEquals(patients.length, returnedPatientBeans.length);
    assertArrayEquals(patients, Arrays.stream(returnedPatientBeans).map(p -> new Patient(p.getFirstName(), p.getLastName())).sorted().toArray());
    return this;
  }

  @Override
  public ConsultationScenario thenTheListOfDoctorsStoredShouldBe(Doctor... doctors) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/server/doctor").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();
    DoctorBean[] returnedDoctorBeans = objectMapper.readValue(jsonResponse, DoctorBean[].class);
    assertEquals(doctors.length, returnedDoctorBeans.length);
    assertArrayEquals(doctors, Arrays.stream(returnedDoctorBeans).map(p -> new Doctor(p.getFirstName(), p.getLastName())).sorted().toArray());
    return this;
  }

  @Override
  public ConsultationScenario thenTheListOfConsultationsStoredShouldBe(Consultation... consultations) throws Exception {
    return this;
  }

  @Override
  public ConsultationScenario thenTheConsultationShouldExistForPatient(Patient patient, String... consultationNames) throws Exception {
    return this;
  }

  @Override
  public ConsultationScenario thenDoctorShouldKnowPatient(Doctor doctor, Patient patient) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/server/doctor/{doctorFirstName}/{doctorLastName}/patients",
                                              doctor.firstName(), doctor.lastName())
                                              .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();
    PatientBean[] returnedPatientBeans = objectMapper.readValue(jsonResponse, PatientBean[].class);
    assertTrue(Arrays.stream(returnedPatientBeans).map(p -> new Patient(p.getFirstName(), p.getLastName())).toList().contains(patient));
    return this;
  }

  @Override
  public ConsultationScenario whenICleanAllData() throws Exception {
    mockMvc.perform((post("/server/clean").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)))
        .andExpect(status().isOk())
        .andExpect(content().string("All data cleaned successfully"));
    return this;
  }
}
