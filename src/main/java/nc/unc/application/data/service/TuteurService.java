package nc.unc.application.data.service;

import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.entity.TuteurHabilitation;
import nc.unc.application.data.enums.StatutActifAutres;
import nc.unc.application.data.repository.TuteurHabilitationRepository;
import nc.unc.application.data.repository.TuteurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TuteurService {

    private final TuteurRepository tuteurRepository;
    private final TuteurHabilitationRepository tuteurHabilitationRepository;

    //Constructeur
    public TuteurService(TuteurRepository tuteurRepository, TuteurHabilitationRepository tuteurHabilitationRepository) {
        this.tuteurRepository = tuteurRepository;
        this.tuteurHabilitationRepository = tuteurHabilitationRepository;
    }

    // Récupérer les tuteurs
    public List<Tuteur> findAllTuteurs(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return tuteurRepository.findAllByOrderByNomTuteur();
        } else {
            return tuteurRepository.search(stringFilter);
        }
    }

    // Nombre de tuteurs
    public long countTuteurs() {
        return tuteurRepository.count();
    }

    // Supprimer un tuteur
    public void deleteTuteur(Tuteur tuteur) {
        tuteurRepository.delete(tuteur);
        // suppression des habilitations du tuteur après sa suppression
        for (TuteurHabilitation tuteurHabilitation: tuteur.getTuteurHabilitations()) {
            deleteTuteurHabilitation(tuteurHabilitation);
        }
    }

    // Sauvegarder un tuteur
    public void saveTuteur(Tuteur tuteur) {
        if (tuteur == null) {
            System.err.println("Le tuteur est nul, le formulaire est-il bien connecté à l'application ?");
            return;
        }
        if (tuteur.getStatutActif() == null) {
            tuteur.setStatutActif(StatutActifAutres.ACTIF.getEnumStringify());
        }
        tuteurRepository.save(tuteur);
    }

    // Sauvegarder une habilitation tuteur
    public void saveTuteurHabilitation(TuteurHabilitation tuteurHabilitation) {
        if (tuteurHabilitation == null) {
            System.err.println("L'habilitation est nulle, le formulaire est-il bien connecté à l'application ?");
            return;
        }
        tuteurHabilitationRepository.save(tuteurHabilitation);
    }

    // supprimer une habilitation tuteur
    public void deleteTuteurHabilitation(TuteurHabilitation tuteurHabilitation) {
        tuteurHabilitationRepository.delete(tuteurHabilitation);
    }

    /**
     * Récupère la liste des tuteurs travailant à l'entreprise dont l'id est passée en paramètre
     * @param id identifiant de l'entreprise
     * @return liste de tuteurs
     */
    public List<Tuteur> findAllTuteursByEntrepriseId(Long id) {
        return tuteurRepository.findAllByEntrepriseIdOrderByNomTuteur(id);
    }

    public List<Tuteur> findAllTuteursSansHabilitations(){
        return tuteurRepository.findAllByTuteurHabilitationsIsNullOrderByNomTuteur();
    }
}
