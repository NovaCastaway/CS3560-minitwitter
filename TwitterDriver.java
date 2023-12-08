package twitterdriver;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TwitterDriver extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        AdminPanel adminPanel=AdminPanel.getInstance();
        HBox adminView=adminPanel.getAdminPanel();
        
        Scene scene=new Scene(adminView, 570 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Control Panel");
        primaryStage.show();
    }
    
}
