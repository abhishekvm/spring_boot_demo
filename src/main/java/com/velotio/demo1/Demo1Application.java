package com.velotio.demo1;

import com.velotio.demo1.domains.Organization;
import com.velotio.demo1.domains.OrganizationRepository;
import com.velotio.demo1.domains.User;
import com.velotio.demo1.domains.UserRepository;
import com.velotio.demo1.services.ActionService;
import com.velotio.demo1.services.TokenService;
import com.velotio.demo1.services.UserService;
import com.velotio.demo1.services.ZapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Map;

@SpringBootApplication
@RestController
public class Demo1Application {

	@Autowired
	UserRepository userRepository;

	@Autowired
	OrganizationRepository organizationRepository;

	@Autowired
	UserService userService;

	@Autowired
	TokenService tokenService;

	@Autowired
	ActionService actionService;

	@Autowired
	ZapService zapService;

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}

	@GetMapping("/admin")
	public String admin(Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		actionService.record("visited admin page", user);

		return String.format("Hello admin %s", user.getName());
	}

	@GetMapping("/developer")
	public String developer(Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		actionService.record("visited developer page", user);

		return String.format("Hello developer %s", user.getName());
	}

	@GetMapping("/security")
	public String security(Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		actionService.record("visited security page", user);

		return String.format("Hello security %s", user.getName());
	}

	@GetMapping("/zap_scan")
	public String zap_scan(HttpServletRequest request) {
		String url = request.getParameter("url");
		String email = request.getUserPrincipal().getName();
		User user = userRepository.findByEmail(email);

		if (url == null) {
			return "No URL provided to scan";
		}

		String reportPath = zapService.scan(url);
		actionService.record("started a zap scan on " + url, user);

		return "Generated report at " + reportPath + " by " + user.getName() + " from " + user.getOrganization().toString();
	}

	@GetMapping("/zap_report")
	public ResponseEntity<String> zap_report(HttpServletRequest request) {
		String subject = (String) request.getAttribute("subject");
		String reportPath = zapService.report();
		String email = subject.split("-")[0];
		Organization organization = organizationRepository.getById(Long.parseLong(subject.split("-")[1]));

		String response = "Generated report at " + reportPath + " by " + email + " from " + organization.getName();

		User user = userRepository.findByEmail(email);

		if (user == null) {
			actionService.record("generated a zap report", email);
		} else {
			actionService.record("generated a zap report", user);
		}

		return new ResponseEntity(response, HttpStatus.OK);
	}

	@GetMapping("/generate")
	public ResponseEntity<String> generate(HttpServletRequest request) {
		String requestEmail = request.getParameter("email");
		String email = request.getUserPrincipal().getName();

		if (!requestEmail.split("@")[1].equals(email.split("@")[1])) {
		    return new ResponseEntity<String>("email does not belong to this organization", HttpStatus.UNAUTHORIZED);
        }

		User adminUser = userRepository.findByEmail(email);
		User user = userRepository.findByEmail(requestEmail);

		String actionName;

		if (user == null) {
			actionName = "generated a token for " + requestEmail;
		} else {
            actionName = "generated a token for " + user.getName();;
		}

        actionService.record(actionName, adminUser);

        return new ResponseEntity<String>(tokenService.generate(requestEmail), HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/register")
	public RedirectView register(@RequestParam Map<String,String> userInfo) {
		userService.signUp(userInfo);
		return new RedirectView("/users");
	}

	@GetMapping(value = "/history", produces = MediaType.TEXT_HTML_VALUE)
	@ResponseBody
	public String history(HttpServletRequest request) {
		String email = request.getUserPrincipal().getName();
		User user = userRepository.findByEmail(email);

		return actionService.history(user.getOrganization());
	}
}
