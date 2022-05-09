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

  public void saveEntreprise(Entreprise entreprise) {
    if (entreprise == null) {
      System.err.println("L'entreprise est nulle, le formulaire est-il bien connecté à l'application ?");
      return;
    }
    entrepriseRepository.save(entreprise);
  }

  // Récupérer toutes les entreprises
  public List<Entreprise> findAllEntreprises() {
    return entrepriseRepository.findAll();
  }
}
