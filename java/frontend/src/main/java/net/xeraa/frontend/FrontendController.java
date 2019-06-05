package net.xeraa.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Controller
public class FrontendController {

	private static final Logger log = LoggerFactory.getLogger(FrontendController.class);

	@Autowired
	private RestTemplate restTemplate;

	private Random random = new Random();

	@Value("${APP_BACKEND:#{'http://localhost:8081'}}")
	private String backendUrl;

	@Value("${APP_FRONTEND:#{'http://localhost:8080'}}")
	private String frontendUrl;

	@RequestMapping("/generate")
	public void generate(@RequestParam(value="size", required=false, defaultValue="5") Integer size, Model model) {
		String callUrl = backendUrl + "/add";
		log.info("Calling {}", callUrl);

		if (size > 10){
			log.warn("Trying to add {} people. This is probably a bit too much and would overload the server", size);
			size = 10;
			log.info("Change the number of people to be added to {}", size);
		}

		for (int i = 0; i < size; i++) {
			restTemplate.getForObject(callUrl, String.class);
		}

		model.addAttribute("size", size);
		log.info("Added {} people", size);
	}

	@RequestMapping("/add")
	public void add(@RequestParam String name, Model model) {
		String callUrl = backendUrl + "/add?name={name}";
		log.info("Calling {}", callUrl);

        restTemplate.exchange(callUrl,
                HttpMethod.GET,
                null,
                String.class,
                name
        ).getBody();

		model.addAttribute("size", 1);
		model.addAttribute("name", name);
		MDC.put("name", name);
		log.info("Added 1 person with name {}", name);
	}

	@RequestMapping("/search")
	public void search(@RequestParam(value="q", required=false, defaultValue="") String q, Model model) {

		String callUrl;
        List persons;
	    if (q.isEmpty()) {
		    callUrl = backendUrl + "/all";
		    log.info("Calling {}", callUrl);
            persons = restTemplate.getForObject(callUrl, List.class);
        } else {
		    callUrl = backendUrl + "/search?q={q}";
		    log.info("Calling {}", callUrl);
            persons = restTemplate.exchange(callUrl,
                    HttpMethod.GET,
                    null,
                    List.class,
                    q
            ).getBody();
			MDC.put("name", q);
			log.info("Searched for {}", q);
        }

		model.addAttribute("size", persons.size());
		model.addAttribute("persons", persons);
	}

	@RequestMapping("/good")
	public void good(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		if(!"World".equals(name)) {
			MDC.put("name", name);
		}
		log.info("Calling something good");
		MDC.clear();
	}

	@RequestMapping("/bad")
	public void bad() {
		log.info("Calling something bad");
		throw new RuntimeException("My bad, something went wrong...");
	}

	@RequestMapping("/null")
	public void nullpointer() {
		log.info("Calling something null");
		throw new NullPointerException("This is indeed null...");
	}

	@RequestMapping("/call")
	public void call(Model model) throws InterruptedException {
		String callUrl = backendUrl + "/slow";
		int millis = this.random.nextInt(2000);
		Thread.sleep(millis);
		//this.tracer.addTag("random-sleep-millis", String.valueOf(millis));
		model.addAttribute("returnValue", restTemplate.getForObject(callUrl, String.class));
		log.info("Calling {} with a delay of {} ms", callUrl, millis);
	}

	@RequestMapping("/call-bad")
	public void callBad() throws InterruptedException {
		String callUrl = frontendUrl + "/bad";
		int millis = this.random.nextInt(2000);
		Thread.sleep(millis);
		//this.tracer.addTag("random-sleep-millis", String.valueOf(millis));
		restTemplate.getForObject(callUrl, String.class);
		log.info("Calling {} with a delay of {} ms", callUrl, millis);
	}

}
