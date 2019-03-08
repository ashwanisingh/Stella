package com.ns.networking.model.foods;

public class FoodDaysSelection {

    private String daysName;
    private boolean isSelected;

    public String getDaysName() {
        return daysName;
    }

    public void setDaysName(String daysName) {
        this.daysName = daysName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public FoodDaysSelection(String daysName, boolean isSelected) {
        this.daysName = daysName;
        this.isSelected = isSelected;
    }
}
