package softuniBlog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import softuniBlog.entity.Article;
import softuniBlog.entity.Comment;

import java.util.Set;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    Set<Comment> findAllByArticle(Article article);

    Set<Comment> findByParentId(Integer parentId);
}
