package nc.unc.application.views.referentPedagogique;

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
import nc.unc.application.data.entity.Etudiant;
import nc.unc.application.data.entity.ReferentPedagogique;
import nc.unc.application.data.service.FormationService;
import nc.unc.application.data.service.LogEnregistrmentService;
import nc.unc.application.data.service.ReferentPedagogiqueService;
import nc.unc.application.views.ConfirmDelete;
import nc.unc.application.views.MainLayout;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;

@Component // utilisé pour les tests
@Scope("prototype") // utilisé pour les tests
@Route(value = "referent_pedagogique", layout = MainLayout.class) // inclusion du MainLayout (header + nav)
@PageTitle("Référents Pédagogique | CFA") // title de la page
@PermitAll //tous les utilisateurs connectés peuvent aller sur


public class ReferentPedagogiqueView extends VerticalLayout {
  Grid<ReferentPedagogique> grid = new Grid<>(ReferentPedagogique.class, false);

  TextField filterText = new TextField();
  Button addReferentButton;

  ReferentPedagogique referentPedagogiqueMaybeToDelete;

  ReferentPedagogiqueConsult modalConsult;
  ReferentPedagogiqueNewOrEdit modalNewOrEdit;
  ConfirmDelete confirmDelete;

  ReferentPedagogiqueService referentPedagogiqueService;
  LogEnregistrmentService logEnregistrmentService;
  FormationService formationService;


  public ReferentPedagogiqueView(ReferentPedagogiqueService referentPedagogiqueService, LogEnregistrmentService logEnregistrmentService, FormationService formationService) {
    this.referentPedagogiqueService = referentPedagogiqueService;
    this.logEnregistrmentService = logEnregistrmentService;
    this.formationService = formationService;
    addClassName("list-view");
    setSizeFull();
    configureGrid();

    modalConsult = new ReferentPedagogiqueConsult();
    modalConsult.addListener(ReferentPedagogiqueConsult.DeleteEvent.class, this::transfertEtudiantFromEventToDelete);
    modalConsult.addListener(ReferentPedagogiqueConsult.CloseEvent.class, e -> closeConsultModal());

    modalNewOrEdit = new ReferentPedagogiqueNewOrEdit();
    modalNewOrEdit.addListener(ReferentPedagogiqueNewOrEdit.SaveEvent.class, this::saveReferentPedagogique);
    modalNewOrEdit.addListener(ReferentPedagogiqueNewOrEdit.SaveEditedEvent.class, this::saveEditedReferentPedagogique);
    modalNewOrEdit.addListener(ReferentPedagogiqueNewOrEdit.CloseEvent.class, e -> closeNewOrEditModal());

    confirmDelete = new ConfirmDelete("ce référent pédagogique");
    confirmDelete.addListener(ConfirmDelete.DeleteEventGrid.class, this::deleteFromConfirmDelete);

    FlexLayout content = new FlexLayout(grid);
    content.setFlexGrow(2, grid);
    content.addClassNames("content", "gap-m");
    content.setSizeFull();

    add(getToolbar(), content, modalConsult, modalNewOrEdit);
    updateList();

  }

  private void configureGrid() {
    grid.addClassNames("etudiant-grid");
    grid.setSizeFull();
    // ajout des colonnes
    // grid.setColumns("nomReferentPedago","prenomReferentPedago","telephoneReferentPedago","emailReferentPedago");

    grid.addColumn(rf -> rf.getNomReferentPedago() + " " + rf.getPrenomReferentPedago()).setHeader("NOM Prénom").setSortable(true);
    grid.addColumn(ReferentPedagogique::getTelephoneReferentPedago).setHeader("Téléphone");
    grid.addColumn(ReferentPedagogique::getEmailReferentPedago).setHeader("Email");

    grid.addComponentColumn(referentPedagogique -> new Button(new Icon(VaadinIcon.EYE), click -> {
      consultReferentPedagogique(referentPedagogique);
    })).setHeader("Consulter");
    // bouton édition référent pédagogique
    grid.addComponentColumn(referentPedagogique -> new Button(new Icon(VaadinIcon.PENCIL), click -> {
      editReferentModal(referentPedagogique);
    })).setHeader("Éditer");
    grid.addComponentColumn(referentPedagogique -> new Button(new Icon(VaadinIcon.TRASH), click -> {
      prepareToDelete(referentPedagogique);
    })).setHeader("Supprimer");

    grid.getColumns().forEach(col -> col.setAutoWidth(true));
  }

