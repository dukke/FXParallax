/*
 * Copyright (c) 2018 Pixel Duke (Pedro Duque Vieira - www.pixelduke.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *   * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *   * Neither the name of Pixel Duke, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL PIXEL DUKE BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
