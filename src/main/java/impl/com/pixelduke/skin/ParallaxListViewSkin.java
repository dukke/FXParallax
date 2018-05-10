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

import com.pixelduke.control.ParallaxListView;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.animation.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

import static impl.com.pixelduke.skin.Utils.changeImageSize;

public class ParallaxListViewSkin<T> extends SkinBase<ParallaxListView<T>> {
    private final Duration ANIMATION_DURATION = Duration.millis(200);

    private final ScrollPane backgroundScrollPane = new ScrollPane();
    private final ListView<T> listView = new ListView<>();

    private VirtualFlow listViewFlow;
    private ScrollBar listViewScrollBar;

    public ParallaxListViewSkin(ParallaxListView<T> control) {
        super(control);

        backgroundScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        backgroundScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        ImageView backgroundNode = control.getBackgroundImage();
        backgroundScrollPane.setContent(backgroundNode);

        control.backgroundImageProperty().addListener(observable -> backgroundScrollPane.setContent(control.getBackgroundImage()));

        updateListViewVirtualFlow();
        updateListViewScrollListeners();

        listView.orientationProperty().bind(control.orientationProperty());
        listView.itemsProperty().bind(control.itemsProperty());
        listView.cellFactoryProperty().bind(control.cellFactoryProperty());
        listView.selectionModelProperty().bind(control.selectionModelProperty());
        listView.focusModelProperty().bind(control.focusModelProperty());

        listView.heightProperty().addListener(observable -> updateBackgroundSize());

        getChildren().addAll(backgroundScrollPane, listView);
    }

    private void updateListViewVirtualFlow() {
        listView.skinProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                listViewFlow = (VirtualFlow) listView.lookup(".virtual-flow");
                listViewScrollBar = (ScrollBar) listView.lookup(".scroll-bar");

                // Setup ScrollBar listeners
                listViewScrollBar.valueProperty().addListener(observable1 -> {
                    double listViewScrollAmount = listViewScrollBar.getValue();
                    double listViewScrollPercentage = listViewScrollAmount / listViewScrollBar.getMax();

                    backgroundScrollPane.setVvalue(listViewScrollPercentage);
                });

                listView.skinProperty().removeListener(this);
            }
        });
    }

    private void updateListViewScrollListeners() {
        listView.addEventFilter(ScrollEvent.ANY, listViewScrollListener);
    }

    private final EventHandler<ScrollEvent> listViewScrollListener = event -> {
        Orientation controlOrientation = getSkinnable().getOrietation();

        double eventScroll = event.getDeltaY();
        double scrollValue = - Math.signum(eventScroll) * getSkinnable().getDefaultScrollAmount();

        double listViewItemsSize = calculateListItemsSize();

        double listViewPosition = listViewFlow.getPosition();
        if(listViewPosition < 1 &&  listViewPosition > 0 || listViewPosition == 1 && scrollValue < 0 || listViewPosition == 0 && scrollValue > 0) {
            double oldBackgroundScrollValue;
            if (controlOrientation.equals(Orientation.VERTICAL)) {
                oldBackgroundScrollValue = backgroundScrollPane.getVvalue();
            } else {
                oldBackgroundScrollValue = backgroundScrollPane.getHvalue();
            }
            double percentageScroll = scrollValue / listViewItemsSize;

            double newBackgroundScrollValue;
            if (controlOrientation.equals(Orientation.VERTICAL)) {
                newBackgroundScrollValue = oldBackgroundScrollValue + percentageScroll;
            } else {
                newBackgroundScrollValue = oldBackgroundScrollValue + percentageScroll;
            }

            final Timeline timeline = new Timeline();
            KeyValue endKeyValue;
            if (controlOrientation.equals(Orientation.VERTICAL)) {
                endKeyValue = new KeyValue(backgroundScrollPane.vvalueProperty(), newBackgroundScrollValue, Interpolator.EASE_IN);
            } else {
                endKeyValue = new KeyValue(backgroundScrollPane.hvalueProperty(), newBackgroundScrollValue, Interpolator.EASE_IN);
            }
            final KeyFrame keyFrame = new KeyFrame(ANIMATION_DURATION, endKeyValue);
            timeline.getKeyFrames().add(keyFrame);

            Transition listViewTransition = new Transition() {
                double oldPosition;
                {
                    setCycleDuration(ANIMATION_DURATION);
                    oldPosition = listViewFlow.getPosition();
                }

                @Override
                protected void interpolate(double frac) {

                    listViewFlow.setPosition(oldPosition + percentageScroll * frac);
                }
            };
            listViewTransition.setInterpolator(Interpolator.EASE_OUT);

            ParallelTransition parallelTransition = new ParallelTransition(timeline, listViewTransition);
            parallelTransition.play();
        }

        event.consume();
    };

    private double calculateListItemsSize() {
        double fixedCellSize = listView.getFixedCellSize();
        double cellSize;
        if (fixedCellSize > 0) { // Cell heights are fixed
            cellSize = fixedCellSize;
        } else {
            // We will calculate the cell size to be the size of the first cell.
            // Afterwards we will assume all cells to be that same size.
            ListCell cell = (ListCell) listView.lookup(".list-cell");
            if (getSkinnable().getOrietation().equals(Orientation.VERTICAL)) {
                cellSize = cell.getHeight();
            } else {
                cellSize = cell.getWidth();
            }
        }

        return cellSize * listView.getItems().size();
    }

    private void updateBackgroundSize() {
        ParallaxListView<T> control = getSkinnable();
        if (listView.getSkin() != null) {
            ImageView backgroundImage = getSkinnable().getBackgroundImage();
            if (getSkinnable().getOrietation().equals(Orientation.VERTICAL)) {
                changeImageSize(backgroundImage, listView.getWidth(), listView.getHeight() + control.getSizeDifference());
            } else {
                changeImageSize(backgroundImage, listView.getWidth() + control.getSizeDifference(), listView.getHeight());
            }
        }
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        backgroundScrollPane.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
        listView.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
    }

    // Pref Sizes
    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
        return listView.prefWidth(-1);
    }

    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return listView.prefHeight(-1);
    }


}
