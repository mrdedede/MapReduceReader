package com.car.mapreducer.homepage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 */
@Controller
public class HomePageController {

    private final ActorSystem actorSystem = ActorSystem.create("MapReducerSytem");
    private final ActorSessionRegistry actorRegistry = new ActorSessionRegistry();

    /** Retourne le screen home en HTML, où les opérations seront demarrées
     * 
     * @return Le HTML de la démarrage de session
     */
    @GetMapping("home")
    public ModelAndView homeScreen(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        if(sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            session.setAttribute("sessionId", sessionId);
        }
        ModelAndView curView = new ModelAndView("home");
        return curView;
    }
    
    /**
     * 
     * @return
     */
    @PostMapping("/mapreduce/init")
    public RedirectView turnOnMapReducer(HttpSession session) {
        List<ActorRef> actors = createActors(actorSystem);
        String sessionId = (String) session.getAttribute("sessionId");
        actorRegistry.addActors(sessionId, actors);
        return new RedirectView("/home");
    }

    /**
     * 
     * @param file
     * @return
     */
    @PostMapping("/file/upload")
    public RedirectView uploadFile(HttpSession session, @RequestParam("fichier") MultipartFile fichier) {
        String uploadDir = "uploads/";
        Path filePath = Paths.get(uploadDir + fichier.getOriginalFilename());
        try {
            fichier.transferTo(filePath);
            session.setAttribute("file", filePath);
        } catch (Exception e) {
            System.out.println("Failed upload failed");
        }
        session.setAttribute("file", filePath);
        initMappers(session);
        return new RedirectView("/home");
    }

    /**
     * 
     * @param mot
     * @return
     */
    @PostMapping("/word/counter")
    public RedirectView postMethodName(HttpSession session, @RequestParam("mot") String mot) {
        String sessionId = (String) session.getAttribute("sessionId");
        List<ActorRef> actors = actorRegistry.getActors(sessionId);
        for(int i=0; i<=2; i++) {
            actors.get(i).tell("----map----", null);
            actors.get(i).tell(mot, null);
            actors.get(i).tell("----reduce----", actors.get(3));
            actors.get(i).tell("----reduce----", actors.get(4));
        }
        return new RedirectView("/home");
    }
    
    

    /**
     * 
     * @param system
     * @return
     */
    public List<ActorRef> createActors(ActorSystem system) {
        ActorRef mapper1 = system.actorOf(Props.create(Mapper.class), "mapperActor1");
        ActorRef mapper2 = system.actorOf(Props.create(Mapper.class), "mapperActor2");
        ActorRef mapper3 = system.actorOf(Props.create(Mapper.class), "mapperActor3");
        ActorRef reducer1 = system.actorOf(Props.create(Reducer.class), "reducerActor1");
        ActorRef reducer2 = system.actorOf(Props.create(Reducer.class), "reducerActor2");
        List<ActorRef> mapReducerList = List.of(mapper1, mapper2, mapper3, reducer1, reducer2);
        return mapReducerList;
    }

    /**
     * 
     * @param session
     */
    public void initMappers(HttpSession session) {
        String sessionId = (String) session.getAttribute("sessionId");
        List<ActorRef> actorList = actorRegistry.getActors(sessionId);
        Path filePath = (Path) session.getAttribute("file");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toString()))) {
            int count = 0;
            String line;
            while((line = reader.readLine()) != null) {
                actorList.get(count).tell(line, null);
                count++;
                if(count == 3) {
                    count = 0;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
