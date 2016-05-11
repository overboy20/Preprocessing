package sample.tools;

import javafx.scene.control.Alert;

public class Error {
    private boolean error;
    private String text;

    public Error(String s){
        text = s;
        error = true;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(this.text);
        alert.showAndWait();
    }

    public Error() {
        error = false;
    }

    public boolean getStatus() {
        return error;
    }
}
