package com.newland.design04.builder;

public interface Packing {
    String pack();
    class Wrapper implements Packing {

        @Override
        public String pack() {
            return "Wrapper";
        }
    }
    class Bottle implements Packing {

        @Override
        public String pack() {
            return "Bottle";
        }
    }
}
