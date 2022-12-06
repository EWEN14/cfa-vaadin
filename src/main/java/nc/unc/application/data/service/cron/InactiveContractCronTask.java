package nc.unc.application.data.service.cron;

import lombok.extern.slf4j.Slf4j;
import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.enums.StatutActifAutres;
import nc.unc.application.data.repository.ContratRepository;
import nc.unc.application.data.repository.EntrepriseRepository;
import nc.unc.application.data.repository.EtudiantRepository;
import nc.unc.application.data.repository.TuteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@Transactional
@Slf4j
public class InactiveContractCronTask {

  @Autowired
  private EtudiantRepository etudiantRepository;

  @Autowired
  private ContratRepository contratRepository;

  @Autowired
  private TuteurRepository tuteurRepository;

  @Scheduled(fixedRate = 5000)
  public void miseAjourStatutActif() {

    // Récupération de tous les contrats avec le statut ACTIF
    List<Contrat> contrats = contratRepository.findAllByStatutActif(StatutActifAutres.ACTIF.getEnumStringify());

    // On fait un stream sur lequel on va filtrer pour ne garder que les contrats dont la date de fin est antérieure
    // à la date du jour.
    contrats.stream().filter(contrat -> contrat.getFinContrat().isBefore(LocalDate.now()))
            .forEach(contrat -> {
              // Pour chaque contrat restant, on passe le contrat et l'étudiant en statut INACTIF
              contratRepository.updateActiveContract(contrat.getId(), StatutActifAutres.INACTIF.getEnumStringify(), LocalDateTime.now());
              log.info("Mise a jour du statut (ACTIF -> INACTIF) du contrat {}", contrat.getCodeContrat());
              etudiantRepository.updateStatusOfEtudiant(contrat.getEtudiant().getId(), StatutActifAutres.INACTIF.getEnumStringify(), LocalDateTime.now());
              log.info("Mise a jour du statut (ACTIF -> INACTIF) de l'étudiant lié au contrat {}, ayant l'identifiant : {}", contrat.getCodeContrat(), contrat.getEtudiant().getId());
            });

    // On récupère la liste des tuteurs encore ACTIF
    List<Tuteur> tuteurList = tuteurRepository.findAllByStatutActif(StatutActifAutres.ACTIF.getEnumStringify());

    for (Tuteur tuteur : tuteurList) {
      // Pour chaque tuteur, on tente de récupérer la liste des contrats ACTIF ayant pour tuteur celui de l'itération en cours
      List<Contrat> contratList = contratRepository.findAllByTuteurIdAndStatutActif(tuteur.getId(), StatutActifAutres.ACTIF.getEnumStringify());
      // Si la liste est vide, cela veut dire que tous les contrats auxquels était lié le tuteur sont désormais INACTIF
      if (contratList == null) {
        // Par conséquent, on rend le tuteur INACTIF à son tour
        tuteurRepository.updateStatusOfTuteur(tuteur.getId(), StatutActifAutres.INACTIF.getEnumStringify(), LocalDateTime.now());
        log.info("Mise a jour du statut (ACTIF -> INACTIF) du tuteur {}", tuteur.getId());
      }
    }

    // TODO : Ajouter la mise a jours du status des entreprises.
  }
}
