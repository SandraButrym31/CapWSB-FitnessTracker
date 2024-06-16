package com.capgemini.wsb.fitnesstracker.statistics.internal;

import static java.time.LocalDate.now;
import static java.util.UUID.randomUUID;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.capgemini.wsb.fitnesstracker.IntegrationTest;
import com.capgemini.wsb.fitnesstracker.IntegrationTestBase;
import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@IntegrationTest
@Transactional
@AutoConfigureMockMvc(addFilters = false)
class StatisticsApiIntegrationTest extends IntegrationTestBase {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void shouldAddStatistics_whenCreatingNewStatistics() throws Exception {
    User user1 = existingUser(generateClient());

    String requestBody = """
        {
            "userId": "%s",
            "totalTrainings": 10,
            "totalDistance": 100.0,
            "totalCaloriesBurned": 1000
        }
        """.formatted(user1.getId());

    mockMvc.perform(post("/v1/statistics")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andDo(log())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.user.id").value(user1.getId()))
        .andExpect(jsonPath("$.totalTrainings").value(10))
        .andExpect(jsonPath("$.totalDistance").value(100.0))
        .andExpect(jsonPath("$.totalCaloriesBurned").value(1000));
  }


  @Test
  void shouldUpdateStatistics_whenUpdatingExistingStatistics() throws Exception {
    User user1 = existingUser(generateClient());
    Statistics statistics1 = persistStatistics(generateStatistics(user1));

    String requestBody = """
        {
            "userId": "%s",
            "totalTrainings": 20,
            "totalDistance": 200.0,
            "totalCaloriesBurned": 2000
        }
        """.formatted(user1.getId());

    mockMvc.perform(put("/v1/statistics/{statisticsId}", statistics1.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andDo(log())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.user.id").value(user1.getId()))
        .andExpect(jsonPath("$.totalTrainings").value(20))
        .andExpect(jsonPath("$.totalDistance").value(200.0))
        .andExpect(jsonPath("$.totalCaloriesBurned").value(2000));
  }

  @Test
  void shouldReturnStatisticsForUser_whenGettingStatisticsForUser() throws Exception {
    User user1 = existingUser(generateClient());
    Statistics statistics1 = persistStatistics(generateStatistics(user1));

    mockMvc.perform(get("/v1/statistics/{userId}", user1.getId())
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(log())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].user.id").value(user1.getId()))
        .andExpect(jsonPath("$[0].totalTrainings").value(statistics1.getTotalTrainings()))
        .andExpect(jsonPath("$[0].totalDistance").value(statistics1.getTotalDistance()))
        .andExpect(jsonPath("$[0].totalCaloriesBurned").value(statistics1.getTotalCaloriesBurned()))
        .andExpect(jsonPath("$[1]").doesNotExist());
  }

  @Test
  void shouldDeleteStatistics_whenDeletingStatisticsById() throws Exception {
    User user1 = existingUser(generateClient());
    Statistics statistics1 = persistStatistics(generateStatistics(user1));

    mockMvc.perform(delete("/v1/statistics")
            .param("statisticsId", String.valueOf(statistics1.getId()))
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(log())
        .andExpect(status().isOk());
  }

  @Test
  void shouldReturnStatisticsByCaloriesBurned_whenGettingStatisticsByCaloriesBurned()
      throws Exception {
    User user1 = existingUser(generateClient());
    Statistics statistics1 = persistStatistics(generateStatisticsWithCalories(user1, 1500));
    Statistics statistics2 = persistStatistics(generateStatisticsWithCalories(user1, 500));

    mockMvc.perform(get("/v1/statistics/calories/{caloriesBurned}", 1000)
            .contentType(MediaType.APPLICATION_JSON))
        .andDo(log())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].user.id").value(user1.getId()))
        .andExpect(jsonPath("$[0].totalTrainings").value(statistics1.getTotalTrainings()))
        .andExpect(jsonPath("$[0].totalDistance").value(statistics1.getTotalDistance()))
        .andExpect(jsonPath("$[0].totalCaloriesBurned").value(statistics1.getTotalCaloriesBurned()))
        .andExpect(jsonPath("$[1]").doesNotExist());
  }


  private static User generateClient() {
    return new User(randomUUID().toString(), randomUUID().toString(), now(),
        randomUUID().toString());
  }

  private static Statistics generateStatistics(User user) {
    return new Statistics(user, 50, 500.5, 10000);
  }

  private static Statistics generateStatisticsWithCalories(User user, int caloriesBurned) {
    return new Statistics(user, 50, 500.5, caloriesBurned);
  }

}
