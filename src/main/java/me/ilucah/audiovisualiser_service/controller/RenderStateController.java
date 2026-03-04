package me.ilucah.audiovisualiser_service.controller;

import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RenderStateController {

    @PutMapping("/renderstate/put")
    public String add(@RequestBody String renderstate) {
        String uuid = UUID.randomUUID().toString();
        // put renderstate in db
        return uuid; // Returns the ID for this render state.
    }

    @DeleteMapping("/renderstate/put")
    public String remove(@RequestBody String renderstate) {
        // put renderstate in db
        return "";
    }
}
