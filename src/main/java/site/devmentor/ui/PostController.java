package site.devmentor.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.devmentor.application.post.PostService;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.auth.post.AuthorAccessOnly;
import site.devmentor.dto.ResponseUtil;
import site.devmentor.dto.comment.CommentDto;
import site.devmentor.dto.comment.CommentCreateResponse;
import site.devmentor.dto.comment.CommentUpdateResponse;
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
    return ResponseEntity.ok(postService.update(id, updateRequest));
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
          @RequestBody CommentDto commentDto,
          @PathVariable long id
  ) {
    return ResponseEntity.ok(postService.replyComment(authUser, commentDto, id));
  }

  // rest api design 에 따른다면 자원의 위치를 알려주는 것이 맞다. 그렇기 때문에 api/posts/:id/comments/:id 가 맞지만..
  // 실제 postId 를 받아서 어떤 검증을 한다?.. 매번 댓글 삭제할 때마다 post 의 삭제여부를 검사 후 삭제한다.?
  @PatchMapping("/{postId}/comments/{commentId}")
  public ResponseEntity<CommentUpdateResponse> updateComment(
          @LoginUser AuthenticatedUser authUser,
          @RequestBody CommentDto commentDto,
          @PathVariable(name = "postId") long postId,
          @PathVariable(name = "commentId") long commentId
  ) {
    return ResponseEntity.ok(postService.editComment(authUser, commentDto, postId, commentId));
  }
}
