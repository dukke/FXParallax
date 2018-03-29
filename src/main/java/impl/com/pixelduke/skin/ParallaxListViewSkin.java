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
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.util.Duration;

public class ParallaxListViewSkin<T> extends SkinBase<ParallaxListView<T>> {
    private final ScrollPane backgroundScrollPane = new ScrollPane();
    private final ListView<T> listView = new ListView<>();

    private final int VERTICAL_DIFFERENCE = 100;
    private final Duration ANIMATION_DURATION = Duration.millis(200);

    private VirtualFlow listViewFlow;
    private ScrollBar listViewScrollBar;

    private int isAlreadyScrolling;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public ParallaxListViewSkin(ParallaxListView<T> control) {
        super(control);

        backgroundScrollPane.setPannable(false);
        ImageView backgroundNode = control.getBackgroundImage();
        backgroundScrollPane.setContent(backgroundNode);

        control.backgroundImageProperty().addListener(observable -> backgroundScrollPane.setContent(control.getBackgroundImage()));

        updateListViewVirtualFlow();
        updateListViewScrollListeners();

        listView.itemsProperty().bind(control.itemsProperty());
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
                listViewScrollBar.valueProperty().addListener(new InvalidationListener() {
                    @Override
                    public void invalidated(Observable observable) {
//                        if (isAlreadyScroolling == 0) {
//                            ImageView backgroundNode = getSkinnable().getBackgroundImage();
//
//                            double listViewScrollAmount = listViewScrollBar.getValue();
//                            double listViewScrollPercentage = listViewScrollAmount / listViewScrollBar.getMax();
//                            double backgroundNewPosition = calculateListItemsHeight() * listViewScrollPercentage;
//
//                            double backgroundScrollPaneAmount = backgroundScrollPane.getVmax() - backgroundScrollPane.getVmin();
//                            backgroundScrollPane.setVvalue(backgroundScrollPane.getVmin() + backgroundScrollPaneAmount * backgroundNewPosition / calculateListItemsHeight());
//                        }
                    }
                });

                listView.skinProperty().removeListener(this);
            }
        });
    }

    private void updateListViewScrollListeners() {
        listView.addEventFilter(ScrollEvent.ANY, listViewScrollListener);
    }

    private final EventHandler<ScrollEvent> listViewScrollListener = event -> {
        ++isAlreadyScrolling;
        double eventScroll = event.getDeltaY();
        double scrollValue = - Math.signum(eventScroll) * getSkinnable().getDefaultScrollAmount();

        double listViewItemsHeight = calculateListItemsHeight();

        double listViewPosition = listViewFlow.getPosition();
        if(listViewPosition < 1 &&  listViewPosition > 0 || listViewPosition == 1 && scrollValue < 0 || listViewPosition == 0 && scrollValue > 0) {
            double oldVBackgroundValue = backgroundScrollPane.getVvalue();
            double percentageScroll = scrollValue / listViewItemsHeight;

            double newBackgroundVValue = oldVBackgroundValue + percentageScroll * (backgroundScrollPane.getVmax() - backgroundScrollPane.getVmin());

            final Timeline timeline = new Timeline();
            final KeyValue endKeyValue = new KeyValue(backgroundScrollPane.vvalueProperty(), newBackgroundVValue, Interpolator.EASE_IN);
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
            parallelTransition.setOnFinished(onFinishedEvent -> --isAlreadyScrolling);
            parallelTransition.play();
        }

        event.consume();
    };

    private double calculateListItemsHeight() {
        double fixedCellSize = listView.getFixedCellSize();
        double cellHeight;
        if (fixedCellSize > 0) { // Cell heights are fixed
            cellHeight = fixedCellSize;

        } else {
            // We will calculate the cell height to be the height of the first cell.
            // Afterwards we will assume all cells to be that same height.
            ListCell cell = (ListCell) listView.lookup(".list-cell");
            cellHeight = cell.getHeight();
        }
//        else {
//            Set<Node> nodes = listView.lookupAll(".list-cell");
//            for (Node node : nodes) {
//                ListCell cell = (ListCell) node;
//                listViewHeight += cell.getHeight();
//            }
//        }
        return cellHeight * listView.getItems().size();
    }

    private void updateBackgroundSize() {
        if (listView.getSkin() != null) {
            ImageView backgroundImage = getSkinnable().getBackgroundImage();
            changeImageHeight(backgroundImage, listView.getWidth(), listView.getHeight() + VERTICAL_DIFFERENCE);
        }
    }

    // Utility method to change an ImageView size by filling the bounding box with width = targetWidth and height = targetHeight
    // the image ratio will be preserved.
    private static void changeImageHeight(ImageView imageView, double targetWidth, double targetHeight) {
        double imageHeight = imageView.getBoundsInLocal().getHeight();
        double imageWidth = imageView.getBoundsInLocal().getWidth();
        double newHeightPercentage = targetHeight / imageHeight;
        double newWidthPercentage = targetWidth / imageWidth;

        double newSizePercentage;
        if (newWidthPercentage < newHeightPercentage) {
            // We can change the width by newHeightPercentage and reach the targetWidth
            newSizePercentage = newHeightPercentage;
        } else {
            // We can change the height by NewWidthPercentage and reach the targetHeight
            newSizePercentage = newWidthPercentage;
        }
        imageView.setFitHeight(imageHeight * newSizePercentage);
        imageView.setFitWidth(imageWidth * newSizePercentage);
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
