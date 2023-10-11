package site.devmentor.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T>{
  private boolean success;
  private T data;

  public static Response ok() {
    Response response = new Response();
    response.success = true;
    return response;
  }

  public Response withData(T data) {
    Response response = new Response();
    response.success = true;
    response.data = data;
    return response;
  }
}
