package ru.yandex.sashanc.pojo;

public class Part {
    private String partNumber;
    private String partNameRu;
    private String partNameEn;
    private String altPartNumber;
    private String supplierPartNumber;
    private int supplierId;
    private String supplierName;

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
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

    public String getAltPartNumber() {
        return altPartNumber;
    }

    public void setAltPartNumber(String altPartNumber) {
        this.altPartNumber = altPartNumber;
    }

    public String getSupplierPartNumber() {
        return supplierPartNumber;
    }

    public void setSupplierPartNumber(String supplierPartNumber) {
        this.supplierPartNumber = supplierPartNumber;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
