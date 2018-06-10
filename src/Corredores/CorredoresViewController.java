/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Corredores;


import basededatos.Corredores;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * FXML Controller class
 *
 * @author El Rubio
 */
public class CorredoresViewController implements Initializable {

    private EntityManager entityManager;
    private Corredores personaSeleccionada;
    @FXML
    private TableView<Corredores> tableViewContactos;
    @FXML
    private TableColumn<Corredores, String> columnNombre;
    @FXML
    private TableColumn<Corredores, String> columnApellidos;
    @FXML
    private TableColumn<Corredores, String> columnEmail;
    @FXML
    private TableColumn<Corredores, String> columnProvincia;
    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldApellidos;
    @FXML
    private AnchorPane rootContactosView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnProvincia.setCellValueFactory(
                cellData -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    if (cellData.getValue().getCarrera() != null) {
                        property.setValue(cellData.getValue().getCarrera().getNombre());
                    }
                    return property;
                });
        tableViewContactos.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    personaSeleccionada = newValue;
                    if (personaSeleccionada != null) {
                        textFieldNombre.setText(personaSeleccionada.getNombre());
                        textFieldApellidos.setText(personaSeleccionada.getApellidos());
                    } else {
                        textFieldNombre.setText("");
                        textFieldApellidos.setText("");
                    }
                });
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void cargarTodasPersonas() {
        Query queryPersonaFindAll = entityManager.createNamedQuery("Persona.findAll");
        List<Corredores> listPersona = queryPersonaFindAll.getResultList();
        tableViewContactos.setItems(FXCollections.observableArrayList(listPersona));
    }

    @FXML
    private void onActionButtonGuardar(ActionEvent event) {
        if (personaSeleccionada != null) {
            personaSeleccionada.setNombre(textFieldNombre.getText());
            personaSeleccionada.setApellidos(textFieldApellidos.getText());
            entityManager.getTransaction().begin();
            entityManager.merge(personaSeleccionada);
            entityManager.getTransaction().commit();

            int numFilaSeleccionada = tableViewContactos.getSelectionModel().getSelectedIndex();
            tableViewContactos.getItems().set(numFilaSeleccionada, personaSeleccionada);
            TablePosition pos = new TablePosition(tableViewContactos, numFilaSeleccionada, null);
            tableViewContactos.getFocusModel().focus(pos);
            tableViewContactos.requestFocus();
        }
    }

    @FXML
    private void onActionButtonNuevo(ActionEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vistaEdicion.fxml"));
            Parent rootDetalleView = fxmlLoader.load();
            // Ocultar la vista de la lista
            rootContactosView.setVisible(false);
            VistaEdicionController VistaEdicionController = (VistaEdicionController) fxmlLoader.getController();
            VistaEdicionController.setRootContactosView(rootContactosView);
            // Añadir la vista de detalle al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootContactosView.getScene().getRoot();
            rootMain.getChildren().add(rootDetalleView);
            personaSeleccionada = new Corredores();
            VistaEdicionController.setTableViewPrevio(tableViewContactos);
            VistaEdicionController.setCorredores(entityManager, personaSeleccionada, true);
            VistaEdicionController.mostrarDatos();
        } catch (IOException ex) {
            Logger.getLogger(CorredoresViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void onActionButtonDeshacer(ActionEvent event) {
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vistaEdicion.fxml"));
            Parent rootDetalleView = fxmlLoader.load();

            // Ocultar la vista de la lista
            rootContactosView.setVisible(false);
            VistaEdicionController VistaEdicionController = (VistaEdicionController) fxmlLoader.getController();
            VistaEdicionController.setRootContactosView(rootContactosView);
            // Añadir la vista de detalle al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootContactosView.getScene().getRoot();
            rootMain.getChildren().add(rootDetalleView);
        } catch (IOException ex) {
            Logger.getLogger(CorredoresViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmar");
            alert.setHeaderText("¿Desea suprimir el siguiente registro?");
            alert.setContentText(personaSeleccionada.getNombre() + " "
            + personaSeleccionada.getApellidos());
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
    // Acciones a realizar si el usuario acepta
                entityManager.getTransaction().begin();
                entityManager.merge(personaSeleccionada);
                entityManager.remove(personaSeleccionada);
                entityManager.getTransaction().commit();
                tableViewContactos.getItems().remove(personaSeleccionada);
                tableViewContactos.getFocusModel().focus(null);
                tableViewContactos.requestFocus();
            } else {
    // Acciones a realizar si el usuario cancela
                int numFilaSeleccionada = tableViewContactos.getSelectionModel().getSelectedIndex();
                tableViewContactos.getItems().set(numFilaSeleccionada, personaSeleccionada);
                TablePosition pos = new TablePosition(tableViewContactos, numFilaSeleccionada, null);
                tableViewContactos.getFocusModel().focus(pos);
                tableViewContactos.requestFocus();   
            }
    }

    @FXML
    private void onActionButtonEditar(ActionEvent event) {
        if (personaSeleccionada != null){
        try {
            // Cargar la vista de detalle
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("vistaEdicion.fxml"));
            Parent rootDetalleView = fxmlLoader.load();

            // Ocultar la vista de la lista
            rootContactosView.setVisible(false);
            VistaEdicionController VistaEdicionController = (VistaEdicionController) fxmlLoader.getController();
            VistaEdicionController.setRootContactosView(rootContactosView);
            // Añadir la vista de detalle al StackPane principal para que se muestre
            StackPane rootMain = (StackPane) rootContactosView.getScene().getRoot();
            rootMain.getChildren().add(rootDetalleView);
            VistaEdicionController.setTableViewPrevio(tableViewContactos);
            System.out.println(entityManager);
            System.out.println(personaSeleccionada);
            VistaEdicionController.setCorredores(entityManager, personaSeleccionada, false);
            VistaEdicionController.mostrarDatos();
        } catch (IOException ex) {
            Logger.getLogger(CorredoresViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        }

}

