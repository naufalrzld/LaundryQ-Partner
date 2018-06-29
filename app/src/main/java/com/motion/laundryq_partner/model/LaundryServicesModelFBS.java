package com.motion.laundryq_partner.model;

import java.util.Map;

public class LaundryServicesModelFBS {
    private boolean delivery_order;
    private Map<String, CategoryModel> categories;
    private Map<String, Map<String, String>> time_operational;

    public LaundryServicesModelFBS() {
    }

    public LaundryServicesModelFBS(boolean delivery_order, Map<String, CategoryModel> categories,
                                   Map<String, Map<String, String>> time_operational) {
        this.delivery_order = delivery_order;
        this.categories = categories;
        this.time_operational = time_operational;
    }

    public boolean getDelivery_order() {
        return delivery_order;
    }

    public void setDelivery_order(boolean delivery_order) {
        this.delivery_order = delivery_order;
    }

    public Map<String, CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(Map<String, CategoryModel> categories) {
        this.categories = categories;
    }

    public Map<String, Map<String, String>> getTime_operational() {
        return time_operational;
    }

    public void setTime_operational(Map<String, Map<String, String>> time_operational) {
        this.time_operational = time_operational;
    }
}
