package nc.unc.application.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;
import nc.unc.application.data.entity.User;
import nc.unc.application.security.AuthenticatedUser;
import nc.unc.application.views.accueil.HomeView;
import nc.unc.application.views.contrat.ContratView;
import nc.unc.application.views.entreprise.EntrepriseView;
import nc.unc.application.views.entretienCollectif.EntretienCollectifView;
import nc.unc.application.views.entretienIndividuelle.EntretienIndividuelView;
import nc.unc.application.views.etudiant.EtudiantView;
import nc.unc.application.views.evenement.EvenementView;
import nc.unc.application.views.formation.FormationListView;
import nc.unc.application.views.logs.LogsView;
import nc.unc.application.views.referentCfa.ReferentCfaView;
import nc.unc.application.views.referentPedagogique.ReferentPedagogiqueView;
import nc.unc.application.views.tuteur.TuteurView;
import nc.unc.application.views.user.UserView;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    /**
     * A simple navigation item component, based on ListItem element.
     */
    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            // Use Lumo classnames for various styling
            link.addClassNames("flex", "mx-s", "p-s", "relative", "text-secondary");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            // Use Lumo classnames for various styling
            text.addClassNames("font-medium", "text-s");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                // Use Lumo classnames for suitable font size and margin
                addClassNames("me-s", "text-l");
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }

    }

    private H1 viewTitle;

    private AuthenticatedUser authenticatedUser;
    private AccessAnnotationChecker accessChecker;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassName("text-secondary");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("m-0", "text-l");

        Header header = new Header(toggle, viewTitle);
        header.addClassNames("bg-base", "border-b", "border-contrast-10", "box-border", "flex", "h-xl", "items-center",
                "w-full");
        return header;
    }

    private Component createDrawerContent() {


        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(
                createNavigation(), createFooter());
        section.addClassNames("flex", "flex-col", "items-stretch", "max-h-full", "min-h-full");
        return section;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        Div div = new Div();

        Image image = new Image("images/CFA.png","logo cfa");
        div.add(image);
        div.addClassNames("cfa-container-icon");

        image.addClassNames("cfa-icon");
        nav.add(div);
        nav.addClassNames("menu", "border-b", "border-contrast-10", "flex-grow", "overflow-auto");
        nav.getElement().setAttribute("aria-labelledby", "views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("list-none", "m-0", "p-0");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            // on ajoute à la navbar les éléments auxquels l'utilisateur a le droit d'accéder
            if (accessChecker.hasAccess(menuItem.getView())) {
                list.add(menuItem);
            }

        }
        return nav;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ // navbar

                new MenuItemInfo("Accueil", "la la-file menu-icon", HomeView.class),

                new MenuItemInfo("Étudiants", "las la-user-graduate menu-icon", EtudiantView.class),

                new MenuItemInfo("Tuteurs", "las la-user-tie menu-icon", TuteurView.class),

                new MenuItemInfo("Entreprises", "las la-building menu-icon", EntrepriseView.class),

                new MenuItemInfo("Contrats", "las la-file-contract menu-icon", ContratView.class),

                new MenuItemInfo("Référent Pédagogique", "las la-chalkboard-teacher menu-icon", ReferentPedagogiqueView.class),

                new MenuItemInfo("Formations", "las la-award menu-icon", FormationListView.class),

                new MenuItemInfo("Evénements", "las la-calendar menu-icon", EvenementView.class),

                new MenuItemInfo("Référents CFA", "las la-user-shield menu-icon", ReferentCfaView.class),

                new MenuItemInfo("Entretiens Individuels", "las la-user-friends menu-icon", EntretienIndividuelView.class),

                new MenuItemInfo("Entretiens Collectifs", "las la-users menu-icon", EntretienCollectifView.class),

                new MenuItemInfo("Utilisateurs", "las la-user-cog menu-icon", UserView.class),

                new MenuItemInfo("Logs", "las la-history menu-icon", LogsView.class)
        };
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("flex", "items-center", "my-s", "px-m", "py-xs");

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();

            Span name = new Span(user.getPrenom()+" "+user.getNom()+" \uD83D\uDEAA");
            name.addClassNames("font-medium", "text-s", "text-secondary","text-center");

            ContextMenu userMenu = new ContextMenu(name);
            userMenu.setOpenOnClick(true);
            userMenu.addItem("Se déconnecter", e -> {
                authenticatedUser.logout();
            });

            layout.add(name);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
