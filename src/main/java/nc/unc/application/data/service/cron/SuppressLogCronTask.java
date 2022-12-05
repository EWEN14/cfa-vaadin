package nc.unc.application.data.service.cron;

import lombok.extern.slf4j.Slf4j;
import nc.unc.application.data.entity.LogEnregistrement;
import nc.unc.application.data.repository.LogEnregistrementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
@Transactional
public class SuppressLogCronTask {

    @Autowired
    private LogEnregistrementRepository repository;

    @Scheduled(fixedRate = 5000)
    public void SuppressLogOfLastMonth(){
        List<LogEnregistrement> logs = repository.findAllByOrderByCreatedAtDesc();

        logs.stream().filter(logEnregistrement -> logEnregistrement.getCreatedAt().getDayOfMonth() <= LocalDate.now().getDayOfMonth() &&
                                                    logEnregistrement.getCreatedAt().getMonthValue() <= LocalDate.now().getMonthValue() &&
                                                    logEnregistrement.getCreatedAt().getYear() <= LocalDate.now().getYear())
                .forEach(logEnregistrement -> {
                    repository.delete(logEnregistrement);
                });
    }
}
