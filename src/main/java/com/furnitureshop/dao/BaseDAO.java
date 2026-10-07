package com.furnitureshop.dao;

import com.furnitureshop.config.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {

    protected Connection conn() throws SQLException {
        return DBConnection.getInstance().getConnection();
    }
}
