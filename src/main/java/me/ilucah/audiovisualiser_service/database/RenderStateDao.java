package me.ilucah.audiovisualiser_service.database;

import me.ilucah.audiovisualiser_service.model.RenderState;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RenderStateDao extends CrudRepository<RenderState, Integer> {

    List<RenderState> findByUsername(String username);

}
