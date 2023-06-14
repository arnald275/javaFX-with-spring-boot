package com.example.javaFX;
import com.example.javaFX.event.StageReadyEvent;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

public class JavaFxMainApp extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception{
        ApplicationContextInitializer<GenericApplicationContext> initializer = new ApplicationContextInitializer<GenericApplicationContext>() {
            @Override
            public void initialize(GenericApplicationContext applicationContext) {
                applicationContext.registerBean(Application.class,()->JavaFxMainApp.this);
                applicationContext.registerBean(Parameters.class,()->getParameters());
                applicationContext.registerBean(HostServices.class,()->getHostServices());
            }
        };

        this.context = new SpringApplicationBuilder()
                .sources(JavaFxMainApp.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.context.publishEvent(new StageReadyEvent(primaryStage));
    }

    @Override
    public void stop() throws Exception {
        this.context.stop();
        Platform.exit();
    }
}
