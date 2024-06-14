package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsDtoId;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingMapper;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingRepository;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl {
    private final TrainingRepository trainingRepository;
    private final UserProvider userProvider;
    private final TrainingMapper trainingMapper;
    private final StatisticsMapper statisticsMapper;
    private final StatisticsRepository statisticsRepository;

    public List<Statistics> getStatistics(){
        return statisticsRepository.findAll();
    }

    public List<Statistics> getUserStatistics(Long userId){
        return statisticsRepository.findByUserId(userId);
    }

    public Statistics createStatisticsEntity(StatisticsDtoId statisticsDtoId){
        User user = userProvider.getUser(statisticsDtoId.getUserId()).get();
        Statistics statistics = statisticsMapper.toEntity(statisticsDtoId);
        statistics.setUser(user);
        statisticsRepository.save(statistics);
        return statistics;
    }

    public void deleteById(Long statisticsId){
        statisticsRepository.deleteById(statisticsId);
    }
}
