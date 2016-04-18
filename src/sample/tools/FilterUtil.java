package sample.tools;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.opencv.core.Mat;
import java.util.LinkedHashMap;
import java.util.Map;
import sample.Filters.Operations;
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
    private static TextField tfScale;
    private static TextField tfDelta;
    private static TextField tfDx;
    private static TextField tfDy;
    //morphological
    private static TextField tfErode;
    private static TextField tfDilate;
    //brightness
    private static TextField tfAlpha;
    private static TextField tfBeta;

    private static Button btApply;
    private static Controller controller;
    private static VBox box;

    //Структура, яка зберігає пари "текстове поле - мітка для цього поля"
    //використовується методом addFields, який додає в меню зправа поля і підписи до них
    private static LinkedHashMap<TextField, String> fields = new LinkedHashMap<>();

    public static void buildParamBilateral() {
        fields.put(tfBilateralDiameter = new TextField("20"), "Diameter:");
        fields.put(tfSigmaColor = new TextField("50"), "sigmaColor:");
        fields.put(tfSigmaSpace = new TextField("20"), "sigmaSpace:");
        addFields(fields);

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
    }

    public static void buildParamAdBilateral(){
        fields.put(tfSize = new TextField("3"), "K size:");
        fields.put(tfSigmaSpace = new TextField("50"), "sigmaSpace:");
        addFields(fields);

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
    }

    public static void buildParamBlur(){
        fields.put(tfSize = new TextField("3"), "K size:");
        addFields(fields);

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Filter.blur(controller.image,
                        Integer.parseInt(tfSize.getText()));
                applyFilter(result);
            }
        });
    }

    public static void buildParamGaussian(){
        fields.put(tfSize = new TextField("3"), "K size:");
        fields.put(tfSigmaX = new TextField("1"), "SigmaX:");
        addFields(fields);

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
    }

    public static void buildParamMedianBlur(){
        fields.put(tfSize = new TextField("3"), "K size:");
        addFields(fields);

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Filter.medianBlur(controller.image,
                        Integer.parseInt(tfSize.getText()));
                applyFilter(result);
            }
        });
    }

    public static void buildParamLaplacian() {
        fields.put(tfSize = new TextField("5"), "K size:");
        fields.put(tfScale = new TextField("1"), "Scale:");
        fields.put(tfDelta = new TextField("1"), "Delta:");
        addFields(fields);

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
    }

    public static void buildParamSobel() {
        fields.put(tfSize = new TextField("3"), "K Size:");
        fields.put(tfDx = new TextField("0"), "Dx:");
        fields.put(tfDy = new TextField("0"), "Dy:");
        addFields(fields);

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
    }

    public static void buildParamErode(){
        fields.put(tfErode = new TextField("1"), "Kernel size:");
        addFields(fields);

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Operations.Erode(controller.image,
                        Integer.parseInt(tfErode.getText()));
                applyFilter(result);
            }
        });
    }

    public static void buildParamDilate(){
        fields.put(tfDilate = new TextField("1"), "Kernel size:");
        addFields(fields);

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result;
                result = Operations.Dilate(controller.image,
                        Integer.parseInt(tfDilate.getText()));
                applyFilter(result);
            }
        });
    }

    public static void buildParamContrast() {
        box.getChildren().clear();

    }

    public static void buildParamBrightness() {
        fields.put(tfAlpha = new TextField("1"), "Alpha");
        fields.put(tfBeta = new TextField("0"), "Beta");
        addFields(fields);

        btApply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Mat result = new Mat(controller.image.rows(),controller.image.cols(),
                        controller.image.type());

                controller.image.convertTo(result, -1,
                        Integer.parseInt(tfAlpha.getText()),
                        Integer.parseInt(tfBeta.getText())
                );
                applyFilter(result);
            }
        });
    }

    private static void addFields(Map<TextField, String> m){
        box.getChildren().clear();
        btApply = new Button("Apply");

        for(Map.Entry<TextField, String> entry : m.entrySet()) {
            box.getChildren().add(new Label(entry.getValue()));
            box.getChildren().add(entry.getKey());
        }
        box.getChildren().add(btApply);
        fields.clear();
    }

    private static void applyFilter(Mat img) {
        //controller.changesList.add(img);
        //controller.currentImageIndex++;
        //controller.btRedo.setDisable(true);
        //controller.btUndo.setDisable(false);
        controller.image = img;
        controller.originalImage.setImage(ImageOperations.mat2Image(img));
    }

    public static void setController(Controller c){
        controller = c;
        box = c.vboxParameters;
    }
}