package com.capgemini.wsb.fitnesstracker.training.api;

import com.capgemini.wsb.fitnesstracker.training.internal.ActivityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class TrainingDtoId {
    private Long id;
    private Long userId;
    private Date startTime;
    private Date endTime;
    private ActivityType activityType;
    private Double distance;
    private Double averageSpeed;
}