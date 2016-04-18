package sample.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import sample.tools.*;

import java.io.File;

public class Controller {

    @FXML public ImageView originalImage;
    @FXML protected ListView<String> listFilters;
    @FXML protected ListView<String> listOperations;
    @FXML public VBox vboxParameters;

    public Mat image;
    //private String originalImagePath;
    public final ObservableList<String> obsListFilters = FXCollections.observableArrayList();
    public final ObservableList<String> obsListOperations = FXCollections.observableArrayList();


    @FXML
    public void chooseFile(ActionEvent actionEvent) throws java.io.IOException {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files","*.bmp", "*.png", "*.jpg", "*.gif"));
        File file = chooser.showOpenDialog(new Stage());

        if(file != null) {
            this.image = Highgui.imread(file.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_COLOR);

            sample.model.Image.setImageMat(this.image);

            //originalImagePath = file.getAbsolutePath();
            Mat newImage = sample.model.Image.getImageMat();
            // show the image
            this.setOriginalImage(newImage);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Please Select a File");
            alert.showAndWait();
        }
    }

    private void setOriginalImage(Mat dst) {
        this.originalImage.setImage(ImageOperations.mat2Image(dst));
        this.originalImage.setFitWidth(450.0);
        this.originalImage.setFitHeight(450.0);
        this.originalImage.setPreserveRatio(true);
    }

    //Filter list handler
    @FXML public void handleMouseClickFilters(MouseEvent arg0) {
        //System.out.println("clicked on " + listFilters.getSelectionModel().getSelectedItem());
        switch (listFilters.getSelectionModel().getSelectedItem()) {
            case Constants.FILTER_1:
                FilterUtil.buildParamBilateral(); break;
            case Constants.FILTER_2:
                FilterUtil.buildParamAdBilateral(); break;
            case Constants.FILTER_3:
                FilterUtil.buildParamBlur(); break;
            case Constants.FILTER_4:
                FilterUtil.buildParamGaussian(); break;
            case Constants.FILTER_5:
                FilterUtil.buildParamMedianBlur(); break;
            case Constants.FILTER_6:
                FilterUtil.buildParamLaplacian(); break;
            case Constants.FILTER_7:
                FilterUtil.buildParamSobel(); break;
        }
    }

    //operations list handler
    @FXML public void handleMouseClickOperations(MouseEvent arg0) {
        //System.out.println("clicked on " + listOperations.getSelectionModel().getSelectedItem());
        switch(listOperations.getSelectionModel().getSelectedItem()) {
            case Constants.OPERATION_1:
                FilterUtil.buildParamErode(); break;
            case Constants.OPERATION_2:
                FilterUtil.buildParamDilate(); break;
            case Constants.OPERATION_3:
                FilterUtil.buildParamContrast(); break;
            case Constants.OPERATION_4:
                FilterUtil.buildParamBrightness(); break;
        }
    }

    public void init(){
        FilterUtil.setController(this);
        listFilters.setItems(obsListFilters);
        obsListFilters.add(Constants.FILTER_1);
        obsListFilters.add(Constants.FILTER_2);
        obsListFilters.add(Constants.FILTER_3);
        obsListFilters.add(Constants.FILTER_4);
        obsListFilters.add(Constants.FILTER_5);
        obsListFilters.add(Constants.FILTER_6);
        obsListFilters.add(Constants.FILTER_7);

        listOperations.setItems(obsListOperations);
        obsListOperations.add(Constants.OPERATION_1);
        obsListOperations.add(Constants.OPERATION_2);
        obsListOperations.add(Constants.OPERATION_3);
        obsListOperations.add(Constants.OPERATION_4);
    }
}
