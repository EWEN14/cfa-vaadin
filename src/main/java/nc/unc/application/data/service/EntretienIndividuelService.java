package nc.unc.application.data.service;

import nc.unc.application.data.entity.EntretienIndividuel;
import nc.unc.application.data.repository.EntretienIndividuelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntretienIndividuelService {

  private EntretienIndividuelRepository entretienIndividuelleRepository;

  public EntretienIndividuelService(EntretienIndividuelRepository entretienIndividuelleRepository){
    this.entretienIndividuelleRepository = entretienIndividuelleRepository;
  }

  public void save(EntretienIndividuel entretienIndividuelle){
    entretienIndividuelleRepository.save(entretienIndividuelle);
  }

  public void delete(EntretienIndividuel entretienIndividuelle){
    entretienIndividuelleRepository.delete(entretienIndividuelle);
  }

  public long count(){
    return entretienIndividuelleRepository.count();
  }

  public List<EntretienIndividuel> findAll(String stringFilter){
    if (stringFilter == null || stringFilter.isEmpty()) {
      return entretienIndividuelleRepository.findAllByOrderByDateDesc();
    } else {
      return entretienIndividuelleRepository.search(stringFilter);
    }
  }
}
