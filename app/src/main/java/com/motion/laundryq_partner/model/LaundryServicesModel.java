package com.motion.laundryq_partner.model;

import java.util.List;

public class LaundryServicesModel {
    private List<TimeOperationModel> timeOperationalList;
    private List<CategoryModel> cagoryList;
    private Boolean deliveryOrder;

    public LaundryServicesModel() {
    }

    public LaundryServicesModel(List<TimeOperationModel> timeOperationalList, List<CategoryModel> cagoryList, Boolean deliveryOrder) {
        this.timeOperationalList = timeOperationalList;
        this.cagoryList = cagoryList;
        this.deliveryOrder = deliveryOrder;
    }

    public List<TimeOperationModel> getTimeOperationalList() {
        return timeOperationalList;
    }

    public void setTimeOperationalList(List<TimeOperationModel> timeOperationalList) {
        this.timeOperationalList = timeOperationalList;
    }

    public List<CategoryModel> getCagoryList() {
        return cagoryList;
    }

    public void setCagoryList(List<CategoryModel> cagoryList) {
        this.cagoryList = cagoryList;
    }

    public Boolean isDeliveryOrder() {
        return deliveryOrder;
    }

    public void setDeliveryOrder(Boolean deliveryOrder) {
        this.deliveryOrder = deliveryOrder;
    }
}
