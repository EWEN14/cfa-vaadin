package nc.unc.application;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.Theme;
import lombok.extern.slf4j.Slf4j;
import nc.unc.application.data.service.cron.InactiveContractCronTask;
import nc.unc.application.data.service.cron.SuppressLogCronTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@EnableScheduling
@Slf4j
@SpringBootApplication
@Theme(value = "cfavaadin-custom")
@PWA(name = "cfa_vaadin", shortName = "cfa_vaadin", offlineResources = {"images/logo.png"})
@NpmPackage(value = "line-awesome", version = "1.3.0")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        SpringApplication.run(InactiveContractCronTask.class);
        SpringApplication.run(SuppressLogCronTask.class);
    }

}
