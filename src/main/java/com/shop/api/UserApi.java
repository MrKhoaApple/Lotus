package com.shop.api;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;

import com.shop.entitty.AppRole;
import com.shop.entitty.Cart;
import com.shop.entitty.User;
import com.shop.common.JwtUtils;
import com.shop.dto.JwtResponse;
import com.shop.dto.LoginRequest;
import com.shop.dto.MessageResponse;
import com.shop.dto.SignupRequest;
import com.shop.repository.UserRepository;
import com.shop.service.SendMailService;
import com.shop.service.implement.UserDetailsImpl;
import com.shop.repository.CartRepository;
import com.shop.repository.AppRoleRepository;

@CrossOrigin("*")
@RestController
@RequestMapping("api/auth")
public class UserApi {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	AppRoleRepository roleRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtUtils jwtUtils;
	
	@Autowired
	SendMailService sendMailService;
	
	@GetMapping
	public ResponseEntity<List<User>> getAll() {
		return ResponseEntity.ok(userRepository.findByStatusTrue());
	}

	@GetMapping("/admin")
	public ResponseEntity<List<User>> getAllAdmin() {
		return ResponseEntity.ok(userRepository.findAll());
	}

	@GetMapping("{id}")
	public ResponseEntity<User> getOne(@PathVariable("id") Long id) {
		if (!userRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(userRepository.findById(id).get());
	}

	@GetMapping("email/{email}")
	public ResponseEntity<User> getOneByEmail(@PathVariable("email") String email) {
		if (userRepository.existsByEmail(email)) {
			return ResponseEntity.ok(userRepository.findByEmail(email).get());
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<User> post(@RequestBody User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			return ResponseEntity.notFound().build();
		}
		if (userRepository.existsById(user.getUserId())) {
			return ResponseEntity.badRequest().build();
		}

		Set<AppRole> roles = new HashSet<>();
		roles.add(new AppRole(1, null));

		user.setRoles(roles);
		
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setToken(jwtUtils.doGenerateToken(user.getEmail()));
		User u = userRepository.save(user);
		Cart c = new Cart(0L, 0.0, u.getAddress(), u.getPhone(), 0.0,u );
		cartRepository.save(c);
		return ResponseEntity.ok(u);
	}

	@PostMapping("/admin/comment")
	public ResponseEntity<User> registerAdminComment(@RequestBody User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			return ResponseEntity.notFound().build();
		}
		if (userRepository.existsById(user.getUserId())) {
			return ResponseEntity.badRequest().build();
		}
		Set<AppRole> roles = new HashSet<>();
		roles.add(new AppRole(3, null));

		user.setRoles(roles);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setToken(jwtUtils.doGenerateToken(user.getEmail()));
		User u = userRepository.save(user);
		return ResponseEntity.ok(u);

	}

	@PostMapping("/admin/order")
	public ResponseEntity<User> registerAdminOrder(@RequestBody User user) {
		if (userRepository.existsByEmail(user.getEmail())) {
			return ResponseEntity.notFound().build();
		}
		if (userRepository.existsById(user.getUserId())) {
			return ResponseEntity.badRequest().build();
		}
		Set<AppRole> roles = new HashSet<>();
		roles.add(new AppRole(4, null));

		user.setRoles(roles);

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setToken(jwtUtils.doGenerateToken(user.getEmail()));
		User u = userRepository.save(user);
		return ResponseEntity.ok(u);
	}

	@PutMapping("{id}")
	public ResponseEntity<User> put(@PathVariable("id") Long id, @RequestBody User user) {
		if (!userRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		if (!id.equals(user.getUserId())) {
			return ResponseEntity.badRequest().build();
		}
		
		User temp = userRepository.findById(id).get();
		
		if(!user.getPassword().equals(temp.getPassword())) {
			user.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		Set<AppRole> roles = new HashSet<>();
		roles.add(new AppRole(1, null));

		user.setRoles(roles);
		return ResponseEntity.ok(userRepository.save(user));
	}

	@PutMapping("admin/{id}")
	public ResponseEntity<User> putAdmin(@PathVariable("id") Long id, @RequestBody User user) {
		if (!userRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		if (!id.equals(user.getUserId())) {
			return ResponseEntity.badRequest().build();
		}
		Set<AppRole> roles = new HashSet<>();
		roles.add(new AppRole(2, null));

		user.setRoles(roles);
		return ResponseEntity.ok(userRepository.save(user));
	}

	@PutMapping("admin/comment/{id}")
	public ResponseEntity<User> putAdminComment(@PathVariable("id") Long id, @RequestBody User user) {
		if (!userRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		if (!id.equals(user.getUserId())) {
			return ResponseEntity.badRequest().build();
		}



		Set<AppRole> roles = new HashSet<>();
		roles.add(new AppRole(3, null));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(roles);
		return ResponseEntity.ok(userRepository.save(user));
	}

	@PutMapping("admin/order/{id}")
	public ResponseEntity<User> putAdminOrder(@PathVariable("id") Long id, @RequestBody User user) {
		if (!userRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		if (!id.equals(user.getUserId())) {
			return ResponseEntity.badRequest().build();
		}
		Set<AppRole> roles = new HashSet<>();
		roles.add(new AppRole(4, null));
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRoles(roles);
		return ResponseEntity.ok(userRepository.save(user));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		if (!userRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
//		cartRepository.deleteByUserId(id);
//		roleRepository.deleteByUserId(id);
		userRepository.deleteById(id);

		return ResponseEntity.ok().build();
	}

	@DeleteMapping("set-status-hide/{id}")
	public ResponseEntity<Void> setStatusHide(@PathVariable("id") Long id) {
		if(!userRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		User u = userRepository.findById(id).get();
		u.setStatus(false);
		userRepository.save(u);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("set-status-show/{id}")
	public ResponseEntity<Void> setStatusShow(@PathVariable("id") Long id) {
		if(!userRepository.existsById(id)) {
			return ResponseEntity.notFound().build();
		}
		User u = userRepository.findById(id).get();
		u.setStatus(true);
		userRepository.save(u);
		return ResponseEntity.ok().build();
	}


	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Validated @RequestBody LoginRequest loginRequest) {
		Optional<User> u = userRepository.findByEmail(loginRequest.getEmail());
		if(u.isPresent()) {
			User user = u.get();
			if(!user.getStatus()) {
				return ResponseEntity.badRequest().build();
			}
		}
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());

		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getName(),
				userDetails.getEmail(), userDetails.getPassword(), userDetails.getPhone(), userDetails.getAddress(),
				userDetails.getGender(), userDetails.getStatus(), userDetails.getImage(), userDetails.getRegisterDate(),
				roles));

	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Validated @RequestBody SignupRequest signupRequest) {

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already taken!"));
		}

		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}
		

		// create new user account
		User user = new User(signupRequest.getName(), signupRequest.getEmail(),
				passwordEncoder.encode(signupRequest.getPassword()), signupRequest.getPhone(),
				signupRequest.getAddress(), signupRequest.getGender(), signupRequest.getStatus(),
				signupRequest.getImage(), signupRequest.getRegisterDate(), jwtUtils.doGenerateToken(signupRequest.getEmail()), signupRequest.getSocialProvider());
		Set<AppRole> roles = new HashSet<>();
		roles.add(new AppRole(1, null));

		user.setRoles(roles);
		userRepository.save(user);
		Cart c = new Cart(0L, 0.0, user.getAddress(), user.getPhone(), 0.0,user);
		cartRepository.save(c);
		return ResponseEntity.ok(new MessageResponse("Register successfully"));

	}


	
	@GetMapping("/logout")
	public ResponseEntity<Void> logout() {
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("send-mail-forgot-password-token")
	public ResponseEntity<String> sendToken(@RequestBody String email) {
		
		if (!userRepository.existsByEmail(email)) {
			return ResponseEntity.notFound().build();
		}
		User user = userRepository.findByEmail(email).get();
		String token = user.getToken();
		sendMaiToken(email,token, "Reset your password");
		return ResponseEntity.ok().build();		
		
	}
	
	public void sendMaiToken(String email, String token, String title) {
		String body = "\r\n"
				+ "    <h2>Please click this link to change your password</h2>\r\n"
				+ "    <a href=\"http://localhost:8989/forgot-password/"+token+"\">Change your password</a>";
		sendMailService.queue(email, title, body);
	}
}
