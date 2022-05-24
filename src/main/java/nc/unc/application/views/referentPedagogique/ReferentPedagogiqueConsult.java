package nc.unc.application.views.referentPedagogique;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import nc.unc.application.data.entity.ReferentPedagogique;
import nc.unc.application.data.enums.Civilite;

public class ReferentPedagogiqueConsult extends Dialog{
  private ReferentPedagogique referentPedagogique;

  private final VerticalLayout content = new VerticalLayout();

  private final FormLayout formReferentPedagogiqueInfos = new FormLayout();
  private final TextField nomReferentPedago = new TextField("NOM");
  private final TextField prenomReferentPedago = new TextField("Prénom");
  private final IntegerField telephoneReferentPedago = new IntegerField("Téléphone");
  private final EmailField emailReferentPedago = new EmailField("Email");
  private final Select<Civilite> civiliteReferentPedago = new Select<>(Civilite.values());
  Binder<ReferentPedagogique> referentPedagogiqueBinder = new BeanValidationBinder<>(ReferentPedagogique.class);

  private final Tab referentPedagogiqueInfosTab = new Tab(VaadinIcon.MALE.create(),new Span("Référent Pédagogique"));

  private final Button close = new Button("Fermer");
  private final Button delete = new Button("Supprimer le référent pédagogique");



  public ReferentPedagogiqueConsult(){
    this.setModal(true);
    this.setWidth("85vw");
    this.setHeight("90vh");

    referentPedagogiqueBinder.bindInstanceFields(this);

    setAllFieldsToReadOnly();

    civiliteReferentPedago.setLabel("Civilité");

    Tabs tabsReferentPedagogique = new Tabs(referentPedagogiqueInfosTab);
    // Au clic sur une des tab, on appelle notre méthode setContent pour pouvoir changer le contenu
    tabsReferentPedagogique.addSelectedChangeListener(selectedChangeEvent ->
            setContent(selectedChangeEvent.getSelectedTab())
    );

    formReferentPedagogiqueInfos.add(nomReferentPedago, prenomReferentPedago, telephoneReferentPedago, emailReferentPedago, civiliteReferentPedago);
    content.setSpacing(false);
    setContent(referentPedagogiqueInfosTab);

    add(tabsReferentPedagogique, content, createButtonsLayout());
  }

  public void setReferentPedagogique(ReferentPedagogique referentPedagogique){
    this.referentPedagogique = referentPedagogique;
    if(referentPedagogique != null){
      referentPedagogiqueBinder.readBean(referentPedagogique);
    }
  }

  private HorizontalLayout createButtonsLayout(){
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

    delete.addClickListener(event -> fireEvent(new DeleteEvent(this, referentPedagogique)));
    close.addClickListener(event -> fireEvent(new CloseEvent(this)));

    return new HorizontalLayout(delete, close);
  }


  private void setContent(Tab tab){
    content.removeAll();

    if(tab.equals(referentPedagogiqueInfosTab)){
      content.add(formReferentPedagogiqueInfos);
    }
  }

  private void setAllFieldsToReadOnly(){
    nomReferentPedago.setReadOnly(true);
    prenomReferentPedago.setReadOnly(true);
    telephoneReferentPedago.setReadOnly(true);
    emailReferentPedago.setReadOnly(true);
    civiliteReferentPedago.setReadOnly(true);
  }

  public void hideDeleteButton(){ delete.setVisible(false); }


  public static abstract class ReferentPedagogiqueConsultFormEvent extends ComponentEvent<ReferentPedagogiqueConsult>{
    private final ReferentPedagogique referentPedagogique;

    protected ReferentPedagogiqueConsultFormEvent(ReferentPedagogiqueConsult source, ReferentPedagogique referentPedagogique){
      super(source, false);
      this.referentPedagogique = referentPedagogique;
    }

    public ReferentPedagogique getReferentPedagogique(){
      return referentPedagogique;
    }

  }

  public static class DeleteEvent extends ReferentPedagogiqueConsultFormEvent{
    DeleteEvent(ReferentPedagogiqueConsult source, ReferentPedagogique referentPedagogique) { super(source, referentPedagogique); }
  }

  public static class CloseEvent extends ReferentPedagogiqueConsultFormEvent{
    CloseEvent(ReferentPedagogiqueConsult source) { super(source, null); }
  }

  public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType, ComponentEventListener<T> listener){
    return getEventBus().addListener(eventType, listener);
  }


}
