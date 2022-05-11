package nc.unc.application.views.formation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.service.ContratService;
import nc.unc.application.data.service.EtudiantService;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.views.MainLayout;
import nc.unc.application.views.etudiant.EtudiantConsult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import java.util.Calendar;
import java.util.Optional;
import java.util.function.Consumer;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "formations/:idFormation", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Formation Étudiants | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class FormationEtudiantView extends VerticalLayout implements BeforeEnterObserver {

  EtudiantService etudiantService;
  FormationService formationService;

  Optional<Formation> formationExist;
  Formation formation;

  EtudiantConsult modalConsult;
  String idFormationStr;
  Span span = new Span("");
  Div messageErreur;
  H2 libelleFormation = new H2();

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer l'étudiant");

  Grid<Etudiant> etudiantGrid = new Grid<>(Etudiant.class, false);
  Grid.Column<Etudiant> fullNameColumn;
  Grid.Column<Etudiant> anneePromotionColumn;

  public FormationEtudiantView(FormationService formationService, EtudiantService etudiantService, ContratService contratService) {
    this.formationService = formationService;
    this.etudiantService = etudiantService;

    // ajout de la modale de consultation de l'étudiant dans la vue
    modalConsult = new EtudiantConsult(contratService);
    // On définit que les différents events vont déclencher une fonction
    // contenant l'objet etudiant (dans le cas du delete dans la modalConsult ou du save dans modalNewOrdEdit).
    modalConsult.addListener(EtudiantConsult.CloseEvent.class, e -> closeConsultModal());
    modalConsult.hideDeleteButton();

    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    add(libelleFormation, etudiantGrid, modalConsult);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    idFormationStr = event.getRouteParameters().get("idFormation").get();
    span.setText("id de la formation : "+idFormationStr);

    getFormation();
  }

  private void configureGrid() {
    etudiantGrid.addClassNames("etudiant-grid");
    etudiantGrid.setSizeFull();
    // ajout des colonnes
    fullNameColumn = etudiantGrid.addColumn(etudiant -> etudiant.getPrenomEtudiant() + " " + etudiant.getNomEtudiant());
    anneePromotionColumn = etudiantGrid.addColumn(Etudiant::getAnneePromotion);
    etudiantGrid.addColumn(Etudiant::getAdmis);
    etudiantGrid.addColumn(Etudiant::getSituationUnc);

    // ajout du bouton de consultation d'un étudiant
    etudiantGrid.addComponentColumn(etudiant -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultEtudiant(etudiant);
    }));

    // on définit que chaque colonne à une largeur autodéterminée
    etudiantGrid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  /**
   * Récupère la formation correspondant à l'id en url
   */
  private void getFormation() {
    long idFormation;
    // avant de chercher la formation, on s'assure que l'identifiant passé en paramètre est bien un nombre
    try {
      // on transforme l'id en paramètre url d'une string en Long, si pas possible le catch est soulevé
      idFormation = Long.parseLong(idFormationStr);
      // on tente de récupérer la formation avec l'id passé en paramètre
      formationExist = formationService.getFormationbyId(idFormation);
      // si la formation existe, on passe ses valeurs dans une variable formation
      if (formationExist.isPresent()) {
        formation = formationExist.get();
        // on définit le texte du titre maintenant qu'on a récupéré la formation
        libelleFormation.setText(formation.getLibelleFormation());
        // récupération des étudiants de la formation
        getEtudiantFromFormation();
      } else {
        showErrorMessage("Il n'existe aucune formation avec l'identifiant "+idFormationStr+".");
      }

    } catch (NumberFormatException nfe) {
      // si identifiant passé en paramètre n'est pas un nombre, on appelle la fonction affichant un message d'erreur.
      showErrorMessage("L'identifiant \""+idFormationStr+"\" passé en URL n'est pas un nombre !");
    }
  }

  /**
   * Récupère la liste des étudiants de la formation et alimente la grille
   */
  private void getEtudiantFromFormation() {
    // alimentation de la grille et définition d'un dataview qui nous servira pour le filtre sur plusieurs colonnes.
    GridListDataView<Etudiant> dataView = etudiantGrid.setItems(etudiantService.findAllEtudiantsFormation(formation.getId()));

    // appel de notre classe EtudiantFilter (voir plus bas) à laquelle on passe les données (liste de tous les étudiants)
    EtudiantFilter etudiantFilter = new EtudiantFilter(dataView);

    // retrait des header par défaut avant le remplacement par les nôtre pour les filtres par colonnes
    etudiantGrid.getHeaderRows().clear();
    // ajout de notre header
    HeaderRow headerRow = etudiantGrid.appendHeaderRow();

    // ajout de nos header customisés sur nos colonnes
    headerRow.getCell(fullNameColumn).setComponent(createFilterHeader("Prénom NOM", etudiantFilter::setFullName));
    headerRow.getCell(anneePromotionColumn).setComponent(createYearFilterHeader(etudiantFilter::setAnneePromotion));
  }

  /**
   * Composant pour créer les filtres sur chaque colonne
   * @param labelText nom du label au dessus du champ pour filtrer
   * @param filterChangeConsumer consumer qui récupère la valeur inscrite dans le champ avant que ne se lance le filtrage
   * @return le verticalLayout contenant le label et le champ pour filtrer
   */
  private static VerticalLayout createFilterHeader(String labelText, Consumer<String> filterChangeConsumer) {
    // label au dessus du champ
    Label label = new Label(labelText);
    label.getStyle().set("padding-top", "var(--lumo-space-m)")
            .set("font-size", "var(--lumo-font-size-xs)");
    // champ pour le filtrage
    TextField textField = new TextField();
    // changeMode en EAGER (direct) car on a déjà les données récupérées, donc pas d'intérêt à rendre LAZY car on filtre, on ne recherche pas
    textField.setValueChangeMode(ValueChangeMode.EAGER);
    textField.setClearButtonVisible(true);
    textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
    textField.setWidthFull();
    textField.getStyle().set("max-width", "100%");
    textField.addValueChangeListener(e -> filterChangeConsumer.accept(e.getValue()));

    // layout contenant le label et le champ de filtrage
    VerticalLayout layout = new VerticalLayout(label, textField);
    layout.getThemeList().clear();
    layout.getThemeList().add("spacing-xs");

    return layout;
  }

  /**
   * Même principe que la fonction précédente, mais spécifiquement pour l'année, avec l'année en cours par défaut.
   * @param filterChangeConsumer
   * @return
   */
  private static VerticalLayout createYearFilterHeader(Consumer<String> filterChangeConsumer) {
    // label au dessus du champ
    Label label = new Label("Année de promotion");
    label.getStyle().set("padding-top", "var(--lumo-space-m)")
            .set("font-size", "var(--lumo-font-size-xs)");
    // champ pour le filtrage
    TextField textField = new TextField();
    // changeMode en EAGER (direct) car on a déjà les données récupérées, donc pas d'intérêt à rendre LAZY car on filtre, on ne recherche pas
    textField.setValueChangeMode(ValueChangeMode.EAGER);
    textField.setClearButtonVisible(true);
    textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
    textField.setWidthFull();
    textField.getStyle().set("max-width", "100%");
    textField.addValueChangeListener(e -> filterChangeConsumer.accept(e.getValue()));
    // on met l'année en cours par défaut dans le champ de filtre de l'année
    textField.setValue(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));

    // layout contenant le label et le champ de filtrage
    VerticalLayout layout = new VerticalLayout(label, textField);
    layout.getThemeList().clear();
    layout.getThemeList().add("spacing-xs");

    return layout;
  }

  private static class EtudiantFilter {
    private final GridListDataView<Etudiant> dataView;

    private String fullName;
    private String anneePromotion;

    public EtudiantFilter(GridListDataView<Etudiant> dataView) {
      this.dataView = dataView;
      this.dataView.addFilter(this::test);
    }

    public void setFullName(String prenom) {
      this.fullName = prenom;
      this.dataView.refreshAll();
    }

    public void setAnneePromotion(String anneePromotion) {
      this.anneePromotion = anneePromotion;
      this.dataView.refreshAll();
    }

    public boolean test(Etudiant etudiant) {
      boolean matchesFullName = matches(etudiant.getPrenomEtudiant() + " " + etudiant.getNomEtudiant(), fullName);
      boolean matchesAnneePromotion;
      if (etudiant.getAnneePromotion() != null) {
         matchesAnneePromotion = matches(etudiant.getAnneePromotion().toString(), anneePromotion);
      } else {
        matchesAnneePromotion = matches("", anneePromotion);
      }

      return matchesFullName && matchesAnneePromotion;
    }

    private boolean matches(String value, String searchTerm) {
      return searchTerm == null || searchTerm.isEmpty() || value
              .toLowerCase().contains(searchTerm.toLowerCase());
    }
  }

  public void showErrorMessage(String message) {
    this.remove(libelleFormation, etudiantGrid);
    messageErreur = new Div(new Span(message));
    this.add(messageErreur);
  }

  // ouverture de modale de consultation d'un étudiant
  public void consultEtudiant(Etudiant etudiant) {
    modalConsult.setEtudiant(etudiant);
    modalConsult.open();
  }

  // fermeture de la modale de consultation d'un étudiant
  private void closeConsultModal() {
    modalConsult.setEtudiant(null);
    modalConsult.close();
    etudiantGrid.asSingleSelect().clear();
  }
}
