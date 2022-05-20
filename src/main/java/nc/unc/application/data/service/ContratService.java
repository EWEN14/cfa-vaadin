package nc.unc.application.data.service;

import nc.unc.application.data.entity.Contrat;
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
  }

  public void deleteContrat(Contrat contrat){
    contratRepository.delete(contrat);
  }

  public List<Contrat> findAllContrats(String filtertext){
    if(filtertext == null || filtertext.isEmpty()){
      return contratRepository.findAll();
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
}
