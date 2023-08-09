package site.devmentor.dto;

import org.springframework.http.ResponseEntity;

public class ResponseUtil {

    public static ResponseEntity<Response> ok() {
        return ResponseEntity.ok(Response.ok());
    }
}