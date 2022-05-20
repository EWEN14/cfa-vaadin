package nc.unc.application.data.service;

import com.lowagie.text.DocumentException;
import nc.unc.application.data.entity.Contrat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

@Service
public class PdfService {

  private static final String PDF_RESOURCES = "/static/";;
  private ContratService contratService;
  private SpringTemplateEngine templateEngine;

  @Autowired
  public PdfService(ContratService contratService, SpringTemplateEngine templateEngine) {
    this.contratService = contratService;
    this.templateEngine = templateEngine;
  }

  public File generatePdf(Long id) throws IOException, DocumentException {
    Context context = getContext(id);
    String html = loadAndFillTemplate(context);
    return renderPdf(html, id);
  }

  private File renderPdf(String html, Long id) throws IOException, DocumentException {
    Contrat contrat = contratService.findContratById(id);
    String annee = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2,4);
    String nomContrat = "CUA_"+contrat.getEtudiant().getNomEtudiant()+ " " + contrat.getEtudiant().getPrenomEtudiant() + "_" +
            contrat.getEntreprise().getEnseigne() + "_" + contrat.getFormation().getCodeFormation() + "_" + annee + "-";

    File file = File.createTempFile(nomContrat, ".pdf");
    OutputStream outputStream = new FileOutputStream(file);
    ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
    renderer.setDocumentFromString(html, new ClassPathResource(PDF_RESOURCES).getURL().toExternalForm());
    renderer.layout();
    renderer.createPDF(outputStream);
    outputStream.close();
    file.deleteOnExit();
    return file;
  }

  private Context getContext(Long id) {
    Context context = new Context();
    Contrat contrat = contratService.findContratById(id);
    context.setVariable("contrat", contrat);
    context.setVariable("download", true);
    String age = String.valueOf(ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), LocalDate.now()));
    context.setVariable("age", age);
    return context;
  }

  private String loadAndFillTemplate(Context context) {
    return templateEngine.process("contrat_pdf", context);
  }
}
