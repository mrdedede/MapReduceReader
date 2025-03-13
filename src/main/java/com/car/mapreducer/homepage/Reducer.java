package com.car.mapreducer.homepage;

import akka.actor.UntypedActor;

public class Reducer extends UntypedActor {
    int wordCounter = 0;
    int mapperCounter = 0;

    @Override
    public void onReceive(Object msg) {
        System.out.println(msg);
    }
    
    public void reduce(int message) {
        this.mapperCounter += 1;
        this.wordCounter += message;
    }

    public void isThisThingOn() {
        System.out.println("Reducer - It's on!");
    }
}
