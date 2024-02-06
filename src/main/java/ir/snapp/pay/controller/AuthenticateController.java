package ir.snapp.pay.controller;


import ir.snapp.pay.configuration.security.jwt.JwtTokenUtil;
import ir.snapp.pay.domain.User;
import ir.snapp.pay.dto.LoginDto;
import ir.snapp.pay.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@AllArgsConstructor
public class AuthenticateController extends BaseController {

	private final JwtTokenUtil jwtTokenUtil;
	private final UserService userService;

	@PostMapping("/authenticate")
	public ResponseEntity<String> getToken(@Valid @RequestBody LoginDto loginDTO) {
		try {
			User user = userService.getUser(loginDTO.getEmail(), loginDTO.getPassword());
			String token = jwtTokenUtil.generateAccessToken(user);
			return success(token);
		} catch (Exception e) {
			return failure(e);
		}
	}
}
