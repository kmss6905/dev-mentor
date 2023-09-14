package site.devmentor.application.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.post.Post;
import site.devmentor.domain.post.PostRepository;
import site.devmentor.dto.post.request.PostCreateUpdateRequest;
import site.devmentor.dto.post.response.PostCreateResponse;
import site.devmentor.dto.post.response.PostUpdateResponse;
import site.devmentor.exception.post.PostNotFoundException;
import site.devmentor.util.DateUtils;

@Service
@Transactional
public class PostService {

  private final PostRepository postRepository;

  public PostService(PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  public PostCreateResponse create(AuthenticatedUser authUser, PostCreateUpdateRequest postCreateUpdateRequest) {
    Post post = Post.builder()
            .userPid(authUser.userPid())
            .title(postCreateUpdateRequest.title())
            .content(postCreateUpdateRequest.content())
            .build();
    Post postSaved = postRepository.save(post);
    return new PostCreateResponse(postSaved.getId());
  }

  public PostUpdateResponse update(long postId, PostCreateUpdateRequest updateRequest) {
    Post post = findPost(postId);
    post.update(updateRequest);
    return new PostUpdateResponse(
            post.getId(),
            DateUtils.toStandardDateFormat(post.getCreatedAt()),
            DateUtils.toStandardDateFormat(post.getUpdatedAt()));
  }

  private Post findPost(long postId) {
    return postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException("cant find post id " + postId));
  }
}
