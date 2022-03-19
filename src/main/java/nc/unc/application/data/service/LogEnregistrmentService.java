package nc.unc.application.data.service;

import nc.unc.application.data.entity.LogEnregistrement;
import nc.unc.application.data.enums.TypeCrud;
import nc.unc.application.data.repository.LogEnregistrementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LogEnregistrmentService {

  private final LogEnregistrementRepository logEnregistrementRepository;

  public LogEnregistrmentService(LogEnregistrementRepository logEnregistrementRepository) {
    this.logEnregistrementRepository = logEnregistrementRepository;
  }

  public List<LogEnregistrement> findAllLogs(String stringFilter) {
    if (stringFilter == null || stringFilter.isEmpty()) {
      return logEnregistrementRepository.findAll();
    } else {
      return logEnregistrementRepository.search(stringFilter);
    }
  }

  public long countLogs() {
    return logEnregistrementRepository.count();
  }

  public void deleteLog(LogEnregistrement logEnregistrement) {
    logEnregistrementRepository.delete(logEnregistrement);
  }

  public void saveLogString(String description_log, TypeCrud typeCrud) {
    LogEnregistrement logEnregistrement = new LogEnregistrement();
    logEnregistrement.setDescription_log(description_log);
    logEnregistrement.setTypeCrud(typeCrud);
    saveLog(logEnregistrement);
  }

  public void saveLog(LogEnregistrement logEnregistrement) {
    if (logEnregistrement == null) {
      System.err.println("L'enregistrement est nul...");
      return;
    }
    logEnregistrementRepository.save(logEnregistrement);
  }
}
