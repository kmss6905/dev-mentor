package site.devmentor.acceptance.utils;

import site.devmentor.auth.LoginDto;
import site.devmentor.domain.comment.Comment;
import site.devmentor.domain.mentor.info.MentorInfo;
import site.devmentor.domain.mentor.request.Memo;
import site.devmentor.domain.mentor.request.MentorRequest;
import site.devmentor.domain.mentor.request.Status;
import site.devmentor.domain.mentor.schedule.ScheduleDetail;
import site.devmentor.domain.mentor.schedule.vo.*;
import site.devmentor.domain.post.Post;
import site.devmentor.domain.user.User;
import site.devmentor.dto.comment.CommentDto;
import site.devmentor.dto.mentor.MentorRequestDto;
import site.devmentor.dto.mentor.MentorRequestStatusDto;
import site.devmentor.dto.mentor.schedule.ScheduleDetailRequest;
import site.devmentor.dto.mentor.schedule.ScheduleDetailStatusRequest;
import site.devmentor.dto.mentor.schedule.ScheduleRequest;
import site.devmentor.dto.mentor.schedule.MentorScheduleUpdateDto;
import site.devmentor.dto.post.request.PostCreateUpdateRequest;
import site.devmentor.dto.user.request.UserCreateRequest;
import site.devmentor.dto.user.request.UserProfileRequest;

import java.time.LocalDateTime;

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

  public static MentorInfo MENTOR_INFO = MentorInfo.builder()
          .userId(1L)
          .currentMentees(0)
          .maxMentees(100)
          .build();

  public static MentorInfo FULL_MENTOR_INFO = MentorInfo.builder()
          .userId(1L)
          .currentMentees(100)
          .maxMentees(100)
          .build();

  public static MentorRequest MENTOR_REQUEST = MentorRequest.builder()
          .mentorUserId(2L)
          .menteeUserId(1L)
          .status(Status.WAITING)
          .memo(new Memo("memo"))
          .build();

  public static MentorRequest MENTOR_REQUEST_ACCEPTED = MentorRequest.builder()
          .menteeUserId(2L)
          .mentorUserId(1L)
          .memo(new Memo("memo"))
          .status(Status.ACCEPTED)
          .build();

  public static MentorRequest MENTOR_REQUEST_DENIED = MentorRequest.builder()
          .menteeUserId(2L)
          .mentorUserId(1L)
          .memo(new Memo("memo"))
          .status(Status.DENIED)
          .build();

  public static ScheduleDetail SCHEDULE_DETAIL = ScheduleDetail.builder()
          .title("title")
          .memo(ScheduleDetailMemo.create(
                  ScheduleMentorMemo.from("mentorMemo"),
                  ScheduleMenteeMemo.from("menteeMemo")
          )).time(ScheduleDetailTime
                  .of(
                          LocalDateTime.of(2023, 1, 1, 0, 0),
                          LocalDateTime.of(2023, 1, 10, 0, 0))
          )
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

  public static MentorRequestDto MENTOR_CREATE_REQUEST = new MentorRequestDto(1L, "memo");

  public static MentorRequestStatusDto MAKE_UPDATE_MENTOR_REQUEST_TO_ACCEPTED = new MentorRequestStatusDto(Status.ACCEPTED);

  public static ScheduleRequest MAKE_SCHEDULE_REQUEST = new ScheduleRequest(
          1L, "title", LocalDateTime.of(2023, 1, 1, 0, 0), LocalDateTime.of(2023,1,10,0,0), "memo"
  );

  public static MentorScheduleUpdateDto MAKE_SCHEDULE_UPDATE_REQUEST = new MentorScheduleUpdateDto(
          "update_title", "update_memo", LocalDateTime.of(2023, 1, 10, 0, 0), LocalDateTime.of(2023, 1, 20, 0, 0)
  );

  public static ScheduleDetailRequest MAKE_SCHEDULE_DETAIL_REQUEST = new ScheduleDetailRequest(
          "detail_title", LocalDateTime.of(2023, 1,
          1, 1, 0), LocalDateTime.of(2023, 1, 1, 2, 0), "menteeMemo", "mentorMemo"
  );

  public static ScheduleDetailRequest MAKE_SCHEDULE_DETAIL_UPDATE_REQUEST = new ScheduleDetailRequest(
          "update_detail_title", LocalDateTime.of(2023, 1,
          1, 10, 0), LocalDateTime.of(2023, 1, 1, 11, 0), "updateMenteeMemo", "updateMentorMemo"
  );

  public static ScheduleDetailStatusRequest MAKE_SCHEDULE_DETAIL_STATUS_DOING_UPDATE_REQUEST = new ScheduleDetailStatusRequest(ScheduleDetailStatus.DOING);

}
