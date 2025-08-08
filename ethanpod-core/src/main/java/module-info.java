module ethanpod.core {
    requires org.apache.logging.log4j;
    requires java.sql;
    requires ethanpod.core;
    exports fr.github.ethanpod.core.item;
    exports fr.github.ethanpod.core.thread;
    exports fr.github.ethanpod.core.exception.technical;
    exports fr.github.ethanpod.core.exception;
    exports fr.github.ethanpod.core.exception.util;
    exports fr.github.ethanpod.core.exception.thread;
    exports fr.github.ethanpod.core.exception.future;
}