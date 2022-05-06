package nc.unc.application.views.login;

import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.login.LoginOverlay;
import com.vaadin.flow.router.*;

import java.util.List;
import java.util.Map;

@PageTitle("Login")
@Route(value = "login")
public class LoginView extends LoginOverlay {

    public LoginView() {
        setAction("login");

        // affichage erreur si mauvais identifiant ou mot de passe
        /* if (this.isError()) {
            this.setError(true);
        }*/

        LoginI18n i18n = LoginI18n.createDefault();
        i18n.setHeader(new LoginI18n.Header());
        i18n.getHeader().setTitle("CFA");
        i18n.getHeader().setDescription("Veuillez entrer votre nom d'utilisateur et votre mot de passe.");
        // i18n.setAdditionalInformation(null);



        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Connexion");
        i18nForm.setUsername("Identifiant");
        i18nForm.setPassword("Mot de passe");
        i18nForm.setSubmit("Connexion");
        i18nForm.setForgotPassword("Mot de passe oublié ?");

        setI18n(i18n);

        setForgotPasswordButtonVisible(false);
        setOpened(true);
    }
}
