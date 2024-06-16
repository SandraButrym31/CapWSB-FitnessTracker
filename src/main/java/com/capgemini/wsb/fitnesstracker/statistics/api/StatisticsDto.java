package com.capgemini.wsb.fitnesstracker.statistics.api;

import com.capgemini.wsb.fitnesstracker.user.api.UserDto;
import lombok.*;

@Data
@Getter
@Setter
public class StatisticsDto {

    private Long id;
    private UserDto user;
    private int totalTrainings;
    private double totalDistance;
    private int totalCaloriesBurned;

    public StatisticsDto(Long id, com.capgemini.wsb.fitnesstracker.user.internal.UserDto dto, int totalTrainings, double totalDistance, int totalCaloriesBurned) {
    }
}
