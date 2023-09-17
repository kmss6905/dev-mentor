package site.devmentor.domain.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.BaseEntity;
import site.devmentor.dto.post.request.PostCreateUpdateRequest;
import site.devmentor.exception.UnauthorizedAccessException;
import site.devmentor.exception.post.PostNotFoundException;

import java.util.Objects;

@Entity
@Table(name = "POST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE POST SET is_deleted = true WHERE id=?")
public class Post extends BaseEntity {

  @Column(nullable = false)
  private String content;

  @Column(nullable = false)
  private String title;

  @Column(name = "is_deleted")
  private boolean isDeleted;

  @Column(name = "author_id")
  private long authorId;


  @Builder
  private Post(String title, String content, long userPid) {
    verifyTitle(title);
    verifyContent(content);
    this.isDeleted = false;
    this.authorId = userPid;
    this.title = title;
    this.content = content;
  }

  public static Post create(AuthenticatedUser authUser, PostCreateUpdateRequest postCreateUpdateRequest) {
    return Post.builder()
            .userPid(authUser.userPid())
            .title(postCreateUpdateRequest.title())
            .content(postCreateUpdateRequest.content())
            .build();
  }

  public void update(PostCreateUpdateRequest updateRequest) {
    verifyNotDeleted();
    verifyTitle(updateRequest.title());
    verifyContent(updateRequest.content());
    if (isTitleNotEqual(updateRequest.title())) {
        this.title = updateRequest.title();
    }
    if (isContentNotEqual(updateRequest.content())) {
      this.content = updateRequest.content();
    }
  }

  public void verifyNotDeleted() {
    if (this.isDeleted) {
      throw new PostNotFoundException(String.valueOf(this.id));
    }
  }

  public void checkOwner(long userPid) {
    if (this.authorId != userPid) {
      throw new UnauthorizedAccessException();
    }
  }

  private boolean isTitleNotEqual(String title) {
    return !Objects.equals(this.title, title);
  }

  private boolean isContentNotEqual(String content) {
    return !Objects.equals(this.content, content);
  }

  private void verifyTitle(String title) {
    if (title == null || title.isEmpty()) {
      throw new IllegalArgumentException("title can not be empty");
    }
  }

  private void verifyContent(String content) {
    if (content == null || content.isEmpty()) {
      throw new IllegalArgumentException("content can not be empty");
    }
  }
}
