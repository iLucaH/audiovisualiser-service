package me.ilucah.audiovisualiser_service.database;

public enum DatabaseType {

    SQLITE("org.sqlite.JDBC",
            "jdbc:sqlite:",
            "CREATE TABLE IF NOT EXISTS userdata (UUID TEXT PRIMARY KEY, TOKENS BIGINT NOT NULL)",
            "INSERT OR IGNORE INTO userdata (UUID, TOKENS) VALUES (?, ?)"),

    MYSQL("com.mysql.cj.jdbc.Driver",
            "",
            "CREATE TABLE IF NOT EXISTS userdata (UUID VARCHAR(36) PRIMARY KEY, TOKENS BIGINT NOT NULL)",
            "INSERT INTO userdata (UUID, TOKENS) VALUES (?, ?) ON DUPLICATE KEY UPDATE TOKENS = VALUES(TOKENS)");

    final String driver, baseUrl, createTableSQL, insert;

    DatabaseType(String driver, String baseUrl, String createTableSQL, String insert) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.createTableSQL = createTableSQL;
        this.insert = insert;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getCreateTableSQL() {
        return createTableSQL;
    }

    public String getDriver() {
        return driver;
    }

    public String getInsert() {
        return insert;
    }
}