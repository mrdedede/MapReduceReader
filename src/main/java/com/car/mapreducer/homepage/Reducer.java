package com.car.mapreducer.homepage;

import akka.actor.UntypedActor;

/**
 * 
 */
public class Reducer extends UntypedActor {
    int wordCounter = 0;
    int mapperCounter = 0;

    /**
     * 
     */
    @Override
    public void onReceive(Object msg) {
        if(this.mapperCounter < 3) {
            this.reduce((int)msg);
            System.out.println(msg);
        } else {
            getSender().tell(this.wordCounter, getSender());
            this.mapperCounter = 0;
        }
    }
    
    /**
     * 
     * @param message
     */
    public void reduce(int message) {
        this.mapperCounter += 1;
        this.wordCounter += message;
    }
}
