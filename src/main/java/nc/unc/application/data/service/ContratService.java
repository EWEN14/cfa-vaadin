package nc.unc.application.data.service;

import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.enums.CodeContrat;
import nc.unc.application.data.repository.ContratRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContratService {

  private final ContratRepository contratRepository;

  public ContratService(ContratRepository contratRepository){
    this.contratRepository = contratRepository;
  }

  public void saveContrat(Contrat contrat){
    contratRepository.save(contrat);

    // si après la mise à jour d'un contrat initial, ses informations de rupture de contrat ne sont pas nulles,
    // on définit alors la date de rupture sur ses avenants comme étant la même
    if (contrat.getCodeContrat() == CodeContrat.CONTRAT) {
      List<Contrat> avenants = contrat.getAvenants();
      if (avenants != null && !avenants.isEmpty()) {
        for (Contrat avenant : avenants) {
          if (contrat.getMotifRupture() != null && contrat.getDateRupture() != null) {
            avenant.setDateRupture(contrat.getDateRupture());
            avenant.setMotifRupture(contrat.getMotifRupture());
          }
        }
        contratRepository.saveAll(avenants);
      }
    }
  }

  /**
   * Suppression d'un contrat et de ses avenants ou juste d'un avenant
   * @param contrat un contrat ou un avenant
   */
  public void deleteContrat(Contrat contrat){
    if (contrat.getCodeContrat() == CodeContrat.AVENANT) {
      // si on est sur un avenant on réduit de 1 tous les avenants dont le numéro d'avenant est supérieur
      // à l'avenant qu'on va effacer
      Contrat contratParent = contrat.getContratParent();
      List<Contrat> avenants = contratParent.getAvenants();

      // on retire l'avenant en paramètre, car pas besoin de passer dans la boucle
      avenants.remove(contrat);

      for (Contrat avenant : avenants) {
        if (avenant.getNumeroAvenant() > contrat.getNumeroAvenant()) {
          avenant.setNumeroAvenant(avenant.getNumeroAvenant() - 1);
        }
      }
      // on sauvegarde pour mettre à jour le nouveau numéro d'avenant
      contratRepository.saveAll(avenants);
    }
    // suppression du contrat ou de l'avenant
    contratRepository.delete(contrat);
  }

  public List<Contrat> findAllContrats(String filtertext){
    if(filtertext == null || filtertext.isEmpty()){
      return contratRepository.findAllByOrderByCreatedAtDesc();
    } else {
      return contratRepository.search(filtertext);
    }
  }

  public void countContrats(){
    contratRepository.count();
  }

  /**
   * retourne la liste des contrats associés à un tuteur
   * @param id identifiant d'un tuteur
   * @return liste des contrats associé au tuteur
   */
  public List<Contrat> findAllContratByTuteurId(Long id) {
    return contratRepository.findAllByTuteurId(id);
  }

  /**
   * retourne la liste des contrats associés à un étudiant
   * @param id identifiant de l'étudiant
   * @return liste de contrats
   */
  public List<Contrat> findAllContratByEtudiantId(Long id) {
    return contratRepository.findAllByEtudiantId(id);
  }

  /**
   * retourne la liste des contrats associés à une entreprise
   * @param id identifiant de l'entreprise
   * @return liste de contrats
   */
  public List<Contrat> findAllContratByEntrepriseId(Long id) {
    return contratRepository.findAllByEntrepriseId(id);
  }

  /**
   * retourne un contrat
   * @param id identifiant du contrat
   * @return le contrat avec l'identifiant passé en paramètre
   */
  public Contrat findContratById(Long id) {
    return contratRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
  }

  /**
   * retourne le nombre d'avenants que possède un contrat original
   * @param avenant l'avenant depuis lequel on va récupérer le parent
   * @return un entier représentant le nombre d'avenants
   */
  public Integer countAllAvenantsFromParents(Contrat avenant) {
    if (avenant.getCodeContrat() == CodeContrat.AVENANT) {
      Contrat contratParent = avenant.getContratParent();
      return contratRepository.getCountOfAvenants(contratParent);
    }
    return 0;
  }

  /**
   * Récupère tous les avenants d'un contrat initial
   * @param contrat le contrat initial "parent"
   * @return une liste de contrats de type avenants, liste potentiellement vide
   */
  public List<Contrat> findAllAvenants(Contrat contrat) {
    return contratRepository.findAllByContratParentOrderByNumeroAvenantAsc(contrat);
  }
}
