package com.car.mapreducer.homepage;

import akka.actor.UntypedActor;

public class Mapper extends UntypedActor{
    
    @Override
    public void onReceive(Object msg) {
        System.out.println(msg);
    }

    public int map(String searched, String document) {
        int count = 0;
        String regex = "[,\\.\\s]";
        for (String word : document.split(regex)) {
            if(word.equals(searched)) {
                count+=1;
            }
        }
        return count;
    }

    public void isThisThingOn() {
        System.out.println("Mapper - It's on!");
    }
}
