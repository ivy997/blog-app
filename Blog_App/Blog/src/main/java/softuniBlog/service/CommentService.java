package softuniBlog.service;

import org.springframework.stereotype.Service;
import softuniBlog.entity.Article;
import softuniBlog.entity.Comment;
import softuniBlog.repository.CommentRepository;

import java.util.Set;

@Service
public class CommentService {
    private CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Set<Comment> listComments(Article article) {
        return this.commentRepository.findAllByArticle(article);
    }
}
