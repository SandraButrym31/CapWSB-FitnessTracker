package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.Training;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDtoId;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingDto;
import com.capgemini.wsb.fitnesstracker.user.api.UserServiceImpl;
import com.capgemini.wsb.fitnesstracker.user.internal.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingMapper {
    private final UserMapper userMapper;
    private final UserServiceImpl userProvider;
    TrainingDto toTraining(Training training) {
        return new TrainingDto(training.getId(),
                userMapper.toDto(training.getUser()),
                training.getStartTime(),
                training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed());
    }

    Training toEntity(TrainingDtoId trainingTO) {
        return new Training(
                trainingTO.getStartTime(),
                trainingTO.getEndTime(),
                trainingTO.getActivityType(),
                trainingTO.getDistance(),
                trainingTO.getAverageSpeed());
    }

    Training toEntityUpdate(TrainingDtoId trainingDtoId) {
        return new Training (trainingDtoId.getId(),
                userProvider.getUser(trainingDtoId.getUserId()).get(),
                trainingDtoId.getStartTime(),
                trainingDtoId.getEndTime(),
                trainingDtoId.getActivityType(),
                trainingDtoId.getDistance(),
                trainingDtoId.getAverageSpeed());
    }
}