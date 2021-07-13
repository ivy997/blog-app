package softuniBlog.bindingModel;

import com.sun.istack.NotNull;

public class CommentBindingModel {
    @NotNull
    private String content;
    private Integer articleId;
    private Integer authorId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }
}
