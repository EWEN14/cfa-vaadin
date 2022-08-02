package nc.unc.application.views.formation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.style.Theme;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import nc.unc.application.data.entity.Evenement;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.service.ContratService;
import nc.unc.application.data.service.EtudiantService;
import nc.unc.application.data.service.EvenementService;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.views.MainLayout;
import nc.unc.application.views.etudiant.EtudiantConsult;
import nc.unc.application.views.evenement.EvenementConsult;
import org.springframework.context.annotation.Scope;

import javax.annotation.security.PermitAll;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.function.Consumer;

@org.springframework.stereotype.Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "evenements/:idFormation", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Formation Evenements | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page

public class FormationEvenementView extends VerticalLayout implements BeforeEnterObserver {

  EvenementService evenementService;
  FormationService formationService;


  Optional<Formation> formationExist;
  Formation formation;

  String idFormationStr;
  Div messageErreur;
  H2 libelleFormation = new H2();

  EvenementConsult modalConsult;

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer l'étudiant");

  Grid<Evenement> evenementGrid = new Grid<>(Evenement.class, false);
  Grid.Column<Evenement> libelle;

  public FormationEvenementView(EvenementService evenementService, FormationService formationService){
      this.formationService = formationService;
      this.evenementService = evenementService;


    // ajout de la modale de consultation de l'évenement dans la vue
    modalConsult = new EvenementConsult(evenementService, formationService.findAllFormations(null));
    // On définit que les différents events vont déclencher une fonction
    // contenant l'objet evenement (dans le cas du delete dans la modalConsult ou du save dans modalNewOrdEdit).
    modalConsult.addListener(EvenementConsult.CloseEvent.class, e -> closeConsultModal());
    modalConsult.hideDeleteButton();

    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    add(libelleFormation, evenementGrid);
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    idFormationStr = event.getRouteParameters().get("idFormation").get();

    getFormation();
  }

  private void configureGrid() {
    evenementGrid.addClassNames("evenement-grid");
    evenementGrid.setSizeFull();
    // ajout des colonnes
    libelle = evenementGrid.addColumn(evenement -> evenement.getLibelle());
    //dateDebut = evenementGrid.addColumn(evenement -> evenement.getDateDebut());
    //dateFin = evenementGrid.addColumn(evenement -> evenement.getDateFin());
    evenementGrid.addColumn(Evenement::getDateDebut).setHeader("Date début");
    evenementGrid.addColumn(Evenement::getDateFin).setHeader("Date fin");


    /*evenementGrid.addColumn(Evenement::getDescription).setHeader("Description");
    evenementGrid.addColumn(Evenement::getDateDebut).setHeader("Date de début");
    evenementGrid.addColumn(Evenement::getDateFin).setHeader("Date de fin");*/

    // ajout du bouton de consultation d'un évenement
    evenementGrid.addComponentColumn(evenement -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultEvenement(evenement);
    })).setHeader("Consulter");
    // on définit que chaque colonne à une largeur autodéterminée
    evenementGrid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  // ouverture de modale de consultation d'un évenement
  public void consultEvenement(Evenement evenement) {
    modalConsult.setEvenement(evenement);
    modalConsult.open();
  }

  // fermeture de la modale de consultation d'un évenement
  private void closeConsultModal() {
    modalConsult.setEvenement(null);
    modalConsult.close();
    evenementGrid.asSingleSelect().clear();
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
        // récupération des evenements de la formation
        getEvenementsFromFormation();
      } else {
        showErrorMessage("Il n'existe aucune formation avec l'identifiant " + idFormationStr + ".");
      }

    } catch (NumberFormatException nfe) {
      // si identifiant passé en paramètre n'est pas un nombre, on appelle la fonction affichant un message d'erreur.
      showErrorMessage("L'identifiant \"" + idFormationStr + "\" passé en URL n'est pas un nombre !");
    }
  }

    /**
     * Récupère la liste des evenements de la formation et alimente la grille
     */
    private void getEvenementsFromFormation() {
      // alimentation de la grille et définition d'un dataview qui nous servira pour le filtre sur plusieurs colonnes.
      GridListDataView<Evenement> dataView = evenementGrid.setItems(evenementService.findAllEvenementsFormation(formation.getId()));

      // appel de notre classe EvenementFilter (voir plus bas) à laquelle on passe les données (liste de tous les évenements)
      EvenementFilter evenementFilter = new EvenementFilter(dataView);

      // retrait des header par défaut avant le remplacement par les nôtre pour les filtres par colonnes
      evenementGrid.getHeaderRows().clear();
      // ajout de notre header
      HeaderRow headerRow = evenementGrid.appendHeaderRow();

      // ajout de nos header customisés sur nos colonnes
      headerRow.getCell(libelle).setComponent(createFilterHeader("Libelle", evenementFilter::setLibelle));
      //headerRow.getCell(dateDebut).setComponent(createDateFilterHeader("Date début", evenementFilter::setDateDebut));
      //headerRow.getCell(dateFin).setComponent(createDateFilterHeader("Date fin", evenementFilter::setDateFin));
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
   * //@param filterChangeConsumer
   * //@return
   */
  /*private static VerticalLayout createDateFilterHeader(String labelText, Consumer<String> filterChangeConsumer) {
    // label au dessus du champ
    Label label = new Label(labelText);
    label.getStyle().set("padding-top", "var(--lumo-space-m)")
            .set("font-size", "var(--lumo-font-size-xs)");
    // champ pour le filtrage
    DatePicker date = new DatePicker();
    // changeMode en EAGER (direct) car on a déjà les données récupérées, donc pas d'intérêt à rendre LAZY car on filtre, on ne recherche pas
    date.setValueChangeMode(ValueChangeMode.EAGER);
    textField.setClearButtonVisible(true);
    textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
    textField.setWidthFull();
    textField.getStyle().set("max-width", "100%");
    textField.addValueChangeListener(e -> filterChangeConsumer.accept(e.getValue()));
    // date.addValueChangeListener(e -> filterChangeConsumer.accept(e.getValue()));

    // on met l'année en cours par défaut dans le champ de filtre de l'année
    date.setWidthFull();
    date.getStyle().set("max-width", "100%");
    // layout contenant le label et le champ de filtrage
    VerticalLayout layout = new VerticalLayout(label, date);
    layout.getThemeList().clear();
    layout.getThemeList().add("spacing-xs");

    return layout;
  }*/

    private static class EvenementFilter {
      private final GridListDataView<Evenement> dataView;

      private String libelle;
      private String dateDebut;
      private String dateFin;


      public EvenementFilter(GridListDataView<Evenement> dataView) {
        this.dataView = dataView;
        this.dataView.addFilter(this::test);
      }

      public void setLibelle(String libelle) {
        this.libelle = libelle;
        this.dataView.refreshAll();
      }

      public void setDateDebut(String dateDebut ) {
        this.dateDebut = dateDebut;
        this.dataView.refreshAll();
      }

      public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
        this.dataView.refreshAll();
      }

      public boolean test(Evenement evenement) {
        boolean matchesLibelle = matches(evenement.getLibelle(), libelle);
        return matchesLibelle;
      }

      private boolean matches(String value, String searchTerm) {
        return searchTerm == null || searchTerm.isEmpty() || value
                .toLowerCase().contains(searchTerm.toLowerCase());
      }
    }

    public void showErrorMessage(String message) {
      this.remove(libelleFormation, evenementGrid);
      messageErreur = new Div(new Span(message));
      this.add(messageErreur);
    }
  }
