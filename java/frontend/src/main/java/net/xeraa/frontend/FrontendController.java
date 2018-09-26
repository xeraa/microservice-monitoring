package net.xeraa.frontend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

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
