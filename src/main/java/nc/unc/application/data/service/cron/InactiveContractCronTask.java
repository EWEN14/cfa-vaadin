package nc.unc.application.data.service.cron;

import lombok.extern.slf4j.Slf4j;
import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.enums.SituationContrat;
import nc.unc.application.data.repository.ContratRepository;
import nc.unc.application.data.repository.EntrepriseRepository;
import nc.unc.application.data.repository.EtudiantRepository;
import nc.unc.application.data.repository.TuteurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

        List<Contrat> contrats = contratRepository.findAllByStatutActif(SituationContrat.ACTIF);

        contrats.stream().filter(contrat -> contrat.getFinContrat().getDayOfMonth() <= LocalDate.now().getDayOfMonth() &&
                                            contrat.getFinContrat().getMonthValue() <= LocalDate.now().getMonthValue() &&
                                    contrat.getFinContrat().getYear() <= LocalDate.now().getYear())
                .forEach(contrat -> {
                    contratRepository.updateActiveContract(contrat.getId(), SituationContrat.INACTIF, LocalDateTime.now());
                    log.info("Mise a jour du statut du contrat {}",contrat.getCodeContrat());
                    etudiantRepository.updateStatusOfEtudiant(contrat.getEtudiant().getId(),SituationContrat.INACTIF, LocalDateTime.now());
                    log.info("Mise a jour du statut de l'étudiant lié au contrat {}, ayant l'identifiant : {}",contrat.getCodeContrat(), contrat.getEtudiant().getId());
                });

        List<Tuteur> tuteurList = tuteurRepository.findAllByStatutActif(SituationContrat.ACTIF);

        for (Tuteur tuteur:tuteurList
             ) {
            List<Contrat> contratList = contratRepository.findAllByTuteurIdAndStatutActif(tuteur.getId(),SituationContrat.ACTIF);
            if (contratList == null){
                log.info("Mise a jour du statut du tuteur {}",tuteur.getId());
                tuteurRepository.updateStatusOfTuteur(tuteur.getId(),SituationContrat.INACTIF, LocalDateTime.now());
            }
        }

        //TODO : Ajouter la mise a jours du status des entreprises.
    }
}