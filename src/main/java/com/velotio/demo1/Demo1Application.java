package com.velotio.demo1;

import com.velotio.demo1.domains.User;
import com.velotio.demo1.domains.UserRepository;
import com.velotio.demo1.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
@RestController
public class Demo1Application {

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(Demo1Application.class, args);
	}

	@GetMapping("/admin")
	public String admin(Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		return String.format("Hello Admin %s", user.getName());
	}

	@GetMapping("/developer")
	public String developer(Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		return String.format("Hello developer", user.getName());
	}

	@GetMapping("/security")
	public String security(Principal principal) {
		User user = userRepository.findByEmail(principal.getName());
		return String.format("Hello security", user.getName());
	}

	@GetMapping("/register")
	public RedirectView register(@RequestParam Map<String,String> userInfo) {
		userService.signUp(userInfo);
		return new RedirectView("/users");
	}
}
