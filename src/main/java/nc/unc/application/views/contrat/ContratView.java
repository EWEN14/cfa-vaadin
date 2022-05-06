package nc.unc.application.views.contrat;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nc.unc.application.data.entity.Contrat;
import nc.unc.application.data.service.ContratService;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "contrats", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Contrats | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class ContratView extends VerticalLayout {

  Grid<Contrat> grid = new Grid<>(Contrat.class);
  TextField filterText = new TextField();
  Button addContratButton;
  //EtudiantConsult modalConsult;
  //EtudiantNewOrEdit modalNewOrEdit;
  ContratService service;
  LogEnregistrmentService logEnregistrmentService;

  public ContratView(ContratService service, LogEnregistrmentService logEnregistrmentService){

    this.service = service;
    this.logEnregistrmentService = logEnregistrmentService;

    addClassName("list-view");
    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    // ajout de la toolbar (recherche + nouveau contrat) et le grid
    // et des modales de consultation et de création/modification
    add(getToolbar(), grid);
    // initialisation des données de la grille à l'ouverture de la vue
    updateList();

  }

  private void configureGrid() {

    grid.addClassNames("contrat-grid");
    grid.setSizeFull();

    // ajout des colonnes
    //grid.setColumns("étudiant", "tuteur", "entreprise", "formation");

    // ajout du bouton de consultation d'un contrat
    grid.addComponentColumn(contrat -> new Button(new Icon(VaadinIcon.EYE), click -> {
      //consultEtudiant(etudiant);
    }));
    grid.addComponentColumn(contrat -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
      //editEtudiantModal(etudiant);
    }));
    // on définit que chaque colonne à une largeur autodéterminée
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar(){
    filterText.setHelperText("Recherche par numéro...");
    filterText.setPrefixComponent(VaadinIcon.SEARCH.create()); // affiche une petite loupe au début du champ
    filterText.setClearButtonVisible(true); // affiche la petite croix dans le champ pour effacer
    // permet de rendre Lazy le changement de valeur, la recherche ne se fera donc qu'après que l'utilisateur
    // ait arrêté de taper dans le champ depuis un petit moment.
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    // appel de la fonction qui va mettre à jour la liste des contrats en tenant compte de ce qu'on a tapé en recherche
    filterText.addValueChangeListener(e -> updateList());

    addContratButton = new Button("Nouveau contrat");
    //addContratButton.addClickListener(click -> addContrat());

    // on met le champ de recherche et le bouton d'ajout dans un HorizontalLayout, pour qu'ils soient côte à côte
    HorizontalLayout toolbar = new HorizontalLayout(filterText, addContratButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  // fonction qui récupère la liste des contrat pour les afficher dans la grille (avec les valeurs de recherche)
  private void updateList() {
    grid.setItems(service.findAllContrats(filterText.getValue()));
  }


}
