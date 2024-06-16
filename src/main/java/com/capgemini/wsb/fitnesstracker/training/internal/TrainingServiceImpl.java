package com.capgemini.wsb.fitnesstracker.training.internal;

import com.capgemini.wsb.fitnesstracker.training.api.*;
import com.capgemini.wsb.fitnesstracker.user.api.User;

import com.capgemini.wsb.fitnesstracker.user.api.UserServiceImpl;
import com.capgemini.wsb.fitnesstracker.user.internal.UserRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class TrainingServiceImpl implements TrainingProvider {

    @Autowired
    private final TrainingRepository trainingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private final UserServiceImpl userProvider;

    @Autowired
    private final TrainingMapper trainingMapper;

    @Override
    public Optional<User> getTraining(final Long trainingId) {
        throw new UnsupportedOperationException("Not finished yet");
    }

    @Override
    public void deleteByUserId(Long userId) {
        Optional<Training> first = trainingRepository.findAll().stream()
            .filter(training -> Objects.equals(training.getUser().getId(), userId))
            .findFirst();
        first.ifPresent(trainingRepository::delete);
    }

    @Override
    public List<Training> getAllTrainings() {
        return trainingRepository.findAll();
    }

    @Override
    public List<Training> getAllTrainingsByUserId(Long userId) {
        return trainingRepository.findAllByUserId(userId);
    }

    @Override
    public List<Training> getAllFinishedTrainings(LocalDateTime afterTime) {
        return trainingRepository.findAllByEndTimeAfter(afterTime);
    }

    @Override
    public List<Training> getTrainingsByActivity(ActivityType activityType) {
        return trainingRepository.findAllByActivityType(activityType);
    }

    @Override
    public Training createTraining(TrainingDtoId trainingIdTO){
        User user = userProvider.getUser(trainingIdTO.getUserId()).get();
        Training training = trainingMapper.toEntity(trainingIdTO);
        training.setUser(user);
        trainingRepository.save(training);
        return training;
    }

    @Override
    public Training updateTraining(Long trainingId, Training updatedTraining) {
        Optional<Training> existingTrainingOpt = trainingRepository.findById(trainingId);
        if (existingTrainingOpt.isPresent()) {
            Training existingTraining = existingTrainingOpt.get();
            existingTraining.setStartTime(updatedTraining.getStartTime());
            existingTraining.setEndTime(updatedTraining.getEndTime());
            existingTraining.setActivityType(updatedTraining.getActivityType());
            existingTraining.setDistance(updatedTraining.getDistance());
            existingTraining.setAverageSpeed(updatedTraining.getAverageSpeed());
            return trainingRepository.save(existingTraining);
        } else {
            throw new TrainingNotFoundException(trainingId);
        }
    }

}


