package ru.yandex.sashanc.services;

import javafx.stage.FileChooser;

import java.io.File;

public class GetFile implements  IGetFile{


    @Override
    public File getFile() {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null){
            System.out.println(selectedFile.getName());
            return selectedFile;
        }
        return null;
    }
}
