package com.newland.design21.state;

/**
 * Author: leell
 * Date: 2022/8/28 12:16:15
 */
public class Context {
    private State state;

    public Context(){
        state = null;
    }

    public void setState(State state){
        this.state = state;
    }

    public State getState(){
        return state;
    }
}
