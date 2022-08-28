package com.newland.design21.state;

/**
 * Author: leell
 * Date: 2022/8/28 12:15:45
 */
public interface State {
    public void doAction(Context context);
}
class StartState implements State {

    public void doAction(Context context) {
        System.out.println("Player is in start state");
        context.setState(this);
    }

    public String toString(){
        return "Start State";
    }
}
class StopState implements State {

    public void doAction(Context context) {
        System.out.println("Player is in stop state");
        context.setState(this);
    }

    public String toString(){
        return "Stop State";
    }
}
