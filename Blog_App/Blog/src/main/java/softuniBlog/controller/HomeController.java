package softuniBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CategoryRepository;

import java.util.List;
import java.util.Set;

@Controller
public class HomeController {
    private ArticleRepository articleRepository;
    private CategoryRepository categoryRepository;

    public HomeController(ArticleRepository articleRepository,
                          CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/")
    public String index(Model model){
        List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("view", "home/index");
        model.addAttribute("categories", categories);

        return "base-layout";
    }

    @GetMapping("/category/{id}")
    public String listArticles(Model model, @PathVariable Integer id) {
        if (!this.categoryRepository.existsById(id)) {
            return "redirect:/";
        }

        Category category = this.categoryRepository.getById(id);
        Set<Article> articles = category.getArticles();

        model.addAttribute("category", category);
        model.addAttribute("articles", articles);
        model.addAttribute("view", "home/list-articles");

        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model) {
        model.addAttribute("view", "error/403");

        return "base-layout";
    }

    // TODO: Create a view for this error
    @RequestMapping("/error/404")
    public String notFound(Model model) {
        model.addAttribute("view", "error/404");

        return "base-layout";
    }
}
