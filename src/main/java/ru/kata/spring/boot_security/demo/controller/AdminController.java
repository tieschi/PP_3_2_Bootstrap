package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String userList(Model model, Principal principal) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("user", userService.loadUserByUsername(principal.getName()));
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "allUsers";
    }

    @GetMapping("/new")
    public String newUserPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "new";
    }

    @RequestMapping("/saveUser")
    public String saveUser(@ModelAttribute("user")User user, @RequestParam(value = "rolessel") String[] selectRoles,
                           @RequestParam(value = "id") int id) {
        Set<Role> roles = new HashSet<>();
        for (String role: selectRoles) {
            roles.add(roleService.findRoleByName(role));
        }
        System.out.println(user);
        user.setRoles(roles);
        user.setId(id);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @RequestMapping("/delete")
    public String deleteUser(@RequestParam("id") int id) {

        userService.deleteUser(id);

        return "redirect:/admin";
    }
}
