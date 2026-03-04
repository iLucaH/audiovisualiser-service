package me.ilucah.audiovisualiser_service.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class RenderStateProvider implements IRenderStateStorageProvider {

    private final HikariDataSource ds;
    private final String tableCreateSQL, insert;

    public RenderStateProvider(String jdbcUrl, String driverClassName, String tableCreateSQL, String insert) {
        this.tableCreateSQL = tableCreateSQL;
        this.insert = insert;

        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(jdbcUrl);
        ds = new HikariDataSource(config);
        createTable();
    }

    public void createTable() {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement(tableCreateSQL)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean exists(String username) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM userdata WHERE username=?")) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void saveRenderState(String username, UUID renderStateUuid, String renderState) {
        if (!exists(username)) {
            try (Connection conn = ds.getConnection();
                 PreparedStatement ps = conn.prepareStatement(insert)) {
                ps.setString(1, username);
                ps.setString(2, renderStateUuid.toString());
                ps.setString(3, renderState);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            setUserDataInDB(username, renderStateUuid, renderState);
        }
    }

    private void setUserDataInDB(String username, UUID renderStateUuid, String renderState) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE userdata SET TOKENS=? WHERE UUID=?")) {
            ps.setString(1, username);
            ps.setString(2, renderStateUuid.toString());
            ps.setString(3, renderState);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getRenderState(String username, UUID renderStateUuid) {
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT TOKENS FROM userdata WHERE UUID=?")) {
            ps.setString(1, username);
            ps.setString(2, renderStateUuid.toString());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("TOKENS");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public Map<UUID, String> getRenderStates(String username) {
        Map<UUID, String> topPlayers = new LinkedHashMap<>();
        try (Connection conn = ds.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT TOKENS FROM userdata WHERE UUID=?")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    //return rs.getString("TOKENS");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return topPlayers;
    }

    @Override
    public void close() {
        ds.close();
    }
}
