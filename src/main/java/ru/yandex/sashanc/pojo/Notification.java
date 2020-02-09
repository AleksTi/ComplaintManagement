package ru.yandex.sashanc.pojo;

import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

public class Notification {
    //Поля QM
    private String notType;
    private int notId;
    private LocalDate notDate;
    private String materialNumber;
    private String materialDesSap;
    //TODO данной колонки нет в формате ZFKAMA
    private String materialDesUser;
    private int complaintQuantity;
    private String defectDescription;
    private String notStatus;
    private int supplierNumber;
    private String supplierName;
    //Поле для связи двух файлов MB51 и QM
    private String purchasingDoc;
    //Поля из MB51
    private String deliveryNote;
    private LocalDate deliveryNoteDate;
    private LocalDate postDate;
    private String invoice;
    private String imageLink;

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public LocalDate getPostDate() {
        return postDate;
    }

    public void setPostDate(LocalDate postDate) {
        this.postDate = postDate;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public LocalDate getDeliveryNoteDate() {
        return deliveryNoteDate;
    }

    public void setDeliveryNoteDate(LocalDate deliveryNoteDate) {
        this.deliveryNoteDate = deliveryNoteDate;
    }

    public String getPurchasingDoc() {
        return purchasingDoc;
    }

    public void setPurchasingDoc(String purchasingDoc) {
        this.purchasingDoc = purchasingDoc;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return notId == that.notId &&
                Objects.equals(notType, that.notType) &&
                Objects.equals(notDate, that.notDate) &&
                Objects.equals(materialNumber, that.materialNumber) &&
                Objects.equals(materialDesSap, that.materialDesSap);
    }

    @Override
    public int hashCode() {

        return Objects.hash(notType, notId, notDate, materialNumber, materialDesSap);
    }

    public String getNotType() {
        return notType;
    }

    public void setNotType(String notType) {
        this.notType = notType;
    }

    public int getNotId() {
        return notId;
    }

    public void setNotId(int notId) {
        this.notId = notId;
    }

    public LocalDate getNotDate() {
        return notDate;
    }

    public void setNotDate(LocalDate notDate) {
        this.notDate = notDate;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getMaterialDesSap() {
        return materialDesSap;
    }

    public void setMaterialDesSap(String materialDesSap) {
        this.materialDesSap = materialDesSap;
    }

    public String getMaterialDesUser() {
        return materialDesUser;
    }

    public void setMaterialDesUser(String materialDesUser) {
        this.materialDesUser = materialDesUser;
    }

    public int getComplaintQuantity() {
        return complaintQuantity;
    }

    public void setComplaintQuantity(int complaintQuantity) {
        this.complaintQuantity = complaintQuantity;
    }

    public String getDefectDescription() {
        return defectDescription;
    }

    public void setDefectDescription(String defectDescription) {
        this.defectDescription = defectDescription;
    }

    public String getNotStatus() {
        return notStatus;
    }

    public void setNotStatus(String notStatus) {
        this.notStatus = notStatus;
    }

    public int getSupplierNumber() {
        return supplierNumber;
    }

    public void setSupplierNumber(int supplierNumber) {
        this.supplierNumber = supplierNumber;
    }
}

