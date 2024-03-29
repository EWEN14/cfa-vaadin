package nc.unc.application.data.service;

import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.NumConventionFormation;
import nc.unc.application.data.enums.CodeContrat;
import nc.unc.application.data.enums.StatutActifAutres;
import nc.unc.application.data.repository.ContratRepository;
import nc.unc.application.data.repository.EtudiantRepository;
import nc.unc.application.data.repository.NumConventionFormationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContratService {

  private final ContratRepository contratRepository;

  private final EtudiantRepository etudiantRepository;

  private final NumConventionFormationRepository numConvRepository;

  public ContratService(ContratRepository contratRepository, EtudiantRepository etudiantRepository,
                        NumConventionFormationRepository numConvRepository){
    this.contratRepository = contratRepository;
    this.etudiantRepository = etudiantRepository;
    this.numConvRepository = numConvRepository;
  }

  public void saveContrat(Contrat contrat){
    // Si le statut du contrat n'est pas défini, on le défini comme actif par défaut
    if (contrat.getStatutActif() == null) {
      contrat.setStatutActif(StatutActifAutres.ACTIF.getEnumStringify());
    }

    // Si le numéro de convention du contrat est vide, on le génère en utilisant le dernier numéro de la table numero_convention_formation
    if (contrat.getCodeContrat() == CodeContrat.CONTRAT &&
            (contrat.getNumeroConventionFormation() == null || contrat.getNumeroConventionFormation().isEmpty())) {
      Optional<NumConventionFormation> numConventionFormation = numConvRepository.findAllById(1L);
      if (numConventionFormation.isPresent()) {
        NumConventionFormation numberToAssign = numConventionFormation.get();
        contrat.setNumeroConventionFormation(LocalDate.now().getYear() +"-"+numberToAssign.getNumeroConvention());

        // on incrémente de 1 le numéro de convention pour la prochaine convention
        numberToAssign.setNumeroConvention(numberToAssign.getNumeroConvention() + 1);
        // mais s'il atteint 1001, on le remet à 1
        if (numberToAssign.getNumeroConvention() == 1001) {
          numberToAssign.setNumeroConvention(1);
        }
        numConvRepository.save(numberToAssign);
      }
    }

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

  public void saveAvenant(Contrat avenant) {
    // la plupart du temps, un avenant est créé du fait que le tuteur change,
    // alors on en profite pour répercuter ce changement dans étudiant
    Etudiant etudiantInAvenant = avenant.getEtudiant();
    etudiantInAvenant.setTuteur(avenant.getTuteur());

    etudiantRepository.save(etudiantInAvenant);
    contratRepository.save(avenant);
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
