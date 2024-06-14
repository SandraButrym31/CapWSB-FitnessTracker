package com.capgemini.wsb.fitnesstracker.statistics.internal;

import com.capgemini.wsb.fitnesstracker.statistics.api.Statistics;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsDto;
import com.capgemini.wsb.fitnesstracker.statistics.api.StatisticsDtoId;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import com.capgemini.wsb.fitnesstracker.user.internal.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StatisticsMapper {
    private final UserMapper userMapper;
    private final UserProvider userProvider;
    StatisticsDto toDto(Statistics statistics){
        return new StatisticsDto(statistics.getId(),
                userMapper.toDto(statistics.getUser()),
                statistics.getTotalTrainings(),
                statistics.getTotalDistance(),
                statistics.getTotalCaloriesBurned());
    }

    Statistics toEntity(StatisticsDtoId statisticsDtoId){
        return new Statistics(statisticsDtoId.getId(),
                userProvider.getUser(statisticsDtoId.getUserId()),
                statisticsDtoId.getTotalTrainings(),
                statisticsDtoId.getTotalDistance(),
                statisticsDtoId.getTotalCaloriesBurned());
    }
}
