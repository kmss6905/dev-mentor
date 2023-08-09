package site.devmentor.domain;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  protected long id;

  @CreatedDate
  @Column(nullable = false, name = "created_dt")
  private LocalDateTime createdDt;

  @LastModifiedDate
  @Column(name = "updated_dt")
  private LocalDateTime updatedDt;
}
