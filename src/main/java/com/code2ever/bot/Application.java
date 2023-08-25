package com.code2ever.bot;

import com.code2ever.bot.data.service.BatchService;
import com.code2ever.bot.data.service.BillService;
import com.code2ever.bot.data.service.PendingPaymentRegister;
import com.code2ever.bot.data.service.PendingPaymentService;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The entry point of the Spring Boot application.
 *
 * Use the @PWA annotation make the application installable on phones, tablets
 * and some desktop browsers.
 *
 */
@SpringBootApplication
@EnableScheduling
@Theme(value = "bot", variant = Lumo.DARK)
public class Application implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    PendingPaymentRegister pendingPaymentRegister(BillService billService,
                                                  PendingPaymentService pendingPaymentService, BatchService batchService){
        return new PendingPaymentRegister(billService,pendingPaymentService, batchService);
    }
//    @Bean
//    SqlDataSourceScriptDatabaseInitializer dataSourceScriptDatabaseInitializer(DataSource dataSource,
//            SqlInitializationProperties properties, SamplePersonRepository repository) {
//        // This bean ensures the database is only initialized when empty
//        return new SqlDataSourceScriptDatabaseInitializer(dataSource, properties) {
//            @Override
//            public boolean initializeDatabase() {
//                if (repository.count() == 0L) {
//                    return super.initializeDatabase();
//                }
//                return false;
//            }
//        };
//    }
}
