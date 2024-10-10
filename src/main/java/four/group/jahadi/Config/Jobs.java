package four.group.jahadi.Config;

import four.group.jahadi.Repository.ActivationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import static four.group.jahadi.Utility.StaticValues.SMS_RESEND_MSEC;

@Configuration
@EnableScheduling
public class Jobs {

    @Autowired
    ActivationRepository activationRepository;

    @Scheduled(fixedRate = 600000, initialDelay = 5000)
    public void scheduleFixedRateTask() {
        activationRepository.deleteExpired(System.currentTimeMillis() - SMS_RESEND_MSEC);
    }

}
