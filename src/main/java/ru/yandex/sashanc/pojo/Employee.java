package ru.yandex.sashanc.pojo;

public class Employee {
    private String id;
    private String department;
    private String surnameRu;
    private String nameRu;
    private String surnameEn;
    private String nameEn;
    private String eMail;
    private String positionRu;
    private String positionEn;

    public String getPositionRu() {
        return positionRu;
    }

    public void setPositionRu(String positionRu) {
        this.positionRu = positionRu;
    }

    public String getPositionEn() {
        return positionEn;
    }

    public void setPositionEn(String positionEn) {
        this.positionEn = positionEn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSurnameRu() {
        return surnameRu;
    }

    public void setSurnameRu(String surnameRu) {
        this.surnameRu = surnameRu;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getSurnameEn() {
        return surnameEn;
    }

    public void setSurnameEn(String surnameEn) {
        this.surnameEn = surnameEn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }
}
