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

package impl.com.pixelduke.skin;

import com.pixelduke.control.ParallaxPane;
import javafx.animation.AnimationTimer;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;

public class ParallaxPaneSkin extends SkinBase<ParallaxPane> {
    private ScrollPane scrollPane;

    private double screenHeight;

    private double lastControlYPosition;

    private double minPos, maxPos;

    private double lastContentWidth, lastContentHeight;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public ParallaxPaneSkin(ParallaxPane control) {
        super(control);

        scrollPane = new ScrollPane();

        initScrollPane();

        getChildren().add(scrollPane);

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateScroll();
            }
        };
        animationTimer.start();
    }


    private void initScrollPane() {
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.contentProperty().bind(getSkinnable().contentProperty());
        scrollPane.setMouseTransparent(true);

    }

    private void updateScroll() {
        double controlYPosition = getSkinnable().localToScreen(0, 0).getY();
        if (lastControlYPosition == controlYPosition) {
            // Control Position hasn't changed.
            return;
        }

        ParallaxPane control = getSkinnable();
        // Control position has changed or this is the first time the method is called

        lastControlYPosition = controlYPosition;

        if (screenHeight == 0) { // This is the first time this method is called
            screenHeight = getSkinnable().getScene().getWindow().getHeight();
            minPos = - control.getBoundsInLocal().getHeight();
            maxPos = screenHeight;
        }

        double newScrollValue;
        if (controlYPosition <= minPos) {
            newScrollValue = 0;
        } else if (controlYPosition >= maxPos) {
            newScrollValue = 1;
        } else {
            double distanceFromMin = controlYPosition - minPos;
            double maxDistanceFromMin = maxPos - minPos;
            newScrollValue = distanceFromMin / maxDistanceFromMin;
        }
        scrollPane.setVvalue(newScrollValue);
    }

    private void updateNodeSize() {
        int verticalSizeDifference = getSkinnable().getVerticalSizeDifference();
        double scrollPaneHeight = scrollPane.getHeight();
        double currentContentHeight = getSkinnable().getContent().getBoundsInLocal().getHeight();
        double expectedContentHeight = scrollPaneHeight + verticalSizeDifference;

        if (currentContentHeight != expectedContentHeight) {
            Utils.changeImageSize(getSkinnable().getContent(), getSkinnable().getWidth(), expectedContentHeight);
        }
    }



    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        scrollPane.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
        if (lastContentWidth != contentWidth || lastContentHeight != contentHeight) {
            updateNodeSize();
            lastContentWidth = contentWidth;
            lastContentHeight = contentHeight;
        }
    }
}
