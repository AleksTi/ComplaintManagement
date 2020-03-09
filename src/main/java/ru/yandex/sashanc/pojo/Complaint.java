package ru.yandex.sashanc.pojo;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Complaint implements Cloneable {

    public static Map<String, String> getColumnHeads() {
        Map<String, String> columnHeads = new HashMap<>();
        columnHeads.put("compDate", "Дата РА");
        columnHeads.put("compNumber", "Номер РА");
        columnHeads.put("relNotList", "Сообщения");
        columnHeads.put("placeDetected", "Место обнаружения");
        columnHeads.put("partNumber", "Номер детали");
        columnHeads.put("partNameRu", "Наименование детали Ru");
        columnHeads.put("partNameEn", "Наименование детали En");
        columnHeads.put("defectQuantity", "Несоотв. кол-во");
        columnHeads.put("defectQuantityToPpm", "Несоотв. кол-во PPM");
        columnHeads.put("deviationDescriptionSap", "Описание несоотв. (SAP)");
        columnHeads.put("deviationDescriptionRu", "Описание несоотв. (Ru)");
        columnHeads.put("deviationDescriptionEn", "Описание несоотв. (En)");
        columnHeads.put("link", "Ссылка");
        columnHeads.put("invoice", "Инвойс");
        columnHeads.put("shippingDate", "Дата отгр/инв");
        columnHeads.put("deliveryDate", "Дата поставки");
        columnHeads.put("detectionDate", "Дата обнаруж");
        columnHeads.put("report8dReceived", "Дата получ 8D");
        columnHeads.put("report8dClosed", "Дата закр 8D");
        columnHeads.put("totalCost", "Сумма затрат");
        columnHeads.put("extraDelivery", "Доп. поставка");
        columnHeads.put("paymentDate", "Дата оплаты");
        columnHeads.put("paidCost", "Возмещён. сумма");
        columnHeads.put("decision", "Решение");
        columnHeads.put("annotation", "Применание");
        columnHeads.put("compContract", "Контракт");
        columnHeads.put("compContractDate", "Дата контракта");
        columnHeads.put("typeGB", "Тип КПП");
        columnHeads.put("actions", "Действия");
        columnHeads.put("partPrice", "Стоимость детали");
        columnHeads.put("serviceCustomCost", "Затраты тамож.");
        columnHeads.put("deliveryCost", "Затраты трансп.");
        columnHeads.put("supplierId", "Id поставщика");
        columnHeads.put("status", "Статус РА");
        return columnHeads;
    }

    //Property are used in report
    private LocalDate compDate;
    private String compNumber;
    private List<Integer> relNotList;
    private String placeDetected;
    private String partNumber;
    private String partNameRu;
    private String partNameEn;
    private int defectQuantity;
    private int defectQuantityToPpm;
    private String deviationDescriptionSap;
    private String deviationDescriptionRu;
    private String deviationDescriptionEn;
    private String link;
    private String invoice;
    private LocalDate shippingDate;
    private LocalDate deliveryDate;
    private LocalDate detectionDate;
    private LocalDate report8dReceived;
    private LocalDate report8dClosed;
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
    private List<String> actions;
    private int partPrice;
    private int serviceCustomCost;
    private int deliveryCost;

    //Property for instance
    private int supplierId;
    private String status;

    public String getDeviationDescriptionSap() {
        return deviationDescriptionSap;
    }

    public void setDeviationDescriptionSap(String deviationDescriptionSap) {
        this.deviationDescriptionSap = deviationDescriptionSap;
    }

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

    public LocalDate getReport8dClosed() {
        return report8dClosed;
    }

    public void setReport8dClosed(LocalDate report8dClosed) {
        this.report8dClosed = report8dClosed;
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

    public String getDeviationDescriptionRu() {
        return deviationDescriptionRu;
    }

    public void setDeviationDescriptionRu(String deviationDescriptionRu) {
        this.deviationDescriptionRu = deviationDescriptionRu;
    }

    public String getDeviationDescriptionEn() {
        return deviationDescriptionEn;
    }

    public void setDeviationDescriptionEn(String deviationDescriptionEn) {
        this.deviationDescriptionEn = deviationDescriptionEn;
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

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public int getServiceCustomCost() {
        return serviceCustomCost;
    }

    public void setServiceCustomCost(int serviceCustomCost) {
        this.serviceCustomCost = serviceCustomCost;
    }

    public int getPartPrice() {
        return partPrice;
    }

    public void setPartPrice(int partPrice) {
        this.partPrice = partPrice;
    }

    public int getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }
}
