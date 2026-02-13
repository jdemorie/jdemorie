package com.demo.consultation.db.service;

import com.demo.consultation.db.model.ConsultationEntity;
import com.demo.consultation.db.model.DoctorEntity;
import com.demo.consultation.db.model.EntityNotFoundException;
import com.demo.consultation.db.model.PatientEntity;
import com.demo.consultation.domain.*;

import java.util.List;
import java.util.stream.Collectors;

public class DatabaseProvider implements DataManager {
  private final DatabaseService dataService;

  public DatabaseProvider(DatabaseService dataService) {
    this.dataService = dataService;
  }

  @Override
  public Patient createPatient(String firstName, String lastName) {
    PatientEntity patientEntity = new PatientEntity();
    patientEntity.setFirstName(firstName);
    patientEntity.setLastName(lastName);
    dataService.save(patientEntity);
    return new Patient(firstName, lastName);
  }

  @Override
  public List<Patient> getPatientList() {
    return dataService.listPatients().stream().map(entity -> new Patient(entity.getFirstName(), entity.getLastName()))
        .collect(Collectors.toList());
  }

  @Override
  public List<Doctor> getDoctorList() {
    return dataService.listDoctors().stream().map(entity -> new Doctor(entity.getFirstName(), entity.getLastName()))
        .collect(Collectors.toList());
  }

  @Override
  public void clear() {
    dataService.clear();
  }

  @Override
  public void deletePatient(String firstName, String lastName) {
    List<PatientEntity> patientEntities = dataService.listPatients();
    for (PatientEntity patientEntity : patientEntities) {
      if (patientEntity.getFirstName().equals(firstName) && patientEntity.getLastName().equals(lastName)) {
        dataService.deletePatient(patientEntity);
        break;
      }
    }
  }

  @Override
  public Doctor createDoctor(String firstName, String lastName) {
    DoctorEntity doctorEntity = new DoctorEntity();
    doctorEntity.setFirstName(firstName);
    doctorEntity.setLastName(lastName);
    dataService.save(doctorEntity);
    return new Doctor(firstName, lastName);
  }

  @Override
  public void deleteDoctor(String firstName, String lastName) {
    List<DoctorEntity> doctorEntities = dataService.listDoctors();
    for (DoctorEntity doctorEntity : doctorEntities) {
      if (doctorEntity.getFirstName().equals(firstName) && doctorEntity.getLastName().equals(lastName)) {
        dataService.deleteDoctor(doctorEntity);
        break;
      }
    }
  }

  @Override
  public Consultation createConsultation(String consultationName, Patient patient, Doctor doctor) throws DataNotFoundException {
    try {
      ConsultationEntity consultationEntity = new ConsultationEntity();
      consultationEntity.setConsultationName(consultationName);
      consultationEntity.setPatient(dataService.getPatientByFirstAndLastName(patient.firstName(), patient.lastName()));
      consultationEntity.setDoctor(dataService.getDoctorByFirstAndLastName(doctor.firstName(), doctor.lastName()));
      dataService.save(consultationEntity);
      return new Consultation(consultationName, patient, doctor);
    } catch (EntityNotFoundException e) {
      throw new DataNotFoundException(consultationName);
    }
  }

  @Override
  public Doctor getDoctorByFirstAndLastName(String firstName, String lastName) throws DataNotFoundException {
    try {
      DoctorEntity doctorEntity = dataService.getDoctorByFirstAndLastName(firstName, lastName);
      Doctor doctor = new Doctor(doctorEntity.getFirstName(), doctorEntity.getLastName());
      List<PatientEntity> patientEntities = doctorEntity.getPatients();
      if (patientEntities != null) {
        for (PatientEntity patientEntity : patientEntities) {
          doctor.addPatient(new Patient(patientEntity.getFirstName(), patientEntity.getLastName()));
        }
      }
      return doctor;
    } catch (EntityNotFoundException e) {
      throw new DataNotFoundException(e.getMessage(), e);
    }
  }

  @Override
  public Patient getPatientByFirstAndLastName(String firstName, String lastName) throws DataNotFoundException {
    try {
      PatientEntity patientEntity = dataService.getPatientByFirstAndLastName(firstName, lastName);
      return new Patient(patientEntity.getFirstName(), patientEntity.getLastName());
    } catch (EntityNotFoundException e) {
      throw new DataNotFoundException(e.getMessage(), e);
    }
  }

  @Override
  public List<Consultation> getConsultationsByPatient(Patient patient) throws DataNotFoundException {
    try {
      PatientEntity patientEntity = dataService.getPatientByFirstAndLastName(patient.firstName(), patient.lastName());
      List<ConsultationEntity> consultationEntities = dataService.getConsultationsByPatient(patientEntity);
      return List.of();
    } catch (EntityNotFoundException e) {
      throw new DataNotFoundException(e.getMessage(), e);
    }
  }

  @Override
  public void addPatientToDoctor(Patient patient, Doctor doctor) throws DataNotFoundException {
    try {
      PatientEntity patientEntity = dataService.getPatientByFirstAndLastName(patient.firstName(), patient.lastName());
      DoctorEntity doctorEntity = dataService.getDoctorByFirstAndLastName(doctor.firstName(), doctor.lastName());
      dataService.assignPatientToDoctor(patientEntity, doctorEntity);
    } catch (EntityNotFoundException e) {
      throw new DataNotFoundException(e.getMessage(), e);
    }
  }

  @Override
  public List<Consultation> getConsultations() {
    return dataService.listConsultations().stream().map(entity -> {
      PatientEntity patientEntity = entity.getPatient();
      DoctorEntity doctorEntity = entity.getDoctor();
      Patient patient = new Patient(patientEntity.getFirstName(), patientEntity.getLastName());
      Doctor doctor = new Doctor(doctorEntity.getFirstName(), doctorEntity.getLastName());
      return new Consultation(entity.getConsultationName(), patient, doctor);
    }).collect(Collectors.toList());
  }
}
