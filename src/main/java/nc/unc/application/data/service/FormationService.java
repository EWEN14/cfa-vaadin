package nc.unc.application.data.service;

import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.repository.FormationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FormationService {


  private FormationRepository formationRepository;

  //Constructeur
  public FormationService(FormationRepository formationRepository) {
    this.formationRepository = formationRepository;
  }

  //Récupérer tous les formations ou la(les) formation(s) si filter n'est pas null
  /*public List<Formation> findAllFormations(String filter){

    if(filter == null || filter.isEmpty()){
      return formationRepository.findAll();
    } else {
      return formationRepository.search(filter);
    }
  }*/

  //Supprimer une formation
  public void removeFormation(Formation formation){
    formationRepository.delete(formation);
  }

  //Sauvegarder une formation
  public void addFormation(Formation formation){
    if (formation == null) {
      System.err.println("La formation est nul, le formulaire est-il bien connecté à l'application ?");
      return;
    }
    formationRepository.save(formation);
  }

  //Retourne le nombre de formations
  public void countFormations(){
    formationRepository.count();
  }
}
