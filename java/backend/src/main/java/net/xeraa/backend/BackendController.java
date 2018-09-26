package net.xeraa.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@RestController
public class BackendController {

    private static final Logger log = LoggerFactory.getLogger(BackendController.class);

    @Autowired
    private RestTemplate restTemplate;

    private Random random = new Random();

    @Value("${APP_BACKEND:#{'http://localhost:8081'}}")
    private String backendUrl;

    @Value("${APP_FRONTEND:#{'http://localhost:8080'}}")
    private String frontendUrl;

    @RequestMapping("/slow")
    public String home() throws InterruptedException {
        final String callUrl = backendUrl + "/slow-call";
        String returnValue = this.backgroundTask1();
        returnValue += this.backgroundTask2();
        returnValue +=  restTemplate.getForObject(callUrl, String.class);
        log.info("You called something slow");
        return returnValue + "slow...";
    }

    @Async
    public String backgroundTask1() throws InterruptedException {
        int millis = this.random.nextInt(1000);
        Thread.sleep(millis);
        //this.tracer.addTag("background-sleep-millis", String.valueOf(millis));
        log.info("Background task ran with a delay of {} ms", millis);
        return "This ";
    }

    public String backgroundTask2() throws InterruptedException {
        int millis = this.random.nextInt(1000);
        Thread.sleep(millis);
        //this.tracer.addTag("background-sleep-millis", String.valueOf(millis));
        log.info("Background task ran with a delay of {} ms", millis);
        return "is ";
    }

    @RequestMapping("/slow-call")
    public String call() throws InterruptedException {
        log.info("Calling another method from /slow");
        return "so ";
    }

}
