package com.capgemini.wsb.fitnesstracker.mail.internal;

import com.capgemini.wsb.fitnesstracker.mail.api.EmailDto;
import com.capgemini.wsb.fitnesstracker.mail.api.EmailSender;
import com.capgemini.wsb.fitnesstracker.training.api.Training;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static java.util.Calendar.DAY_OF_MONTH;

@Service
@RequiredArgsConstructor
public class EmailSenderImpl implements EmailSender {
    private final JavaMailSender javaMailSender;

    @Override
    public void send(EmailDto email) {
        final SimpleMailMessage simpleEmail = new SimpleMailMessage();
        simpleEmail.setTo(email.toAddress());
        simpleEmail.setSubject(email.subject());
        simpleEmail.setText(email.content());
        javaMailSender.send(simpleEmail);
    }

    @Override
    public void sendEmail(String user, List<Training> trainingList) {
        send(createEmail(user, trainingList));
    }

    private EmailDto createEmail(final String user, final List<Training> trainingList){
        final List<Training> trainings = trainingList.stream().filter(training -> training.getStartTime().after(returnBeginningOfLastWeek()) && training.getStartTime().before(returnYesterday())).toList();
        final StringBuilder builder = new StringBuilder("""
                %s trainings completed last week,
                accumulating %s units of distance
                You've finished %s trainings so far.
                Last week's trainings can be found below:
                ----
                """.formatted(trainings.size(),
                trainings.isEmpty() ? 0 : trainings.stream().mapToDouble(Training::getDistance).sum(),
                trainings.size()
        ));
        trainings.forEach(training -> builder.append("""
            Training start: %s
            Training end: %s
            Activity type: %s
            Distance: %s
            Average speed: %s
            ----
            """.formatted(training.getStartTime(),
                training.getEndTime() == null ? "-" : training.getEndTime(),
                training.getActivityType(),
                training.getDistance(),
                training.getAverageSpeed()
        )));
        System.out.println(builder); //task requirement
        return new EmailDto(user, "Last week's trainings", builder.toString());
    }

    private Date returnBeginningOfLastWeek() {
        final Calendar now = Calendar.getInstance();
        now.add(DAY_OF_MONTH, -7);
        return now.getTime();
    }

    private Date returnYesterday() {
        final Calendar now = Calendar.getInstance();
        now.add(DAY_OF_MONTH, -1);
        return now.getTime();
    }
}