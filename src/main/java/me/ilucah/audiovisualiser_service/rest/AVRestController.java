package me.ilucah.audiovisualiser_service.rest;

import me.ilucah.audiovisualiser_service.prompt.PromptResolve;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AVRestController {

    @GetMapping("/")
    public String entry() {
        return "Invalid Usage. - on Entry";
    }

    @GetMapping("/error")
    public String error() {
        return "Invalid Usage. - on Error";
    }

    // /prompt?token=1234&prompt=write+me+something
    @GetMapping("/prompt")
    public String prompt(@RequestParam Long token, @RequestParam String prompt) {
        PromptResolve resolve = PromptResolve.builder().success(true).prompt("here is your message!").build();
        return resolve.toJson();
    }

}
