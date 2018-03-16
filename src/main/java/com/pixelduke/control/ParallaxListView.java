package com.pixelduke.control;

import impl.com.pixelduke.skin.ParallaxListViewSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.image.ImageView;

public class ParallaxListView<T> extends Control {
    private static final String DEFAULT_STYLE_CLASS = "parallax-list-view";

    public ParallaxListView() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }

    public ParallaxListView(ObservableList<T> items) {
        this();
        setItems(items);
    }


    final ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<>(this, "items");
    public ObservableList<T> getItems() {
        return items.get();
    }
    public void setItems(ObservableList<T> items) {
        this.items.set(items);
    }
    public ObjectProperty<ObservableList<T>> itemsProperty() {
        return items;
    }


    public ObjectProperty<ImageView> backgroundNodeProperty() {
        return backgroundNode;
    }
    public void setBackgroundNode(ImageView backgroundNode) {
        this.backgroundNode.set(backgroundNode);
    }
    public ImageView getBackgroundNode() {
        return this.backgroundNode.get();
    }
    private ObjectProperty<ImageView> backgroundNode = new SimpleObjectProperty<>();


    @Override
    public String getUserAgentStylesheet() {
        return ParallaxListView.class.getResource("parallaxListView.css").toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ParallaxListViewSkin<T>(this);
    }

}
