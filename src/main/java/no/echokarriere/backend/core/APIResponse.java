package no.echokarriere.backend.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class APIResponse<T> {
    APIStatus status;
    T data;
    String message;

    public static <T> APIResponse<T> success(T data) {
        return new APIResponse<>(APIStatus.SUCCESS, data, null);
    }

    public static APIResponse<Object> failure(String message) {
        return new APIResponse<>(APIStatus.ERROR, null, message);
    }

    public enum APIStatus {
        @JsonProperty("success")
        SUCCESS,
        @JsonProperty("error")
        ERROR
    }
}
