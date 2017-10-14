package net.xeraa.frontend;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class FrontendController {

	private static final Logger log = Logger.getLogger(FrontendController.class.getName());

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Tracer tracer;

	@Autowired
	private SpanAccessor accessor;

	@Autowired
	private Random random;

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
		log.log(Level.INFO, "Calling something good");
	}

	@RequestMapping("/bad")
	public void bad() {
		log.log(Level.INFO, "Calling something bad");
		throw new RuntimeException("My bad, something went wrong...");
	}

	@RequestMapping("/null")
	public void nullpointer() {
		log.log(Level.INFO, "Calling something null");
		throw new NullPointerException("This is indeed null...");
	}

	@RequestMapping("/call")
	public void call(Model model) throws InterruptedException {
		String callUrl = backendUrl + "/slow";
		int millis = this.random.nextInt(2000);
		Thread.sleep(millis);
		this.tracer.addTag("random-sleep-millis", String.valueOf(millis));
		model.addAttribute("returnValue", restTemplate.getForObject(callUrl, String.class));
		log.log(Level.INFO, () -> String.format("Calling %s with a delay of %s ms", callUrl, millis));
	}

	@RequestMapping("/call-bad")
	public void callBad() throws InterruptedException {
		String callUrl = frontendUrl + "/bad";
		int millis = this.random.nextInt(2000);
		Thread.sleep(millis);
		this.tracer.addTag("random-sleep-millis", String.valueOf(millis));
		restTemplate.getForObject(callUrl, String.class);
		log.log(Level.INFO, () -> String.format("Calling %s with a delay of %s ms", callUrl, millis));
	}

}
