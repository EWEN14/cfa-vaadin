package nc.unc.application.data.service;

import com.lowagie.text.DocumentException;
import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.enums.CodeContrat;
import nc.unc.application.utils.Utils;
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
import java.time.MonthDay;
import java.time.YearMonth;
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

  public File generateConventionPdf(Long id) throws IOException, DocumentException {
    Context context = getContext(id);
    String html = loadAndFillTemplateConvention(context);
    return renderPdf(html, id);
  }

  private File renderPdf(String html, Long id) throws IOException, DocumentException {
    Contrat contrat = contratService.findContratById(id);
    String annee = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(2,4);
    String nomContrat = contrat.getEtudiant().getNomEtudiant()+ " " + contrat.getEtudiant().getPrenomEtudiant() + "_" +
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
    context.setVariable("avenant", contrat.getCodeContrat() == CodeContrat.AVENANT);
    String age = String.valueOf(ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), LocalDate.now()));
    context.setVariable("age", age);

    if (contrat.getSalaireNegocie() != null) {
      context.setVariable("salaireNego", contrat.getSalaireNegocie());
    } else {
      if (ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), contrat.getDebutContrat()) > 21
        && ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), contrat.getFinContrat()) > 21) {
        context.setVariable("pourcent85", true);
      } else if (ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), contrat.getDebutContrat()) < 21
              && ChronoUnit.YEARS.between(contrat.getEtudiant().getDateNaissanceEtudiant(), contrat.getFinContrat()) < 21) {
        context.setVariable("pourcent75", true);
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
        context.setVariable("periode75", periode75pourcent);
        String periode85pourcent = Utils.frenchDateFormater(debut85pourcent) + " au " + Utils.frenchDateFormater(contrat.getFinContrat());
        context.setVariable("periode85", periode85pourcent);
      }
    }

    return context;
  }

  private String loadAndFillTemplate(Context context) {
    return templateEngine.process("contrat_pdf", context);
  }

  private String loadAndFillTemplateConvention(Context context) {
    return templateEngine.process("convention_pdf", context);
  }
}
