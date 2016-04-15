package sample.tools;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.opencv.core.Mat;
import sample.controler.Controller;
import sample.Filters.Filter;

// Даний клас реалізовує заповнення правого меню відповідними компонентами
// в залежності від вибраного пункту в лівому меню
public class FilterUtil {

    private static TextField tfBilateralDiameter;
    private static TextField tfSigmaColor;
    private static TextField tfSigmaSpace;
    private static TextField tfSigmaX;
    private static TextField tfSize;
    private static Button btApply;

    private static Controller controller;
    private static VBox box;

    public static void buildParamBilateral() {
        box.getChildren().clear();
        tfBilateralDiameter = new TextField("20");
        tfSigmaColor = new TextField("50");
        tfSigmaSpace = new TextField("20");
        btApply = new Button("Apply");

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Filter.bilateralFilter(controller.image,
                        Integer.parseInt(tfBilateralDiameter.getText()),
                        Integer.parseInt(tfSigmaColor.getText()),
                        Integer.parseInt(tfSigmaSpace.getText()));
                applyFilter(result);
            }
        });

        box.getChildren().add(new Label("Diameter:"));
        box.getChildren().add(tfBilateralDiameter);
        box.getChildren().add(new Label("sigmaColor:"));
        box.getChildren().add(tfSigmaColor);
        box.getChildren().add(new Label("sigmaSpace:"));
        box.getChildren().add(tfSigmaSpace);
        box.getChildren().add(btApply);
    }

    public static void buildParamAdBilateral(){
        box.getChildren().clear();
        tfSize = new TextField("3");
        tfSigmaSpace = new TextField("50");
        btApply = new Button("Apply");

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Filter.adaptiveBilateralFilter(controller.image,
                        Integer.parseInt(tfSize.getText()),
                        Integer.parseInt(tfSigmaSpace.getText()));
                applyFilter(result);
            }
        });

        box.getChildren().add(new Label("K size:"));
        box.getChildren().add(tfSize);
        box.getChildren().add(new Label("sigmaSpace:"));
        box.getChildren().add(tfSigmaSpace);
        box.getChildren().add(btApply);
    }

    public static void buildParamBlur(){
        box.getChildren().clear();
        tfSize = new TextField("3");
        btApply = new Button("Apply");

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Filter.blur(controller.image,
                        Integer.parseInt(tfSize.getText()));
                applyFilter(result);
            }
        });

        box.getChildren().add(new Label("K size:"));
        box.getChildren().add(tfSize);
        box.getChildren().add(btApply);
    }

    public static void buildParamGaussian(){
        box.getChildren().clear();
        tfSize = new TextField("3");
        tfSigmaX = new TextField("1");
        btApply = new Button("Apply");

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Filter.gaussianBlur(controller.image,
                        Integer.parseInt(tfSize.getText()),
                        Double.parseDouble(tfSigmaX.getText()));
                applyFilter(result);
            }
        });

        box.getChildren().add(new Label("K size:"));
        box.getChildren().add(tfSize);
        box.getChildren().add(new Label("SigmaX:"));
        box.getChildren().add(tfSigmaX);
        box.getChildren().add(btApply);
    }

    public static void buildParamMedianBlur(){
        box.getChildren().clear();
        tfSize = new TextField("3");
        btApply = new Button("Apply");

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Filter.medianBlur(controller.image,
                        Integer.parseInt(tfSize.getText()));
                applyFilter(result);
            }
        });

        box.getChildren().add(new Label("K size:"));
        box.getChildren().add(tfSize);
        box.getChildren().add(btApply);
    }

    public static void buildParamLaplacian() {
        box.getChildren().clear();
        final TextField tfSize = new TextField("5");
        final TextField tfScale = new TextField("1");
        final TextField tfDelta = new TextField("1");
        btApply = new Button("Apply");

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Filter.laplacian(controller.image,
                        Integer.parseInt(tfSize.getText()),
                        Double.parseDouble(tfScale.getText()),
                        Double.parseDouble(tfDelta.getText()));
                applyFilter(result);
            }
        });

        box.getChildren().add(new Label("K size:"));
        box.getChildren().add(tfSize);
        box.getChildren().add(new Label("Scale:"));
        box.getChildren().add(tfScale);
        box.getChildren().add(new Label("Delta:"));
        box.getChildren().add(tfDelta);
        box.getChildren().add(btApply);
    }

    public static void buildParamSobel() {
        box.getChildren().clear();
        final TextField tfSize = new TextField("3");
        final TextField tfDx = new TextField("0");
        final TextField tfDy = new TextField("0");
        btApply = new Button("Apply");

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Filter.sobel(controller.image,
                        Integer.parseInt(tfDx.getText()),
                        Integer.parseInt(tfDy.getText()),
                        Integer.parseInt(tfSize.getText()));
                applyFilter(result);

            }
        });

        box.getChildren().add(new Label("Dx:"));
        box.getChildren().add(tfDx);
        box.getChildren().add(new Label("Dy:"));
        box.getChildren().add(tfDy);
        box.getChildren().add(new Label("K Size:"));
        box.getChildren().add(tfSize);
        box.getChildren().add(btApply);

    }

    public static void setController(Controller c){
        controller = c;
        box = c.vboxParameters;
    }

    private static void applyFilter(Mat img) {
        //controller.changesList.add(img);
        //controller.currentImageIndex++;
        //controller.btRedo.setDisable(true);
        //controller.btUndo.setDisable(false);
        controller.image = img;
        controller.originalImage.setImage(ImageOperations.mat2Image(img));
    }
}