package site.devmentor.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.devmentor.application.post.PostService;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.auth.post.AuthorAccessOnly;
import site.devmentor.dto.ResponseUtil;
import site.devmentor.dto.comment.CommentCreateDto;
import site.devmentor.dto.comment.CommentCreateResponse;
import site.devmentor.dto.post.request.PostCreateUpdateRequest;
import site.devmentor.dto.post.response.PostCreateResponse;

import java.net.URI;

@RestController
@RequestMapping("/api/posts")
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping
  public ResponseEntity<?> create(
          @LoginUser AuthenticatedUser authUser,
          @RequestBody PostCreateUpdateRequest postCreateUpdateRequest) {
    PostCreateResponse postCreateResponse = postService.create(authUser, postCreateUpdateRequest);
    return ResponseEntity
            .created(URI.create("/api/posts/" + postCreateResponse.getPostId()))
            .body(postCreateResponse);
  }

  @AuthorAccessOnly
  @PatchMapping("/{id}")
  public ResponseEntity<?> update(
          @PathVariable long id,
          @RequestBody PostCreateUpdateRequest updateRequest) {
    return ResponseEntity.ok(postService
            .update(id, updateRequest));
  }

  @AuthorAccessOnly
  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(
          @PathVariable long id
  ) {
    postService.delete(id);
    return ResponseUtil.ok();
  }

  @PostMapping("/{id}/comments")
  public ResponseEntity<CommentCreateResponse> replyComment(
          @LoginUser AuthenticatedUser authUser,
          @RequestBody CommentCreateDto commentCreateDto,
          @PathVariable long id
  ) {
    CommentCreateResponse commentCreateResponse = postService.replyComment(authUser, commentCreateDto, id);
    return ResponseEntity.ok(commentCreateResponse);
  }
}
