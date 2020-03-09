package ru.yandex.sashanc.pojo;

import java.util.List;

public class LetterInfoNot {
    private int id;
    private String partNumber;
    private List<Integer> notNumberList;
    private String partDescriptionSap;
    private String deviationDescriptionSap;
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public List<Integer> getNotNumberList() {
        return notNumberList;
    }

    public void setNotNumberList(List<Integer> notNumberList) {
        this.notNumberList = notNumberList;
    }

    public String getPartDescriptionSap() {
        return partDescriptionSap;
    }

    public void setPartDescriptionSap(String partDescriptionSap) {
        this.partDescriptionSap = partDescriptionSap;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDeviationDescriptionSap() {
        return deviationDescriptionSap;
    }

    public void setDeviationDescriptionSap(String deviationDescriptionSap) {
        this.deviationDescriptionSap = deviationDescriptionSap;
    }
}
