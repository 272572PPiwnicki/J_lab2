package pl.pwr.imageprocessor;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML private ImageView logoImage;
    @FXML private ImageView originalImageView;
    @FXML private ImageView processedImageView;
    @FXML private ComboBox<String> operationBox;
    @FXML private Button executeButton;
    @FXML private Button saveButton;
    private BufferedImage processedBuffered;


    private BufferedImage originalBuffered;

    @FXML
    public void initialize() {
        // logo
        Image logo = new Image(getClass().getResourceAsStream("/assets/pwr_logo.png"));
        logoImage.setImage(logo);

        // przygotowanie rozwijanej listy operacji
        operationBox.getItems().addAll("Convert to Grayscale", "Resize", "Rotate");
        operationBox.getSelectionModel().clearSelection(); // brak domyślnego wyboru

        // zablokuj przyciski do czasu wczytania obrazu
        executeButton.setDisable(true);
        operationBox.setDisable(true);
        saveButton.setDisable(true);
    }

    @FXML
    private void handleLoadImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Wybierz obraz");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("JPEG files", "*.jpg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            if (!file.getName().toLowerCase().endsWith(".jpg")) {
                showWarning("Niedozwolony format pliku");
                return;
            }

            try {
                originalBuffered = ImageIO.read(file);
                if (originalBuffered == null) {
                    showError("Nie udało się załadować pliku");
                    return;
                }

                Image image = SwingFXUtils.toFXImage(originalBuffered, null);
                originalImageView.setImage(image);
                processedBuffered = null;
                saveButton.setDisable(true);
                processedImageView.setImage(null);

                showSuccess("Pomyślnie załadowano plik");

                // odblokuj przyciski
                operationBox.setDisable(false);
                executeButton.setDisable(false);

            } catch (IOException e) {
                showError("Nie udało się załadować pliku: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleExecute() {
        if (originalBuffered == null) {
            showError("Najpierw wczytaj obraz.");
            return;
        }

        String selected = operationBox.getValue();
        if (selected == null || selected.isEmpty()) {
            showWarning("Nie wybrano operacji do wykonania");
            return;
        }

        BufferedImage processed = null;

        try {
            switch (selected) {
                case "Convert to Grayscale":
                    processed = convertToGrayscale(originalBuffered);
                    break;
                case "Resize":
                    processed = resizeImage(originalBuffered, 0.5);
                    break;
                case "Rotate":
                    processed = rotateImage(originalBuffered);
                    break;
                default:
                    showWarning("Nieznana operacja.");
                    return;
            }

            processedBuffered = processed;  // zapisz do pola globalnego
            processedImageView.setImage(SwingFXUtils.toFXImage(processed, null));
            saveButton.setDisable(false);  // aktywuj przycisk zapisu

        } catch (Exception e) {
            showError("Błąd przetwarzania obrazu: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveImage() {
        if (processedBuffered == null || processedImageView.getImage() == null) {
            showCustomOrangeWarning("Na pliku nie zostały wykonane żadne operacje!");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Zapisz obraz");
        dialog.setHeaderText("Podaj nazwę pliku (3–100 znaków):");

        dialog.showAndWait().ifPresent(name -> {
            if (name.length() < 3) {
                showWarning("Wpisz co najmniej 3 znaki");
                return;
            }

            if (name.length() > 100) {
                showWarning("Nazwa pliku jest za długa (max 100 znaków)");
                return;
            }

            File outputDir = new File(System.getProperty("user.home") + "\\OneDrive\\Obrazy");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }

            File outputFile = new File(outputDir, name + ".jpg");

            if (outputFile.exists()) {
                showWarning("Plik " + name + ".jpg już istnieje w systemie. Podaj inną nazwę pliku!");
                return;
            }

            try {
                ImageIO.write(processedBuffered, "jpg", outputFile);
                showSuccess("Zapisano obraz w pliku " + name + ".jpg");
            } catch (IOException e) {
                showError("Nie udało się zapisać pliku: " + e.getMessage());
            }
        });
    }

    // === Operacje ===
    private BufferedImage convertToGrayscale(BufferedImage src) {
        BufferedImage gray = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(src.getColorModel().getColorSpace(),
                gray.getColorModel().getColorSpace(), null);
        op.filter(src, gray);
        return gray;
    }

    private BufferedImage resizeImage(BufferedImage src, double scale) {
        int newW = (int)(src.getWidth() * scale);
        int newH = (int)(src.getHeight() * scale);
        BufferedImage resized = new BufferedImage(newW, newH, src.getType());
        resized.getGraphics().drawImage(src, 0, 0, newW, newH, null);
        return resized;
    }

    private BufferedImage rotateImage(BufferedImage src) {
        int w = src.getWidth();
        int h = src.getHeight();
        BufferedImage rotated = new BufferedImage(h, w, src.getType());
        AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(90), 0, 0);
        tx.translate(0, -w);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        op.filter(src, rotated);
        return rotated;
    }

    // === Alerty ===
    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Błąd");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ostrzeżenie");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showSuccess(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sukces");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showCustomOrangeWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Ostrzeżenie");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.getDialogPane().setStyle("-fx-background-color: orange;");
        alert.showAndWait();
    }

}
