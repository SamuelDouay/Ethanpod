package fr.github.ethanpod.logic.sql.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class SqlQueryBuilder implements QueryBuilder {

    protected final StringBuilder queryBuilder;

    protected final List<String> selectColumns = new ArrayList<>();
    protected final List<String> fromTables = new ArrayList<>();
    protected final List<String> joins = new ArrayList<>();
    protected final List<String> whereConditions = new ArrayList<>();
    protected final List<String> groupByColumns = new ArrayList<>();
    protected final List<String> orderByColumns = new ArrayList<>();

    private boolean hasLimitOffsetPlaceholders = false;
    private String customLimitOffset = null;
    private boolean structuredMode = false;

    protected SqlQueryBuilder() {
        this.queryBuilder = new StringBuilder();
    }

    protected SqlQueryBuilder select(String... columns) {
        structuredMode = true;
        selectColumns.addAll(Arrays.asList(columns));
        return this;
    }

    protected SqlQueryBuilder from(String table) {
        structuredMode = true;
        fromTables.add(table);
        return this;
    }

    protected SqlQueryBuilder innerJoin(String table, String condition) {
        structuredMode = true;
        joins.add("INNER JOIN " + table + " ON " + condition);
        return this;
    }

    protected SqlQueryBuilder leftJoin(String table, String condition) {
        structuredMode = true;
        joins.add("LEFT JOIN " + table + " ON " + condition);
        return this;
    }

    protected SqlQueryBuilder where(String condition) {
        structuredMode = true;
        whereConditions.add(condition);
        return this;
    }

    protected SqlQueryBuilder orderBy(String... columns) {
        structuredMode = true;
        orderByColumns.addAll(Arrays.asList(columns));
        return this;
    }

    protected SqlQueryBuilder groupBy(String... columns) {
        structuredMode = true;
        groupByColumns.addAll(Arrays.asList(columns));
        return this;
    }

    // Pour les placeholders de pagination
    protected SqlQueryBuilder limitOffset() {
        structuredMode = true;
        this.hasLimitOffsetPlaceholders = true;
        return this;
    }

    // Pour les valeurs fixes (ex: LIMIT 8)
    protected SqlQueryBuilder limitValue(int limit) {
        structuredMode = true;
        this.customLimitOffset = "LIMIT " + limit;
        return this;
    }

    // Compatibilité avec l'ancien append
    protected void append(String clause) {
        if (structuredMode && queryBuilder.isEmpty()) structuredMode = false;
        if (!queryBuilder.isEmpty() && !clause.trim().startsWith("LIMIT") &&
                !clause.trim().startsWith("ORDER BY") && !queryBuilder.toString().trim().endsWith("(")) {
            queryBuilder.append(" ");
        }
        queryBuilder.append(clause);
    }

    protected void reset() {
        queryBuilder.setLength(0);
    }

    @Override
    public String build() {
        if (structuredMode && !selectColumns.isEmpty() && !fromTables.isEmpty()) {
            reset();
            appendSelect();
            appendFrom();
            appendJoins();
            appendWhere();
            appendGroupBy();
            appendOrderBy();
            if (customLimitOffset != null) {
                queryBuilder.append(customLimitOffset);
            } else if (hasLimitOffsetPlaceholders) {
                queryBuilder.append("LIMIT ? OFFSET ?");
            }
        }
        return queryBuilder.toString().trim();
    }

    private void appendSelect() {
        queryBuilder.append("SELECT ").append(String.join(", ", selectColumns)).append("\n");
    }

    private void appendFrom() {
        queryBuilder.append("FROM ").append(String.join(", ", fromTables)).append("\n");
    }

    private void appendJoins() {
        for (String join : joins) queryBuilder.append(join).append("\n");
    }

    private void appendWhere() {
        if (!whereConditions.isEmpty()) {
            queryBuilder.append("WHERE ").append(String.join(" ", whereConditions)).append("\n");
        }
    }

    private void appendGroupBy() {
        if (!groupByColumns.isEmpty()) {
            queryBuilder.append("GROUP BY ").append(String.join(", ", groupByColumns)).append("\n");
        }
    }

    private void appendOrderBy() {
        if (!orderByColumns.isEmpty()) {
            queryBuilder.append("ORDER BY ").append(String.join(", ", orderByColumns)).append("\n");
        }
    }

    @Override
    public String toString() {
        return build();
    }

    @Override
    public Object[] getParameters() {
        return new Object[]{};
    }
}