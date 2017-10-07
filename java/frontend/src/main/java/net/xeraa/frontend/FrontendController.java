package net.xeraa.frontend;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class FrontendController {

	private static final Logger log = Logger.getLogger(FrontendController.class.getName());

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Random random;

	@Value("${APP_BACKEND:#{'http://localhost:8081'}}")
	private String backendUrl;

	@Value("${APP_FRONTEND:#{'http://localhost:8080'}}")
	private String frontendUrl;

	@RequestMapping("/good")
	public String good(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		if(!"World".equals(name)) {
			MDC.put("name", name);
		}
		log.log(Level.INFO, "Calling something good");
		return "good";
	}

	@RequestMapping("/bad")
	public String bad() {
		log.log(Level.INFO, "Calling something bad");
		throw new RuntimeException("My bad, something went wrong...");
	}

	@RequestMapping("/call")
	public String callHome() throws InterruptedException {
		String callUrl = frontendUrl + "/home";
		log.log(Level.INFO, "Calling " + callUrl);
		Thread.sleep(this.random.nextInt(2000));
		return restTemplate.getForObject(callUrl, String.class);
	}

	@RequestMapping("/call-bad")
	public String callBad() throws InterruptedException {
		String callUrl = frontendUrl + "/bad";
		log.log(Level.INFO, "Calling " + callUrl);
		Thread.sleep(this.random.nextInt(2000));
		return restTemplate.getForObject(callUrl, String.class);
	}

	@RequestMapping("/call-nested")
	public String callNested() throws InterruptedException {
		String callUrl = frontendUrl + "/call";
		log.log(Level.INFO, "Calling " + callUrl);
		Thread.sleep(this.random.nextInt(1000));
		return restTemplate.getForObject(callUrl, String.class);
	}

}
