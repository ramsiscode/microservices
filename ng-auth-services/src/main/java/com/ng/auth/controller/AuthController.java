package com.ng.auth.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ng.auth.dto.JwtResponse;
import com.ng.auth.dto.SignInResponseDto;
import com.ng.auth.model.Code;
import com.ng.auth.model.User;
import com.ng.auth.security.AppUserAuthentication;
import com.ng.auth.security.AppUserDetail;
import com.ng.auth.security.AuthenticationFlow;
import com.ng.auth.security.CustomTotp;
import com.ng.auth.service.ICodeService;
import com.ng.auth.service.IUserService;
import com.ng.auth.utils.CaptchaUtil;
import com.ng.auth.utils.JWTUtil;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
public class AuthController {

	private final static String USER_AUTHENTICATION_OBJECT = "USER_AUTHENTICATION_OBJECT";

	private final PasswordEncoder passwordEncoder;

	private final String userNotFoundEncodedPassword;

	private static String uiCaptcha;

	public static User mainUser;

	private AppUserAuthentication appUserAuthentication;

	@Autowired
	private IUserService userService;

	@Autowired
	private ICodeService codeService;
	@Autowired
	private JWTUtil jwtUtil;

	public AuthController(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;

		this.userNotFoundEncodedPassword = this.passwordEncoder.encode("userNotFoundPassword");
	}

	@GetMapping("/authenticate")
	public AuthenticationFlow authenticate(HttpServletRequest request) {

		System.out.println("In authenticate");
		// _LOGGER_.info("In authenticate of " + getClass().getName());

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth instanceof AppUserAuthentication) {
			return AuthenticationFlow.AUTHENTICATED;
		}

		HttpSession httpSession = request.getSession(false);
		if (httpSession != null) {
			httpSession.invalidate();
		}

