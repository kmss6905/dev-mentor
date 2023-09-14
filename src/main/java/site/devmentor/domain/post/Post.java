package site.devmentor.domain.post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import site.devmentor.domain.BaseEntity;

@Entity
@Table(name = "POST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseEntity {

  private String title;
  private String content;

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
