package com.ns.networking.model.foods;

import java.util.List;

public class FoodCategorySelection {

    private String foodType;
    private boolean isSelected;
    private List<FoodDaysSelection> mFoodDaysSelection;

    public FoodCategorySelection(String foodType, boolean isSelected, List<FoodDaysSelection> mFoodDaysSelection) {
        this.foodType = foodType;
        this.isSelected = isSelected;
        this.mFoodDaysSelection = mFoodDaysSelection;
    }

    public String getFoodType() {
        return foodType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public List<FoodDaysSelection> getmFoodDaysSelection() {
        return mFoodDaysSelection;
    }

    public void setmFoodDaysSelection(List<FoodDaysSelection> mFoodDaysSelection) {
        this.mFoodDaysSelection = mFoodDaysSelection;
    }
}
