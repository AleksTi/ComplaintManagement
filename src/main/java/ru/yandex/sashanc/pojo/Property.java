package ru.yandex.sashanc.pojo;

import java.util.Objects;

public class Property {
    private int id;
    private String property;
    private String propertyValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property1 = (Property) o;
        return id == property1.id &&
                Objects.equals(property, property1.property);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, property);
    }
}
