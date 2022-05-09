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

  public EtudiantService(EtudiantRepository etudiantRepository) {
    this.etudiantRepository = etudiantRepository;
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

  /**
   * Récupère tous les étudiants encadré par un tuteur
   * @param id l'identifiant du tuteur
   * @return liste d'étudiants
   */
  public List<Etudiant> findAllEtudiantsTuteur(Long id) {
    return etudiantRepository.findAllByTuteurId(id);
  }

  /**
   * Récupère tous les étudiants d'une formation
   * @param id l'identidiant de la formation
   * @return liste d'étudiants
   */
  public List<Etudiant> findAllEtudiantsFormation(Long id) {
    return etudiantRepository.findAllByFormationId(id);
  }
}
