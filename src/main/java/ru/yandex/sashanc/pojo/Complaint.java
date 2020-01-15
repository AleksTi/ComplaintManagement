package ru.yandex.sashanc.pojo;

import java.util.Date;
import java.util.List;

public class Complaint {
    private String compNumber;
    private Date compDate;
    private String compContract;
    private Date compContractDate;
    private String typeGB;
    private String invoice;
    private Date shippingDate;
    private Date deliveryDate;
    private Date detectionDate;
    private String partNumber;
    private String partDescription;
    private String deviationDescription;
    private int defectQuantity;
    private List<Integer> relNotList;
    private String action1;
    private String action2;
    private String action3;
    private int partPrice;
    private int serviceCost;
    private int deliveryCost;


    public String getCompNumber() {
        return compNumber;
    }

    public void setCompNumber(String compNumber) {
        this.compNumber = compNumber;
    }

    public Date getCompDate() {
        return compDate;
    }

    public void setCompDate(Date compDate) {
        this.compDate = compDate;
    }

    public String getCompContract() {
        return compContract;
    }

    public void setCompContract(String compContract) {
        this.compContract = compContract;
    }

    public Date getCompContractDate() {
        return compContractDate;
    }

    public void setCompContractDate(Date compContractDate) {
        this.compContractDate = compContractDate;
    }

    public String getTypeGB() {
        return typeGB;
    }

    public void setTypeGB(String typeGB) {
        this.typeGB = typeGB;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public Date getDetectionDate() {
        return detectionDate;
    }

    public void setDetectionDate(Date detectionDate) {
        this.detectionDate = detectionDate;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getPartDescription() {
        return partDescription;
    }

    public void setPartDescription(String partDescription) {
        this.partDescription = partDescription;
    }

    public String getDeviationDescription() {
        return deviationDescription;
    }

    public void setDeviationDescription(String deviationDescription) {
        this.deviationDescription = deviationDescription;
    }

    public int getDefectQuantity() {
        return defectQuantity;
    }

    public void setDefectQuantity(int defectQuantity) {
        this.defectQuantity = defectQuantity;
    }

    public List<Integer> getRelNotList() {
        return relNotList;
    }

    public void setRelNotList(List<Integer> relNotList) {
        this.relNotList = relNotList;
    }

    public String getAction1() {
        return action1;
    }

    public void setAction1(String action1) {
        this.action1 = action1;
    }

    public String getAction2() {
        return action2;
    }

    public void setAction2(String action2) {
        this.action2 = action2;
    }

    public String getAction3() {
        return action3;
    }

    public void setAction3(String action3) {
        this.action3 = action3;
    }

    public int getPartPrice() {
        return partPrice;
    }

    public void setPartPrice(int partPrice) {
        this.partPrice = partPrice;
    }

    public int getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(int serviceCost) {
        this.serviceCost = serviceCost;
    }

    public int getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }
}
