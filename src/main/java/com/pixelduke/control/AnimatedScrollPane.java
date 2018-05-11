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

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

public class AnimatedScrollPane extends ScrollPane {
    private static final int DEFAULT_SCROLL_AMOUNT = 150;
    private final Duration ANIMATION_DURATION = Duration.millis(200);

//    ScrollBar scrollBar;


    public AnimatedScrollPane() {
//        skinProperty().addListener(new InvalidationListener() {
//            @Override
//            public void invalidated(Observable observable) {
//                scrollBar = (ScrollBar) lookup(".scroll-bar");
//
//                // Setup ScrollBar listeners
//                scrollBar.valueProperty().addListener(observable1 -> {
//                    double scrollAmount = scrollBar.getValue();
//                    double scrollPercentage = scrollAmount / scrollBar.getMax();
//                    setVvalue(scrollPercentage);
//                });
//
//                skinProperty().removeListener(this);
//            }
//        });

        addEventFilter(ScrollEvent.ANY, event -> {
            double eventScroll = - event.getDeltaY();
            double verticalPosition = getVvalue();
            double vMax = getVmax();
            double vMin = getVmin();

            if(verticalPosition < vMax &&  verticalPosition > vMin || verticalPosition == vMax && eventScroll < 0 || verticalPosition == vMin && eventScroll > 0) {
                double oldScrollValue = getVvalue();
                double scrollValue = (Math.signum(eventScroll) * DEFAULT_SCROLL_AMOUNT) / getHeight();
                double newScrollValue = oldScrollValue  + scrollValue;

                // Animate
                final Timeline timeline = new Timeline();
                KeyValue endKeyValue = new KeyValue(vvalueProperty(), newScrollValue, Interpolator.EASE_BOTH);
                final KeyFrame keyFrame = new KeyFrame(ANIMATION_DURATION, endKeyValue);
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
            }

            event.consume();
        });
    }

    public AnimatedScrollPane(Node content) {
        this();
        setContent(content);
    }


}