  private HorizontalLayout getToolbar() {
    filterText.setHelperText("Recherche par nom ou prénom...");
    filterText.setPrefixComponent(VaadinIcon.SEARCH.create());
    filterText.setClearButtonVisible(true);
    filterText.setValueChangeMode(ValueChangeMode.LAZY);

    filterText.addValueChangeListener(e -> updateList());

    addReferentButton = new Button("Nouveau Référent Pédagogique");
    addReferentButton.addClickListener(click -> addReferent());

    HorizontalLayout toolbar = new HorizontalLayout(filterText, addReferentButton);
    toolbar.addClassName("toolbar");
    return toolbar;
  }

  private void saveReferentPedagogique(ReferentPedagogiqueNewOrEdit.SaveEvent event) {
    ReferentPedagogique referentPedagogique = event.getReferentPedagogique();

    referentPedagogiqueService.addReferentPedagogique(referentPedagogique);

    logEnregistrmentService.saveLogAjoutString(referentPedagogique.toString());

    updateList();
    closeNewOrEditModal();
    Notification.show(referentPedagogique.getPrenomReferentPedago() + " " + referentPedagogique.getNomReferentPedago() + " créé(e).");
  }

  private void saveEditedReferentPedagogique(ReferentPedagogiqueNewOrEdit.SaveEditedEvent event) {
    ReferentPedagogique referentPedagogique = event.getReferentPedagogique();
    ReferentPedagogique referentPedagogiqueOriginal = event.getReferentPedagogiqueOriginal();

    referentPedagogique.setNomReferentPedago(referentPedagogique.getNomReferentPedago().toUpperCase());

    referentPedagogiqueService.addReferentPedagogique(referentPedagogique);

    logEnregistrmentService.saveLogEditString(referentPedagogiqueOriginal.toString(), referentPedagogique.toString());

    updateList();
    closeNewOrEditModal();
    Notification.show(referentPedagogique.getPrenomReferentPedago() + " " + referentPedagogique.getNomReferentPedago() + " modifiée(e)");
  }

  private void transfertEtudiantFromEventToDelete(ReferentPedagogiqueConsult.DeleteEvent event) {
    ReferentPedagogique referentPedagogique = event.getReferentPedagogique();
    deleteReferentPedagogique(referentPedagogique);
  }

  private void deleteReferentPedagogique(ReferentPedagogique referentPedago) {
    if (referentPedago != null) {
      referentPedagogiqueService.removeReferentPedagogique(referentPedago);

      logEnregistrmentService.saveLogDeleteString(referentPedago.toString());

      updateList();
      closeConsultModal();
      Notification.show(referentPedago.getPrenomReferentPedago() + " " + referentPedago.getNomReferentPedago() + " retirée(e)");
    }
  }

  private void deleteFromConfirmDelete(ConfirmDelete.DeleteEventGrid event) {
    Boolean supprimer = event.getSuppression();
    if (supprimer) {
      deleteReferentPedagogique(referentPedagogiqueMaybeToDelete);
    }
    referentPedagogiqueMaybeToDelete = null;
    closeConfirmDelete();
  }

  private void prepareToDelete(ReferentPedagogique referentPedagogique) {
    referentPedagogiqueMaybeToDelete = referentPedagogique;
    openConfirmDelete();
  }

  public void editReferentModal(ReferentPedagogique referentPedagogique) {
    if (referentPedagogique == null) {
      closeNewOrEditModal();
    } else {
      modalNewOrEdit.setReferentPedagogique(referentPedagogique);
      modalNewOrEdit.open();
      addClassName("editing");
    }
  }

  void addReferent() {
    grid.asSingleSelect().clear();
    editReferentModal(new ReferentPedagogique());
  }

  public void consultReferentPedagogique(ReferentPedagogique referentPedagogique) {
    modalConsult.setReferentPedagogique(referentPedagogique);
    modalConsult.open();
  }

  private void closeConsultModal() {
    modalConsult.setReferentPedagogique(null);
    modalConsult.close();
    grid.asSingleSelect().clear();
  }

  private void closeNewOrEditModal() {
    modalNewOrEdit.setReferentPedagogique(null);
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
    grid.setItems(referentPedagogiqueService.findAllReferentPedagogique(filterText.getValue()));
  }

}
