package kaem0n.meetoo.controllers;

import kaem0n.meetoo.entities.User;
import kaem0n.meetoo.exceptions.BadRequestException;
import kaem0n.meetoo.payloads.user.UserLoginDTO;
import kaem0n.meetoo.payloads.user.UserLoginResponseDTO;
import kaem0n.meetoo.payloads.user.UserRegistrationDTO;
import kaem0n.meetoo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserService us;

    @PostMapping("/register")
    public User register(@Validated @RequestBody UserRegistrationDTO payload, BindingResult validation) {
        if (validation.hasErrors()) throw new BadRequestException(validation.getAllErrors());
        else return us.register(payload);
    }

    @PostMapping("/login")
    public UserLoginResponseDTO login(@RequestBody UserLoginDTO payload) {
        return us.login(payload);
    }
}
