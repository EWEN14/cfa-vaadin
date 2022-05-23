package nc.unc.application.data.controller;

import com.lowagie.text.DocumentException;
import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.enums.CodeContrat;
import nc.unc.application.data.repository.ContratRepository;
import nc.unc.application.data.service.ContratService;
import nc.unc.application.data.service.PdfService;
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
