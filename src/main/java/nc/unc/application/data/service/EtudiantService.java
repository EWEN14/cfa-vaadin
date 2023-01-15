package nc.unc.application.data.service;

import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.enums.StatutActifAutres;
import nc.unc.application.data.enums.StatutActifEntreprise;
import nc.unc.application.data.repository.EtudiantRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class EtudiantService {

  private final EtudiantRepository etudiantRepository;

  public EtudiantService(EtudiantRepository etudiantRepository) {
    this.etudiantRepository = etudiantRepository;
  }

  public List<Etudiant> findAllEtudiants(String stringFilter) {
    if (stringFilter == null || stringFilter.isEmpty()) {
      return etudiantRepository.findAllByOrderByAnneePromotionDescNomEtudiantAsc();
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
    if (etudiant.getStatutActif() == null) {
      etudiant.setStatutActif(StatutActifAutres.ACTIF.getEnumStringify());
    }
    etudiantRepository.save(etudiant);
  }

  /**
   * Récupère tous les étudiants encadrés par un tuteur
   * @param id l'identifiant du tuteur
   * @return liste d'étudiants, ordonné par leur nom de famille et leur année de promotion
   */
  public List<Etudiant> findAllEtudiantsTuteur(Long id) {
    return etudiantRepository.findAllByTuteurIdOrderByNomEtudiantAscAnneePromotionDesc(id);
  }

  /**
   * Récupère tous les étudiants d'une formation
   * @param id l'identidiant de la formation
   * @return liste d'étudiants, ordonné par leur nom de famille et leur année de promotion
   */
  public List<Etudiant> findAllEtudiantsFormation(Long id) {
    return etudiantRepository.findAllByFormationIdOrderByNomEtudiantAscAnneePromotionDesc(id);
  }

  /**
   * Récupère tous les étudiants qui sont liées à l'entreprise avec l'id correspondante
   * @param id identifiant d'une entreprise
   * @return liste d'étudiants, ordonné par leur nom de famille et leur année de promotion
   */
  public List<Etudiant> findAllEtudiantsByEntrepriseId(Long id) {
    return etudiantRepository.findAllByEntrepriseIdOrderByNomEtudiantAscAnneePromotionDesc(id);
  }

  /**
   * Récupère tous les étudiants dont la situation entreprise est "sans objet"
   * @return liste d'étudiants, ordonné par leur nom de famille et leur année de promotion
   */
  public List<Etudiant> findAllEtudiantsSansEntreprise(){
    return etudiantRepository.findAllByStatutActifAndEntrepriseIsNullOrderByNomEtudiantAscAnneePromotionDesc(StatutActifAutres.ACTIF.getEnumStringify());
  }

  public List<Etudiant> findAllEtudiantsEnSuivi() {
    List<Etudiant> etudiantsSuivre = etudiantRepository.findAllBySuivreEtudiantTrueAndStatutActif(StatutActifAutres.ACTIF.getEnumStringify());
    return etudiantRepository.findAllBySuivreEtudiantTrueAndStatutActif(StatutActifAutres.ACTIF.getEnumStringify());
  }
}
