package site.devmentor.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.devmentor.application.post.PostService;
import site.devmentor.auth.AuthenticatedUser;
import site.devmentor.auth.LoginUser;
import site.devmentor.dto.post.PostCreateRequest;
import site.devmentor.dto.post.PostCreateResponse;

import java.net.URI;

@RestController
@RequestMapping("/api/post")
public class PostController {

  private final PostService postService;

  public PostController(PostService postService) {
    this.postService = postService;
  }

  @PostMapping
  public ResponseEntity<?> create(
          @LoginUser AuthenticatedUser authUser,
          @RequestBody PostCreateRequest postCreateRequest) {
    PostCreateResponse postCreateResponse = postService.create(authUser, postCreateRequest);
    return ResponseEntity
            .created(URI.create("/api/post/" + postCreateResponse.postId()))
            .body(postCreateResponse);
  }
}
