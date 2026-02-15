package com.demo.consultation.controller;

import com.demo.consultation.ConsultationScenario;
import com.demo.consultation.domain.ConsultationId;
import com.demo.consultation.domain.DoctorId;
import com.demo.consultation.domain.PatientId;
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
  public ConsultationScenario whenPatientHaveAConsultationWithADoctor(PatientId patientId, DoctorId doctorId, String consultationName)
      throws Exception {
    DoctorBean doctorBean = new DoctorBean(doctorId.firstName(), doctorId.lastName());
    String jsonContent = objectMapper.writeValueAsString(doctorBean);
    MvcResult mvcResult = mockMvc.perform(
            post("/server/consultation/{consultationName}/patient/{patientFirstName}/{patientLastName}/add",
                 consultationName, patientId.firstName(), patientId.lastName())
                .content(jsonContent).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String responseContent = mvcResult.getResponse().getContentAsString();
    objectMapper.readValue(responseContent, ConsultationBean.class);
    return this;
  }

  @Override
  public ConsultationScenario whenIAssignAPatientToADoctor(PatientId patientId, DoctorId doctorId) throws Exception {
    DoctorBean doctorBean = new DoctorBean(doctorId.firstName(), doctorId.lastName());
    PatientBean patientBean = new PatientBean(patientId.firstName(), patientId.lastName());
    mockMvc.perform(put("/server/doctor/{doctorFirstName}/{doctorLastName}/patient/{patientFirstName}/{patientLastName}/assign",
                        doctorBean.getFirstName(), doctorBean.getLastName(), patientBean.getFirstName(), patientBean.getLastName())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().string("Patient assigned to a doctor"));
    return this;
  }

  @Override
  public ConsultationScenario thenTheListOfPatientsStoredShouldBe(PatientId... patientIds) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/server/patient").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();
    PatientBean[] returnedPatientBeans = objectMapper.readValue(jsonResponse, PatientBean[].class);
    assertEquals(patientIds.length, returnedPatientBeans.length);
    assertArrayEquals(patientIds, Arrays.stream(returnedPatientBeans).map(p -> new PatientId(p.getFirstName(), p.getLastName())).toArray());
    return this;
  }

  @Override
  public ConsultationScenario thenTheListOfDoctorsStoredShouldBe(DoctorId... doctorIds) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/server/doctor").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();
    DoctorBean[] returnedDoctorBeans = objectMapper.readValue(jsonResponse, DoctorBean[].class);
    assertEquals(doctorIds.length, returnedDoctorBeans.length);
    assertArrayEquals(doctorIds, Arrays.stream(returnedDoctorBeans).map(p -> new DoctorId(p.getFirstName(), p.getLastName())).toArray());
    return this;
  }

  @Override
  public ConsultationScenario thenTheListOfConsultationsStoredShouldBe(ConsultationId... consultationIds) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/server/consultation").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();
    ConsultationBean[] returnedConsultationBeans = objectMapper.readValue(jsonResponse, ConsultationBean[].class);
    assertEquals(consultationIds.length, returnedConsultationBeans.length);
    assertArrayEquals(consultationIds, Arrays.stream(returnedConsultationBeans).map(p -> new ConsultationId(p.getConsultationName())).toArray());
    return this;
  }

  @Override
  public ConsultationScenario thenTheConsultationShouldExistForPatient(PatientId patientId, ConsultationId... consultationIds) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/server/patient/{firstName}/{lastName}/consultations",
                                              patientId.firstName(), patientId.lastName())
                                              .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();
    ConsultationBean[] returnedConsultationBeans = objectMapper.readValue(jsonResponse, ConsultationBean[].class);
    assertEquals(consultationIds.length, returnedConsultationBeans.length);
    assertArrayEquals(consultationIds, Arrays.stream(returnedConsultationBeans).map(p -> new ConsultationId(p.getConsultationName())).toArray());
    return this;
  }

  @Override
  public ConsultationScenario thenDoctorShouldKnowPatient(DoctorId doctorId, PatientId patientId) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/server/doctor/{doctorFirstName}/{doctorLastName}/patients",
                                              doctorId.firstName(), doctorId.lastName())
                                              .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();
    PatientBean[] returnedPatientBeans = objectMapper.readValue(jsonResponse, PatientBean[].class);
    assertTrue(Arrays.stream(returnedPatientBeans).map(p -> new PatientId(p.getFirstName(), p.getLastName())).toList().contains(patientId));
    return this;
  }

  @Override
  public ConsultationScenario thenDoctorShouldHaveTheListOfPatients(DoctorId doctorId, PatientId... patientIds) throws Exception {
    MvcResult mvcResult = mockMvc.perform(get("/server/doctor/{doctorFirstName}/{doctorLastName}/patients",
                                              doctorId.firstName(), doctorId.lastName())
                                              .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
    String jsonResponse = mvcResult.getResponse().getContentAsString();
    PatientBean[] returnedPatientBeans = objectMapper.readValue(jsonResponse, PatientBean[].class);
    assertEquals(patientIds.length, returnedPatientBeans.length);
    assertArrayEquals(patientIds, Arrays.stream(returnedPatientBeans).map(p -> new PatientId(p.getFirstName(), p.getLastName())).toArray());
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
