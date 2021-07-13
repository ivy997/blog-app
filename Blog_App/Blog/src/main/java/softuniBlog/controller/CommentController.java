package softuniBlog.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import softuniBlog.bindingModel.CommentBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.entity.Comment;
import softuniBlog.entity.User;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CommentRepository;
import softuniBlog.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/article/{id}")
public class CommentController {
    private CommentRepository commentRepository;
    private ArticleRepository articleRepository;
    private UserRepository userRepository;

    public CommentController(CommentRepository commentRepository,
                             ArticleRepository articleRepository,
                             UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/comment")
    public RedirectView createProcess(CommentBindingModel commentBindingModel, @PathVariable Integer id) {
        if (StringUtils.isEmpty(commentBindingModel.getContent())) {
            return new RedirectView("/article/" + id);
        }

        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User author = this.userRepository.findByEmail(user.getUsername());
        Article article = this.articleRepository.getById(id);

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String date = format.format(System.currentTimeMillis());

        Comment comment = new Comment(author, null, article, commentBindingModel.getContent(), new Date(date));

        this.commentRepository.saveAndFlush(comment);
        
        return new RedirectView("/article/" + id);
    }
}
