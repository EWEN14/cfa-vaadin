package nc.unc.application.data.service;

import nc.unc.application.data.entity.ReferentPedagogique;
import nc.unc.application.data.repository.ReferentPedagogiqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferentPedagogiqueService {

  //Objet repository
  private ReferentPedagogiqueRepository referentPedagogiqueRepository;

  //Constructeur
  public ReferentPedagogiqueService(ReferentPedagogiqueRepository referentPedagogiqueRepository) {
    this.referentPedagogiqueRepository = referentPedagogiqueRepository;
  }

  //Récupérer tous les référents pédagogiques ou le(s) référent(s) si filter n'est pas null
  public List<ReferentPedagogique> findAllReferentPedagogique(String filter){

    if(filter == null || filter.isEmpty()){
        return referentPedagogiqueRepository.findAll();
      } else {
        return referentPedagogiqueRepository.search(filter);
      }
  }

  //Supprimer un référent pédagogique
  public void removeReferentPedagogique(ReferentPedagogique referentPedagogique){
    referentPedagogiqueRepository.delete(referentPedagogique);
  }

  //Sauvegarder un référent pédagogique
  public void saveReferentPedagogique(ReferentPedagogique referentPedagogique){
    if (referentPedagogique == null) {
      System.err.println("Le référent pédagogique est nul, le formulaire est-il bien connecté à l'application ?");
      return;
    }
    referentPedagogiqueRepository.save(referentPedagogique);
  }

  //Retourne le nombre de referents pédagogiques
  public void countReferentPedagogique(){
    referentPedagogiqueRepository.count();
  }
}
