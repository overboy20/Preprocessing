package sample.controler;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import sample.tools.*;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

public class Controller {

    @FXML public ImageView originalImage;
    @FXML public VBox vboxParameters;
    @FXML protected ListView<String> listFilters;
    @FXML protected ListView<String> listOperations;
    @FXML protected ListView<String> listHistory;
    @FXML protected Button btSelect;
    @FXML protected Button btDelete;

    public Mat image;
    private String originalImagePath;
    private String originalImageExtension;
    private String originalImageName;
    public final ObservableList<String> obsListFilters = FXCollections.observableArrayList();
    public final ObservableList<String> obsListOperations = FXCollections.observableArrayList();
    public final ObservableList<String> obsListHistory = FXCollections.observableArrayList();

    public ArrayList<Mat> changeList = new ArrayList<>();


    @FXML public void chooseFile(ActionEvent actionEvent) throws java.io.IOException {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open File");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files","*.bmp", "*.png", "*.jpg", "*.gif"));
        File file = chooser.showOpenDialog(new Stage());

        if(file != null) {
            this.image = Highgui.imread(file.getAbsolutePath(), Highgui.CV_LOAD_IMAGE_COLOR);
            changeList.clear();
            obsListHistory.clear();
            changeList.add(image);
            obsListHistory.add("Original image");

            //sample.model.Image.setImageMat(this.image);

            originalImagePath = file.getAbsolutePath();
            originalImageName = file.getName().substring(0, file.getName().length()-4);

            originalImageExtension = originalImagePath.substring(originalImagePath.length()-3);
            //Mat newImage = sample.model.Image.getImageMat();
            // show the image
            this.setOriginalImage(this.image);
            System.out.println("Loaded");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Please Select a File");
            alert.showAndWait();
            System.out.println("Error loading");
        }
    }

    //save to original image
    @FXML public void saveToFile()throws java.io.IOException {
        Highgui.imwrite( this.originalImagePath, this.image );
        System.out.println("Saving");
    }

    @FXML public void SaveAsNewFile() throws java.io.IOException {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy_HH.mm.ss");
        Date date = new Date();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As...");
        fileChooser.setInitialFileName(this.originalImageName + "(" + dateFormat.format(date) + ")" +
                                        "." + this.originalImageExtension);
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(ImageOperations.mat2Image(this.image),
                        null), this.originalImageExtension, file);
                System.out.println("Save as successful");
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private void setOriginalImage(Mat dst) {
        this.originalImage.setImage(ImageOperations.mat2Image(dst));
        this.originalImage.setFitWidth(450.0);
        this.originalImage.setFitHeight(450.0);
        this.originalImage.setPreserveRatio(true);
    }

    //Filter list handler
    @FXML public void handleMouseClickFilters() {
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
    @FXML public void handleMouseClickOperations() {
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

    @FXML public void handleMouseClickSelect() {
        int index = listHistory.getSelectionModel().getSelectedIndex();
        if (index >= 0) {
            //this.originalImage.setImage(changeList.);
            setOriginalImage(changeList.get(index));
            this.image = changeList.get(index);
        }
    }

    @FXML public void handleMouseClickDelete() {
        int index = listHistory.getSelectionModel().getSelectedIndex();
        if (index == 0 && obsListHistory.size() == 1) {
            changeList.clear();
            obsListHistory.clear();
            this.image = new Mat(image.rows(), image.cols(), image.type());
            setOriginalImage(this.image);

        }
        else if (index == 0) {
            this.image = changeList.get(index + 1);
            setOriginalImage(this.image);
            changeList.remove(index);
            obsListHistory.remove(index);
        }
        else if (index != -1) {
            this.image = changeList.get(index - 1);
            setOriginalImage(this.image);
            changeList.remove(index);
            obsListHistory.remove(index);
        }
    }

    @FXML public void closeApplication(){
        Platform.exit();
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

        listHistory.setItems(obsListHistory);
    }
}
