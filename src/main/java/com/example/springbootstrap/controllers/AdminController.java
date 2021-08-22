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

//    @GetMapping("/{id}")
//    public String getUser(@PathVariable("id") int id, Model model) {
//        model.addAttribute("user", userService.findById(id));
//        return "user/show";
//    }

    @DeleteMapping()
    public String deleteUser(int id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

//    @GetMapping("/{id}")
//    public String update(Model model, @PathVariable("id") int id) {
//        model.addAttribute("editUser", userService.findById(id));
//        return "admin/all";
//    }

    @PostMapping
    public String createUser(
            @ModelAttribute(value = "roleAdmin") String roleAdmin,
            @ModelAttribute("user") User user) {

        Set<Role> setRole = new HashSet<>();
        if (roleAdmin.contains("on")) {
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
        return "admin/create";
    }
}
