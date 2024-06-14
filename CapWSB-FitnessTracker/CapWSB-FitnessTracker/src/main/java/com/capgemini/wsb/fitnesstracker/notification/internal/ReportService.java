package com.capgemini.wsb.fitnesstracker.notification.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.TrainingProvider;
import com.capgemini.wsb.fitnesstracker.user.api.User;
import com.capgemini.wsb.fitnesstracker.user.api.UserProvider;
import lombok.Data;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@EnableScheduling
@Data
public class ReportService {
    private final UserProvider userProvider;
    private final TrainingProvider trainingProvider;
    private final EmailSender emailSender;

    @Scheduled(fixedRate = 1209600000)
    public void generateReport(){
        final List<User> userList = userProvider.findAllUsers();
        for (User user : userList){
            emailSender.sendEmail(user.getEmail(), trainingProvider.getAllTrainingsByUserId(user.getId()));
        }
    }
}