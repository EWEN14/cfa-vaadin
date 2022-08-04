package nc.unc.application.views.referentCfa;

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
import nc.unc.application.data.entity.ReferentCfa;
import nc.unc.application.data.entity.ReferentPedagogique;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.data.service.ReferentCfaService;
import nc.unc.application.data.service.ReferentPedagogiqueService;
import nc.unc.application.views.ConfirmDelete;
import nc.unc.application.views.MainLayout;
import nc.unc.application.views.referentPedagogique.ReferentPedagogiqueConsult;
import nc.unc.application.views.referentPedagogique.ReferentPedagogiqueNewOrEdit;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "referent_cfa", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Référents CFA") // title de la page
@PermitAll //tous les utilisateurs connectés peuvent aller sur
public class ReferentCfaView extends VerticalLayout {
  Grid<ReferentCfa> grid = new Grid<>(ReferentCfa.class, false);

  TextField filterText = new TextField();
  Button addReferentButton;

  ReferentCfa referentCfaMaybeToDelete;

  ReferentCfaConsult modalConsult;
  ReferentCfaNewOrEdit modalNewOrEdit;
  ConfirmDelete confirmDelete;

  ReferentCfaService referentCfaService;
  LogEnregistrmentService logEnregistrmentService;

  public ReferentCfaView(ReferentCfaService referentCfaService, LogEnregistrmentService logEnregistrmentService) {
    this.referentCfaService = referentCfaService;
    this.logEnregistrmentService = logEnregistrmentService;
    addClassName("list-view");
    setSizeFull();
    configureGrid();

    modalConsult = new ReferentCfaConsult();
    modalConsult.addListener(ReferentCfaConsult.DeleteEvent.class, this::transfertReferentFromEventToDelete);
    modalConsult.addListener(ReferentCfaConsult.CloseEvent.class, e -> closeConsultModal());

    modalNewOrEdit = new ReferentCfaNewOrEdit();
    modalNewOrEdit.addListener(ReferentCfaNewOrEdit.SaveEvent.class, this::saveReferentCfa);
    modalNewOrEdit.addListener(ReferentCfaNewOrEdit.SaveEditedEvent.class, this::saveEditedReferentCfa);
    modalNewOrEdit.addListener(ReferentCfaNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

    confirmDelete = new ConfirmDelete("ce référent cfa");
    confirmDelete.addListener(ConfirmDelete.DeleteEventGrid.class, this::deleteFromConfirmDelete);

    FlexLayout content = new FlexLayout(grid);
    content.setFlexGrow(2, grid);
    content.addClassNames("content", "gap-m");
    content.setSizeFull();

    add(getToolbar(), content, modalConsult, modalNewOrEdit);
    updateList();

  }

  private void configureGrid() {
    grid.addClassNames("referentCfa-grid");
    grid.setSizeFull();
    // ajout des colonnes
    // grid.setColumns("nomReferentPedago","prenomReferentPedago","telephoneReferentPedago","emailReferentPedago");

    grid.addColumn(rf -> rf.getNomReferentCfa() + " " + rf.getPrenomReferentCfa()).setHeader("NOM Prénom").setSortable(true);
    grid.addColumn(ReferentCfa::getTelephoneReferentCfa).setHeader("Téléphone");
    grid.addColumn(ReferentCfa::getEmailReferentCfa).setHeader("Email");

    grid.addComponentColumn(referentCfa -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultReferentCfa(referentCfa);
    })).setHeader("Consulter");
    // bouton édition référent cfa
    grid.addComponentColumn(referentCfa -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
      editReferentModal(referentCfa);
    })).setHeader("Éditer");
    grid.addComponentColumn(referentCfa -> new Button(new Icon(VaadinIcon.TRASH), click -> {
      prepareToDelete(referentCfa);
    })).setHeader("Supprimer");

    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar() {
    filterText.setHelperText("Recherche par nom ou prénom...");
    filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);

    filterText.addValueChangeListener(e -> updateList());

    addReferentButton = new Button("Nouveau Référent CFA");
    addReferentButton.addClickListener(click -> addReferent());

    HorizontalLayout toolbar = new HorizontalLayout(filterText, addReferentButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  private void saveReferentCfa(ReferentCfaNewOrEdit.SaveEvent event) {
    ReferentCfa referentCfa = event.getReferentCfa();

    referentCfa.setNomReferentCfa(referentCfa.getNomReferentCfa().toUpperCase());

    referentCfaService.saveReferentCfa(referentCfa);

    logEnregistrmentService.saveLogAjoutString(referentCfa.toString());

    updateList();
    closeNewOrEditModal();
    Notification.show(referentCfa.getPrenomReferentCfa() + " " + referentCfa.getNomReferentCfa() + " créé(e).");
  }

  private void saveEditedReferentCfa(ReferentCfaNewOrEdit.SaveEditedEvent event) {
    ReferentCfa referentCfa = event.getReferentCfa();
    ReferentCfa referentCfaOriginal = event.getReferentCfaOriginal();

    referentCfa.setNomReferentCfa(referentCfa.getNomReferentCfa().toUpperCase());

    referentCfaService.saveReferentCfa(referentCfa);

    logEnregistrmentService.saveLogEditString(referentCfaOriginal.toString(), referentCfa.toString());

    updateList();
    closeNewOrEditModal();
    Notification.show(referentCfa.getPrenomReferentCfa() + " " + referentCfa.getNomReferentCfa() + " modifiée(e)");
  }

  private void transfertReferentFromEventToDelete(ReferentCfaConsult.DeleteEvent event) {
    ReferentCfa referentCfa = event.getReferentCfa();
    deleteReferentCfa(referentCfa);
  }

  private void deleteReferentCfa(ReferentCfa referentCfa) {
    if (referentCfa != null) {
      referentCfaService.removeReferentCfa(referentCfa);

      logEnregistrmentService.saveLogDeleteString(referentCfa.toString());

      updateList();
      closeConsultModal();
      Notification.show(referentCfa.getPrenomReferentCfa() + " " + referentCfa.getNomReferentCfa() + " retirée(e)");
    }
  }

  private void deleteFromConfirmDelete(ConfirmDelete.DeleteEventGrid event) {
    Boolean supprimer = event.getSuppression();
    if (supprimer) {
      deleteReferentCfa(referentCfaMaybeToDelete);
    }
    referentCfaMaybeToDelete = null;
    closeConfirmDelete();
  }

  private void prepareToDelete(ReferentCfa referentCfa) {
    referentCfaMaybeToDelete = referentCfa;
    openConfirmDelete();
  }

  public void editReferentModal(ReferentCfa referentCfa) {
    if (referentCfa == null) {
      closeNewOrEditModal();
    } else {
      modalNewOrEdit.setReferentCfa(referentCfa);
      modalNewOrEdit.open();
      addClassName("editing");
    }
  }

  void addReferent() {
    grid.asSingleSelect().clear();
    editReferentModal(new ReferentCfa());
  }

  public void consultReferentCfa(ReferentCfa referentCfa) {
    modalConsult.setReferentCfa(referentCfa);
    modalConsult.open();
  }

  private void closeConsultModal() {
    modalConsult.setReferentCfa(null);
    modalConsult.close();
    grid.asSingleSelect().clear();
  }

  private void closeNewOrEditModal() {
    modalNewOrEdit.setReferentCfa(null);
    modalNewOrEdit.close();
    grid.asSingleSelect().clear();
  }

  private void openConfirmDelete() {
    confirmDelete.open();
  }

  private void closeConfirmDelete() {
    confirmDelete.close();
    grid.asSingleSelect().clear();
  }

  private void updateList() {
    grid.setItems(referentCfaService.findAllReferentCfa(filterText.getValue()));
  }

}
