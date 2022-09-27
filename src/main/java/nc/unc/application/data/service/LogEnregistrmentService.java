package nc.unc.application.data.service;

import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.server.VaadinSession;
import nc.unc.application.data.entity.LogEnregistrement;
import nc.unc.application.data.entity.User;
import nc.unc.application.data.enums.TypeCrud;
import nc.unc.application.data.repository.LogEnregistrementRepository;
import org.springframework.stereotype.Service;
import nc.unc.application.security.AuthenticatedUser;

import java.util.*;

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

  /**
   * Définition d'un log de sauvegarde d'un élément
   * @param description_log la description du log, correspondant aux information de l'élément ajouté (son toString)
   */
  public void saveLogAjoutString(String description_log) {
    LogEnregistrement logEnregistrement = new LogEnregistrement();
    logEnregistrement.setDescription_log(description_log);
    logEnregistrement.setTypeCrud(TypeCrud.AJOUT);

    setExecutant(logEnregistrement);
    saveLog(logEnregistrement);
  }

  /**
   * définition d'un log dans le cas d'une modification d'élément
   * @param oldValues anciennes valeurs de l'élément
   * @param newValues nouvelles valeurs de l'élément
   */
  public void saveLogEditString(String oldValues, String newValues) {
    LogEnregistrement logEnregistrement = new LogEnregistrement();
    logEnregistrement.setDescription_log(
            "ANCIENNES VALEURS : \n" + oldValues + "\n\uD83C\uDD95 REMPLACÉES PAR : \n" + newValues
    );
    logEnregistrement.setTypeCrud(TypeCrud.MODIFICATION);

    setExecutant(logEnregistrement);
    saveLog(logEnregistrement);
  }

  /**
   * Définition d'un log dans le cas de la suppression d'un élément
   * @param description_log
   */
  public void saveLogDeleteString(String description_log) {
    LogEnregistrement logEnregistrement = new LogEnregistrement();
    logEnregistrement.setDescription_log(description_log);
    logEnregistrement.setTypeCrud(TypeCrud.SUPPRESSION);

    setExecutant(logEnregistrement);
    saveLog(logEnregistrement);
  }

  /**
   * Sauvegarde d'un log
   * @param logEnregistrement
   */
  public void saveLog(LogEnregistrement logEnregistrement) {
    if (logEnregistrement == null) {
      System.err.println("L'enregistrement est nul...");
      return;
    }
    logEnregistrementRepository.save(logEnregistrement);
  }

  /**
   * Défini qui est la personne qui a effectuée l'action provoquant l'enregistrement d'un log
   * @param logEnregistrement
   */
  public void setExecutant(LogEnregistrement logEnregistrement) {
    Optional<User> maybeUser = authenticatedUser.get();
    if (maybeUser.isPresent()) {
      User user = maybeUser.get();
      logEnregistrement.setExecutant(user.getNom() + " " + user.getPrenom());
    }
  }

  public void deleteAll(List<LogEnregistrement> logs){
    logEnregistrementRepository.deleteAll(logs);
  }

  /**
   * Suppression des anciens logs
   */
  public void deleteAncienLogs(){

      //Récupérer le mois actuel
      Calendar cal = new GregorianCalendar();
      cal.setTime(new Date());
      int moisactuel = cal.get(Calendar.MONTH);

      //Récupérer les logs dont le mois de la date de création est inférieur au mois actuel
      List<LogEnregistrement> logs = findAllLogs(null);
      List<LogEnregistrement> logsASupprimer = new ArrayList<>();
      if(logs.size() > 0){
        for(LogEnregistrement log : logs){
          int mois = log.getCreatedAt().getMonthValue();
          if(mois < moisactuel){
            logsASupprimer.add(log);
          }
        }
      }
      //Suppresion de la liste des logs
      if(logsASupprimer.size() > 0){
        deleteAll(logsASupprimer);
      }
    }

}
