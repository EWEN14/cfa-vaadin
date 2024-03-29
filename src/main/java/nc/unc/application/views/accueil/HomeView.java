package nc.unc.application.views.accueil;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.entity.Tuteur;
import nc.unc.application.data.enums.StatutActifEntreprise;
import nc.unc.application.data.service.*;
import nc.unc.application.views.ConfirmDelete;
import nc.unc.application.views.MainLayout;
import nc.unc.application.views.etudiant.EtudiantConsult;
import nc.unc.application.views.tuteur.TuteurConsult;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import java.time.LocalDate;
import java.util.*;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "accueil", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@RouteAlias(value = "", layout = MainLayout.class)
@PageTitle("Accueil | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class HomeView extends VerticalLayout {

  Grid<Etudiant> gridEtudiantSansFormation = new Grid<>(Etudiant.class, false);
  Grid<Etudiant> gridEtudiantEnSuivi = new Grid<>(Etudiant.class, false);
  Grid<Tuteur> gridTuteurSansHabilitation = new Grid<>(Tuteur.class, false);

  TuteurConsult tuteurConsult;
  ConfirmDelete tuteurConfirmDelete;
  Tuteur tuteurToMaybeDelete;

  EtudiantConsult modalConsult;
  ConfirmDelete modalConfirmDelete;
  Etudiant etudiantToMaybeDelete;

  EtudiantService etudiantService;
  ContratService contratService;
  EntrepriseService entrepriseService;
  TuteurService tuteurService;
  FormationService formationService;
  LogEnregistrmentService logEnregistrmentService;
  H3 titreChiffres = new H3("Chiffres importants");
  H3 titreEtudiantSansEntreprise = new H3("Étudiants actifs sans entreprise");
  H3 titreEtudiantSuivis = new H3("Étudiants actifs en suivi");
  H3 titreTuteurSansHabilitation = new H3("Tuteurs actifs sans habilitations");

  VerticalLayout formationsChiffres = new VerticalLayout();
  Select<Integer> selectYear = new Select<>();

  public HomeView(FormationService formationService, EntrepriseService entrepriseService, EtudiantService etudiantService, TuteurService tuteurService, LogEnregistrmentService logEnregistrmentService, ContratService contratService) {
    this.formationService = formationService;
    this.etudiantService = etudiantService;
    this.contratService = contratService;
    this.tuteurService = tuteurService;
    this.entrepriseService = entrepriseService;
    this.logEnregistrmentService = logEnregistrmentService;

    addClassName("list-view");
    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    // On définit que les différents events vont déclencher une fonction
    // contenant l'objet tuteur (dans le cas du delete dans la tuteurConsult).
    tuteurConsult = new TuteurConsult(etudiantService, contratService);
    tuteurConsult.addListener(TuteurConsult.DeleteEvent.class, this::transfertTuteurFromEventToDelete);
    tuteurConsult.addListener(TuteurConsult.CloseEvent.class, e -> closeConsultModal());

    // On définit que les différents events vont déclencher une fonction
    // contenant l'objet etudiant (dans le cas du delete dans la modalConsult ou du save dans modalNewOrdEdit).
    modalConsult = new EtudiantConsult(contratService);
    modalConsult.addListener(EtudiantConsult.DeleteEvent.class, this::transfertEtudiantFromEventToDelete);
    modalConsult.addListener(EtudiantConsult.CloseEvent.class, e -> closeConsultModal());

    modalConfirmDelete = new ConfirmDelete("cet étudiant");
    modalConfirmDelete.addListener(ConfirmDelete.DeleteEventGrid.class, this::deleteFromConfirmDelete);

    tuteurConfirmDelete = new ConfirmDelete("ce tuteur");
    tuteurConfirmDelete.addListener(ConfirmDelete.DeleteEventGrid.class, this::deleteFromConfirmDeleteTuteur);

    // ajout d'un FlexLayout dans lequel on place la grille
    FlexLayout content = new FlexLayout(gridEtudiantSansFormation);
    content.setFlexGrow(2, gridEtudiantSansFormation);
    content.addClassNames("content", "gap-m");
    content.setSizeFull();
    content.setHeight("350px");

    gridEtudiantEnSuivi.setHeight("350px");
    // FlexLayout contentGridEtudiantsSuivi = new FlexLayout(gridEtudiantEnSuivi);
    // content.setFlexGrow(2, gridEtudiantEnSuivi);
    // content.addClassNames("content", "gap-m");
    // content.setSizeFull();
    // content.setHeight("350px");
    // content.setWidth();

    FlexLayout content1 = new FlexLayout(gridTuteurSansHabilitation);
    content1.setFlexGrow(2, gridTuteurSansHabilitation);
    content1.addClassNames("content", "gap-m");
    content1.setSizeFull();
    content1.setHeight("350px");

    this.setMinHeight("500vh");

    Span entreprises_actives = new Span(createIcon(VaadinIcon.WORKPLACE), new Span("Entreprises actives : " + entrepriseService.CountBystatutActifEntreprise(StatutActifEntreprise.ENTREPRISE_ACTIVE.getEnumStringify())));
    entreprises_actives.getElement().getThemeList().add("badge");

    H5 titreEtudiantsFormation = new H5("Étudiants inscrits par formations :");

    HorizontalLayout layout = new HorizontalLayout(entreprises_actives);

    selectYear.addValueChangeListener(change -> afficherChiffresFormation(selectYear.getValue()));

    add(titreChiffres, layout, titreEtudiantsFormation, selectYear);
    setItemsSelectAnnee();
    afficherChiffresFormation(selectYear.getValue());
    add(formationsChiffres);

    add(titreEtudiantSansEntreprise, content,
            modalConsult, titreTuteurSansHabilitation, content1, tuteurConsult,
            titreEtudiantSuivis, gridEtudiantEnSuivi);
    // initialisation des données de la grille à l'ouverture de la vue
    updateList();
  }

  public void afficherChiffresFormation(Integer annee){
    Calendar calendar =new GregorianCalendar();
    calendar.setTime(new Date());
    List<Formation> formations = formationService.findAllFormations(null);
    formationsChiffres.removeAll();

    for(Formation f: formations){
      List<Etudiant> etudiantsAnneeActuel = new ArrayList<>();
      for(Etudiant e: f.getEtudiants()){
        // System.out.println(f.getEtudiants().size());
        if(e.getAnneePromotion() != null){
          if(Objects.equals(e.getAnneePromotion(), annee)){
            etudiantsAnneeActuel.add(e);
          }
        }
      }
      Span formation  = new Span(createIcon(VaadinIcon.ACADEMY_CAP), new Span(f.getCodeFormation() + " : " + etudiantsAnneeActuel.size()));
      formation.getElement().getThemeList().add("badge success");
      HorizontalLayout layout1 = new HorizontalLayout();
      layout1.add(formation);
      formationsChiffres.add(layout1);
    }
  }
  private Icon createIcon(VaadinIcon vaadinIcon) {
    Icon icon = vaadinIcon.create();
    icon.getStyle().set("padding", "var(--lumo-space-xs");
    return icon;
  }

  private void configureGrid() {
    gridEtudiantSansFormation.addClassNames("etudiant-grid");
    gridEtudiantSansFormation.setSizeFull();
    // ajout des colonnes
    gridEtudiantSansFormation.addColumn(etudiant -> etudiant.getNomEtudiant() + " " + etudiant.getPrenomEtudiant()).setHeader("NOM Prénom").setSortable(true);
    gridEtudiantSansFormation.addColumn(Etudiant::getAnneePromotion).setHeader("Année de promotion").setSortable(true);
    gridEtudiantSansFormation.addColumn(Etudiant::getTelephoneEtudiant1).setHeader("Téléphone");
    gridEtudiantSansFormation.addColumn(Etudiant::getSituationUnc).setHeader("Situation à l'UNC").setSortable(true);
    // ajout du bouton de consultation d'un étudiant
    gridEtudiantSansFormation.addComponentColumn(etudiant -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultEtudiant(etudiant);
    })).setHeader("Consulter");
    // on définit que chaque colonne à une largeur autodéterminée
    gridEtudiantSansFormation.getColumns().forEach(col -> col.setAutoWidth(true));

    gridEtudiantEnSuivi.addClassNames("etudiant-grid-suivi");
    // ajout des colonnes
    gridEtudiantEnSuivi.addColumn(etudiant -> etudiant.getNomEtudiant() + " " + etudiant.getPrenomEtudiant()).setHeader("NOM Prénom").setSortable(true);
    gridEtudiantEnSuivi.addColumn(Etudiant::getAnneePromotion).setHeader("Année de promotion").setSortable(true);
    gridEtudiantEnSuivi.addColumn(Etudiant::getTelephoneEtudiant1).setHeader("Téléphone");
    gridEtudiantEnSuivi.addColumn(Etudiant::getSituationUnc).setHeader("Situation à l'UNC").setSortable(true);
    // ajout du bouton de consultation d'un étudiant
    gridEtudiantEnSuivi.addComponentColumn(etudiant -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultEtudiant(etudiant);
    })).setHeader("Consulter");
    // on définit que chaque colonne à une largeur autodéterminée
    gridEtudiantEnSuivi.getColumns().forEach(col -> col.setAutoWidth(true));
    gridEtudiantEnSuivi.setHeight("350px");

    gridTuteurSansHabilitation.addClassNames("tuteur-grid");
    gridTuteurSansHabilitation.setSizeFull();
    gridTuteurSansHabilitation.addColumn(tuteur -> tuteur.getNomTuteur() + " " + tuteur.getPrenomTuteur()).setHeader("NOM Prénom").setSortable(true);
    gridTuteurSansHabilitation.addColumn(tuteur -> tuteur.getEntreprise() != null ? tuteur.getEntreprise().getEnseigne() : "").setHeader("Entreprise");
    gridTuteurSansHabilitation.addColumn(Tuteur::getTelephoneTuteur1).setHeader("Téléphone");
    // bouton consultation tuteur
    gridTuteurSansHabilitation.addComponentColumn(tuteur -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultTuteur(tuteur);
    })).setHeader("Consulter");
    gridTuteurSansHabilitation.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private void transfertEtudiantFromEventToDelete(EtudiantConsult.DeleteEvent event) {
    Etudiant etudiant = event.getEtudiant();
    deleteEtudiant(etudiant);
  }

  // suppression de l'étudiant en utilisant EtudiantService
  private void deleteEtudiant(Etudiant etudiantToDelete) {
    if (etudiantToDelete != null) {
      etudiantService.deleteEtudiant(etudiantToDelete);

      // ajout du log de suppression
      logEnregistrmentService.saveLogDeleteString(etudiantToDelete.toString());

      updateList();
      closeConsultModal();
      Notification.show(etudiantToDelete.getPrenomEtudiant() + " " + etudiantToDelete.getNomEtudiant() + " retiré(e)");
    }
  }

  private void deleteFromConfirmDelete(ConfirmDelete.DeleteEventGrid event) {
    Boolean supprimer = event.getSuppression();
    if (supprimer) {
      deleteEtudiant(etudiantToMaybeDelete);
    }
    etudiantToMaybeDelete = null;
    closeConfirmDelete();
  }

  private void deleteFromConfirmDeleteTuteur(ConfirmDelete.DeleteEventGrid event) {
    Boolean supprimer = event.getSuppression();
    if (supprimer) {
      deleteTuteur(tuteurToMaybeDelete);
    }
    tuteurToMaybeDelete = null;
    closeConfirmDeleteTuteur();
  }

  private void transfertTuteurFromEventToDelete(TuteurConsult.DeleteEvent event){
    Tuteur tuteur = event.getTuteur();
    deleteTuteur(tuteur);
  }

  // suppression du tuteur en utilisant TuteurService
  private void deleteTuteur(Tuteur tuteur) {
    if (tuteur != null) {
      tuteurService.deleteTuteur(tuteur);

      // ajout du log de suppression
      logEnregistrmentService.saveLogDeleteString(tuteur.toString());

      updateList();
      closeTuteurModal();
      Notification.show(tuteur.getPrenomTuteur() + " " + tuteur.getNomTuteur() + " retiré(e)");
    }
  }

  public void consultTuteur(Tuteur tuteur) {
    tuteurConsult.setTuteur(tuteur);
    tuteurConsult.open();
  }

  private void closeTuteurModal() {
    tuteurConsult.setTuteur(null);
    tuteurConsult.close();
    gridEtudiantSansFormation.asSingleSelect().clear();
    gridEtudiantEnSuivi.asSingleSelect().clear();
  }

  // ouverture de modale de consultation d'un étudiant
  public void consultEtudiant(Etudiant etudiant) {
    modalConsult.setEtudiant(etudiant);
    modalConsult.open();
  }

  private void closeConsultModal() {
    modalConsult.setEtudiant(null);
    modalConsult.close();
    gridEtudiantSansFormation.asSingleSelect().clear();
    gridEtudiantEnSuivi.asSingleSelect().clear();
  }

  private void openConfirmDelete() {
    modalConfirmDelete.open();
  }

  private void closeConfirmDelete() {
    modalConfirmDelete.close();
    gridEtudiantSansFormation.asSingleSelect().clear();
    gridEtudiantEnSuivi.asSingleSelect().clear();
  }

  private void closeConfirmDeleteTuteur() {
    tuteurConfirmDelete.close();
    gridEtudiantSansFormation.asSingleSelect().clear();
    gridEtudiantEnSuivi.asSingleSelect().clear();
  }

  // fonction qui récupère alimente les grilles
  private void updateList() {
    gridEtudiantSansFormation.setItems(etudiantService.findAllEtudiantsSansEntreprise());
    gridEtudiantEnSuivi.setItems(etudiantService.findAllEtudiantsEnSuivi());
    gridTuteurSansHabilitation.setItems(tuteurService.findAllTuteursSansHabilitations());
  }

  // set des années dans la sélection des années sur le nombre d'étudiants par formation
  private void setItemsSelectAnnee() {
    int currentYear = LocalDate.now().getYear();
    List<Integer> listeAnnee = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      listeAnnee.add(currentYear - i);
    }
    selectYear.setItems(listeAnnee);
    selectYear.setValue(currentYear);
  }
}
