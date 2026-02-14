package com.demo.consultation.db.service;

import com.demo.consultation.db.model.ConsultationEntity;
import com.demo.consultation.db.model.DoctorEntity;
import com.demo.consultation.db.model.EntityNotFoundException;
import com.demo.consultation.db.model.PatientEntity;
import com.demo.consultation.domain.*;

import java.util.ArrayList;
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
    return new Patient(new PatientId(firstName, lastName));
  }

  @Override
  public List<Patient> getPatientList() {
    return dataService.listPatients().stream().map(entity -> new Patient(new PatientId(entity.getFirstName(), entity.getLastName())))
        .collect(Collectors.toList());
  }

  @Override
  public List<Doctor> getDoctorList() {
    return dataService.listDoctors().stream().map(entity -> new Doctor(entity.getFirstName(), entity.getLastName()))
        .collect(Collectors.toList());
  }

  @Override
  public void save(Doctor doctor) throws DataNotFoundException {
    try {
      DoctorEntity doctorEntity = dataService.getDoctorByFirstAndLastName(doctor.doctorId().firstName(), doctor.doctorId().lastName());
      List<Patient> patients = doctor.getPatients();
      List<PatientEntity> patientEntities = doctorEntity.getPatients();
      if (patientEntities == null) {
        patientEntities = new ArrayList<>();
      }
      List<PatientId> currentPatientIds = patientEntities.stream().map(entity -> new PatientId(entity.getFirstName(), entity.getLastName())).toList();
      List<PatientId> toAdd = new ArrayList<>();
      for (Patient patient : patients) {
        if (!currentPatientIds.contains(patient.patientId())) {
          toAdd.add(patient.patientId());
        }
      }
      List<PatientId> toRemove = new ArrayList<>();
      for (PatientId patientId : currentPatientIds) {
        if (patients.stream().noneMatch(patient -> patient.patientId().equals(patientId))) {
          toRemove.add(patientId);
        }
      }
      for (PatientId patientId : toAdd) {
        PatientEntity patientEntity = dataService.getPatientByFirstAndLastName(patientId.firstName(), patientId.lastName());
        patientEntities.add(patientEntity);
      }
      for (PatientId patientId : toRemove) {
        PatientEntity patientEntity = dataService.getPatientByFirstAndLastName(patientId.firstName(), patientId.lastName());
        patientEntities.remove(patientEntity);
      }
      dataService.save(doctorEntity);
    } catch (EntityNotFoundException e) {
      throw new DataNotFoundException(e.getMessage(), e);
    }
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
      consultationEntity.setPatient(dataService.getPatientByFirstAndLastName(patient.patientId().firstName(), patient.patientId().lastName()));
      consultationEntity.setDoctor(dataService.getDoctorByFirstAndLastName(doctor.doctorId().firstName(), doctor.doctorId().lastName()));
      dataService.save(consultationEntity);
      return new Consultation(new ConsultationId(consultationName), patient, doctor);
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
          doctor.addPatient(new Patient(new PatientId(patientEntity.getFirstName(), patientEntity.getLastName())));
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
      return new Patient(new PatientId(patientEntity.getFirstName(), patientEntity.getLastName()));
    } catch (EntityNotFoundException e) {
      throw new DataNotFoundException(e.getMessage(), e);
    }
  }

  @Override
  public List<Consultation> getConsultations() {
    return dataService.listConsultations().stream().map(entity -> {
      PatientEntity patientEntity = entity.getPatient();
      DoctorEntity doctorEntity = entity.getDoctor();
      Patient patient = new Patient(new PatientId(patientEntity.getFirstName(), patientEntity.getLastName()));
      Doctor doctor = new Doctor(doctorEntity.getFirstName(), doctorEntity.getLastName());
      return new Consultation(new ConsultationId(entity.getConsultationName()), patient, doctor);
    }).collect(Collectors.toList());
  }
}
