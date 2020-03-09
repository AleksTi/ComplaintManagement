package ru.yandex.sashanc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;
import ru.yandex.sashanc.db.ComplaintDaoImpl;
import ru.yandex.sashanc.db.IComplaintDao;
import ru.yandex.sashanc.pojo.Complaint;
import ru.yandex.sashanc.pojo.Notification;
import ru.yandex.sashanc.services.ComplaintService;
import ru.yandex.sashanc.services.IComplaintService;
import ru.yandex.sashanc.services.NotificationService;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Controller {
    private static final Logger logger = Logger.getLogger(Controller.class);

    private ObservableList<Notification> notificationsData = FXCollections.observableArrayList();
    private ObservableList<Complaint> complaintsData = FXCollections.observableArrayList();
    private Path fileNameSapQM;
    private Path fileNameSapMB51;
    private List<File> imageFileList;
    private List<Notification> notifications;

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
    private Button openImagesFolder;


    @FXML
    void initialize(){
        setNotTableMultiple();
        setComplaintTableColumns();

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

        openImagesFolder.setOnAction(event -> {
            Desktop desktop = null;
            if (Desktop.isDesktopSupported()) {
                desktop = Desktop.getDesktop();
            }
            try {
                if (notTable.getSelectionModel().getSelectedItems().get(0) != null &&
                        !notTable.getSelectionModel().getSelectedItems().get(0).getImageLink().toString().equals("")) {
                    File imagesDirectory = new File(notTable.getSelectionModel().getSelectedItems().get(0).getImageLink().toString());
                    if (imagesDirectory.exists()) {
                        desktop.open(imagesDirectory);
                        imageChosenDirLabel.setText("");
                    } else {
                        imageChosenDirLabel.setText("Папка с изображениями не найдена");
                    }
                } else {
                    imageChosenDirLabel.setText("Папка с изображениями не найдена");
                }
            } catch (IOException ioe) {
                logger.info(ioe);
            }
        });

        getFileMB51.setOnAction(event -> getFile(fieldSapMB51));

        selectFile.setOnAction(event -> getFile(fieldSap));

        createDocs.setOnAction(event -> {
            fileNameSapQM = Paths.get(fieldSap.getText().trim());
            fileNameSapMB51 = Paths.get(fieldSapMB51.getText().trim());
            if(new File(fileNameSapQM.toString()).exists()){
                setCellTypeValues();
                notificationsData.addAll(new NotificationService().getListNotifications(fileNameSapQM, fileNameSapMB51));
                notTable.setItems(notificationsData);
                infoLabel.setText("");
            } else {
                infoLabel.setText("Поле \"Имя файла SAP QM\" не заполнено!");
            }
        });

        complaintsFromDb.setOnAction(event -> {
            IComplaintDao complDao = new ComplaintDaoImpl();
            List<Complaint> complaintList = complDao.getComplaintList();
            complaintsData.addAll(complaintList);
            claimTable.setItems(complaintsData);
        });

        imageAdd.setOnAction(event -> {
            chooseImageFolderButton.setDisable(!imageAdd.isSelected());
            if(!imageAdd.isSelected()){
                imageChosenDirLabel.setText(null);
            }
            logger.info("CheckBox переключен");
        });

        /**
         * Метод отрабатывает событие на нажатие кнопки "Создать РА"
         */
        createComplaint.setOnAction(event -> {
            if (checkNotSuppliersId()) {
                LocalDate date;
                if (complaintDateUser.getValue() != null) {
                    date = complaintDateUser.getValue();
                } else {
                    date = LocalDate.now();
                }
                List<File> fileList;
                if (imageFileList == null) {
                    fileList = Collections.emptyList();
                } else {
                    fileList = imageFileList;
                }
                IComplaintService complaintService = new ComplaintService();
                complaintsData.addAll(complaintService.generateComplaint(
                        notTable.getSelectionModel().getSelectedItems(),
                        complDescRu.getText(),
                        complDescEn.getText(),
                        date,
                        imageAdd.isSelected(),
                        complaintType.getValue(),
                        fileList));
                claimTable.setItems(complaintsData);
            } else {
                infoLabelBottom.setText("Выбраны сообщения для разных поставщиков");
            }
        });
    }

    private void setNotTableMultiple(){
        notTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        notifications = new ArrayList<>();
        ListChangeListener<Notification> multiSelection = changed -> {
            infoLabelBottom.setText("");
            while (changed.next()){
                notifications.clear();
                notifications.addAll(changed.getList());
            }
//            for (Notification n : notifications) {
//                logger.info(notifications.size() + " " + n.getNotId() + " " + notifications.indexOf(n));
//            }
        };
        notTable.getSelectionModel().getSelectedItems().addListener(multiSelection);
    }

    private void setComplaintTableColumns(){
        Field[] fields = Complaint.class.getDeclaredFields();
        logger.info("Complaint fields = " + fields.length);
        for (Field field : fields) {
            TableColumn tableColumn = new TableColumn();
            tableColumn.setId(field.getName());
            tableColumn.setText(Complaint.getColumnHeads().get(field.getName()));
            tableColumn.setPrefWidth(150);
            tableColumn.setEditable(true);
            tableColumn.setCellValueFactory(new PropertyValueFactory(field.getName()));
            logger.info("Complaint field.getName() = " + field.getName());
            claimTable.getColumns().add(tableColumn);
        }
    }

    private void setCellTypeValues(){
        Field[] fields = Notification.class.getDeclaredFields();
        logger.info("Notification fields = " + fields.length);
        for (Field field : fields) {
            TableColumn tableColumn = new TableColumn();
            tableColumn.setId(field.getName());
            //TODO Сделать вывод имён корректным
            tableColumn.setText(field.getName());
            //TODO Сделать ширину не жёсткой
            tableColumn.setPrefWidth(150);
            tableColumn.setEditable(true);
            PropertyValueFactory<Notification, ?> pvf = new PropertyValueFactory<>(field.getName());
            tableColumn.setCellValueFactory(pvf);
            logger.info("Notification field.getName() = " + field.getName());
            notTable.getColumns().add(tableColumn);
        }


//        c0.setCellValueFactory(new PropertyValueFactory<Notification, String>("notType"));
//        c1.setCellValueFactory(new PropertyValueFactory<Notification, Integer>("notId"));
//        c2.setCellValueFactory(new PropertyValueFactory<Notification, LocalDate>("notDate"));
//        c3.setCellValueFactory(new PropertyValueFactory<Notification, String>("materialNumber"));
//        c4.setCellValueFactory(new PropertyValueFactory<Notification, String>("materialDesSap"));
//        c5.setCellValueFactory(new PropertyValueFactory<Notification, String>("supplierName"));
//        c6.setCellValueFactory(new PropertyValueFactory<Notification, Integer>("complaintQuantity"));
//        c7.setCellValueFactory(new PropertyValueFactory<Notification, String>("defectDescription"));
//        c8.setCellValueFactory(new PropertyValueFactory<Notification, String>("notStatus"));
//        c9.setCellValueFactory(new PropertyValueFactory<Notification, Integer>("supplierNumber"));
//        c10.setCellValueFactory(new PropertyValueFactory<Notification, String>("materialDesUser"));
//        c11.setCellValueFactory(new PropertyValueFactory<Notification, String>("purchasingDoc"));
//        c12.setCellValueFactory(new PropertyValueFactory<Notification, String>("deliveryNote"));
//        c13.setCellValueFactory(new PropertyValueFactory<Notification, LocalDate>("deliveryNoteDate"));
//        c14.setCellValueFactory(new PropertyValueFactory<Notification, LocalDate>("postDate"));
    }

    private void getFile(TextField textField){
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null){
            textField.setText(selectedFile.getAbsolutePath());
        } else {
            logger.info("File not found");
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

    private boolean checkNotSuppliersId(){
        boolean isChecked = true;
        int supplierNumber = notifications.get(0).getSupplierNumber();
        for (Notification n : notifications){
            if(supplierNumber != n.getSupplierNumber()){
                isChecked = false;
                break;
            }
        }
        return isChecked;
    }
}
