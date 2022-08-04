package nc.unc.application.data.service;

import nc.unc.application.data.entity.ReferentCfa;
import nc.unc.application.data.repository.ReferentCfaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferentCfaService {

  //Objet repository
  private ReferentCfaRepository referentCfaRepository;

  //Constructeur
  public ReferentCfaService(ReferentCfaRepository referentCfaRepository) {
    this.referentCfaRepository = referentCfaRepository;
  }

  //Récupérer tous les référents cfa ou le(s) référent(s) si filter n'est pas null
  public List<ReferentCfa> findAllReferentCfa(String filter){

    if(filter == null || filter.isEmpty()){
      return referentCfaRepository.findAll();
    } else {
      return referentCfaRepository.search(filter);
    }
  }

  //Supprimer un référent cfa
  public void removeReferentCfa(ReferentCfa referentCfa){
    referentCfaRepository.delete(referentCfa);
  }

  //Sauvegarder un référent cfa
  public void saveReferentCfa(ReferentCfa referentCfa){
    if (referentCfa == null) {
      System.err.println("Le référent cfa est nul, le formulaire est-il bien connecté à l'application ?");
      return;
    }
    referentCfaRepository.save(referentCfa);
  }

  //Retourne le nombre de referents cfa
  public void countReferentCfa(){
    referentCfaRepository.count();
  }
}
