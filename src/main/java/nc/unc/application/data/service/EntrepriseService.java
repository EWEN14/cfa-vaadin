package nc.unc.application.data.service;

import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.repository.EntrepriseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    Entreprise entrepriseExistante = entrepriseRepository.findByNumeroRidet(entreprise.getNumeroRidet());
    if(entrepriseExistante == null){
      entrepriseRepository.save(entreprise);
      return true;
    }
    else{
      if (Objects.equals(entrepriseExistante.getId(), entreprise.getId())) {
        entrepriseRepository.save(entreprise);
        return true;
      }
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
