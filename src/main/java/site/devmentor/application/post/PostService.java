package site.devmentor.application.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.post.Post;
import site.devmentor.domain.post.PostRepository;
import site.devmentor.dto.post.PostCreateRequest;
import site.devmentor.dto.post.PostCreateResponse;

@Service
@Transactional
public class PostService {

  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public PostCreateResponse create(AuthenticatedUser authUser, PostCreateRequest postCreateRequest) {
    Post post = Post.builder()
            .userPid(authUser.userPid())
            .content(postCreateRequest.content())
            .build();
    Post postSaved = postRepository.save(post);
    return new PostCreateResponse(postSaved.getId());
  }
}
