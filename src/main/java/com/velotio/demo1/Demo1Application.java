package com.velotio.demo1;

import com.velotio.demo1.domains.User;
import com.velotio.demo1.domains.UserRepository;
import com.velotio.demo1.services.TokenService;
import com.velotio.demo1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	UserService userService;

	@Autowired
	TokenService tokenService;

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}

	@GetMapping("/admin")
	public String admin(Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		return String.format("Hello admin %s", user.getName());
	}

	@GetMapping("/developer")
	public String developer(Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		return String.format("Hello developer %s", user.getName());
	}

	@GetMapping("/security")
	public String security(Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		return String.format("Hello security %s", user.getName());
	}

	@GetMapping("/ingest")
	public String ingest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (!(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))) {
			return "Please pass the token";
		}

		String token = bearerToken.substring(7, bearerToken.length());

		if (!tokenService.validate(token)) {
			return "Invalid token";
		}

		String subject = tokenService.getSubject(token);

		return "ingesting by " + subject.split("-")[0] + " from " + subject.split("-")[1];
	}

	@GetMapping("/generate")
	public String generate(@RequestParam String email, @RequestParam String organization) {
		return tokenService.generate(email, organization);
	}

	@GetMapping("/register")
	public RedirectView register(@RequestParam Map<String,String> userInfo) {
		userService.signUp(userInfo);
		return new RedirectView("/users");
	}
}
