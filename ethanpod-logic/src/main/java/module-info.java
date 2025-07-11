module ethanpod.logic {
    requires ethanpod.core;
    requires ethanpod.util;
    requires java.sql;
    requires org.apache.logging.log4j;

    exports fr.github.ethanpod.logic;
    exports fr.github.ethanpod.logic.sql.dao;
    exports fr.github.ethanpod.logic.services;
}