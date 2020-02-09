package ru.yandex.sashanc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;
import ru.yandex.sashanc.Main;
import ru.yandex.sashanc.db.ComplaintDao;
import ru.yandex.sashanc.db.IComplaintDao;
import ru.yandex.sashanc.db.NotificationDao;
import ru.yandex.sashanc.db.connection.ConnectionManagerImpl;
import ru.yandex.sashanc.db.connection.IConnectionManager;
import ru.yandex.sashanc.pojo.Complaint;
import ru.yandex.sashanc.pojo.Notification;
import ru.yandex.sashanc.services.ComplaintService;
import ru.yandex.sashanc.services.Service;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class);

    private ObservableList<Notification> notificationsData = FXCollections.observableArrayList();
    private ObservableList<Complaint> complaintsData = FXCollections.observableArrayList();
    private Path fileNameSapQM;
    private Path fileNameSapMB51;
    private List<File> imageFileList;
    private IConnectionManager conManager = ConnectionManagerImpl.getInstance();



    @FXML
    private TextField fieldSapMB51;

    @FXML
    private Button createDocs;

    @FXML
    private TextField fieldSap;

    @FXML
    private Label infoLabel;

    @FXML
    private Label notNumberView;

    @FXML
    private Button selectFile;

    @FXML
    private Button createComplaint;

    @FXML
    private Button getFileMB51;

    @FXML
    private TableView<Notification> notTable;

    @FXML
    private TableView<Complaint> claimTable;

    @FXML
    private TableColumn<Notification, String> c0;

    @FXML
    private TableColumn<Notification, Integer> c1;

    @FXML
    private TableColumn<Notification, LocalDate> c2;

    @FXML
    private TableColumn<Notification, String> c3;

    @FXML
    private TableColumn<Notification, String> c4;

    @FXML
    private TableColumn<Notification, String> c5;

    @FXML
    private TableColumn<Notification, Integer> c6;

    @FXML
    private TableColumn<Notification, String> c7;

    @FXML
    private TableColumn<Notification, String> c8;

    @FXML
    private TableColumn<Notification, Integer> c9;

    @FXML
    private TableColumn<Notification, String> c10;

    @FXML
    private TableColumn<Notification, String> c11;

    @FXML
    private TableColumn<Notification, String> c12;

    @FXML
    private TableColumn<Notification, LocalDate> c13;

    @FXML
    private TableColumn<Notification, LocalDate> c14;

    @FXML
    private DatePicker complaintDateUser;

    @FXML
    private Label infoLabelBottom;

    @FXML
    private TextArea complDescRu;

    @FXML
    private TextArea complDescEn;

    @FXML
    private CheckBox imageAdd;

    @FXML
    private Button complaintsFromDb;

    @FXML
    private ChoiceBox<String> complaintType;

    @FXML
    private Button chooseImageFolderButton;

    @FXML
    private Label imageChosenDirLabel;



    @FXML
    void initialize(){
        chooseImageFolderButton.setOnAction(event -> {
            List<File> fileList = getImageFiles();
            if(!fileList.isEmpty()){
                imageChosenDirLabel.setText(fileList.get(0).getAbsolutePath());
                imageFileList = fileList;
            } else {
                imageChosenDirLabel.setText("Не выбрано ни одного файла, будет выполнен автоматический поиск");
                imageFileList = Collections.emptyList();
            }
        });

        getFileMB51.setOnAction(event -> getFile(fieldSapMB51));

        selectFile.setOnAction(event -> getFile(fieldSap));

        createDocs.setOnAction(event -> {
            fileNameSapQM = Paths.get(fieldSap.getText().trim());
            fileNameSapMB51 = Paths.get(fieldSapMB51.getText().trim());

            File file = new File(fileNameSapQM.toString());
            System.out.println("file: " + fileNameSapQM.toString());
            if(file.exists()){
                new Service().createComplaintDocs(fileNameSapQM, fileNameSapMB51);
                setCellTypeValues();
                initData(new NotificationDao().getListNotifications(fileNameSapQM, fileNameSapMB51));
                notTable.setItems(notificationsData);
                infoLabel.setText("");
            } else {
                infoLabel.setText("Поля не заполнены!");
            }
        });

        complaintsFromDb.setOnAction(event -> {
            IComplaintDao complDao = new ComplaintDao();
            List<Complaint> complaintList = complDao.getComplaintList();
            Field[] fields = Complaint.class.getDeclaredFields();
            logger.info("fields = " + fields.length);
            for (Field field : fields) {
                TableColumn tableColumn = new TableColumn();
                tableColumn.setId(field.getName());
                tableColumn.setText(field.getName());
                tableColumn.setMaxWidth(500);
                tableColumn.setEditable(true);
                tableColumn.setCellValueFactory(new PropertyValueFactory(field.getName()));
                logger.info("field.getName() = " + field.getName());
                claimTable.getColumns().add(tableColumn);
            }
            for (Complaint complaint : complaintList) {
                complaintsData.add(complaint);
            }
            claimTable.setItems(complaintsData);
        });

        imageAdd.setOnAction(event -> {
            chooseImageFolderButton.setDisable(!imageAdd.isSelected());
            if(!imageAdd.isSelected()){
                imageChosenDirLabel.setText(null);
            }
            logger.info("CheckBox переключен");
        });

        createComplaint.setOnAction(event -> {
            boolean isImageAdd = imageAdd.isSelected();
            LocalDate date;
            if (complaintDateUser.getValue() != null) {
                date = complaintDateUser.getValue();
            } else {
                date = LocalDate.now();
            }
            List<File> fileList;
            if(imageFileList == null){
                fileList = Collections.emptyList();
            } else {
                fileList = imageFileList;
            }
            Notification not = notTable.getSelectionModel().getSelectedItem();
            String complaintDescriptionRu = complDescRu.getText();
            String complaintDescriptionEn = complDescEn.getText();
            String typeComplaint = complaintType.getValue();
            infoLabelBottom.setText("Дата РА: " + date.toString() + ", номер сообщения: " + not.getNotId());
            ComplaintService complaintService = new ComplaintService();
            complaintService.generateComplaint(not, complaintDescriptionRu, complaintDescriptionEn, date, isImageAdd, typeComplaint, fileList);
        });
    }

    private void setCellTypeValues(){
        c0.setCellValueFactory(new PropertyValueFactory<Notification, String>("notType"));
        c1.setCellValueFactory(new PropertyValueFactory<Notification, Integer>("notId"));
        c2.setCellValueFactory(new PropertyValueFactory<Notification, LocalDate>("notDate"));
        c3.setCellValueFactory(new PropertyValueFactory<Notification, String>("materialNumber"));
        c4.setCellValueFactory(new PropertyValueFactory<Notification, String>("materialDesSap"));
        c5.setCellValueFactory(new PropertyValueFactory<Notification, String>("supplierName"));
        c6.setCellValueFactory(new PropertyValueFactory<Notification, Integer>("complaintQuantity"));
        c7.setCellValueFactory(new PropertyValueFactory<Notification, String>("defectDescription"));
        c8.setCellValueFactory(new PropertyValueFactory<Notification, String>("notStatus"));
        c9.setCellValueFactory(new PropertyValueFactory<Notification, Integer>("supplierNumber"));
        c10.setCellValueFactory(new PropertyValueFactory<Notification, String>("materialDesUser"));
        c11.setCellValueFactory(new PropertyValueFactory<Notification, String>("purchasingDoc"));
        c12.setCellValueFactory(new PropertyValueFactory<Notification, String>("deliveryNote"));
        c13.setCellValueFactory(new PropertyValueFactory<Notification, LocalDate>("deliveryNoteDate"));
        c14.setCellValueFactory(new PropertyValueFactory<Notification, LocalDate>("postDate"));
    }

    private void initData(List<Notification> notList) {
        for (Notification not : notList) {
            notificationsData.add(not);
        }
    }

    private void getFile(TextField textField){
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null){
            textField.setText(selectedFile.getAbsolutePath());
            System.out.println(selectedFile.getName());
        } else {
            System.out.println("File not found");
        }
    }

    private List<File> getImageFiles(){

        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        List<File> selectedFiles = fc.showOpenMultipleDialog(null);
        if (selectedFiles == null || selectedFiles.isEmpty()){
            selectedFiles = Collections.emptyList();
            logger.info("File not found");
        }
        return selectedFiles;
    }
}
