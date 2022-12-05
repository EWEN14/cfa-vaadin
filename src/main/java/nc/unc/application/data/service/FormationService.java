package nc.unc.application.data.service;

import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.enums.StatutActifAutres;
import nc.unc.application.data.repository.FormationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FormationService {

  private FormationRepository formationRepository;

  //Constructeur
  public FormationService(FormationRepository formationRepository) {
    this.formationRepository = formationRepository;
  }

  //Récupérer tous les formations ou la(les) formation(s) si filter n'est pas null
  public List<Formation> findAllFormations(String filter){

    if(filter == null || filter.isEmpty()){
      return formationRepository.findAllByOrderByLibelleFormationAsc();
    } else {
      return formationRepository.search(filter);
    }
  }

  //Supprimer une formation
  public void removeFormation(Formation formation){
    formationRepository.delete(formation);
  }

  //Sauvegarder une formation
  public void saveFormation(Formation formation){
    if (formation == null) {
      System.err.println("La formation est nulle, le formulaire est-il bien connecté à l'application ?");
      return;
    }
    if (formation.getStatutActif() == null) {
      formation.setStatutActif(StatutActifAutres.ACTIF.getEnumStringify());
    }
    formationRepository.save(formation);
  }

  //Retourne le nombre de formations
  public long countFormations(){
    return formationRepository.count();
  }

  // retourne les formation pour lequel le tuteur n'est pas habilité
  public List<Formation> getAllFormationNonHabilite(List<Long> idFormationHabilite) {
    return formationRepository.findAllByIdNotIn(idFormationHabilite);
  }

  // retourne la formation avec l'id passé en paramètre,
  // si elle existe (d'où le Optional)
  public Optional<Formation> getFormationbyId(Long id) {
    return formationRepository.findById(id);
  }
}
