package com.demo.consultation.domain;

import com.demo.consultation.GlobalConsultationTest;
import org.junit.jupiter.api.BeforeEach;

public class ConsultationManagerTest extends GlobalConsultationTest {
  @BeforeEach
  public void setUp() throws Exception {
    scenario = new ConsultationManagerScenario();
    scenario.givenADocManager()
        .thenTheListOfDoctorsStoredShouldBe()
        .thenTheListOfPatientsStoredShouldBe()
        .thenTheListOfConsultationsStoredShouldBe();
  }
}
