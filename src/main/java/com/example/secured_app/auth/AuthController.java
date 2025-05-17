package com.example.secured_app.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "register";
    }

    @GetMapping("/change")
    public String getPasswordChangePage() {
        return "change-password";
    }

    @PostMapping("/change")
    public String changePassword(@ModelAttribute Account account, RedirectAttributes redirectAttributes, Model model) {
        try {
            authService.changePassword(account);
            redirectAttributes.addAttribute("message", "Na podany mail został wysłany link do zmiany hasła");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/change/{id}")
    public String change(@PathVariable String id, RedirectAttributes redirectAttributes) {
        try {
            authService.changePassword(id);
            redirectAttributes.addAttribute("message", "hasło zmienione poprawnie");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addAttribute("message", e.getMessage());
        } catch (Exception e) {
            redirectAttributes.addAttribute("message", "Coś poszło nie tak");
        }
        return "redirect:/";
    }

    @PostMapping("/register")
    public String postAccount(@ModelAttribute Account account, RedirectAttributes redirectAttributes, Model model) {
        try {
            authService.register(account);
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e.getMessage());
            return "register";
        }
        redirectAttributes.addAttribute("message", "Udało Ci się zarejestrować nowe konto," +
                " możesz się teraz zalogować");
        return "redirect:/";
    }


    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping
    public String getHomePage(@RequestParam(required = false) String message,
                              Model model) {
        model.addAttribute("message", message);
        provideEmail(model);
        return "index";
    }

    private void provideEmail(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            model.addAttribute("email", user.getUsername());
        }
    }
}
