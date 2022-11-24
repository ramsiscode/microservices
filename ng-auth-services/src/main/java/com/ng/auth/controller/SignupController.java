package com.ng.auth.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.jboss.aerogear.security.otp.Totp;
import org.jboss.aerogear.security.otp.api.Base32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codahale.passpol.PasswordPolicy;
import com.codahale.passpol.Status;
import com.ng.auth.model.Code;
import com.ng.auth.model.User;
import com.ng.auth.security.SignupResponse;
import com.ng.auth.service.ICodeService;
import com.ng.auth.service.IUserService;
import com.ng.auth.utils.GenerateRandomOTP;

@RestController

@CrossOrigin("*")
@RequestMapping("/auth")
public class SignupController {


    private final PasswordEncoder passwordEncoder;

    @Autowired
    private IUserService userService;

    @Autowired
    private ICodeService codeService;

    private final PasswordPolicy passwordPolicy;

    public static List<String> codes = new ArrayList<>();

    private char[] CHARACTERS;

    private static final int CODE_LENGTH = 6;

    private static final int GROUPS_NBR = 4;

    private Logger LOGGER = LoggerFactory.getLogger(SignupController.class);

    public SignupController(PasswordEncoder passwordEncoder, PasswordPolicy passwordPolicy) {
        this.passwordEncoder = passwordEncoder;
        this.passwordPolicy = passwordPolicy;

    }

//    @PostMapping("/signup")
//    public SignupResponse signup(@RequestParam("username") @NotEmpty String username,
//                                 @RequestParam("password") @NotEmpty String password,
//                                 @RequestParam("totp") boolean totp) {
//
//        Optional<User> user = userService.getUserByUserName(username);
//        Long count = user.stream().count();
//
//        if (count > 0) {
//            return new SignupResponse(SignupResponse.Status.USERNAME_TAKEN);
//        }
//
//        Status status = this.passwordPolicy.check(password);
//        if (status != Status.OK) {
//            return new SignupResponse(SignupResponse.Status.WEAK_PASSWORD);
//        }
//
//        if (totp) {
//            String secret = Base32.random();
//
//            CHARACTERS = (secret + "1234567890").toCharArray();
//            if (codes.size() != 9) {
//                for (String code : GenerateRandomOTP.generateCodes(9, CHARACTERS, CODE_LENGTH, GROUPS_NBR)) {
//                    codes.add(code);
//                }
//            } else {
//                codes = new ArrayList<>();
//                for (String code : GenerateRandomOTP.generateCodes(9, CHARACTERS, CODE_LENGTH, GROUPS_NBR)) {
//                    codes.add(code);
//                }
//            }
//
//
//            User dbUser = new User(username, this.passwordEncoder.encode(password), secret, true, false);
//            User saveUser = userService.saveUser(dbUser);
//            List<Code> secretCodes = new ArrayList<>();
//
//            for (String str : codes) {
//                secretCodes.add(new Code(str, saveUser));
//            }
//
//
//            saveUser.setCodes(secretCodes);
//            codeService.saveCode(secretCodes);
//
//            System.out.println(saveUser.getCodes());
//
//            SignupResponse signupResponse = new SignupResponse(SignupResponse.Status.OK, username, secret, codes);
//
//            System.out.println(signupResponse.toString());
//            return signupResponse;
//        }
//
//        User dbUser = new User(username, this.passwordEncoder.encode(password), null, true, false);
//
//        userService.saveUser(dbUser);
//        return new SignupResponse(SignupResponse.Status.OK);
//    }

    @PostMapping("/signup")
    public SignupResponse signup(@RequestBody User user) {

        LOGGER.info("Inside " + getClass().getName());

        Optional<User> dbUser = userService.getUserByUserName(user.getUsername());
        Long count = dbUser.stream().count();

        if (count > 0) {
            return new SignupResponse(SignupResponse.Status.USERNAME_TAKEN);
        }

        Status status = this.passwordPolicy.check(user.getPassword());
        if (status != Status.OK) {
            return new SignupResponse(SignupResponse.Status.WEAK_PASSWORD);
        }

        if (user.isTotp()) {
            String secret = Base32.random();

            CHARACTERS = (secret + "1234567890").toCharArray();
            if (codes.size() != 9) {
                for (String code : GenerateRandomOTP.generateCodes(9, CHARACTERS, CODE_LENGTH, GROUPS_NBR)) {
                    codes.add(code);
                }
            } else {
                codes = new ArrayList<>();
                for (String code : GenerateRandomOTP.generateCodes(9, CHARACTERS, CODE_LENGTH, GROUPS_NBR)) {
                    codes.add(code);
                }
            }


            User createdUser = new User(user.getUsername(), this.passwordEncoder.encode(user.getPassword()), secret, true, false);
            User saveUser = userService.saveUser(createdUser);
            List<Code> secretCodes = new ArrayList<>();

            for (String str : codes) {
                secretCodes.add(new Code(str, saveUser));
            }


            saveUser.setCodes(secretCodes);
            codeService.saveCode(secretCodes);

            System.out.println(saveUser.getCodes());

            SignupResponse signupResponse = new SignupResponse(SignupResponse.Status.OK, user.getUsername(), secret, codes);

            LOGGER.info(signupResponse.toString());
            return signupResponse;
        }

        User withOut2FaUser = new User(user.getUsername(), this.passwordEncoder.encode(user.getPassword()), null, true, false);

        userService.saveUser(withOut2FaUser);
        LOGGER.info(withOut2FaUser.toString());
        return new SignupResponse(SignupResponse.Status.OK, withOut2FaUser.getUsername(), withOut2FaUser.getSecret());

    }

    @PostMapping("/signup-confirm-secret")
    public ResponseEntity<?> signupConfirmSecret(@RequestBody Map<String, String> loggedInuser) {

        LOGGER.info("Inside " + getClass().getName());
        try {
            Optional<User> existingUser = userService.getUserByUserName(loggedInuser.get("username"));

            List<Code> newCodes = codeService.getSecurityCode(existingUser.get());
            List<String> backCode = new ArrayList<>();


            if (existingUser.isPresent()) {
                String secret = existingUser.get().getSecret();
                LOGGER.info(secret);
                Totp totp = new Totp(secret);

                for (Code c : newCodes) {
                    backCode.add(c.getSecurityCode());
                }

                if (backCode.contains(loggedInuser.get("code"))) {
                    existingUser.get().setEnabled(true);
                    return ResponseEntity.ok(true);
                }

                if (!loggedInuser.get("code").toLowerCase().matches("[a-z]")) {
                    System.out.println("in Totp" + loggedInuser.get("code"));
                    Thread.sleep(10500);
                    if (totp.verify(loggedInuser.get("code"))) {
                        System.out.println("totp");
                        existingUser.get().setEnabled(true);
                        return ResponseEntity.ok(true);
                    }
                }
            }
        } catch (InterruptedException | NumberFormatException e) {
            return new ResponseEntity<>("Invalid OTP please try with another OTP", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.ok(false);
    }
}
