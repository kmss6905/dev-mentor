package site.devmentor.application.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.domain.comment.Comment;
import site.devmentor.domain.comment.CommentRepository;
import site.devmentor.domain.post.Post;
import site.devmentor.domain.post.PostRepository;
import site.devmentor.dto.comment.CommentCreateResponse;
import site.devmentor.dto.comment.CommentCreateDto;
import site.devmentor.dto.post.request.PostCreateUpdateRequest;
import site.devmentor.dto.post.response.PostCreateResponse;
import site.devmentor.dto.post.response.PostUpdateResponse;
import site.devmentor.exception.post.PostNotFoundException;
import site.devmentor.util.DateUtils;

@Service
@Transactional
public class PostService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;

  public PostService(PostRepository postRepository, CommentRepository commentRepository) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
  }

  public PostCreateResponse create(AuthenticatedUser authUser, PostCreateUpdateRequest postCreateUpdateRequest) {
    Post post = Post.create(authUser, postCreateUpdateRequest);
    Post savedPost = savePost(post);
    return PostCreateResponse.from(savedPost);
  }

  public PostUpdateResponse update(long postId, PostCreateUpdateRequest updateRequest) {
    Post post = findPost(postId);
    post.update(updateRequest);
    return PostUpdateResponse.from(post);
  }

  public CommentCreateResponse replyComment(AuthenticatedUser authUser, CommentCreateDto commentCreateDto, long postId) {
    Post post = findPost(postId);
    post.verifyNotDeleted();
    Comment savedComment = savedComment(Comment.create(authUser, postId, commentCreateDto));
    return CommentCreateResponse.from(savedComment);
  }

  private Comment savedComment(Comment comment) {
    return commentRepository.save(comment);
  }

  private Post findPost(long postId) {
    return postRepository.findById(postId)
            .orElseThrow(() -> new PostNotFoundException(String.valueOf(postId)));
  }

  private Post savePost(Post post) {
    return postRepository.save(post);
  }

  public void delete(long id) {
    postRepository.deleteById(id);
  }
}