		// _LOGGER_.info("Authenticate Method of " + getClass().getName() + "
		// Completed");
		return AuthenticationFlow.NOT_AUTHENTICATED;

	}

	@GetMapping("/getcaptcha")
	public ResponseEntity<?> getCaptcha(HttpSession httpSession) {

		// _LOGGER_.info("In getCaptcha of " + getClass().getName());

		User user = new User();
		String captcha = CaptchaUtil.getCaptcha(user);
		uiCaptcha = captcha;

		// _LOGGER_.info("Captcha : " + captcha);

		httpSession.setAttribute("captcha", captcha);
		System.out.println(httpSession.getAttribute("captcha"));
		SignInResponseDto signInResponse = new SignInResponseDto("Capcha generated!!", captcha, HttpStatus.OK.value());
		// _LOGGER_.info("getCaptcha Method of " + getClass().getName() + " Completed");
		return ResponseEntity.ok(signInResponse);
	}

	@PostMapping("/signin")
	public ResponseEntity<?> login(@RequestBody Map<String, String> user, HttpSession httpSession) {

		// _LOGGER_.info("In login of " + getClass().getName());
		try {

			System.out.println("In signin   ");

			String captcha = (String) httpSession.getAttribute("captcha");

			System.out.println(user);
			User existingUser = userService.getUserByUserName(user.get("username")).get();

			mainUser = existingUser;

			System.out.println("SignIn Completed 1");
			if (existingUser != null) {
				boolean pwMatches = this.passwordEncoder.matches(user.get("password"), existingUser.getPasswordHash());
				System.out.println(pwMatches);

				if (pwMatches && existingUser.getEnabled().booleanValue()) {
					System.out.println("SignIn Completed 2");
					AppUserDetail detail = new AppUserDetail(existingUser);

					AppUserAuthentication userAuthentication = new AppUserAuthentication(detail);

					appUserAuthentication = userAuthentication;
					// exception handling require

					System.out.println("Captcha" + captcha);

					if (user.get("captcha").equals(uiCaptcha)) {
						System.out.println("SignIn Completed 3");
						if (isNotBlank(existingUser.getSecret())) {
							System.out.println("SignIn Completed 4");

							System.out.println(existingUser.toString());

							httpSession.setAttribute("loginInUser", existingUser);
							httpSession.setAttribute(USER_AUTHENTICATION_OBJECT, userAuthentication);

//                        if (isUserInAdditionalSecurityMode(detail.getAppUserId())) {
//                            return ResponseEntity.ok().body(AuthenticationFlow.TOTP_ADDITIONAL_SECURITY);
//                        }
							System.out.println("SignIn Completed 5");

							SignInResponseDto dto = new SignInResponseDto();
							dto.setData(mainUser.getUsername());
							dto.setMessage("SignIn SuccessFully");
							dto.setStatusCode(HttpStatus.OK.value());
							final UserDetails userDetails = userService.loadUserByUsername(user.get("username"));
							final String token = jwtUtil.generateToken(userDetails);
							System.out.println(token);
							//return new JwtResponse(token);
							return ResponseEntity.ok(new JwtResponse(token));
						}

						System.out.println("SignIn Completed 6");
						SecurityContextHolder.getContext().setAuthentication(userAuthentication);
						System.out.println("SignIn Completed 7");
						return new ResponseEntity<>(new SignInResponseDto("User Authenticated", HttpStatus.OK.value()),
								HttpStatus.OK);
					} else {
						System.out.println("SignIn Completed 8");
						return new ResponseEntity<>(
								new SignInResponseDto("Invalid Captcha !!", HttpStatus.INTERNAL_SERVER_ERROR.value()),
								HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
			} else {
				System.out.println("SignIn Completed 9");
				this.passwordEncoder.matches(user.get("password"), this.userNotFoundEncodedPassword);
			}
			System.out.println("SignIn Completed 10");
			return new ResponseEntity<>(new SignInResponseDto("Invalid password !!", HttpStatus.UNAUTHORIZED.value()),
					HttpStatus.UNAUTHORIZED);

		} catch (NoSuchElementException e) {
			System.out.println("SignIn Completed 11");
			return new ResponseEntity<>(new SignInResponseDto("Username not found !!", HttpStatus.UNAUTHORIZED.value()),
					HttpStatus.UNAUTHORIZED);

		}
	}

//    @PostMapping("/signin")
//    public ResponseEntity<AuthenticationFlow> login(@RequestParam String username,
//                                                    @RequestParam String password, HttpSession httpSession) {
//
//        System.out.println("In signin");
//
//        User user = userService.getUserByUserName(username).get();
//
//        if (user != null) {
//            boolean pwMatches = this.passwordEncoder.matches(password, user.getPasswordHash());
//            System.out.println(pwMatches);
//
//            if (pwMatches && user.getEnabled().booleanValue()) {
//
//                AppUserDetail detail = new AppUserDetail(user);
//
//                AppUserAuthentication userAuthentication = new AppUserAuthentication(detail);
//
//                if (isNotBlank(user.getSecret())) {
//
//                    httpSession.setAttribute("loginInUser", user);
//                    httpSession.setAttribute(USER_AUTHENTICATION_OBJECT, userAuthentication);
//
//                    if (isUserInAdditionalSecurityMode(detail.getAppUserId())) {
//                        return ResponseEntity.ok().body(AuthenticationFlow.TOTP_ADDITIONAL_SECURITY);
//                    }
//
//                    return ResponseEntity.ok().body(AuthenticationFlow.TOTP);
//                }
//
//                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
//                return ResponseEntity.ok().body(AuthenticationFlow.AUTHENTICATED);
//            }
//        } else {
//            this.passwordEncoder.matches(password, this.userNotFoundEncodedPassword);
//        }
//
//        return ResponseEntity.ok().body(AuthenticationFlow.NOT_AUTHENTICATED);
//    }

//    @PostMapping("/verify-totp")
//    public ResponseEntity<AuthenticationFlow> totp(@RequestParam String code,
//                                                   HttpSession httpSession) {
//
//        User user = (User) httpSession.getAttribute("loginInUser");
//
//
//        List<Code> codes =codeService.getSecurityCode(user);
//        List<String> backCode = new ArrayList<>();
//
//
//        System.out.println("In verify-totp");
//
//        AppUserAuthentication userAuthentication = (AppUserAuthentication) httpSession
//                .getAttribute(USER_AUTHENTICATION_OBJECT);
//
//        if (userAuthentication == null) {
//            return ResponseEntity.ok().body(AuthenticationFlow.NOT_AUTHENTICATED);
//        }
//
//        AppUserDetail detail = (AppUserDetail) userAuthentication.getPrincipal();
//        if (isUserInAdditionalSecurityMode(detail.getAppUserId())) {
//            return ResponseEntity.ok().body(AuthenticationFlow.TOTP_ADDITIONAL_SECURITY);
//        }
//
//        String secret = ((AppUserDetail) userAuthentication.getPrincipal()).getSecret();
//        if (isNotBlank(secret) && isNotBlank(code)) {
//            CustomTotp totp = new CustomTotp(secret);
//
//            for(Code c : codes){
//                backCode.add(c.getSecurityCode());
//            }
//
//
//                if (backCode.contains(code)) {
//                    return ResponseEntity.ok().body(AuthenticationFlow.AUTHENTICATED);
//                }
//
//
//            if (totp.verify(code, 2, 2).isValid()) {
//                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
//                return ResponseEntity.ok().body(AuthenticationFlow.AUTHENTICATED);
//            }
//
//            setAdditionalSecurityFlag(detail.getAppUserId());
//            return ResponseEntity.ok().body(AuthenticationFlow.TOTP_ADDITIONAL_SECURITY);
//        }
//
//        return ResponseEntity.ok().body(AuthenticationFlow.NOT_AUTHENTICATED);
//    }

	@SuppressWarnings("unused")
	@PostMapping("/verify-totp")
	public ResponseEntity<?> totp(@RequestBody Map<String, String> secretCode, HttpSession httpSession) {

		// _LOGGER_.info("In totp of " + getClass().getName());

		Map<String, String> response = new HashMap<String, String>();

		try {

			User user = (User) httpSession.getAttribute("loginInUser");

			List<Code> codes = codeService.getSecurityCode(mainUser);
			List<String> backCode = new ArrayList<>();

			// System.out.println("In verify-totp" + user.getId() + "**" +
			// secretCode.get("code"));

//			AppUserAuthentication userAuthentication = (AppUserAuthentication) httpSession
//					.getAttribute(USER_AUTHENTICATION_OBJECT);
//
//			System.out.println(userAuthentication.getPrincipal() + "######" + USER_AUTHENTICATION_OBJECT);

			if (appUserAuthentication == null) {
				response.put("Message", String.valueOf(AuthenticationFlow.NOT_AUTHENTICATED));
				return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}

			AppUserDetail detail = (AppUserDetail) appUserAuthentication.getPrincipal();
//            if (isUserInAdditionalSecurityMode(detail.getAppUserId())) {
//                return ResponseEntity.ok().body(AuthenticationFlow.TOTP_ADDITIONAL_SECURITY);
//            }

			String secret = ((AppUserDetail) appUserAuthentication.getPrincipal()).getSecret();
			if (isNotBlank(secret) && isNotBlank(secretCode.get("code"))) {

				CustomTotp totp = new CustomTotp(secret);

				for (Code c : codes) {
					backCode.add(c.getSecurityCode());
				}

				if (backCode.contains(secretCode.get("code"))) {
					response.put("Message", String.valueOf(AuthenticationFlow.AUTHENTICATED));
					response.put("userId", String.valueOf(mainUser.getId()));
					System.out.println(response);
					return new ResponseEntity<>(response, HttpStatus.OK);
				}

				if (!secretCode.get("code").toLowerCase().matches("[a-z]")) {

					if (totp.verify(secretCode.get("code"), 2, 2).isValid()) {

						SecurityContextHolder.getContext().setAuthentication(appUserAuthentication);
						response.put("Message", String.valueOf(AuthenticationFlow.AUTHENTICATED));
						response.put("userId", String.valueOf(mainUser.getId()));
						return new ResponseEntity<>(response, HttpStatus.OK);
					}
				}

//                setAdditionalSecurityFlag(detail.getAppUserId());
//                return ResponseEntity.ok().body(AuthenticationFlow.TOTP_ADDITIONAL_SECURITY);
			}

		} catch (NumberFormatException e) {
			response.put("Message", "Invalid OTP!!!");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			response.put("Message", "Something went wrong!!!");
			return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("Message", String.valueOf(AuthenticationFlow.NOT_AUTHENTICATED));
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@PostMapping("/codes")
	public ResponseEntity<?> getBackUpCodesOfLoginUser(@RequestBody Map<String, String> username) {

		// _LOGGER_.info("In getBackUpCodesOfLoginUser of " + getClass().getName());

		List<Code> securityCode = new ArrayList<>();
		Map<String, Object> response = new HashMap<>();
		Map<Object, String> securityCodeIntoMap = new HashMap<>();
		try {
			securityCode = codeService.getScurityCodeOfUser(username.get("username"));
		} catch (Exception e) {
			response.put("Message", e.getMessage());
			response.put("Status Code", String.valueOf(HttpStatus.NOT_FOUND.value()));
			return ResponseEntity.ok(response);
		}
		int i = 0;
		String secret = "";
		for (Code c : securityCode) {

			securityCodeIntoMap.put(i, c.getSecurityCode());
			secret = c.getUser().getSecret();
			i++;
		}

		response.put("Message", "Codes Fetch");
		response.put("codes", securityCodeIntoMap);
		response.put("Secret", secret);
		return ResponseEntity.ok(response);

	}

	@PostMapping("/verify-totp-additional-security")
	public ResponseEntity<AuthenticationFlow> verifyTotpAdditionalSecurity(@RequestParam String code1,
			@RequestParam String code2, @RequestParam String code3, HttpSession httpSession) {

		System.out.println("In verify-totp-additional-security");

		AppUserAuthentication userAuthentication = (AppUserAuthentication) httpSession
				.getAttribute(USER_AUTHENTICATION_OBJECT);
		if (userAuthentication == null) {
			return ResponseEntity.ok().body(AuthenticationFlow.NOT_AUTHENTICATED);
		}

		if (code1.equals(code2) || code1.equals(code3) || code2.equals(code3)) {
			return ResponseEntity.ok().body(AuthenticationFlow.NOT_AUTHENTICATED);
		}
		List<String> backCode = SignupController.codes;

		String secret = ((AppUserDetail) userAuthentication.getPrincipal()).getSecret();
		if (isNotBlank(secret) && isNotBlank(code1) && isNotBlank(code2) && isNotBlank(code3)) {
			CustomTotp totp = new CustomTotp(secret);

			List<Integer> securityCode = Arrays.asList(Integer.parseInt(code1), Integer.parseInt(code2),
					Integer.parseInt(code3));

			if (backCode.containsAll(securityCode)) {
				System.out.println("AUTHENTICATED");
				return ResponseEntity.ok().body(AuthenticationFlow.AUTHENTICATED);
			} else {
				System.out.println("NOT AUTHENTICATED");
			}

			// check 25 hours into the past and future.
			long noOf30SecondsIntervals = TimeUnit.HOURS.toSeconds(25) / 30;
			CustomTotp.Result result = totp.verify(List.of(code1, code2, code3), noOf30SecondsIntervals,
					noOf30SecondsIntervals);
			if (result.isValid()) {
				if (result.getShift() > 2 || result.getShift() < -2) {
					httpSession.setAttribute("totp-shift", result.getShift());
				}

				AppUserDetail detail = (AppUserDetail) userAuthentication.getPrincipal();
				clearAdditionalSecurityFlag(detail.getAppUserId());
				httpSession.removeAttribute(USER_AUTHENTICATION_OBJECT);

				SecurityContextHolder.getContext().setAuthentication(userAuthentication);
				return ResponseEntity.ok().body(AuthenticationFlow.AUTHENTICATED);
			}
		}

		return ResponseEntity.ok().body(AuthenticationFlow.NOT_AUTHENTICATED);
	}

	@GetMapping("/totp-shift")
	public String getTotpShift(HttpSession httpSession) {
		Long shift = (Long) httpSession.getAttribute("totp-shift");
		if (shift == null) {
			return null;
		}
		httpSession.removeAttribute("totp-shift");

		StringBuilder out = new StringBuilder();
		long total30Seconds = (int) Math.abs(shift);
		long hours = total30Seconds / 120;
		total30Seconds = total30Seconds % 120;
		long minutes = total30Seconds / 2;
		boolean seconds = total30Seconds % 2 != 0;

		if (hours == 1) {
			out.append("1 hour ");
		} else if (hours > 1) {
			out.append(hours).append(" hours ");
		}

		if (minutes == 1) {
			out.append("1 minute ");
		} else if (minutes > 1) {
			out.append(minutes).append(" minutes ");
		}

		if (seconds) {
			out.append("30 seconds ");
		}

		return out.append(shift < 0 ? "behind" : "ahead").toString();
	}

	private static boolean isNotBlank(String str) {
		return str != null && !str.isBlank();
	}

	private Boolean isUserInAdditionalSecurityMode(long appUserId) {
		return userService.getAdditionalSecurity(appUserId);
	}

	private void setAdditionalSecurityFlag(Long appUserId) {
		userService.setAdditionalSecurity(appUserId);
	}

	private void clearAdditionalSecurityFlag(Long appUserId) {
		userService.updateAdditionalSecurity(appUserId);
	}

}
