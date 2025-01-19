package com.example.IBOR.User;

import com.example.IBOR.Car.Car;
import com.example.IBOR.Car.CarRepository;
import com.example.IBOR.CarPart.CarPart;
import com.example.IBOR.CarPart.CarPartRepository;
import com.example.IBOR.ImageEncoder;
import jakarta.persistence.Access;
import jakarta.validation.Valid;
import org.springframework.security.authorization.method.HandleAuthorizationDenied;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.config.annotation.web.*;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {
    private UserService userService;
    private UserRepository userRepository;
    private CarRepository carRepository;
    private CarPartRepository carPartRepository;

    public UserController(UserService userService, UserRepository userRepository, CarRepository carRepository, CarPartRepository carPartRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.carRepository = carRepository;
        this.carPartRepository = carPartRepository;
    }

    @GetMapping({"/", "/home"})
    public String getHome(Model model) {
        List<Car> cars = (List<Car>) carRepository.findAll();
        List<CarPart> carParts = (List<CarPart>) carPartRepository.findAll();
        model.addAttribute("cars", cars.size() > 3 ? cars.subList(0, 2) : cars);
        model.addAttribute("carParts", carParts.size() > 3 ? carParts.subList(0, 2) : carParts);
        model.addAttribute("encoder", new ImageEncoder());
        return "home";
    }

    @GetMapping("/sign-in")
    public String getSignIn() {
        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String getSignUp(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "sign-up";
    }

    @PostMapping("/submit")
    public String submitUser(@Valid User user, BindingResult bindingResult, Model model) {
        System.out.println(user.getUsername());
        System.out.println(user.getEmail());
        System.out.println(user.getPassword());
        return userService.submitUser(user, bindingResult, model);
    }

    @GetMapping("/profile")
    public String getShowProfile(Principal principal, Model model) {
        User user = userRepository.getUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/about-us")
    public String getAboutUs() {
        return "aboutUs";
    }

    @GetMapping("/contacts")
    public String getContacts() {
        return "contacts";
    }

    @GetMapping("/access-denied")
    public String getAccessDenied() {
        return "error/accessDenied";
    }
}