package nc.unc.application.data.service;

import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.repository.EtudiantRepository;
import nc.unc.application.data.repository.TuteurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TuteurService {

    //Objets repository(Injection de dépendances avec @Autowired)
    @Autowired
    private TuteurRepository tuteurRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;

    //Constructeur
    public TuteurService(){

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

    //Récupérer tous les étudiants
    public void findAllEtudiants(){
        etudiantRepository.findAll();
    }

    // TODO : mettre les findAll de compagnie, tuteur, référent, interlocuteur CFA


}
