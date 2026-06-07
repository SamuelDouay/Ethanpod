package fr.github.ethanpod.logic.sql.query;

public interface QueryBuilder {
    String build();

    Object[] getParameters();
}
