package impl.com.pixelduke.skin;

import com.pixelduke.control.ParallaxListView;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.animation.*;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SkinBase;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ParallaxListViewSkin<T> extends SkinBase<ParallaxListView<T>> {
    private final ScrollPane backgroundScrollPane = new ScrollPane();
    private final ListView<T> listView = new ListView<>();

    private final int VERTICAL_DISPLACEMENT = 5000;
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
    public ParallaxListViewSkin(ParallaxListView control) {
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

        updateBackgroundSize();
        updateListViewVirtualFlow();
        updateListViewScrollListeners();

        listView.itemsProperty().bind(control.itemsProperty());
        listView.itemsProperty().addListener(observable -> updateBackgroundSize());
        backgroundNode.fitWidthProperty().bind(listView.widthProperty());



        getChildren().addAll(backgroundScrollPane, listView);
    }

    @Override
    protected void layoutChildren(double contentX, double contentY, double contentWidth, double contentHeight) {
        backgroundScrollPane.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
        listView.resizeRelocate(contentX, contentY, contentWidth, contentHeight);
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

    private final EventHandler<ScrollEvent> listViewScrollListener = event -> {
        ++isAlreadyScroolling;
        double eventScroll = event.getDeltaY();
        double scrollValue = - eventScroll;

        double listViewPosition = listViewFlow.getPosition();
        if(listViewPosition < 1 &&  listViewPosition > 0 || listViewPosition == 1 && scrollValue < 0 || listViewPosition == 0 && scrollValue > 0) {
            double oldVValue = backgroundScrollPane.getVvalue();
            double newVValue = oldVValue + scrollValue / VERTICAL_DISPLACEMENT;

            final Timeline timeline = new Timeline();
            final KeyValue endKeyValue = new KeyValue(backgroundScrollPane.vvalueProperty(), newVValue, Interpolator.EASE_IN);
            final KeyFrame keyFrame = new KeyFrame(ANIMATION_DURATION, endKeyValue);
            timeline.getKeyFrames().add(keyFrame);

            Transition listViewTransition = new Transition() {
                final static double INCREMENT = 0.0025;

                {
                    setCycleDuration(ANIMATION_DURATION);
                }

                @Override
                protected void interpolate(double frac) {
                    double oldPosition = listViewFlow.getPosition();
                    listViewFlow.setPosition(oldPosition + INCREMENT * frac * Math.signum(scrollValue));
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
        double listViewHeight = 0;
//        if (fixedCellSize > 0) { // Cell heights are fixed
//            double height = ((ListCell)listView.lookup(".list-cell")).getHeight();
//            listViewHeight = height * listView.getItems().size();
//        } else {
//            Set<Node> nodes = listView.lookupAll(".list-cell");
//            for (Node node : nodes) {
//                ListCell cell = (ListCell) node;
//                listViewHeight += cell.getHeight();
//            }
//        }
        listViewHeight = listView.getItems().size() * CELL_HEIGHT;
        return listViewHeight;
    }

    private void updateBackgroundSize() {
//        BooleanBinding listViewShowing = Bindings.selectBoolean(listView.sceneProperty(), "window", "showing");
        if (listView.getSkin() != null) {
            ImageView backgroundNode = getSkinnable().getBackgroundNode();
            double listItemsHeight = calculateListItemsHeight();
            backgroundNode.setFitHeight(listItemsHeight);
        } else {
            listView.skinProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    if (listView.getSkin() != null) {
                        ImageView backgroundNode = getSkinnable().getBackgroundNode();
                        double listItemsHeight = calculateListItemsHeight();
                        backgroundNode.setPreserveRatio(true);
                        backgroundNode.setFitHeight(listItemsHeight);
                        listView.skinProperty().removeListener(this);
                    }
                }
            });
        }

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
