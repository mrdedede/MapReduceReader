package com.car.mapreducer.homepage;

import java.util.ArrayList;
import java.util.List;

import akka.actor.UntypedActor;

/**
 * 
 */
public class Mapper extends UntypedActor{
    Boolean sendingData = false;
    Boolean waitingWord = false;
    List<String> texts;
    int counter = 0;

    public Mapper() {
        this.texts = new ArrayList<>();
    }
    
    /**
     * 
     */
    @Override
    public void onReceive(Object msg) {
        if(msg == "----map----") {
            this.waitingWord = true;
        } else if (msg == "----reduce----"){
            getSender().tell(this.counter, getSender());
        } else if (waitingWord) {
            this.counter = this.map((String) msg, texts);
            this.waitingWord = false;
        } else {
            System.out.println(msg);
            this.texts.add(msg.toString());
        }
    }

    /**
     * 
     * @param searched
     * @param document
     * @return
     */
    public int map(String searched, List<String> document) {
        int count = 0;
        for(String line : document) {
            String regex = "[,\\.\\s]";
            for (String word : line.split(regex)) {
                if(word.equals(searched)) {
                    count+=1;
                }
            }
        }
        return count;
    }
}
