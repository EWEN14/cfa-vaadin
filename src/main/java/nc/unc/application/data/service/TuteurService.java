package nc.unc.application.data.service;

import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.entity.TuteurHabilitation;
import nc.unc.application.data.repository.EntrepriseRepository;
import nc.unc.application.data.repository.TuteurHabilitationRepository;
import nc.unc.application.data.repository.TuteurRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

    //Récupérer les tuteurs
    public List<Tuteur> findAllTuteurs(String stringFilter) {
        if (stringFilter == null || stringFilter.isEmpty()) {
            return tuteurRepository.findAll();
        } else {
            return tuteurRepository.search(stringFilter);
        }
    }

    //Nombre de tuteurs
    public long countTuteurs() {
        return tuteurRepository.count();
    }

    //Supprimer un tuteur
    public void deleteTuteur(Tuteur tuteur) {
        tuteurRepository.delete(tuteur);
    }

    //Sauvegarder un tuteur
    public void saveTuteur(Tuteur tuteur) {
        if (tuteur == null) {
            System.err.println("Le tuteur est nul, le formulaire est-il bien connecté à l'application ?");
            return;
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
}
