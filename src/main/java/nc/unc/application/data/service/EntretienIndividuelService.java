package nc.unc.application.data.service;

import nc.unc.application.data.entity.EntretienIndividuel;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.repository.EntretienIndividuelRepository;
import nc.unc.application.data.repository.EtudiantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntretienIndividuelService {

  private EntretienIndividuelRepository entretienIndividuelleRepository;

  private EtudiantRepository etudiantRepository;

  public EntretienIndividuelService(EntretienIndividuelRepository entretienIndividuelleRepository,
                                    EtudiantRepository etudiantRepository){
    this.entretienIndividuelleRepository = entretienIndividuelleRepository;
    this.etudiantRepository = etudiantRepository;
  }

  public void save(EntretienIndividuel entretienIndividuelle){
    Etudiant etudiantSuivre = entretienIndividuelle.getEtudiant();
    // si suivi étudiant coché dans l'entretien on met l'étudiant à suivre, sinon à false
    etudiantSuivre.setSuivreEtudiant(entretienIndividuelle.getSuivreEtudiant());
    etudiantRepository.save(etudiantSuivre);

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
