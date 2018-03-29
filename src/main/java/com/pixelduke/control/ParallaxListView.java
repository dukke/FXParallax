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

import impl.com.pixelduke.skin.ParallaxListViewSkin;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
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


    public final ObjectProperty<ObservableList<T>> itemsProperty() {
        return items;
    }
    public final ObservableList<T> getItems() {
        return items.get();
    }
    public final void setItems(ObservableList<T> items) {
        this.items.set(items);
    }
    private final ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<>(this, "items");


    public final ObjectProperty<ImageView> backgroundImageProperty() {
        return backgroundImage;
    }
    public final void setBackgroundImage(ImageView backgroundNode) {
        this.backgroundImage.set(backgroundNode);
    }
    public final ImageView getBackgroundImage() {
        return this.backgroundImage.get();
    }
    private final ObjectProperty<ImageView> backgroundImage = new SimpleObjectProperty<>();

    /**
     * The amount of scroll that is done in the List when the user performs a scroll by, for example, scrolling the
     * mouse wheel.
     *
     * @defaultValue 150
     * @return A {@link DoubleProperty} with the scroll amount
     */
    public final DoubleProperty defaultScrollAmountProperty() { return defaultScrollAmount; }
    public final void setDefaultScrollAmount(double amount) { defaultScrollAmount.set(amount); }
    public final double getDefaultScrollAmount() { return defaultScrollAmount.get(); }
    private final SimpleDoubleProperty defaultScrollAmount = new SimpleDoubleProperty(150);

    /**
     * The orientation of the List, equal to {@link javafx.scene.control.ListView} orientation property.
     * @defaultValue Horizontal
     * @return A {@link ObjectProperty} with the orientation
     */
    public final ObjectProperty<Orientation> orientationProperty() { return orientation; }
    public final void setOrientation(Orientation orientation) { this.orientation.set(orientation); }
    public final Orientation getOrietation() { return this.orientation.get(); }
    private final ObjectProperty<Orientation> orientation = new SimpleObjectProperty<>(Orientation.VERTICAL);


    private final DoubleProperty sizeDifference = new SimpleDoubleProperty(100);
    public final DoubleProperty sizeDifferenceProperty() { return sizeDifference; }
    public final void setSizeDifference(double value) { sizeDifference.set(value); }
    public final double getSizeDifference() { return sizeDifference.get(); }


    @Override
    public String getUserAgentStylesheet() {
        return ParallaxListView.class.getResource("parallaxListView.css").toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ParallaxListViewSkin<>(this);
    }

}
