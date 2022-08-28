package com.newland.design04.builder;

public class MealBuilder {

    public Meal prepareVegMeal (){
        Meal meal = new Meal();
        meal.addItem(new Item.VegBurger());
        meal.addItem(new Item.Coke());
        return meal;
    }

    public Meal prepareNonVegMeal (){
        Meal meal = new Meal();
        meal.addItem(new Item.ChickenBurger());
        meal.addItem(new Item.Pepsi());
        return meal;
    }
}
