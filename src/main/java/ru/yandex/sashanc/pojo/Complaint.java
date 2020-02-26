package ru.yandex.sashanc.pojo;

import java.time.LocalDate;

public class Complaint {
    //Property are used in report
    private LocalDate compDate;
    private String compNumber;
    //TODO Заменить на List<Integer>
    private String relNotList;
    private String placeDetected;
    private String partNumber;
    private String partNameRu;
    private String partNameEn;

    private int defectQuantity;
    private int defectQuantityToPpm;
    private String deviationDescription;
    private String link;
    private String invoice;
    private LocalDate shippingDate;
    private LocalDate deliveryDate;
    private LocalDate detectionDate;
    private LocalDate report8dReceived;
    private LocalDate getReport8dClosed;
    private double totalCost;
    private String extraDelivery;
    private LocalDate paymentDate;
    private double paidCost;
    private String decision;
    private String annotation;

    //Property are used in complaint
    private String compContract;
    private LocalDate compContractDate;
    private String typeGB;
    private String action1;
    private String action2;
    private String action3;
    private int partPrice;
    private int serviceCost;
    private int deliveryCost;

    //Property for instance
    private int supplierId;
    private String status;

    public String getPartNameRu() {
        return partNameRu;
    }

    public void setPartNameRu(String partNameRu) {
        this.partNameRu = partNameRu;
    }

    public String getPartNameEn() {
        return partNameEn;
    }

    public void setPartNameEn(String partNameEn) {
        this.partNameEn = partNameEn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDate getReport8dReceived() {
        return report8dReceived;
    }

    public void setReport8dReceived(LocalDate report8dReceived) {
        this.report8dReceived = report8dReceived;
    }

    public LocalDate getGetReport8dClosed() {
        return getReport8dClosed;
    }

    public void setGetReport8dClosed(LocalDate getReport8dClosed) {
        this.getReport8dClosed = getReport8dClosed;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public String getExtraDelivery() {
        return extraDelivery;
    }

    public void setExtraDelivery(String extraDelivery) {
        this.extraDelivery = extraDelivery;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public double getPaidCost() {
        return paidCost;
    }

    public void setPaidCost(double paidCost) {
        this.paidCost = paidCost;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public int getDefectQuantityToPpm() {
        return defectQuantityToPpm;
    }

    public void setDefectQuantityToPpm(int defectQuantityToPpm) {
        this.defectQuantityToPpm = defectQuantityToPpm;
    }

    public String getPlaceDetected() {
        return placeDetected;
    }

    public void setPlaceDetected(String placeDetected) {
        this.placeDetected = placeDetected;
    }

    public String getCompNumber() {
        return compNumber;
    }

    public void setCompNumber(String compNumber) {
        this.compNumber = compNumber;
    }

    public LocalDate getCompDate() {
        return compDate;
    }

    public void setCompDate(LocalDate compDate) {
        this.compDate = compDate;
    }

    public String getCompContract() {
        return compContract;
    }

    public void setCompContract(String compContract) {
        this.compContract = compContract;
    }

    public LocalDate getCompContractDate() {
        return compContractDate;
    }

    public void setCompContractDate(LocalDate compContractDate) {
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

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(LocalDate shippingDate) {
        this.shippingDate = shippingDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public LocalDate getDetectionDate() {
        return detectionDate;
    }

    public void setDetectionDate(LocalDate detectionDate) {
        this.detectionDate = detectionDate;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
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

    public String getRelNotList() {
        return relNotList;
    }

    public void setRelNotList(String relNotList) {
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
