package me.ctrlmaniac.api.controllers.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.ctrlmaniac.api.entities.account.Account;
import me.ctrlmaniac.api.payloads.auth.AuthRequest;
import me.ctrlmaniac.api.payloads.auth.AuthResponse;
import me.ctrlmaniac.api.services.account.AccountService;
import me.ctrlmaniac.api.utils.JwtTokenUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authManager;

	@Autowired
	private AccountService accountService;

	@Autowired
	private JwtTokenUtil jwtUtil;

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthRequest request) {
		try {
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(),
					request.getPassword());

			Authentication auth = authManager.authenticate(token);

			User user = (User) auth.getPrincipal();
			Account account = accountService.findByEmail(user.getUsername());

			String accessToken = jwtUtil.generateAccessToken(account);
			AuthResponse response = new AuthResponse(account.getEmail(), accessToken);

			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (BadCredentialsException ex) {
			return new ResponseEntity<>("Account non autorizzato", HttpStatus.UNAUTHORIZED);
		}
	}
}
