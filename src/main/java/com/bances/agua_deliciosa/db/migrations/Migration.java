package com.bances.agua_deliciosa.db.migrations;

import java.sql.Connection;
import java.sql.SQLException;

public interface Migration {
    void migrate(Connection connection) throws SQLException;
} 