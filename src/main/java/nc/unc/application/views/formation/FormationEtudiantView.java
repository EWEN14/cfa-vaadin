package nc.unc.application.views.formation;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.Formation;
import nc.unc.application.data.service.EtudiantService;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import java.util.Optional;

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

  String idFormationStr;
  Span span = new Span("");
  Div messageErreur;
  H2 libelleFormation = new H2();
  Grid<Etudiant> etudiantGrid = new Grid<>(Etudiant.class);

  public FormationEtudiantView(FormationService formationService, EtudiantService etudiantService) {
    this.formationService = formationService;
    this.etudiantService = etudiantService;

    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    add(libelleFormation, span, etudiantGrid);
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
    etudiantGrid.setColumns("prenomEtudiant", "nomEtudiant", "anneePromotion", "admis", "situationUnc");
    // ajout du bouton de consultation d'un étudiant
    /*etudiantGrid.addComponentColumn(etudiant -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultEtudiant(etudiant);
    }));
    etudiantGrid.addComponentColumn(etudiant -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
      editEtudiantModal(etudiant);
    }));*/
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
    etudiantGrid.setItems(etudiantService.findAllEtudiantsFormation(formation.getId()));
  }

  public void showErrorMessage(String message) {
    this.remove(libelleFormation, etudiantGrid);
    messageErreur = new Div(new Span(message));
    this.add(messageErreur);
  }
}
