package nc.unc.application.views.user;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.User;
import nc.unc.application.data.enums.Role;

public class UserConsult extends Dialog {

  // Objet user
  private User user;

  private final H3 titre = new H3("Consultation d'un utilisateur");

  FormLayout form = new FormLayout();
  TextField username = new TextField("Identifiant utilisateur");
  PasswordField hashedPassword = new PasswordField("Mot de passe");
  TextField nom = new TextField("NOM");
  TextField prenom = new TextField("Prénom");
  CheckboxGroup<Role> roles = new CheckboxGroup<>();
  private final DatePicker dateCreation = new DatePicker("Date de création");
  private final DatePicker dateMiseAJour = new DatePicker("Date de mise à jour");
  Binder<User> binder = new BeanValidationBinder<>(User.class);

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer l'utilisateur");

  public UserConsult(){

    // On définit que la fenêtre qui s'ouvre est une modale, ce qui fait qu'on ne peut rien faire sur l'application
    // tant que la modale n'est pas fermée
    this.setModal(true);
    this.setWidth("85vw");

    //Liste des rôles
    roles.setLabel("Rôles");
    roles.setItems(Role.values());
    roles.select(Role.USER);
    roles.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);
    // fonction qui met tous les champs en ReadOnly, pour qu'ils ne soient pas modifiables
    setAllFieldsToReadOnly();

    hashedPassword.setHelperText("Mot de passe encodé");

    // instanciation du binder qui servira au remplissage automatique du formulaire d'informations rattachés à l'user
    binder.bindInstanceFields(this);

    // ajout des champs et des boutons d'action dans le formulaire
    form.add(username, hashedPassword, nom, prenom, roles, new Div(), dateCreation, dateMiseAJour, createButtonsLayout());

    // ajout du formulaire dans la modale
    add(titre, form);
  }

  // Méthode appelée à l'ouverture de la vue pour alimenter les champs du formulaire.
  public void setUser(User user) {
    this.user = user;
    if (user != null) {
      //Transforme les dates en LocalDate et rempli les champs de dates
      dateCreation.setValue(user.getCreatedAt().toLocalDate());
      dateMiseAJour.setValue(user.getUpdatedAt().toLocalDate());
      // on passe les éléments du user en paramètre pour les appliquer sur les champs du formulaire
      binder.readBean(user);
    }
  }

  private HorizontalLayout createButtonsLayout() {
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    // évènements delete et close
    delete.addClickListener(event -> fireEvent(new UserConsult.DeleteEvent(this, user)));
    close.addClickListener(event -> fireEvent(new UserConsult.CloseEvent(this)));

    return new HorizontalLayout(delete, close);
  }

  private void setAllFieldsToReadOnly() {
    // champs d'infos générales de l'user
    username.setReadOnly(true);
    hashedPassword.setReadOnly(true);
    nom.setReadOnly(true);
    prenom.setReadOnly(true);
    roles.setReadOnly(true);
    dateCreation.setReadOnly(true);
    dateMiseAJour.setReadOnly(true);
  }

  // Event "global" (class mère), qui étend les deux events ci-dessous, dont le but est de fournir l'user
  // que l'on consulte dans le formulaire.
  public static abstract class UserConsultFormEvent extends ComponentEvent<UserConsult> {
    private final User user;

    protected UserConsultFormEvent(UserConsult source, User user) {
      super(source, false);
      this.user = user;
    }

    public User getUser() {
      return user;
    }
  }

  // Event au clic sur le bouton de suppression (classe fille)
  public static class DeleteEvent extends UserConsult.UserConsultFormEvent {
    DeleteEvent(UserConsult source, User user) {
      super(source, user);
    }
  }

  // Event au clic sur le bouton de fermerture du formulaire (classe fille)
  public static class CloseEvent extends UserConsult.UserConsultFormEvent {
    CloseEvent(UserConsult source) {
      super(source, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }
}
