package nc.unc.application.data.service;

import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.repository.EntrepriseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntrepriseService {

  private final EntrepriseRepository entrepriseRepository;

  public EntrepriseService(EntrepriseRepository entrepriseRepository) {
    this.entrepriseRepository = entrepriseRepository;
  }

  public long countEntreprises() {
    return entrepriseRepository.count();
  }

  public void deleteEntreprise(Entreprise entreprise) {
    entrepriseRepository.delete(entreprise);
  }

  public boolean saveEntreprise(Entreprise entreprise) {
    Entreprise entreprise1 = entrepriseRepository.findByNumeroRidet(entreprise.getNumeroRidet());
    if(entreprise1 == null){
      entrepriseRepository.save(entreprise);
      return true;
    }
    else{
      return false;
    }
  }

  // Récupérer toutes les entreprises
  public List<Entreprise> findAllEntreprises(String filtertext) {
    if (filtertext == null || filtertext.isEmpty()) {
      return entrepriseRepository.findAllByOrderByEnseigneAsc();
    } else {
      return entrepriseRepository.search(filtertext);
    }
  }

  //Nombre d'entreprises actives
  public Integer CountBystatutActifEntreprise(String statut){
    return  entrepriseRepository.CountBystatutActifEntreprise(statut);
  }
}
