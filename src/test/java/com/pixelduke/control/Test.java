package com.pixelduke.control;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.scenicview.ScenicView;

public class Test extends Application {

    @Override
    public void start(Stage primaryStage) {
        BorderPane rootNode = new BorderPane();

        ParallaxListView parallaxListView = new ParallaxListView(FXCollections.observableArrayList("Portugal", "Spain", "United States",
                "Israel", "South Africa", "New Zealand", "Turkey", "Denmark", "Sweden", "Angola", "Canada", "Argentina",
                "Brazil", "Uruguai", "South Korea", "England", "Ireland", "Scotland", "Wales", "Australia", "Peru", "Palestine", "Portugal", "Spain", "United States",
                "Israel", "South Africa", "New Zealand", "Turkey", "Denmark", "Sweden", "Angola", "Canada", "Argentina",
                "Brazil", "Uruguai", "South Korea", "England", "Ireland", "Scotland", "Wales", "Australia", "Peru", "Palestine", "Portugal", "Spain", "United States",
                "Israel", "South Africa", "New Zealand", "Turkey", "Denmark", "Sweden", "Angola", "Canada", "Argentina",
                "Brazil", "Uruguai", "South Korea", "England"));
        ImageView image = new ImageView(new Image(Test.class.getResource("bay-landscape-wallpaper-725x483.jpg").toExternalForm()));
        parallaxListView.setBackgroundNode(image);

        parallaxListView.setPrefWidth(993);

        rootNode.setCenter(parallaxListView);

        Scene scene = new Scene(rootNode);

//        ScenicView.show(scene);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
