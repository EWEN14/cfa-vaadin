package nc.unc.application.data.service;

import nc.unc.application.data.entity.Entreprise;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.repository.EntrepriseRepository;
import nc.unc.application.data.repository.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtudiantService {

  private final EtudiantRepository etudiantRepository;
  private final EntrepriseRepository entrepriseRepository;

  public EtudiantService(EtudiantRepository etudiantRepository, EntrepriseRepository entrepriseRepository) {
    this.etudiantRepository = etudiantRepository;
    this.entrepriseRepository = entrepriseRepository;
  }

  public List<Etudiant> findAllEtudiants(String stringFilter) {
    if (stringFilter == null || stringFilter.isEmpty()) {
      return etudiantRepository.findAll();
    } else {
      return etudiantRepository.search(stringFilter);
    }
  }

  public long countEtudiants() {
    return etudiantRepository.count();
  }

  public void deleteEtudiant(Etudiant etudiant) {
    etudiantRepository.delete(etudiant);
  }

  public void saveEtudiant(Etudiant etudiant) {
    if (etudiant == null) {
      System.err.println("L'étudiant est nul, le formulaire est-il bien connecté à l'application ?");
      return;
    }
    etudiantRepository.save(etudiant);
  }

  public List<Etudiant> findAllEtudiantsTuteur(Long id) {
    return etudiantRepository.findAllByTuteurId(id);
  }

  // TODO : mettre les findAll de compagnie, tuteur, référent, interlocuteur CFA
  // quand je les aurais créé

  public List<Entreprise> findAllEntreprises() {
    return entrepriseRepository.findAll();
  }
}
