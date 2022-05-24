package nc.unc.application.views.user;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import nc.unc.application.data.entity.User;
import nc.unc.application.data.service.*;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;

import javax.annotation.security.PermitAll;// utilisé pour les tests

@Scope("prototype") // utilisé pour les tests
@Route(value = "users", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Utilisateurs | CFA") // title de la page
@PermitAll // tous les utilisateurs connectés peuvent aller sur cette page
public class UserView extends VerticalLayout {

  Grid<User> grid = new Grid<>(User.class);

  TextField filterText = new TextField();
  Button addUserButton;

  UserConsult modalConsult;
  UserNewOrEdit modalNewOrEdit;

  UserService userService;
  LogEnregistrmentService logEnregistrmentService;


  public UserView(UserService userService, LogEnregistrmentService logEnregistrmentService) {
    this.userService = userService;
    this.logEnregistrmentService = logEnregistrmentService;

    addClassName("list-view");
    setSizeFull(); // permet que le verticalLayout prenne tout l'espace sur l'écran (pas de "vide" en bas)
    configureGrid(); // configuration de la grille (colonnes, données...)

    // ajout de la modale de consultation de l'étudiant dans la vue
    modalConsult = new UserConsult();

    // On définit que les différents events vont déclencher une fonction
    // contenant l'objet user (dans le cas du delete dans la modalConsult ou du save dans modalNewOrdEdit).
    modalConsult.addListener(UserConsult.DeleteEvent.class, this::deleteUser);
    modalConsult.addListener(UserConsult.CloseEvent.class, e -> closeConsultModal());

    // ajout de la modale d'édition ou de création d'un user dans la vue
    modalNewOrEdit = new UserNewOrEdit();
    modalNewOrEdit.addListener(UserNewOrEdit.SaveEvent.class, this::saveUser);
    modalNewOrEdit.addListener(UserNewOrEdit.SaveEditedEvent.class, this::saveEditedUser);
    modalNewOrEdit.addListener(UserNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

    // ajout d'un FlexLayout dans lequel on place la grille
    FlexLayout content = new FlexLayout(grid);
    content.setFlexGrow(2, grid);
    content.addClassNames("content", "gap-m");
    content.setSizeFull();

    // ajout de la toolbar (recherche + nouveau user) et du content (grid + formulaire)
    // et des modales de consultation et de création/modification
    add(getToolbar(), content, modalConsult, modalNewOrEdit);
    // initialisation des données de la grille à l'ouverture de la vue
    updateList();
  }


  private void configureGrid() {
    grid.addClassNames("user-grid");
    grid.setSizeFull();
    // ajout des colonnes
    grid.setColumns("prenom", "nom");
    // ajout du bouton de consultation d'un utilisateur
    grid.addComponentColumn(user -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultUser(user);
    })).setHeader("Consulter");;
    // bouton édition utilisateur
    grid.addComponentColumn(user -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
      System.out.println("*************************************************user"+user);
      editUserModal(user);
    })).setHeader("Éditer");;
    // on définit que chaque colonne à une largeur autodéterminée
    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }


  private HorizontalLayout getToolbar() {
    filterText.setHelperText("Recherche par nom ou prénom...");
    filterText.setPrefixComponent(VaadinIcon.SEARCH.create()); // affiche une petite loupe au début du champ
    filterText.setClearButtonVisible(true); // affiche la petite croix dans le champ pour effacer
    // permet de rendre Lazy le changement de valeur, la recherche ne se fera donc qu'après que l'utilisateur
    // ait arrêté de taper dans le champ depuis un petit moment.
    filterText.setValueChangeMode(ValueChangeMode.LAZY);
    // appel de la fonction qui va mettre à jour la liste d'étudiants en tenant compte de ce qu'on a tapé en recherche
    filterText.addValueChangeListener(e -> updateList());

    addUserButton = new Button("Nouveau utilisateur");
    addUserButton.addClickListener(click -> addUser());

    // on met le champ de recherche et le bouton d'ajout dans un HorizontalLayout, pour qu'ils soient côte à côte
    HorizontalLayout toolbar = new HorizontalLayout(filterText, addUserButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  // sauvegarde du nouveau utilisateur en utilisant UserService
  private void saveUser(UserNewOrEdit.SaveEvent event) {
    // utilisation du getUser de la classe mère UserFormEvent pour récupérer l'user
    User user = event.getUser();
    // mise en majuscule du nom, définition sexe et âge avant sauvegarde
    setName(user);
    // sauvegarde de l'user
    userService.save(user);

    // ajout du log d'ajout
    logEnregistrmentService.saveLogAjoutString(user.toString());

    // mise à jour de la grid, fermeture du formulaire et notification
    updateList();
    closeNewOrEditModal();
    Notification.show(user.getPrenom() + " " + user.getNom() + " créé(e).");
  }

  // sauvegarde de l'user modifié en utilisant UserService
  private void saveEditedUser(UserNewOrEdit.SaveEditedEvent event) {
    // utilisation du getUser de la classe mère UserFormEvent pour récupérer l'user
    User user = event.getUser();
    // récupération de l'user original avant modification
    User userOriginal = event.getUserOriginal();
    // mise en majuscule du nom avant sauvegarde
    setName(user);

    // sauvegarde de l'user
    userService.save(user);

    // ajout du log de modification
    logEnregistrmentService.saveLogEditString(userOriginal.toString(), user.toString());

    updateList();
    closeNewOrEditModal();
    Notification.show(user.getPrenom() + " " + user.getNom() + " modifié(e)");
  }

  // suppression de l'user en utilisant userService
  private void deleteUser(UserConsult.DeleteEvent event) {
    User user = event.getUser();
    if (user != null) {
      userService.delete(user.getId());
      // ajout du log de suppression
      logEnregistrmentService.saveLogDeleteString(user.toString());
      updateList();
      closeConsultModal();
      Notification.show(user.getPrenom() + " " + user.getNom() + " retiré(e)");
    }
  }

  // ajout d'un user
  void addUser() {
    // on retire le focus s'il y avait une ligne sélectionnée
    grid.asSingleSelect().clear();
    // appel de la fonction d'edition de l'user, en passant un nouveau user
    editUserModal(new User());
  }
  // si user null, on ferme le formulaire, sinon on l'affiche (new or edit)
  public void editUserModal(User user) {
    if (user == null) {
      closeNewOrEditModal();
    } else {
      modalNewOrEdit.setUser(user);
      modalNewOrEdit.open();
      addClassName("editing");
    }
  }

  // ouverture de modale de consultation d'un user
  public void consultUser(User user) {
    modalConsult.setUser(user);
    modalConsult.open();
  }

  private void closeConsultModal() {
    modalConsult.setUser(null);
    modalConsult.close();
    grid.asSingleSelect().clear();
  }

  private void closeNewOrEditModal() {
    modalNewOrEdit.setUser(null);
    modalNewOrEdit.close();
    grid.asSingleSelect().clear();
  }

  // fonction qui récupère la liste des users pour les afficher dans la grille (avec les valeurs de recherche)
  private void updateList() {
    grid.setItems(userService.findAllUsers(filterText.getValue()));
  }

  // fonction qui met le nom de l'étudiant en majuscule
  private void setName(User user) {
    System.out.println("***************************************************"+user.getNom() + " "+user.getPrenom());
    user.setNom(user.getNom().toUpperCase());
  }
}
