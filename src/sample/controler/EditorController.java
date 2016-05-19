package sample.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class EditorController {

    @FXML public Canvas myCanvas;
    @FXML public ColorPicker colorPicker;
    @FXML public ComboBox cbLineWidth;

    private GraphicsContext graphicsContext;

    enum Tool {PEN, LINE, RECTANGLE, ELLIPSE}
    Tool tool = Tool.PEN;

    double initX, initY, maxX, maxY, endX, endY;

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
    }

    //ініціалізація класу контроллера
    public void init(ImageView i){
        Image im = i.getImage();
        //висота і ширина канвасу
        double fitW = i.getBoundsInParent().getWidth();
        double fitH = i.getBoundsInParent().getHeight();
        maxX = fitW;
        maxY = fitH;

        graphicsContext = myCanvas.getGraphicsContext2D();
        myCanvas.setWidth(fitW);
        myCanvas.setHeight(fitH);
        graphicsContext.drawImage(im, 0, 0, fitW, fitH);

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
                        if (tool == Tool.LINE) {
                            endX = event.getX();
                            endY = event.getY();
                            graphicsContext.beginPath();
                            graphicsContext.moveTo(initX, initY);
                            graphicsContext.lineTo(endX, endY);
                            graphicsContext.stroke();
                        }
                        else if (tool == Tool.RECTANGLE) {

                        }
                        else if (tool == Tool.ELLIPSE) {

                        }
                    }
                });

        //повторна ініціалізація параметрів малювання після зміни кольору і товщини лінії
        colorPicker.setOnAction(event -> initDraw());
        cbLineWidth.setOnAction(event -> initDraw());

        //додавання у випадаючий список допустимих значень ширини лінії
        ObservableList<String> obs = FXCollections.observableArrayList();
        for (int j = 1; j <= 10; j++) obs.add(Integer.toString(j));
        cbLineWidth.setItems(obs);
        cbLineWidth.getSelectionModel().select(0);
    }
}