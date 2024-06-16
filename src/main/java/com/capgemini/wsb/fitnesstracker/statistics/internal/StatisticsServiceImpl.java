package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsDtoId;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl {
    private final StatisticsMapper statisticsMapper;
    private final StatisticsRepository statisticsRepository;

    public List<Statistics> getStatistics(){
        return statisticsRepository.findAll();
    }

    public List<Statistics> getUserStatistics(Long userId){
        return statisticsRepository.findByUserId(userId);
    }

    public Statistics createStatisticsEntity(StatisticsDtoId statisticsDtoId) {
        Statistics entity = statisticsMapper.toEntity(statisticsDtoId);

        return statisticsRepository.save(entity);
    }

    public void deleteById(Long statisticsId){
        statisticsRepository.deleteById(statisticsId);
    }

    public List<Statistics> getStatisticsByCaloriesBurned(int caloriesBurned){
        return statisticsRepository.findByTotalCaloriesBurnedGreaterThan(caloriesBurned);
    }
}
