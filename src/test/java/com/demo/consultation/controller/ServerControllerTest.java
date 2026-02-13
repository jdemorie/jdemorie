package com.demo.consultation.controller;

import com.demo.consultation.GlobalConsultationTest;
import com.demo.consultation.application.ServerApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ServerApplication.class)
public class ServerControllerTest extends GlobalConsultationTest {
  @Autowired
  private WebApplicationContext wac;

  @BeforeEach
  public void setUp() throws Exception {
    scenario = new ServerConsultationManagerScenario(wac);
    scenario.givenADocManager()
        .whenICleanAllData()
        .thenTheListOfPatientsStoredShouldBe();
  }
}
