package nc.unc.application.data.service;

import nc.unc.application.data.entity.EntretienCollectif;
import nc.unc.application.data.repository.EntretienCollectifRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntretienCollectifService {

  private EntretienCollectifRepository entretienCollectifRepository;

  public EntretienCollectifService(EntretienCollectifRepository entretienCollectifRepository){
  this.entretienCollectifRepository = entretienCollectifRepository;
  }

  public void save(EntretienCollectif entretienCollectif){
    entretienCollectifRepository.save(entretienCollectif);
  }

  public void delete(EntretienCollectif entretienCollectif){
    entretienCollectifRepository.delete(entretienCollectif);
  }

  public long count(){
    return entretienCollectifRepository.count();
  }

  public List<EntretienCollectif> findAll(String stringFilter){
    if (stringFilter == null || stringFilter.isEmpty()) {
      return entretienCollectifRepository.findAllByOrderByDateDesc();
    } else {
      return entretienCollectifRepository.search(stringFilter);
    }
  }
}
