package nc.unc.application.data.service.cron;

import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.enums.SituationContrat;
import nc.unc.application.data.repository.ContratRepository;
import nc.unc.application.data.repository.EtudiantRepository;
import nc.unc.application.data.repository.TuteurRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@Transactional
public class InactiveContractCronTask {
    private static final Logger log = LoggerFactory.getLogger(InactiveContractCronTask.class);

    @Autowired
    private EtudiantRepository etudiantRepository;

    @Autowired
    private ContratRepository contratRepository;

    @Autowired
    private TuteurRepository tuteurRepository;

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {

        String choix = SituationContrat.INACTIF;
        List<Contrat> contrats = contratRepository.findAll();

        contrats.stream().filter(contrat -> contrat.getFinContrat().getDayOfMonth() <= LocalDate.now().getDayOfMonth())
                .forEach(contrat -> {
                    contratRepository.updateActiveContract(contrat.getId(), choix);
                    etudiantRepository.updateStatusOfEtudiant(contrat.getEtudiant().getId(),choix);
                    tuteurRepository.updateStatusOfTuteur(contrat.getTuteur().getId(),choix);
                });
    }
}