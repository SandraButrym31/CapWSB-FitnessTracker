package com.capgemini.wsb.fitnesstracker.notification.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.training.internal.TrainingServiceImpl;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserServiceImpl;
import lombok.Data;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@EnableScheduling
@Data
public class ReportService {
    private final UserServiceImpl userProvider;
    private final TrainingServiceImpl trainingProvider;
    private final EmailSender emailSender;
    private boolean isReportGenerated = false;


    @Scheduled(fixedRate = 1209600000)
    public void generateReport(){
        final List<User> userList = userProvider.findAllUsers();
        for (User user : userList){
            emailSender.sendEmail(user.getEmail(), trainingProvider.getAllTrainingsByUserId(user.getId()));
        }
    }
}