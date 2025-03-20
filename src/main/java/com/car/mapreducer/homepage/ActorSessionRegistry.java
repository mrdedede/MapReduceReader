package com.car.mapreducer.homepage;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import akka.actor.ActorRef;

/**
 * 
 */
public class ActorSessionRegistry {
    private final ConcurrentHashMap<String, List<ActorRef>> sessionActors = new ConcurrentHashMap<>();

    /**
     * 
     * @param sessionId
     * @param actors
     */
    public void addActors(String sessionId, List<ActorRef> actors) {
        sessionActors.put(sessionId, actors);
    }

    /**
     * 
     * @param sessionId
     * @return
     */
    public List<ActorRef> getActors(String sessionId) {
        return sessionActors.get(sessionId);
    }

    /**
     * 
     * @param sessionId
     */
    public void removeActors(String sessionId) {
        sessionActors.remove(sessionId);
    }
}
