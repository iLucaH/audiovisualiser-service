package me.ilucah.audiovisualiser_service.database;

import java.util.Map;

public interface IRenderStateStorageProvider {

    boolean exists(String username);

    void saveRenderState(String username, String renderStateUuid, String renderState);

    void removeRenderState(String username, String renderStateUuid);

    String getRenderState(String username, String renderStateUuid);

    Map<String, String> getRenderStates(String username);

    void close();
}
