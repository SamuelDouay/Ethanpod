module ethanpod.event {
    requires ethanpod.core;
    requires org.apache.logging.log4j;
    requires javafx.graphics;
    requires ethanpod.util;
    requires com.google.common;
    exports fr.github.ethanpod.event;
    exports fr.github.ethanpod.event.request;
    exports fr.github.ethanpod.event.updated;
}