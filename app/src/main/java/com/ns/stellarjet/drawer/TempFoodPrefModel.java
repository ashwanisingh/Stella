package com.ns.stellarjet.drawer;

import java.util.List;

public class TempFoodPrefModel {


    /**
     * food_category : veg
     * days : ["Monday","Wednesday"]
     */

    private String food_category;
    private List<String> days;

    public String getFood_category() {
        return food_category;
    }

    public void setFood_category(String food_category) {
        this.food_category = food_category;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }
}
