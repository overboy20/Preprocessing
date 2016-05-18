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
import javafx.scene.paint.Color;
import sample.tools.ResizableCanvas;

public class EditorController {

    @FXML public Canvas myCanvas;
    @FXML public ColorPicker colorPicker;
    @FXML public ComboBox cbLineWidth;

    private GraphicsContext graphicsContext;

    enum Tool {PEN, BRUSH, LINE, RECTANGLE, ELLIPSE, FILL};
    Tool tool = Tool.PEN;

    double initX, initY, maxX, maxY;

    @FXML public void handlePenClicked() {
        tool = Tool.PEN;
    }

    @FXML public void handleBrushClicked() {
        tool = Tool.BRUSH;
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
        initDraw();
        switch (tool) {
            case PEN:
            {
                initX = event.getX();
                initY = event.getY();
                //System.out.println(initX + "/" + initY);
                graphicsContext.lineTo(event.getX(), event.getY());
                graphicsContext.stroke();
            } break;
        }
    }

    private void initDraw(){
        graphicsContext.setStroke(colorPicker.getValue());
        graphicsContext.setLineWidth(cbLineWidth.getSelectionModel().getSelectedIndex()+1);
    }

    public void init(ImageView i){
        Image im = i.getImage();
        double fitW = i.getBoundsInParent().getWidth();
        double fitH = i.getBoundsInParent().getHeight();
        maxX = fitW;
        maxY = fitH;

        graphicsContext = myCanvas.getGraphicsContext2D();
        myCanvas.setWidth(fitW);
        myCanvas.setHeight(fitH);
        graphicsContext.drawImage(im, 0, 0, fitW, fitH);

        myCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        graphicsContext.beginPath();
                        graphicsContext.moveTo(event.getX(), event.getY());
                        graphicsContext.stroke();
                    }
                });

        myCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        draw(event);
                    }});
        myCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {

                    }
                });

        //додавання у випадаючий список допустимих значень ширини лінії
        ObservableList<String> obs = FXCollections.observableArrayList();
        for (int j = 1; j <= 10; j++) obs.add(Integer.toString(j));
        cbLineWidth.setItems(obs);
        cbLineWidth.getSelectionModel().select(0);
    }
}