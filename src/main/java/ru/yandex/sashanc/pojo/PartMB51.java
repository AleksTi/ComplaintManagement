package ru.yandex.sashanc.pojo;

import java.util.Date;
import java.util.Objects;

public class PartMB51 {
    private String materialDoc;
    private String materialNum;
    private Date materialDocDate;
    private String deliveryNote;
    private Date deliveryDate;
    private String invoice;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartMB51 partMB51 = (PartMB51) o;
        return Objects.equals(materialDoc, partMB51.materialDoc) &&
                Objects.equals(materialNum, partMB51.materialNum) &&
                Objects.equals(materialDocDate, partMB51.materialDocDate) &&
                Objects.equals(deliveryNote, partMB51.deliveryNote) &&
                Objects.equals(deliveryDate, partMB51.deliveryDate) &&
                Objects.equals(invoice, partMB51.invoice);
    }

    @Override
    public int hashCode() {

        return Objects.hash(materialDoc, materialNum, materialDocDate, deliveryNote, deliveryDate, invoice);
    }

    public String getMaterialDoc() {
        return materialDoc;
    }

    public void setMaterialDoc(String materialDoc) {
        this.materialDoc = materialDoc;
    }

    public String getMaterialNum() {
        return materialNum;
    }

    public void setMaterialNum(String materialNum) {
        this.materialNum = materialNum;
    }

    public Date getMaterialDocDate() {
        return materialDocDate;
    }

    public void setMaterialDocDate(Date materialDocDate) {
        this.materialDocDate = materialDocDate;
    }

    public String getDeliveryNote() {
        return deliveryNote;
    }

    public void setDeliveryNote(String deliveryNote) {
        this.deliveryNote = deliveryNote;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}
