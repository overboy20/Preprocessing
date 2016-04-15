package sample.controler;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import sample.tools.ImageOperations;

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

    public void init(){
        listFilters.setItems(obsListFilters);
        obsListFilters.add("Filter 1");
        obsListFilters.add("Filter 2");
        obsListFilters.add("Filter 3");
        obsListFilters.add("Filter 4");
        obsListFilters.add("Filter 5");

        listOperations.setItems(obsListOperations);
        obsListOperations.add("Erode");
        obsListOperations.add("Dilate");
        obsListOperations.add("Contrast");
        obsListOperations.add("Brightness");
    }
}
