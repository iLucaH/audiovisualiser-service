package me.ilucah.audiovisualiser_service.database;

import java.util.Map;
import java.util.UUID;

public interface IRenderStateStorageProvider {

    boolean exists(String username);

    void saveRenderState(String username, UUID renderStateUuid, String renderState);

    String getRenderState(String username, UUID renderStateUuid);

    Map<UUID, String> getRenderStates(String username);

    void close();
}
