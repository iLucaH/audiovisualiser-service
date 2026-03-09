package me.ilucah.audiovisualiser_service.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.ilucah.audiovisualiser_service.controller.AuthController;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Slf4j
public class RegisterResolve {

    @Getter
    public enum Status {
        SUCCESS(0, "Successfully created your account!"),
        INVALID_USERNAME_NULL(0, "Please enter a valid username!"),
        INVALID_USERNAME_MIN_CHARS(0, "Please enter a username with at least " + AuthController.USERNAME_MIN_CHARS + " characters!"),
        INVALID_USERNAME_MAX_CHARS(0, "Please enter a username with no more than " + AuthController.USERNAME_MAX_CHARS + " characters!"),
        INVALID_PASSWORD_NULL(0, "Please enter a valid password!"),
        USERNAME_TAKEN(0, "This username is taken!"),
        PASSWORD_UNSAFE(0, "Your password is not secure enough!");

        private final int id;
        private final String message;

        Status(int id, String message) {
            this.id = id;
            this.message = message;
        }

        public String build() {
            return new RegisterResolve(id, message).toJson();
        }

    }

    private int success;
    private String resolve;

    public String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Unsuccessful register resolve mapping from object mapper to string!");
        }
        return null;
    }

}
