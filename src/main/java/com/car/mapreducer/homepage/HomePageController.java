package com.car.mapreducer.homepage;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HomePageController {

    /** Retourne le screen home en HTML, où les opérations seront demarrées
     * 
     * @return Le HTML de la démarrage de session
     */
    @GetMapping("home")
    public ModelAndView homeScreen() {
        ModelAndView curView = new ModelAndView("home");
        return curView;
    }
    
    /**
     * 
     * @return
     */
    @PostMapping("/mapreduce/init")
    public void turnOnMapReducer() {
        ActorSystem system = ActorSystem.create("MapReducerSytem");
        List<ActorRef> actors = createActors(system);
        for(int i = 0; i < actors.size(); i+=1 ) {
            System.out.println(actors.get(i));
        }
    }

    public List<ActorRef> createActors(ActorSystem system) {
        ActorRef mapper1 = system.actorOf(Props.create(Mapper.class), "mapperActor1");
        ActorRef mapper2 = system.actorOf(Props.create(Mapper.class), "mapperActor2");
        ActorRef mapper3 = system.actorOf(Props.create(Mapper.class), "mapperActor3");
        ActorRef reducer1 = system.actorOf(Props.create(Reducer.class), "reducerActor1");
        ActorRef reducer2 = system.actorOf(Props.create(Reducer.class), "reducerActor2");
        List<ActorRef> mapReducerList = List.of(mapper1, mapper2, mapper3, reducer1, reducer2);
        return mapReducerList;
    }


    
}
