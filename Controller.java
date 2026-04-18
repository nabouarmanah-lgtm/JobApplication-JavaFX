package scenebuilde;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.*;

public class Controller {

    @FXML private TextField nameField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;

    @FXML private RadioButton singleRadio;
    @FXML private RadioButton marriedRadio;
    @FXML private RadioButton divorcedRadio;

    @FXML private DatePicker datePicker;

    @FXML private ListView<String> availableList;
    @FXML private ListView<String> selectedList;

    @FXML private TextArea textArea;
    @FXML private Slider fontSlider;
    @FXML private ColorPicker colorPicker;

    @FXML private ImageView imageView;

    private ToggleGroup group = new ToggleGroup();

    @FXML
    public void initialize() {

        // radio buttons
        singleRadio.setToggleGroup(group);
        marriedRadio.setToggleGroup(group);
        divorcedRadio.setToggleGroup(group);

        // skills
        availableList.getItems().addAll(
                "Java",
                "C++",
                "Python",
                "HTML",
                "CSS",
                "JavaScript"
        );

        // slider
        fontSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            textArea.setStyle("-fx-font-size: " + newVal.intValue() + "px;");
        });

        // color
        colorPicker.setOnAction(e -> {
            textArea.setStyle(
                    "-fx-control-inner-background: #" +
                    colorPicker.getValue().toString().substring(2, 8) +
                    "; -fx-font-size: " + (int) fontSlider.getValue() + "px;"
            );
        });
    }

    @FXML
    private void moveRight() {
        String selected = availableList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            availableList.getItems().remove(selected);
            selectedList.getItems().add(selected);
        }
    }

    @FXML
    private void moveLeft() {
        String selected = selectedList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            selectedList.getItems().remove(selected);
            availableList.getItems().add(selected);
        }
    }

    @FXML
    private void handleImage() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            imageView.setImage(new Image(file.toURI().toString()));
        }
    }

    @FXML
    private void handleUpload() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );

        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                textArea.clear();

                while ((line = reader.readLine()) != null) {
                    textArea.appendText(line + "\n");
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleCoverLetter() {
        handleUpload(); // نفس الفكرة (من المحاضرات)
    }

    @FXML
    private void handleSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("data.txt");

        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {

                writer.println("Name: " + nameField.getText());
                writer.println("Address: " + addressField.getText());
                writer.println("Phone: " + phoneField.getText());

                RadioButton selected = (RadioButton) group.getSelectedToggle();
                if (selected != null)
                    writer.println("Status: " + selected.getText());

                writer.println("Date: " + datePicker.getValue());
                writer.println("Skills: " + selectedList.getItems());
                writer.println("Cover Letter:\n" + textArea.getText());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}