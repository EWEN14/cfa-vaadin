package nc.unc.application.data.service;

import com.vaadin.flow.server.VaadinSession;
import nc.unc.application.data.entity.LogEnregistrement;
import nc.unc.application.data.entity.User;
import nc.unc.application.data.enums.TypeCrud;
import nc.unc.application.data.repository.LogEnregistrementRepository;
import org.springframework.stereotype.Service;
import nc.unc.application.security.AuthenticatedUser;

import java.util.List;
import java.util.Optional;

@Service
public class LogEnregistrmentService {

  private final LogEnregistrementRepository logEnregistrementRepository;
  private final AuthenticatedUser authenticatedUser;

  public LogEnregistrmentService(LogEnregistrementRepository logEnregistrementRepository, AuthenticatedUser authenticatedUser) {
    this.logEnregistrementRepository = logEnregistrementRepository;
    this.authenticatedUser = authenticatedUser;
  }

  public List<LogEnregistrement> findAllLogs(String stringFilter) {
    if (stringFilter == null || stringFilter.isEmpty()) {
      return logEnregistrementRepository.findAllByOrderByCreatedAtDesc();
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

  public void saveLogAjoutString(String description_log) {
    LogEnregistrement logEnregistrement = new LogEnregistrement();
    logEnregistrement.setDescription_log(description_log);
    logEnregistrement.setTypeCrud(TypeCrud.AJOUT);

    setExecutant(logEnregistrement);
    saveLog(logEnregistrement);
  }

  public void saveLogEditString(String oldValues, String newValues) {
    LogEnregistrement logEnregistrement = new LogEnregistrement();
    logEnregistrement.setDescription_log(
            "ANCIENNES VALEURS : \n" + oldValues + "\n\uD83C\uDD95 REMPLACÉES PAR : \n" + newValues
    );
    logEnregistrement.setTypeCrud(TypeCrud.MODIFICATION);

    setExecutant(logEnregistrement);
    saveLog(logEnregistrement);
  }

  public void saveLogDeleteString(String description_log) {
    LogEnregistrement logEnregistrement = new LogEnregistrement();
    logEnregistrement.setDescription_log(description_log);
    logEnregistrement.setTypeCrud(TypeCrud.SUPPRESSION);

    setExecutant(logEnregistrement);
    saveLog(logEnregistrement);
  }

  public void saveLog(LogEnregistrement logEnregistrement) {
    if (logEnregistrement == null) {
      System.err.println("L'enregistrement est nul...");
      return;
    }
    logEnregistrementRepository.save(logEnregistrement);
  }

  public void setExecutant(LogEnregistrement logEnregistrement) {
    Optional<User> maybeUser = authenticatedUser.get();
    if (maybeUser.isPresent()) {
      User user = maybeUser.get();
      logEnregistrement.setExecutant(user.getNom() + " " + user.getPrenom());
    }
  }
}
