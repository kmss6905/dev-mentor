package site.devmentor.acceptance.utils;

import site.devmentor.auth.LoginDto;
import site.devmentor.domain.comment.Comment;
import site.devmentor.domain.post.Post;
import site.devmentor.domain.user.User;
import site.devmentor.dto.comment.CommentDto;
import site.devmentor.dto.post.request.PostCreateUpdateRequest;
import site.devmentor.dto.user.request.UserCreateRequest;
import site.devmentor.dto.user.request.UserProfileRequest;

public class Fixture {

  public static final String COMMON_PASSWORD = "commonPassword";
  public static final String NEW_USER_ID = "newuser";
  public static final String NEW_USER_EMAIL = "newuser@google.com";
  public static User USER_ONE = User.builder()
          .userId("user1")
          .email("user1@github.io")
          .password(COMMON_PASSWORD)
          .build();

  public static User USER_TWO = User.builder()
          .userId("user2")
          .email("user2@github.io")
          .password(COMMON_PASSWORD)
          .build();

  public static Post USER_ONE_POST = Post.builder()
          .userPid(1L)
          .content("content")
          .title("title")
          .build();

  public static Comment USER_ONE_POST_COMMENT = Comment.builder()
          .postId(1L)
          .authorId(1L)
          .content("comment")
          .build();

  public static PostCreateUpdateRequest MAKE_POST_REQUEST = new PostCreateUpdateRequest("title", "content");

  public static UserCreateRequest MAKE_DUPLICATED_ID_USER_REQUEST =
          new UserCreateRequest(USER_ONE.getUserId(), COMMON_PASSWORD, USER_ONE.getEmail());

  public static UserCreateRequest MAKE_DUPLICATED_EMAIL_USER_REQUEST =
          new UserCreateRequest("user3", COMMON_PASSWORD, USER_ONE.getEmail());

  public static LoginDto MAKE_USER_ONE_LOGIN_REQUEST = new LoginDto(USER_ONE.getUserId(), USER_ONE.getPassword());
  public static LoginDto MAKE_USER_ONE_WRONG_PASSWORD_REQUEST = new LoginDto(USER_ONE.getUserId(), "wrongpassword");
  public static UserProfileRequest MAKE_USER_PROFILE_REQUEST = new UserProfileRequest("this is user content");

  public static CommentDto MAKE_COMMENT_REQUEST = new CommentDto("comment");
}
