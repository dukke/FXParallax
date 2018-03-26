package impl.com.pixelduke.skin;

import com.pixelduke.control.ParallaxListView;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.animation.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ParallaxListViewSkin<T> extends SkinBase<ParallaxListView<T>> {
    private final ScrollPane backgroundScrollPane = new ScrollPane();
    private final ListView<T> listView = new ListView<>();

    private final int VERTICAL_DIFFERENCE = 200;
    private final int DEFAULT_SCROLL_AMOUNT = 150;
    private final Duration ANIMATION_DURATION = Duration.millis(200);

    private VirtualFlow listViewFlow;
    private ScrollBar listViewScrollBar;

    private int isAlreadyScroolling;

    private Rectangle rect = new Rectangle();

    private final int CELL_HEIGHT = 40;

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public ParallaxListViewSkin(ParallaxListView<T> control) {
        super(control);

        backgroundScrollPane.setPannable(false);

        ImageView backgroundNode = control.getBackgroundNode();
        rect.setFill(Color.BLACK);
        rect.widthProperty().bind(backgroundNode.fitWidthProperty());
        rect.heightProperty().bind(backgroundNode.fitHeightProperty());
        rect.setOpacity(0.5);
        Group group = new Group();
        group.getChildren().addAll(backgroundNode, rect);

        backgroundScrollPane.setContent(group);
        control.backgroundNodeProperty().addListener(observable -> backgroundScrollPane.setContent(control.getBackgroundNode()));

//        updateBackgroundSize();
        updateListViewVirtualFlow();
        updateListViewScrollListeners();

        listView.itemsProperty().bind(control.itemsProperty());
        listView.heightProperty().addListener(observable -> updateBackgroundSize());
//        backgroundNode.fitWidthProperty().bind(listView.widthProperty());

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
//                            ImageView backgroundNode = getSkinnable().getBackgroundNode();
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

    static double inc = 0;
    private final EventHandler<ScrollEvent> listViewScrollListener = event -> {
        ++isAlreadyScroolling;
        double eventScroll = event.getDeltaY();
        double scrollValue = - Math.signum(eventScroll) * DEFAULT_SCROLL_AMOUNT;

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
            parallelTransition.setOnFinished(onFinishedEvent -> --isAlreadyScroolling);
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
//        BooleanBinding listViewShowing = Bindings.selectBoolean(listView.sceneProperty(), "window", "showing");
        if (listView.getSkin() != null) {
            ImageView backgroundNode = getSkinnable().getBackgroundNode();
//            double listItemsHeight = calculateListItemsHeight();
            backgroundNode.setPreserveRatio(true); // TODO: Maybe we shoulsdn't be changing this property
            backgroundNode.setFitHeight(listView.getHeight() + VERTICAL_DIFFERENCE);
            if (backgroundNode.getBoundsInParent().getWidth() < listView.getWidth()) {
                backgroundNode.setFitWidth(listView.getWidth());
                backgroundNode.setFitHeight(0);
            }

        }
//        else {
//            listView.skinProperty().addListener(new InvalidationListener() {
//                @Override
//                public void invalidated(Observable observable) {
//                    ImageView backgroundNode = getSkinnable().getBackgroundNode();
////                    double listItemsHeight = calculateListItemsHeight();
//                    backgroundNode.setPreserveRatio(true); // TODO: Maybe we shoulsdn't be changing this property
//                    backgroundNode.setFitHeight(listView.getHeight() + VERTICAL_DIFFERENCE);
//                    listView.skinProperty().removeListener(this);
//                }
//            });
//        }

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
