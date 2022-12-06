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
@Transactional
@Slf4j
public class SuppressLogCronTask {

  @Autowired
  private LogEnregistrementRepository repository;

  @Scheduled(fixedRate = 5000)
  public void SuppressLogOfLastMonth() {
    // On récupère tous les logs, du plus récent au plus ancien
    List<LogEnregistrement> logs = repository.findAllByOrderByCreatedAtDesc();

    // On utilise un stream en filtrant ceux dont la date de création est inférieure à la date du jour moins un mois
    logs.stream().filter(logEnregistrement -> logEnregistrement.getCreatedAt().toLocalDate().isBefore(LocalDate.now().minusMonths(1)))
            .forEach(logEnregistrement -> {
              // Chaque log restant sera supprimé. Ce qui fait qu'on ne garde que les logs datant de moins d'un mois.
              repository.delete(logEnregistrement);
            });
  }
}
