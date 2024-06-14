package com.capgemini.wsb.fitnesstracker.statistics.api;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatisticsDtoId {

    private Long id;
    private Long userId;
    private int totalTrainings;
    private double totalDistance;
    private int totalCaloriesBurned;
}