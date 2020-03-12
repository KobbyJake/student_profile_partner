/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.animation.TranslateTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javax.persistence.Query;
import model.Document;
import view.Alpha;
import view.ScreensController;

/**
 * FXML Controller class
 *
 * @author Jake
 */
public class SRCController implements Initializable, ControlledScreen {

    public Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
    public double width = screenSize.getWidth(), height = screenSize.getHeight();
    ScreensController myController;
    @FXML
    private Button btnLeftScroll;
    @FXML
    private ComboBox<String> cboTitle;
    @FXML
    private Button btnRightScroll;

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
        baseContainer.setPrefSize(width - (width * 0.5 / 100), height - (height * 4 / 100));
    }

    @FXML
    private BorderPane baseContainer;
    @FXML
    private Text txtTitle;
    @FXML
    private Color x4;
    @FXML
    private Font x3;
    @FXML
    private Text lblDescriptionHeader;
    @FXML
    private TextArea txaDescription;
    private Pagination srcPage;
    private Image[] images;
    javax.persistence.EntityManager entitymanager;
    Map<String, ImageView> photoStore = new HashMap();
    Map<String, String> commentStore = new HashMap();
    @FXML
    FlowPane imageHolder;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        entitymanager = DBConnection.getEntityManger();
        loadExecutives();
    }

    @FXML
    private void resetPage(MouseEvent event) {
    }

    @FXML
    private void LoadPageData(MouseEvent event) {
    }

    @FXML
    private void scrollLeft(ActionEvent event) {
        if (this.cboTitle.getSelectionModel().getSelectedIndex() > 0) {
            this.cboTitle.getSelectionModel().select(this.cboTitle.getSelectionModel().getSelectedIndex() - 1);
        }
    }

    @FXML
    private void showImage(ActionEvent event) {
        setContent(this.cboTitle.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void scrollRight(ActionEvent event) {

        if (this.cboTitle.getSelectionModel().getSelectedIndex() < this.cboTitle.getItems().size() - 1) {
            this.cboTitle.getSelectionModel().select(this.cboTitle.getSelectionModel().getSelectedIndex() + 1);
        }
    }

    private void loadExecutives() {
        try {
            Query query = this.entitymanager.createQuery("SELECT d FROM Document d WHERE d.reference LIKE 'SRC'");
            query.getResultList().stream().forEach((item) -> {
                Document currentDoc = (Document) item;
                File image = new File("lib/image.png");
                image.deleteOnExit();
                FileOutputStream fos;
                try {
                    fos = new FileOutputStream("lib/image.png");
                    fos.write(currentDoc.getFile());
                    fos.close();
                    this.photoStore.put(currentDoc.getTitle(), new ImageView(new Image(image.toURI().toURL().toString(), 520, 570, false, true)));
//                    this.photoStore.put(currentDoc.getTitle(), new ImageView(new Image(image.toURI().toURL().toString(), 550, 500, false, true)));
                    this.commentStore.put(currentDoc.getTitle(), currentDoc.getNote());
                    this.cboTitle.getItems().add(currentDoc.getTitle());
                } catch (Exception ex) {
                    Logger.getLogger(SRCController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });
            this.cboTitle.getSelectionModel().select(0);
            setContent(this.cboTitle.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Image Loader Error");
            alert.setContentText(e.getMessage());
            alert.initOwner(Alpha.stage); alert.show();
        }
    }

    private void setContent(String key) {

        if (this.imageHolder.getChildren().isEmpty()) {
            this.imageHolder.getChildren().add(this.photoStore.get(key));
            this.btnLeftScroll.setDisable(true);
            this.txaDescription.setText(this.commentStore.get(key));
            this.txtTitle.setText(key);
        } else {

            this.btnLeftScroll.setDisable(true);
            this.cboTitle.setDisable(true);
            this.btnRightScroll.setDisable(true);

            if (this.cboTitle.getSelectionModel().getSelectedIndex() < this.cboTitle.getItems().indexOf(key)) {
                this.remove(this.imageHolder.getChildren().get(0), this.photoStore.get(key), key);
                this.swap();
            } else {
                this.remove(this.imageHolder.getChildren().get(0), this.photoStore.get(key), key);
                this.swap();

            }
            this.txaDescription.clear();
            this.txtTitle.setText("");
        }

    }

    private void swap() {
        TranslateTransition translateTransition;
        translateTransition = TranslateTransitionBuilder.create()
                .duration(Duration.seconds(4))
                .node(this.imageHolder.getChildren().get(0))
                .fromY(0)
                .toY(-800)
                .cycleCount(2)
                .autoReverse(true)
                .build();
        ScaleTransition scaleTransition = ScaleTransitionBuilder.create()
                .node(this.imageHolder.getChildren().get(0))
                .duration(Duration.seconds(3))
                .toX(0)
                .toY(0)
                .cycleCount(2)
                .autoReverse(true)
                .build();
        translateTransition.play();
        scaleTransition.play();
    }

    private void remove(Node current, Node next, String key) {
        next.setOpacity(0);
        FadeTransition fadeTransition = FadeTransitionBuilder.create()
                .duration(Duration.seconds(4))
                .node(next)
                .fromValue(0)
                .toValue(1)
                .cycleCount(1)
                .autoReverse(false)
                .build();

        KeyValue keyValuePale = new KeyValue(current.opacityProperty(), 0);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(2), (ActionEvent t) -> {
            Node node = this.imageHolder.getChildren().get(0);
            this.imageHolder.getChildren().remove(0);
            node.setOpacity(1.0);
            fadeTransition.play();
            this.imageHolder.getChildren().add(next);

            if (this.cboTitle.getSelectionModel().getSelectedIndex() == 0) {
                this.btnLeftScroll.setDisable(true);
            } else {
                this.btnLeftScroll.setDisable(false);
            }
            if (this.cboTitle.getSelectionModel().getSelectedIndex() == this.cboTitle.getItems().size() - 1) {
                this.btnRightScroll.setDisable(true);
            } else {
                this.btnRightScroll.setDisable(false);

            }

            this.cboTitle.setDisable(false);
            this.txaDescription.setText(this.commentStore.get(key));
            this.txtTitle.setText(key);

        }, keyValuePale);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(2);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().add(keyFrame);

        timeline.play();
    }
//    EventHandler<ActionEvent> onFinished = (ActionEvent t) -> {
//        Node node = this.imageHolder.getChildren().get(0);
//        this.imageHolder.getChildren().remove(0);
//        node.setOpacity(1.0);
//
//    };

    @FXML
    private void scanLeftKey(KeyEvent event) {
        if (event.getCode() == event.getCode().LEFT) {
            if (this.cboTitle.getSelectionModel().getSelectedIndex() > 0) {
                this.cboTitle.getSelectionModel().select(this.cboTitle.getSelectionModel().getSelectedIndex() - 1);
            }
        }
    }

    @FXML
    private void scanRightKey(KeyEvent event) {
        if (event.getCode() == event.getCode().RIGHT) {
            if (this.cboTitle.getSelectionModel().getSelectedIndex() < this.cboTitle.getItems().size() - 1) {
                this.cboTitle.getSelectionModel().select(this.cboTitle.getSelectionModel().getSelectedIndex() + 1);
            }
        }
    }

    @FXML
    private void startIndexPage(ActionEvent event) {
        Alpha.startIndex(this.baseContainer);
    }
}
