package me.ilucah.audiovisualiser_service.controller;

import me.ilucah.audiovisualiser_service.database.DatabaseType;
import me.ilucah.audiovisualiser_service.database.RenderStateProvider;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class RenderStateController {

    private final RenderStateProvider sqlProvider;

    public RenderStateController() {
        sqlProvider = new RenderStateProvider(DatabaseType.SQLITE);
    }

    @PutMapping("/renderstate/put")
    public String add(Authentication authentication, @RequestBody String renderState) {
        String uuid = UUID.randomUUID().toString();
        sqlProvider.saveRenderState(authentication.getName(), uuid, renderState);
        return uuid; // Returns the ID for this render state.
    }

    @DeleteMapping("/renderstate/put")
    public String remove(Authentication authentication, @RequestBody String renderStateUUID) {
        sqlProvider.removeRenderState(authentication.getName(), renderStateUUID);
        return "";
    }

}
