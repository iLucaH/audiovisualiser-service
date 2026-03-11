package me.ilucah.audiovisualiser_service.controller;

import me.ilucah.audiovisualiser_service.database.RenderStateDao;
import me.ilucah.audiovisualiser_service.model.RenderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/renderState")
public class RenderStateController {

    private static final int RENDER_STATE_ID_PRESENT= 0;
    private static final int RENDER_STATE_ID_NOT_EXIST = 1;
    private static final int RENDER_STATE_ID_NOT_PRESENT = 2;

    @Autowired
    private RenderStateDao renderStateDao;

    @PostMapping("/add")
    public int add(Principal principal) {
        RenderState renderState = new RenderState(principal.getName(), "My render state !");
        renderStateDao.save(renderState);
        return renderState.getId();
    }

    @GetMapping("/getAll")
    public List<RenderState> getAll(Principal principal) {
        return renderStateDao.findByUsername(principal.getName());
    }

    @GetMapping("/get")
    public RenderState get(@RequestBody int id) {
        return renderStateDao.findById(id).orElse(new RenderState(-1, "", ""));
    }

    @DeleteMapping("/delete")
    public int delete(@RequestBody int id) {
        if (!renderStateDao.existsById(id))
            return RENDER_STATE_ID_NOT_EXIST;
        Optional<RenderState> renderStateOptional = renderStateDao.findById(id);
        if (renderStateOptional.isEmpty())
            return RENDER_STATE_ID_NOT_PRESENT;
        return RENDER_STATE_ID_PRESENT;
    }

    @DeleteMapping("/deleteAll")
    public boolean deleteAll(Principal principal) {
        for (RenderState renderState : renderStateDao.findByUsername(principal.getName())) {
            renderStateDao.deleteById((int) renderState.getId());
        }
        return true;
    }

}
