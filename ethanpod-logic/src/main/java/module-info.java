module ethanpod.logic {
    requires ethanpod.core;
    requires ethanpod.util;
    requires java.sql;
    requires org.apache.logging.log4j;
    requires com.zaxxer.hikari;
    requires org.xerial.sqlitejdbc;
    requires ethanpod.exception;
    requires ethanpod.event;
    requires com.google.common;

    exports fr.github.ethanpod.logic.sql.dao;
    exports fr.github.ethanpod.logic.sql.setting;
    exports fr.github.ethanpod.logic.handler;
}