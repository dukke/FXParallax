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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
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


    private final ObjectProperty<ObservableList<T>> items = new SimpleObjectProperty<>(this, "items");
    public final ObservableList<T> getItems() {
        return items.get();
    }
    public final void setItems(ObservableList<T> items) {
        this.items.set(items);
    }
    public final ObjectProperty<ObservableList<T>> itemsProperty() {
        return items;
    }


    private final ObjectProperty<ImageView> backgroundImage = new SimpleObjectProperty<>();
    public final ObjectProperty<ImageView> backgroundImageProperty() {
        return backgroundImage;
    }
    public final void setBackgroundImage(ImageView backgroundNode) {
        this.backgroundImage.set(backgroundNode);
    }
    public final ImageView getBackgroundImage() {
        return this.backgroundImage.get();
    }



    @Override
    public String getUserAgentStylesheet() {
        return ParallaxListView.class.getResource("parallaxListView.css").toExternalForm();
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ParallaxListViewSkin<T>(this);
    }

}
