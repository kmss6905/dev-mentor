package site.devmentor.domain.comment;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.BaseEntity;
import site.devmentor.dto.comment.CommentCreateDto;

@Table(name = "COMMENT")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

  @Column(name = "post_id")
  private long postId;

  @Column(name = "author_id")
  private long authorId;

  private String content;

  @Builder
  private Comment(long postId, long authorId, String content) {
    verifyContent(content);
    this.postId = postId;
    this.authorId = authorId;
    this.content = content;
  }
  private void verifyContent(String content) {
    if (content == null || content.trim().isEmpty()) {
      throw new IllegalArgumentException("comment's content can not be empty");
    }
  }
  public static Comment create(AuthenticatedUser authUser, long postId, CommentCreateDto commentCreateDto) {
    return Comment.builder()
            .postId(postId)
            .authorId(authUser.userPid())
            .content(commentCreateDto.comment())
            .build();
  }

  public String getContent() {
    return content;
  }
}
