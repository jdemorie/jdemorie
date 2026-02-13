package com.demo.consultation.db.service;

import com.demo.consultation.db.model.*;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService {
  private final PatientRepository patientRepository;
  private final DoctorRepository doctorRepository;
  private final ConsultationRepository consultationRepository;

  public DatabaseService(PatientRepository patientRepository, DoctorRepository doctorRepository, ConsultationRepository consultationRepository) {
    this.patientRepository = patientRepository;
    this.doctorRepository = doctorRepository;
    this.consultationRepository = consultationRepository;
  }

  public List<PatientEntity> listPatients() {
    Iterable<PatientEntity> iterable = patientRepository.findAll();
    List<PatientEntity> bookEntities = new ArrayList<>();
    iterable.forEach(bookEntities::add);
    return bookEntities;
  }

  public List<DoctorEntity> listDoctors() {
    Iterable<DoctorEntity> iterable = doctorRepository.findAll();
    List<DoctorEntity> userEntities = new ArrayList<>();
    iterable.forEach(userEntities::add);
    return userEntities;
  }

  public List<ConsultationEntity> listConsultations() {
    return consultationRepository.findAll();
  }

  public PatientEntity save(PatientEntity patient) {
    return patientRepository.save(patient);
  }

  public DoctorEntity save(DoctorEntity doctor) {
    return doctorRepository.save(doctor);
  }

  public ConsultationEntity save(ConsultationEntity consultation) {
    return consultationRepository.save(consultation);
  }

  public void deletePatient(PatientEntity patient) {
    patientRepository.delete(patient);
  }

  public void deleteDoctor(DoctorEntity doctor) {
    doctorRepository.delete(doctor);
  }

  public void deleteConsultation(ConsultationEntity consultation) {
    consultationRepository.delete(consultation);
  }

  public PatientEntity getPatientByFirstAndLastName(String firstName, String lastName) throws EntityNotFoundException {
    PatientEntity patientEntity = new PatientEntity();
    patientEntity.setFirstName(firstName);
    patientEntity.setLastName(lastName);
    Example<PatientEntity> example = Example.of(patientEntity);
    List<PatientEntity> patientEntities = patientRepository.findAll(example);
    if (patientEntities.isEmpty()) {
      throw new EntityNotFoundException("Patient not found with first name: " + firstName + " and last name: " + lastName);
    }
    if (patientEntities.size() > 1) {
      throw new EntityNotFoundException("Multiple patients found with first name: " + firstName + " and last name: " + lastName);
    }
    return patientEntities.getFirst();
  }

  public DoctorEntity getDoctorByFirstAndLastName(String firstName, String lastName) throws EntityNotFoundException {
    DoctorEntity doctorEntity = new DoctorEntity();
    doctorEntity.setFirstName(firstName);
    doctorEntity.setLastName(lastName);
    Example<DoctorEntity> example = Example.of(doctorEntity);
    List<DoctorEntity> doctorEntities = doctorRepository.findAll(example);
    if (doctorEntities.isEmpty()) {
      throw new EntityNotFoundException("Doctor not found with first name: " + firstName + " and last name: " + lastName);
    }
    if (doctorEntities.size() > 1) {
      throw new EntityNotFoundException("Multiple doctors found with first name: " + firstName + " and last name: " + lastName);
    }
    return doctorEntities.getFirst();
  }

  public void assignPatientToDoctor(PatientEntity patient, DoctorEntity doctor) throws EntityNotFoundException {
    if (doctor.getPatients() == null) {
      doctor.setPatients(new ArrayList<>());
    }
    doctor.getPatients().add(patient);
    patient.setDoctor(doctor);
    doctorRepository.save(doctor);
    patientRepository.save(patient);
  }

  public List<ConsultationEntity> getConsultationsByPatient(PatientEntity patient) {
    ConsultationEntity consultationEntity = new ConsultationEntity();
    consultationEntity.setPatient(patient);
    Example<ConsultationEntity> example = Example.of(consultationEntity);
    return consultationRepository.findAll(example);
  }

  public void clear() {
    consultationRepository.deleteAll();
    patientRepository.deleteAll();
    doctorRepository.deleteAll();
  }
}
