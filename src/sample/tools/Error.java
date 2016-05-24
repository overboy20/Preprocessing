package sample.tools;

import javafx.scene.control.Alert;

//клас, який реалізує виведення на екран
//відповідного повідомлення про помилку
public class Error {

    //чи виникла помилка
    private boolean error;
    //текст помилки
    private String text;

    //якщо об'єкт даного класу було створено через цей конструктор,
    //то виникла помилка і виводиться відповідне повідомлення
    public Error(String s){
        text = s;
        error = true;
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(this.text);
        alert.showAndWait();
    }

    //Якщо об'єкт класу створювався з пустим конструктором, то помилки немає
    public Error() {
        error = false;
    }

    //отримуємо статус помилки (виникла/не виникла)
    public boolean getStatus() {
        return error;
    }
}
