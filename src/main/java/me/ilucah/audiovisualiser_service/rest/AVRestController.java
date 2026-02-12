package me.ilucah.audiovisualiser_service.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AVRestController {

    @GetMapping("/")
    public String entry() {
        return "Invalid Usage. - on Entry";
    }

}
