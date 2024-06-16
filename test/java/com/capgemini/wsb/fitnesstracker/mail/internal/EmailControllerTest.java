package com.capgemini.wsb.fitnesstracker.mail.internal;

import static java.time.LocalDate.now;
import static java.util.UUID.randomUUID;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.capgemini.wsb.fitnesstracker.IntegrationTest;
import com.capgemini.wsb.fitnesstracker.IntegrationTestBase;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.notification.internal.ReportService;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingServiceImpl;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.internal.UserServiceImpl;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@IntegrationTest
class ReportControllerTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @SpyBean
  private UserServiceImpl userService;

  @SpyBean
  private TrainingServiceImpl trainingService;

  @SpyBean
  private EmailSenderImpl emailSender;

  @InjectMocks
  @Autowired
  private ReportService reportService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void shouldGenerateAndSendReports_whenGeneratingReports() throws Exception {
    User user = existingUser(generateClient());
    Training training = persistTraining(generateTraining(user));

    List<Training> trainings = Collections.singletonList(training);

    doReturn(Collections.singletonList(user)).when(userService).findAllUsers();
    doReturn(trainings).when(trainingService).getAllTrainingsByUserId(user.getId());

    mockMvc.perform(get("/api/reports/generate-and-send")
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(log())
        .andExpect(status().isOk());

    verify(userService, times(2)).findAllUsers();
    verify(trainingService, times(2)).getAllTrainingsByUserId(1L);
    verify(emailSender, times(2)).sendEmail(user.getEmail(), trainings);
  }

  private static User generateClient() {
    return new User(randomUUID().toString(), randomUUID().toString(), now(),
        "test@example.com");
  }

  private static Training generateTraining(User user) throws ParseException {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    return new Training(
        user,
        sdf.parse("2024-06-10 08:00:00"),
        sdf.parse("2024-01-10 09:30:00"),
        ActivityType.RUNNING,
        10.5,
        8.2);
  }
}