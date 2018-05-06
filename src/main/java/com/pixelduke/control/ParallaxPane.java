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

import impl.com.pixelduke.skin.ParallaxPaneSkin;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ParallaxPane extends Control {
    private static final String DEFAULT_STYLE_CLASS = "parallax-pane";

    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/

    public ParallaxPane() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
    }

    public ParallaxPane(Image image){
        this();
        setImage(image);
    }

    public final void setImage(Image image) {
        ImageView imageView = new ImageView(image);
        setContent(imageView);
    }

    /* I just have this method because we need to have a method with this name to be able to have an  image property in FXML
       not intended to be used. */
    public final Image getImage() {
        return null;
    }

    /***************************************************************************
     *                                                                         *
     * Properties                                                              *
     *                                                                         *
     **************************************************************************/

    private final ObjectProperty<Node> content = new SimpleObjectProperty<>();
    public final void setContent(Node content) { this.content.set(content); }
    public final Node getContent() { return content.get(); }
    public final ObjectProperty<Node> contentProperty() { return content; }

    private final IntegerProperty verticalSizeDifference = new SimpleIntegerProperty(100);
    public final void setVerticalSizeDifference(int value) { verticalSizeDifference.set(value); }
    public final int getVerticalSizeDifference() { return verticalSizeDifference.get(); }
    public final IntegerProperty verticalSizeDifferenceProperty() { return verticalSizeDifference; }


    /* SKIN */

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ParallaxPaneSkin(this);
    }

    @Override
    public String getUserAgentStylesheet() {
        return ParallaxListView.class.getResource("parallaxPane.css").toExternalForm();
    }
}
