package ru.yandex.sashanc.pojo;

import java.time.LocalDate;

public class Contract {
    private int id;
    private int supplierId;
    private String supplierSapName;
    private String employeeShortName;
    private String employeeId;
    private String contractName;
    private String supplierShortName;
    private String complaintTitle;
    private String contractNumber;
    private LocalDate date;
    private String paymentItem;
    private String researchItem;
    private String returnTerms;
    private int ppmTarget;
    private String bank;
    private String contractAdress;
    private String employeeShortNameQuality;
    private String employeeIdQuality;

    public String getEmployeeShortNameQuality() {
        return employeeShortNameQuality;
    }

    public void setEmployeeShortNameQuality(String employeeShortNameQuality) {
        this.employeeShortNameQuality = employeeShortNameQuality;
    }

    public String getEmployeeIdQuality() {
        return employeeIdQuality;
    }

    public void setEmployeeIdQuality(String employeeIdQuality) {
        this.employeeIdQuality = employeeIdQuality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierSapName() {
        return supplierSapName;
    }

    public void setSupplierSapName(String supplierSapName) {
        this.supplierSapName = supplierSapName;
    }

    public String getEmployeeShortName() {
        return employeeShortName;
    }

    public void setEmployeeShortName(String employeeShortName) {
        this.employeeShortName = employeeShortName;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getContractName() {
        return contractName;
    }

    public void setContractName(String contractName) {
        this.contractName = contractName;
    }

    public String getSupplierShortName() {
        return supplierShortName;
    }

    public void setSupplierShortName(String supplierShortName) {
        this.supplierShortName = supplierShortName;
    }

    public String getComplaintTitle() {
        return complaintTitle;
    }

    public void setComplaintTitle(String complaintTitle) {
        this.complaintTitle = complaintTitle;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getPaymentItem() {
        return paymentItem;
    }

    public void setPaymentItem(String paymentItem) {
        this.paymentItem = paymentItem;
    }

    public String getResearchItem() {
        return researchItem;
    }

    public void setResearchItem(String researchItem) {
        this.researchItem = researchItem;
    }

    public String getReturnTerms() {
        return returnTerms;
    }

    public void setReturnTerms(String returnTerms) {
        this.returnTerms = returnTerms;
    }

    public int getPpmTarget() {
        return ppmTarget;
    }

    public void setPpmTarget(int ppmTarget) {
        this.ppmTarget = ppmTarget;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getContractAdress() {
        return contractAdress;
    }

    public void setContractAdress(String contractAdress) {
        this.contractAdress = contractAdress;
    }
}
