package nc.unc.application.data.service;

import nc.unc.application.data.entity.Evenement;
import nc.unc.application.data.repository.EvenementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvenementService {

  private final EvenementRepository evenementRepository;

  public EvenementService(EvenementRepository evenementRepository) {
    this.evenementRepository = evenementRepository;
  }

  public void saveEvenement(Evenement evenement){
    this.evenementRepository.save(evenement);
  }

  public void deleteEvenement(Evenement evenement){
    this.evenementRepository.delete(evenement);
  }

  public List<Evenement> findAllEvenements(String stringFilter){
    if (stringFilter == null || stringFilter.isEmpty()) {
      return evenementRepository.findAllByOrderByDateDebutDesc();
    } else {
      return evenementRepository.search(stringFilter);
    }
  }

  public List<Evenement> findAllEvenementsFormation(Long id){
    return evenementRepository.findAllByFormationsIdOrderByDateDebutAsc(id);
  }
  public Long countEvenenements(){
    return this.evenementRepository.count();
  }
}
