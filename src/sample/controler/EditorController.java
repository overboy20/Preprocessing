package sample.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class EditorController {

    @FXML public Canvas myCanvas;
    @FXML public ColorPicker colorPicker;
    @FXML public ComboBox cbLineWidth;
    @FXML public CheckBox checkboxFill;
    @FXML public ColorPicker colorFill;

    private GraphicsContext graphicsContext;

    enum Tool {PEN, LINE, RECTANGLE, ELLIPSE}
    Tool tool = Tool.PEN;

    double initX, initY, maxX, maxY, endX, endY;
    boolean fill;
    public Controller mainController;

    @FXML public void handlePenClicked() {
        tool = Tool.PEN;
    }
    @FXML public void handleLineClicked() {
        tool = Tool.LINE;
    }
    @FXML public void handleRectangleClicked() {
        tool = Tool.RECTANGLE;
    }
    @FXML public void handleEllipseClicked() {
        tool = Tool.ELLIPSE;
    }

    private void draw(MouseEvent event) {
        switch (tool) {
            case PEN:
            {
                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();
            } break;
        }
    }

    //ініціалізація параметрів для малювання (колір, товщина лінії)
    private void initDraw(){
        graphicsContext.setStroke(colorPicker.getValue());
        graphicsContext.setLineWidth(cbLineWidth.getSelectionModel().getSelectedIndex()+1);
        graphicsContext.setFill(colorFill.getValue());
    }

    //ініціалізація класу контроллера
    public void init(Controller c){
        ImageView i = c.originalImage;
        mainController = c;
        Image im = i.getImage();
        fill = false;
        //висота і ширина канвасу
        double fitW = i.getBoundsInParent().getWidth();
        double fitH = i.getBoundsInParent().getHeight();
        maxX = fitW;
        maxY = fitH;

        graphicsContext = myCanvas.getGraphicsContext2D();
        //myCanvas.setWidth(fitW);
        //myCanvas.setHeight(fitH);

        myCanvas.setWidth(650);
        myCanvas.setHeight(fitH * 650 / fitW);

        graphicsContext.drawImage(im, 0, 0, myCanvas.getWidth(), myCanvas.getHeight());

        //подія для натискання кнопки миші
        myCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        initX = event.getX();
                        initY = event.getY();
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(initX, initY);
                        graphicsContext.stroke();
                    }
                });

        //подія для переміщення натиснутої кнопки миші
        myCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        draw(event);
                    }});

        //подія для відпускання кнопки миші
        myCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        endX = event.getX();
                        endY = event.getY();
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(initX, initY);

                        if (tool == Tool.LINE) {
                            graphicsContext.lineTo(endX, endY);
                        }
                        else if (tool == Tool.RECTANGLE) {
                            if (!fill)
                                graphicsContext.rect(initX, initY, endX-initX, endY-initY);
                            else {
                                graphicsContext.fillRect(initX, initY, endX-initX, endY-initY);
                                //рамка
                                graphicsContext.rect(initX, initY, endX-initX, endY-initY);
                            }
                        }
                        else if (tool == Tool.ELLIPSE) {
                            if (!fill)
                                graphicsContext.strokeOval(initX, initY, endX-initX, endY-initY);
                            else {
                                graphicsContext.fillOval(initX, initY, endX-initX, endY-initY);
                                //рамка
                                graphicsContext.strokeOval(initX, initY, endX-initX, endY-initY);
                            }
                        }
                        graphicsContext.stroke();
                    }
                });

        //повторна ініціалізація параметрів малювання після зміни кольору і товщини лінії
        colorPicker.setOnAction(event -> initDraw());
        cbLineWidth.setOnAction(event -> initDraw());

        //подія, яка виникаж при зміні пункту "заливка"
        checkboxFill.setOnAction(event -> {
            fill = !fill;
            colorFill.setDisable(!fill);
            initDraw();
        });

        colorFill.setOnAction(event -> initDraw());

        //додавання у випадаючий список допустимих значень ширини лінії
        ObservableList<String> obs = FXCollections.observableArrayList();
        for (int j = 1; j <= 10; j++) obs.add(Integer.toString(j));
        cbLineWidth.setItems(obs);
        cbLineWidth.getSelectionModel().select(0);
    }

    //закриття редактора
    @FXML public void onClose(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Answer");
        alert.setHeaderText("Are you really want to close editor?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage stage = (Stage) myCanvas.getScene().getWindow();
            stage.close();
        }
        else {
            alert.close();
        }
    }

    @FXML public void saveImage(){
        Alert alertSave = new Alert(Alert.AlertType.CONFIRMATION);
        alertSave.setTitle("Answer");
        alertSave.setHeaderText("Save changes?");
        alertSave.showAndWait();
        if (alertSave.getResult() == ButtonType.OK) {
            WritableImage i = new WritableImage((int)myCanvas.getWidth(), (int)myCanvas.getHeight());
            myCanvas.snapshot(null, i);

            /*BufferedImage bi = SwingFXUtils.fromFXImage(i, null);

            Mat im = bufferedImageToMat(bi);
            mainController.changeList.add(im);
            mainController.image = im;*/
            //mainController.obsListHistory.add("Image editor");
            mainController.originalImage.setImage(i);
            Stage stage = (Stage) myCanvas.getScene().getWindow();
            stage.close();
        }
        else {
            alertSave.close();
        }
    }

    private Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
}