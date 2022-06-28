package nc.unc.application.views.user;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.checkbox.CheckboxGroupVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.User;
import nc.unc.application.data.enums.Role;


public class UserNewOrEdit extends Dialog {

  // Objets users
  private User user;
  private User cloneUser;

  FormLayout form = new FormLayout();
  TextField username = new TextField("Identifiant utilisateur");
  PasswordField hashedPassword = new PasswordField("Mot de Passe");
  TextField nom = new TextField("NOM");
  TextField prenom = new TextField("Prénom");
  CheckboxGroup<Role> roles = new CheckboxGroup<>();

  Binder<User> binder = new BeanValidationBinder<>(User.class);

  Button save = new Button("Sauvegarder");
  Button close = new Button("Fermer");


  public UserNewOrEdit() {
    this.setWidth("85vw");

    // on fait le bind avec le nom des champs du formulaire et des attributs de l'entité user,
    // (les noms sont les mêmes et permet de faire en sorte de binder automatiquement)
    binder.bindInstanceFields(this);

    roles.setLabel("Rôles");
    roles.setItems(Role.values());
    roles.addThemeVariants(CheckboxGroupVariant.LUMO_VERTICAL);

    hashedPassword.setHelperText("Mot de passe actuel encodé. Changer de valeur pour changer de mot de passe.");

    // ajout des champs et des boutons d'action dans le formulaire
    form.add(username, hashedPassword, nom, prenom, roles, createButtonsLayout());
    // ajout du formulaire dans la modale
    add(form);
  }

  private HorizontalLayout createButtonsLayout() {
    save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // évènements save, close
    save.addClickListener(event -> validateAndSave());
    close.addClickListener(event -> fireEvent(new UserNewOrEdit.CloseEvent(this)));

    // met le bouton de sauvegarde actif que si le binder est valide
    binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

    return new HorizontalLayout(save, close);
  }

  // fonction qui va alimenter le binder d'un user
  public void setUser(User user) {
    this.user = user;
    // on doit remettre le cloneUser à null (sinon garde ancienne valeur de l'edit)
    this.cloneUser = null;
    // copie de l'user si c'est un edit (pour garder les anciennes valeurs qu'on mettra dans le log)
    if (user != null && user.getId() != null) {
      try {
        this.cloneUser = (User) user.clone();
      } catch (CloneNotSupportedException e) {
        e.printStackTrace();
      }
    }
    // alimentation du binder
    binder.readBean(this.user);
  }

  // fonction qui vérifie que le bean a bien un user, avant de lancer l'event de sauvegarde
  private void validateAndSave() {
    try {
      binder.writeBean(user);
      if (this.cloneUser == null) {
        System.out.println("userSAVE"+user);
        fireEvent(new UserNewOrEdit.SaveEvent(this, user));
      } else {
        fireEvent(new UserNewOrEdit.SaveEditedEvent(this, user, cloneUser));
      }
    } catch (ValidationException e) {
      e.printStackTrace();
    }
  }


  // Event "global" (class mère), qui étend les trois event ci-dessous, dont le but est de fournir l'user
  // qu'on manipule dans le formulaire
  public static abstract class UserFormEvent extends ComponentEvent<UserNewOrEdit> {
    private final User user;
    private final User userOriginal;

    protected UserFormEvent(UserNewOrEdit source, User user, User userOriginal) {
      super(source, false);
      this.user = user;
      this.userOriginal = userOriginal;
    }

    public User getUser() {
      return this.user;
    }

    public User getUserOriginal() {
      return this.userOriginal;
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère l'user du formulaire (classe fille)
  public static class SaveEvent extends UserNewOrEdit.UserFormEvent {
    SaveEvent(UserNewOrEdit source, User user) {
      super(source, user, null);
    }
  }

  // Event au clic sur le bouton de sauvegarde qui récupère l'user du formulaire et sa version originale (avant modification)
  public static class SaveEditedEvent extends UserNewOrEdit.UserFormEvent {
    SaveEditedEvent(UserNewOrEdit source, User user, User userOriginal) {
      super(source, user, userOriginal);
    }
  }

  // Event au clic sur le bouton de fermeture du formulaire (classe fille)
  public static class CloseEvent extends UserNewOrEdit.UserFormEvent {
    CloseEvent(UserNewOrEdit source) {
      super(source, null, null);
    }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
    return getEventBus().addListener(eventType, listener);
  }

}
