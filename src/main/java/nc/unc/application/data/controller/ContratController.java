package nc.unc.application.data.controller;

import com.lowagie.text.DocumentException;
import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.enums.CodeContrat;
import nc.unc.application.data.repository.ContratRepository;
import nc.unc.application.data.service.ContratService;
import nc.unc.application.data.service.PdfService;
import nc.unc.application.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Controller
@RequestMapping(path = "/contrat-generation")
@PermitAll
public class ContratController {

  private final ContratService contratService;
  private final PdfService pdfService;

  @Autowired
  public ContratController(ContratService contratService, PdfService pdfService) {
    this.contratService = contratService;
    this.pdfService = pdfService;
  }

  @GetMapping("/{id}")
  public String getContratById(@PathVariable("id") long id, Model model) {
    Contrat contrat = contratService.findContratById(id);
    model.addAttribute("contrat", contrat);
    model.addAttribute("download", false);
    model.addAttribute("avenant", contrat.getCodeContrat() == CodeContrat.AVENANT);
    String age = String.valueOf(ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), LocalDate.now()));
    model.addAttribute("age", age);
    if (contrat.getSalaireNegocie() != null) {
      model.addAttribute("salaireNego", contrat.getSalaireNegocie());
    } else {
      if (ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), contrat.getDebutContrat()) > 21
              && ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), contrat.getFinContrat()) > 21) {
        model.addAttribute("pourcent85", true);
      } else if (ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), contrat.getDebutContrat()) < 21
              && ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), contrat.getFinContrat()) < 21) {
        model.addAttribute("pourcent75", true);
      } else {
        int moisAnniversaire = contrat.getEtudiant().getDateNaissanceEtudiant().getMonthValue();
        LocalDate debutContrat = contrat.getDebutContrat();
        LocalDate finContrat = contrat.getFinContrat();
        LocalDate fin75pourcent = LocalDate.now();

        for (LocalDate date = debutContrat; date.isBefore(finContrat); date = date.plusMonths(1)) {
          if (date.getMonthValue() == moisAnniversaire) {
            int  lastday = date.lengthOfMonth();
            fin75pourcent = date.withDayOfMonth(lastday);
          }
        }
        LocalDate debut85pourcent = fin75pourcent.plusDays(1);
        String periode75pourcent = Utils.frenchDateFormater(contrat.getDebutContrat()) + " au " + Utils.frenchDateFormater(fin75pourcent);
        model.addAttribute("periode75", periode75pourcent);
        String periode85pourcent = Utils.frenchDateFormater(debut85pourcent) + " au " + Utils.frenchDateFormater(contrat.getFinContrat());
        model.addAttribute("periode85", periode85pourcent);
      }
    }
    return "contrat_pdf";
  }

  @GetMapping("/download/{id}")
  public void downloadPDFResource(HttpServletResponse response, @PathVariable("id") long id) {
    try {
      Path file = Paths.get(pdfService.generatePdf(id).getAbsolutePath());
      if (Files.exists(file)) {
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition",
                "attachment; filename=" + file.getFileName());
        Files.copy(file, response.getOutputStream());
        response.getOutputStream().flush();
      }
    } catch (DocumentException | IOException ex) {
      ex.printStackTrace();
    }
  }

}
