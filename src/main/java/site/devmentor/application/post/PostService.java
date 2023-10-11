package site.devmentor.application.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devmentor.auth.AppUser;
import site.devmentor.domain.comment.Comment;
import site.devmentor.domain.comment.CommentRepository;
import site.devmentor.domain.post.Post;
import site.devmentor.domain.post.PostRepository;
import site.devmentor.dto.comment.CommentCreateResponse;
import site.devmentor.dto.comment.CommentDto;
import site.devmentor.dto.comment.CommentUpdateResponse;
import site.devmentor.dto.post.request.PostCreateUpdateRequest;
import site.devmentor.dto.post.response.PostCreateResponse;
import site.devmentor.dto.post.response.PostUpdateResponse;
import site.devmentor.exception.comment.CommentNotFoundException;
import site.devmentor.exception.post.PostNotFoundException;

@Service
@Transactional
public class PostService {

  private final PostRepository postRepository;
  private final CommentRepository commentRepository;

  public PostService(PostRepository postRepository, CommentRepository commentRepository) {
    this.postRepository = postRepository;
    this.commentRepository = commentRepository;
  }

  public PostCreateResponse create(AppUser authUser, PostCreateUpdateRequest postCreateUpdateRequest) {
    Post post = Post.create(authUser, postCreateUpdateRequest);
    Post savedPost = savePost(post);
    return PostCreateResponse.from(savedPost);
  }

  public PostUpdateResponse update(long postId, PostCreateUpdateRequest updateRequest) {
    Post post = findPost(postId);
    post.update(updateRequest);
    return PostUpdateResponse.from(post);
  }

  public CommentCreateResponse replyComment(AppUser authUser, CommentDto commentDto, long postId) {
    Post post = findPost(postId);
    post.verifyNotDeleted();
    Comment savedComment = savedComment(Comment.create(authUser, postId, commentDto));
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

  public CommentUpdateResponse editComment(AppUser authUser, CommentDto commentDto, long postId, long commentId) {
    Post post = findPost(postId);
    post.verifyNotDeleted();
    Comment comment = findComment(commentId);
    comment.edit(authUser, commentDto, post);
    return CommentUpdateResponse.from(comment);
  }

  private Comment findComment(long id) {
    return commentRepository.findById(id)
            .orElseThrow(() -> new CommentNotFoundException(String.valueOf(id)));
  }
}
