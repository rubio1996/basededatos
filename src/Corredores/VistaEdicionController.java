/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Corredores;

import basededatos.Carrera;
import basededatos.Corredores;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author 1daw
 */
public class VistaEdicionController implements Initializable {

    private TableView tableViewPrevio;
    private Corredores Corredores;
    private EntityManager entityManager;
    private boolean nuevaPersona;
    private Pane rootContactosView;
    private Character estadoCivil;
    private Boolean jubilado;
    @FXML
    private javafx.scene.control.TextField textFieldNombres;
    @FXML
    private javafx.scene.control.TextField textFieldApellidos;
    @FXML
    private javafx.scene.control.TextField textFieldTelefono;
    @FXML
    private javafx.scene.control.TextField textFieldEMail;
    @FXML
    private javafx.scene.control.TextField textFieldhijos;
    @FXML
    private javafx.scene.control.TextField textFieldsalario;
    @FXML
    private javafx.scene.control.RadioButton radioButtonSoltero;
    @FXML
    private javafx.scene.control.RadioButton radioButtonViudo;
    @FXML
    private javafx.scene.control.RadioButton radioButtonCasado;

    public static final char CASADO = 'C';
    public static final char SOLTERO = 'S';
    public static final char VIUDO = 'V';
    @FXML
    private ImageView imageViewFoto;
    @FXML
    private CheckBox checkBoxJubilado;
    @FXML
    private ComboBox<Carrera> comboBoxCarrera;

    public void setRootContactosView(Pane rootContactosView) {
        this.rootContactosView = rootContactosView;
    }
    @FXML
    private AnchorPane rootPersonaDetalleView;

    public void mostrarDatos() {
        textFieldNombres.setText(Corredores.getNombre());
        textFieldApellidos.setText(Corredores.getApellidos());
        textFieldTelefono.setText(Corredores.getTelefono());
        textFieldEMail.setText(Corredores.getEmail());
        if (Corredores.getEstadoCivil() != null) {
            switch (Corredores.getEstadoCivil()) {
                case CASADO:
                    radioButtonCasado.setSelected(true);
                    break;
                case SOLTERO:
                    radioButtonSoltero.setSelected(true);
                    break;
                case VIUDO:
                    radioButtonViudo.setSelected(true);
                    break;
            }
        }
        Query queryProvinciaFindAll = entityManager.createNamedQuery("Provincia.findAll");
        List listProvincia = queryProvinciaFindAll.getResultList();
        comboBoxCarrera.setItems(FXCollections.observableList(listProvincia));
        if (Corredores.getCarrera() != null) {
            comboBoxCarrera.setValue(Corredores.getCarrera());
        }
        comboBoxCarrera.setCellFactory((ListView<Carrera> l) -> new ListCell<Carrera>() {
            @Override
            protected void updateItem(Carrera Carrera, boolean empty) {
                super.updateItem(Carrera, empty);
                if (Carrera == null || empty) {
                    setText("");
                } else {
                    setText(Carrera.getCODIGOCARRERA() + "-" + Carrera.getNombre());
                }
            }
        });
        comboBoxCarrera.setConverter(new StringConverter<Carrera>() {
            @Override
            public String toString(Carrera Carrera) {
                if (Carrera == null) {
                    return null;
                } else {
                    return Carrera.getCODIGOCARRERA() + "-" + Carrera.getNombre();
                }
            }

            @Override

            public Carrera fromString(String userId) {
                return null;
            }
        });
        // Falta implementar el código para el resto de controles
    }

    @FXML
    private void onActionButtonExaminar(ActionEvent event) {
        String CARPETA_FOTOS = null;
        File carpetaFotos = new File(CARPETA_FOTOS);
        if (!carpetaFotos.exists()) {
            carpetaFotos.mkdir();
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar imagen");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes (jpg, png)", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("Todos los archivos", "*.*")
        );
        File file = fileChooser.showOpenDialog(rootPersonaDetalleView.getScene().getWindow());
        if (file != null) {
            try {
                Files.copy(file.toPath(), new File(CARPETA_FOTOS + "/" + file.getName()).toPath());
                Corredores.setFoto(file.getName());
                Image image = new Image(file.toURI().toString()) {
                };
                imageViewFoto.setImage(image);
            } catch (FileAlreadyExistsException ex) {
                Alert alert = new Alert(AlertType.WARNING, "Nombre de archivo duplicado");
                alert.showAndWait();
            } catch (IOException ex) {
                Alert alert = new Alert(AlertType.WARNING, "No se ha podido guardar la imagen");
                alert.showAndWait();
            }
        }
    }

    @FXML
    private void onActionButtonCancelar(ActionEvent event) {
        StackPane rootMain = (StackPane) rootPersonaDetalleView.getScene().getRoot();
        rootMain.getChildren().remove(rootPersonaDetalleView);

        rootContactosView.setVisible(true);
        entityManager.getTransaction().rollback();
        int numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
        TablePosition pos = new TablePosition(tableViewPrevio, numFilaSeleccionada, null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();
    }

    public void setTableViewPrevio(TableView tableViewPrevio) {
        this.tableViewPrevio = tableViewPrevio;
    }

    public void setCorredores(EntityManager entityManager, Corredores Corredores, boolean nuevaPersona) {
        this.entityManager = entityManager;
        entityManager.getTransaction().begin();
        if (!nuevaPersona) {
            System.out.println(Corredores);
            System.out.println(entityManager);
            this.Corredores = entityManager.find(Corredores.class, Corredores.getId());
        } else {
            this.Corredores = Corredores;
        }
        this.nuevaPersona = nuevaPersona;
    }

    /**
     * Initializes the controller class.
     */
    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        StackPane rootMain = (StackPane) rootPersonaDetalleView.getScene().getRoot();
        rootMain.getChildren().remove(rootPersonaDetalleView);

        rootContactosView.setVisible(true);
        Corredores.setNombre(textFieldNombres.getText());
        Corredores.setApellidos(textFieldApellidos.getText());
        Corredores.setTelefono(textFieldTelefono.getText());
        Corredores.setEmail(textFieldEMail.getText());

        if (nuevaPersona) {
            entityManager.persist(Corredores);
        } else {
            entityManager.merge(Corredores);
        }
        entityManager.getTransaction().commit();
        int numFilaSeleccionada;
        if (nuevaPersona) {
            tableViewPrevio.getItems().add(Corredores);
            numFilaSeleccionada = tableViewPrevio.getItems().size() - 1;
            tableViewPrevio.getSelectionModel().select(numFilaSeleccionada);
            tableViewPrevio.scrollTo(numFilaSeleccionada);
        } else {
            numFilaSeleccionada = tableViewPrevio.getSelectionModel().getSelectedIndex();
            tableViewPrevio.getItems().set(numFilaSeleccionada, Corredores);
        }
        TablePosition pos = new TablePosition(tableViewPrevio, numFilaSeleccionada, null);
        tableViewPrevio.getFocusModel().focus(pos);
        tableViewPrevio.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
