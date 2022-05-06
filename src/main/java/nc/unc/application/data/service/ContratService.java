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
}
