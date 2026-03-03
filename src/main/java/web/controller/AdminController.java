package web.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.repository.RoleRepository;
import web.service.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
@Transactional
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/user-list";
    }

    @GetMapping("/users/new")
    public String showNewUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/user-form-create";
    }

    @PostMapping("/users")
    public String addUser(@ModelAttribute("user") User user,
                          BindingResult result,
                          @RequestParam(value = "roleIds", required = false) Set<Long> roleIds,
                          Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin/user-form-create";
        }

        // Устанавливаем выбранные роли
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> selectedRoles = new HashSet<>(roleRepository.findAllById(roleIds));
            user.setRoles(selectedRoles);
        } else {
            // По умолчанию — роль USER
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new EntityNotFoundException("Default role USER not found"));
            user.setRoles(Set.of(userRole));
        }

        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String showEditUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            return "redirect:/admin/users?error=user_not_found";
        }
        model.addAttribute("user", user);
        model.addAttribute("allRoles", roleRepository.findAll());
        return "admin/user-form-edit";
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable Long id,
                             @ModelAttribute("user") User user,
                             BindingResult result,
                             @RequestParam(value = "roleIds", required = false) Set<Long> roleIds,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("allRoles", roleRepository.findAll());
            return "admin/user-form-edit";
        }

        user.setId(id);

        // Устанавливаем выбранные роли
        if (roleIds != null && !roleIds.isEmpty()) {
            Set<Role> selectedRoles = new HashSet<>(roleRepository.findAllById(roleIds));
            user.setRoles(selectedRoles);
        }

        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
        } catch (Exception e) {
            // Логирование ошибки
        }
        return "redirect:/admin/users";
    }
}