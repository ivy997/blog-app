package softuniBlog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import softuniBlog.entity.Tag;
import softuniBlog.repository.TagRepository;

@Controller
public class TagController {
    private TagRepository tagRepository;

    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @GetMapping("/tag/{name}")
    public String articlesWithTags(Model model, @PathVariable String name) {
        Tag tag = this.tagRepository.findByName(name);

        if (tag == null) {
            return "redirect:/";
        }

        model.addAttribute("tag", tag);
        model.addAttribute("view", "tag/articles");

        return "base-layout";
    }
}
