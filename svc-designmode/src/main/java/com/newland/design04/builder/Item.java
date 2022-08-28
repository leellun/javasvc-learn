package com.newland.design04.builder;

public interface Item {
    public String name();
    public Packing packing();
    public float price();

    abstract class Burger implements Item {

        @Override
        public Packing packing() {
            return new Packing.Wrapper();
        }

        @Override
        public abstract float price();
    }
    abstract class ColdDrink implements Item {

        @Override
        public Packing packing() {
            return new Packing.Bottle();
        }

        @Override
        public abstract float price();
    }
    class VegBurger extends Burger {

        @Override
        public float price() {
            return 25.0f;
        }

        @Override
        public String name() {
            return "Veg Burger";
        }
    }
    class ChickenBurger extends Burger {

        @Override
        public float price() {
            return 50.5f;
        }

        @Override
        public String name() {
            return "Chicken Burger";
        }
    }
    class Coke extends ColdDrink {

        @Override
        public float price() {
            return 30.0f;
        }

        @Override
        public String name() {
            return "Coke";
        }
    }
    class Pepsi extends ColdDrink {

        @Override
        public float price() {
            return 35.0f;
        }

        @Override
        public String name() {
            return "Pepsi";
        }
    }
}
