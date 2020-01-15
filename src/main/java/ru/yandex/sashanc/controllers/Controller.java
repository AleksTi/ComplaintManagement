package ru.yandex.sashanc.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import ru.yandex.sashanc.db.NotificationDao;
import ru.yandex.sashanc.pojo.Complaint;
import ru.yandex.sashanc.pojo.Notification;
import ru.yandex.sashanc.services.ComplaintService;
import ru.yandex.sashanc.services.ImageService;
import ru.yandex.sashanc.services.Service;

import java.io.File;
import java.util.Date;
import java.util.List;

public class Controller {

    private ObservableList<Notification> notificationsData = FXCollections.observableArrayList();
    private String fileNameSapQM;
    private String fileNameSapMB51;



    @FXML
    private TextField field1C;

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
    private TableView<Notification> notTable;

    @FXML
    private TableColumn<Notification, String> c0;

    @FXML
    private TableColumn<Notification, Integer> c1;

    @FXML
    private TableColumn<Notification, Date> c2;

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
    void initialize(){
        createDocs.setOnAction(event -> {
            fileNameSapQM = fieldSap.getText().trim();
            fileNameSapMB51 = field1C.getText().trim();
            if(!fileNameSapQM.equals("")&&!fileNameSapMB51.equals("")){
                new Service().createComplaintDocs(fileNameSapQM, fileNameSapMB51);
                setCellValues();
                initData(new NotificationDao().getListNotifications(fileNameSapQM));
                notTable.setItems(notificationsData);
            } else {
                infoLabel.setText("Поля не заполнены!");
            }
        });

        selectFile.setOnAction(event -> {
            FileChooser fc = new FileChooser();
            File selectedFile = fc.showOpenDialog(null);
            if (selectedFile != null){
                fieldSap.setText(selectedFile.getAbsolutePath());
                System.out.println(selectedFile.getName());
            } else {
                System.out.println("File not found");
            }
        });

        createComplaint.setOnAction(event -> {
            Notification not = notTable.getSelectionModel().getSelectedItem();
            notNumberView.setText("Вы выбрали " + not.getNotId());
            ComplaintService complaintService = new ComplaintService();
            complaintService.generateComplaint(not);
            ImageService imageService = new ImageService();
            imageService.inputImageToSheet();
            List<File> fileList = imageService.getImages("C:\\Users\\Sasha\\Desktop\\Complaint DB\\Photos");
            for(File f : fileList){
                System.out.println(f.getName());
            }
            System.out.println(not.getNotId());
        });
    }

    private  void setCellValues(){
        c0.setCellValueFactory(new PropertyValueFactory<Notification, String>("notType"));
        c1.setCellValueFactory(new PropertyValueFactory<Notification, Integer>("notId"));
        c2.setCellValueFactory(new PropertyValueFactory<Notification, Date>("notDate"));
        c3.setCellValueFactory(new PropertyValueFactory<Notification, String>("materialNumber"));
        c4.setCellValueFactory(new PropertyValueFactory<Notification, String>("materialDesSap"));
        c6.setCellValueFactory(new PropertyValueFactory<Notification, Integer>("complaintQuantity"));
        c7.setCellValueFactory(new PropertyValueFactory<Notification, String>("defectDescription"));
        c8.setCellValueFactory(new PropertyValueFactory<Notification, String>("notStatus"));
        c9.setCellValueFactory(new PropertyValueFactory<Notification, Integer>("supplierNumber"));
        c5.setCellValueFactory(new PropertyValueFactory<Notification, String>("supplierName"));
    }

    private void initData(List<Notification> notList) {
        for (Notification not : notList) {
            notificationsData.add(not);
        }
    }
}
