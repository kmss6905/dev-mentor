package site.devmentor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response{
  private boolean success;

  public static Response ok() {
    Response response = new Response();
    response.success = true;
    return response;
  }
}
