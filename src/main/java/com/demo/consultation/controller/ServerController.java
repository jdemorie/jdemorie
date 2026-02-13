package com.demo.consultation.controller;

import com.demo.consultation.db.service.DatabaseProvider;
import com.demo.consultation.db.service.DatabaseService;
import com.demo.consultation.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/server")
public class ServerController {
  private final String version;
  private final ConsultationManager consultationManager;

  public ServerController(DatabaseService bookService) {
    this.consultationManager = new ConsultationManager(new DatabaseProvider(bookService));
    this.version = "1.0.0";
  }

  @GetMapping(path = "version", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> serverVersion() {
    return ResponseEntity.ok("Server version: " + version);
  }

  @PostMapping(path = "clean", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> cleanAll() {
    consultationManager.clear();
    return ResponseEntity.ok("All data cleaned successfully");
  }

  @GetMapping(path = "patient", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PatientBean>> getPatients() {
    List<Patient> patientList = consultationManager.getPatientList();
    return ResponseEntity.ok(patientList.stream().map(patient -> {
      PatientBean patientBean = new PatientBean();
      patientBean.setFirstName(patient.firstName());
      patientBean.setLastName(patient.lastName());
      return patientBean;
    }).toList());
  }

  @PostMapping(path = "patient", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<PatientBean> createPatient(@RequestBody PatientBean patientBean) {
    Patient patient = consultationManager.createPatient(patientBean.getFirstName(), patientBean.getLastName());
    return ResponseEntity.ok(new PatientBean(patient.firstName(), patient.lastName()));
  }

  @DeleteMapping(path = "patient", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> deletePatient(@RequestBody PatientBean patientBean) {
    consultationManager.deletePatient(patientBean.getFirstName(), patientBean.getLastName());
    return ResponseEntity.ok("Patient deleted successfully");
  }

  @PostMapping(path = "doctor", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<DoctorBean> createDoctor(@RequestBody DoctorBean doctorBean) {
    Doctor doctor = consultationManager.createDoctor(doctorBean.getFirstName(), doctorBean.getLastName());
    return ResponseEntity.ok(new DoctorBean(doctor.firstName(), doctor.lastName()));
  }

  @DeleteMapping(path = "doctor", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> deleteDoctor(@RequestBody DoctorBean doctorBean) {
    consultationManager.deleteDoctor(doctorBean.getFirstName(), doctorBean.getLastName());
    return ResponseEntity.ok("Doctor deleted successfully");
  }

  @GetMapping(path = "doctor", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DoctorBean>> getDoctors() {
    List<Doctor> doctorList = consultationManager.getDoctorList();
    return ResponseEntity.ok(doctorList.stream().map(doctor -> {
      DoctorBean doctorBean = new DoctorBean();
      doctorBean.setFirstName(doctor.firstName());
      doctorBean.setLastName(doctor.lastName());
      return doctorBean;
    }).toList());
  }

  @PutMapping(path = "doctor/{doctorFirstName}/{doctorLastName}/patient/{patientFirstName}/{patientLastName}/assign", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<String> assignPatientToDoctor(@PathVariable("doctorFirstName") String doctorFirstName,
                                                      @PathVariable("doctorLastName") String doctorLastName,
                                                      @PathVariable("patientFirstName") String patientFirstName,
                                                      @PathVariable("patientLastName") String patientLastName) throws DataNotFoundException {
    Doctor doctor = consultationManager.getDoctorByFirstAndLastName(doctorFirstName, doctorLastName);
    Patient patient = consultationManager.getPatientByFirstAndLastName(patientFirstName, patientLastName);
    consultationManager.addPatientToDoctor(patient, doctor);
    return ResponseEntity.ok("Patient assigned to a doctor");
  }

  @GetMapping(path = "doctor/{doctorFirstName}/{doctorLastName}/patients", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<List<PatientBean>> getPatientAssignedToADoctor(@PathVariable("doctorFirstName") String doctorFirstName,
                                                                       @PathVariable("doctorLastName") String doctorLastName)
      throws DataNotFoundException {
    List<PatientBean> patientBeanList = new ArrayList<>();
    Doctor doctor = consultationManager.getDoctorByFirstAndLastName(doctorFirstName, doctorLastName);
    for (Patient patient : doctor.getPatients()) {
      PatientBean patientBean = new PatientBean();
      patientBean.setFirstName(patient.firstName());
      patientBean.setLastName(patient.lastName());
      patientBeanList.add(patientBean);
    }
    return ResponseEntity.ok(patientBeanList);
  }

  @PostMapping(path = "consultation/{consultationName}/patient/{patientFirstName}/{patientLastName}/add", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
  public ResponseEntity<ConsultationBean> addConsultationForPatient(@PathVariable("consultationName") String consultationName,
                                                                    @PathVariable("patientFirstName") String patientFirstName,
                                                                    @PathVariable("patientLastName") String patientLastName,
                                                                    @RequestBody DoctorBean doctorBean) throws DataNotFoundException {
    Doctor doctor = consultationManager.getDoctorByFirstAndLastName(doctorBean.getFirstName(), doctorBean.getLastName());
    Patient patient = consultationManager.getPatientByFirstAndLastName(patientFirstName, patientLastName);
    Consultation consultation = consultationManager.createConsultation(patient, doctor, consultationName);
    ConsultationBean consultationBean = new ConsultationBean();
    consultationBean.setConsultationName(consultation.consultationName());
    return ResponseEntity.ok(consultationBean);
  }
}
