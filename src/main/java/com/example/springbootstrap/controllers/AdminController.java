package com.example.springbootstrap.controllers;

import com.example.springbootstrap.models.Role;
import com.example.springbootstrap.models.User;
import com.example.springbootstrap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllUsers(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        model.addAttribute("listUsers", userService.findAll());
        model.addAttribute("roles", userService.findAllRoles());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("rolesOfUser", user.getRoles());
        return "admin/all";
    }

    @DeleteMapping()
    public String deleteUser(int id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping()
    public String createUser(
            @ModelAttribute(value = "select_role") String role,
            @ModelAttribute("user") User user) {

        Set<Role> setRole = new HashSet<>();
        if (role.contains("1")) {
            setRole.add(userService.findAllRoles().get(1));
            setRole.add(userService.findAllRoles().get(0));
        } else {
            setRole.add(userService.findAllRoles().get(0));
        }
        user.setRoles(setRole);
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/create")
    public String newUser(Model model, Principal principal) {
        User user = userService.getUserByUsername(principal.getName());
        String roleAdmin = null;
        model.addAttribute("user", new User());
        model.addAttribute("roleAdmin", roleAdmin);
        model.addAttribute("email", user.getEmail());
        model.addAttribute("rolesOfUser", user.getRoles());
        model.addAttribute("roles", userService.findAllRoles());

        return "admin/create";
    }
}
