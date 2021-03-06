package softuniBlog.controller.admin;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.bindingModel.UserEditBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Role;
import softuniBlog.entity.User;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.RoleRepository;
import softuniBlog.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/users")
public class AdminUserController {
    private UserRepository userRepository;
    private ArticleRepository articleRepository;
    private RoleRepository roleRepository;

    public AdminUserController(UserRepository userRepository,
                               ArticleRepository articleRepository,
                               RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/")
    public String listUsers(Model model) {
        List<User> users = this.userRepository.findAll();

        model.addAttribute("users", users);
        model.addAttribute("view", "admin/user/list");

        return "base-layout";
    }

    @GetMapping("/edit/{id}")
    public String edit(Model model, @PathVariable Integer id) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/admin/users/";
        }

        User user = this.userRepository.getById(id);
        List<Role> roles = this.roleRepository.findAll();

        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        model.addAttribute("view", "admin/user/edit");

        return "base-layout";
    }

    @PostMapping("/edit/{id}")
    public String editProcess(UserEditBindingModel userEditBindingModel, @PathVariable Integer id) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/admin/users/";
        }

        User user = this.userRepository.getById(id);

        if (!StringUtils.isEmpty(userEditBindingModel.getPassword())
            && !StringUtils.isEmpty(userEditBindingModel.getConfirmPassword())) {
            if (userEditBindingModel.getPassword().equals(userEditBindingModel.getConfirmPassword())) {
                BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

                user.setPassword(bCryptPasswordEncoder.encode(userEditBindingModel.getPassword()));
            }
        }

        user.setEmail(userEditBindingModel.getEmail());
        user.setFullName(userEditBindingModel.getFullName());

        Set<Role> roles = new HashSet<>();

        for (Integer roleId : userEditBindingModel.getRoles()) {
            roles.add(this.roleRepository.getById(roleId));
        }

        user.setRoles(roles);

        this.userRepository.saveAndFlush(user);

        return "redirect:/admin/users/";
    }

    @GetMapping("/delete/{id}")
    public String delete(Model model, @PathVariable Integer id) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/admin/users/";
        }

        User user = this.userRepository.getById(id);

        model.addAttribute("user", user);
        model.addAttribute("view", "admin/user/delete");

        return "base-layout";
    }

    @PostMapping("/delete/{id}")
    public String deleteProcess(@PathVariable Integer id) {
        if (!this.userRepository.existsById(id)) {
            return "redirect:/admin/users/";
        }

        User user = this.userRepository.getById(id);

        for (Article article : user.getArticles()) {
            this.articleRepository.delete(article);
        }

        this.userRepository.delete(user);

        return "redirect:/admin/users/";
    }
}
