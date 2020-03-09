package ru.yandex.sashanc.services;

import ru.yandex.sashanc.db.ILetterInfoDao;
import ru.yandex.sashanc.db.LetterInfoDaoImpl;
import ru.yandex.sashanc.pojo.LetterInfoNot;
import ru.yandex.sashanc.pojo.Notification;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LetterInfoService {

    public void createLetter(List<Notification> notifications, Path path){
        List<LetterInfoNot> letterInfoNots = new CopyOnWriteArrayList<>();
        List<LetterInfoNot> letterInfoNots1C = new CopyOnWriteArrayList<>();
        String deviationDescSap;
        boolean isNotAdded;
        for (Notification not : notifications) {
            isNotAdded = false;
            deviationDescSap = "";
            if (not.getDefectDescription() != null) {
                deviationDescSap = not.getDefectDescription();
            }
            if(!deviationDescSap.contains("КЧП")){
                isNotAdded = addItem(letterInfoNots, deviationDescSap, isNotAdded, not);
            } else {
                isNotAdded = addItem(letterInfoNots1C, deviationDescSap, isNotAdded, not);
            }
            if(!isNotAdded){
                LetterInfoNot letterInfoNot = new LetterInfoNot();
                letterInfoNot.setPartNumber(not.getMaterialNumber());
                letterInfoNot.setPartDescriptionSap(not.getMaterialDesSap());
                letterInfoNot.setDeviationDescriptionSap(not.getDefectDescription());
                letterInfoNot.setNotNumberList(new ArrayList<>());
                letterInfoNot.getNotNumberList().add(not.getNotId());
                letterInfoNot.setQuantity(not.getComplaintQuantity());
                if(!deviationDescSap.contains("КЧП")) {
                    letterInfoNots.add(letterInfoNot);
                } else {
                    letterInfoNots1C.add(letterInfoNot);
                }
            }
        }
        ILetterInfoDao letterInfoDao = new LetterInfoDaoImpl();
        letterInfoDao.createLetter(letterInfoNots, letterInfoNots1C, path);
    }

    private boolean addItem(List<LetterInfoNot> letterInfoNots, String deviationDescSap, boolean isNotAdded, Notification not) {
        for (LetterInfoNot letterNot : letterInfoNots) {
            if (not.getMaterialNumber().equals(letterNot.getPartNumber()) && deviationDescSap.equals(letterNot.getDeviationDescriptionSap())) {
                letterNot.setQuantity(letterNot.getQuantity() + not.getComplaintQuantity());
                letterNot.getNotNumberList().add(not.getNotId());
                isNotAdded = true;
                break;
            }
        }
        return isNotAdded;
    }
}
