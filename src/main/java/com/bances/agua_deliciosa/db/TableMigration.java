package com.bances.agua_deliciosa.db;

import lombok.Value;
import java.util.List;
import java.util.Collections;
import java.util.function.Function;

@Value
public class TableMigration {
    private final String fileName;
    private final List<String> tableNames;
    private final Function<String, Boolean> tableExistsChecker;
    
    public TableMigration(String fileName, String tableName, Function<String, Boolean> tableExistsChecker) {
        this.fileName = fileName;
        this.tableNames = Collections.singletonList(tableName);
        this.tableExistsChecker = tableExistsChecker;
    }
    
    public TableMigration(String fileName, List<String> tableNames, Function<String, Boolean> tableExistsChecker) {
        this.fileName = fileName;
        this.tableNames = tableNames;
        this.tableExistsChecker = tableExistsChecker;
    }
    
    public boolean allTablesExist() {
        return tableNames.stream().allMatch(this::tableExists);
    }
    
    private boolean tableExists(String tableName) {
        try {
            return tableExistsChecker.apply(tableName);
        } catch (Exception e) {
            return false;
        }
    }
    
    public List<String> getTableNames() {
        return tableNames;
    }
} 