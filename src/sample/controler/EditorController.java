package sample.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

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
    public void init(ImageView i){
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
                            else graphicsContext.fillRect(initX, initY, endX-initX, endY-initY);
                        }
                        else if (tool == Tool.ELLIPSE) {
                            if (!fill)
                                graphicsContext.strokeOval(initX, initY, endX-initX, endY-initY);
                            else graphicsContext.fillOval(initX, initY, endX-initX, endY-initY);
                        }
                        graphicsContext.stroke();
                    }
                });

        //повторна ініціалізація параметрів малювання після зміни кольору і товщини лінії
        colorPicker.setOnAction(event -> initDraw());
        cbLineWidth.setOnAction(event -> initDraw());

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
}