module ethanpod.view {
    exports fr.github.ethanpod.view;
    exports fr.github.ethanpod.view.layout;
    exports fr.github.ethanpod.view.context;
    exports fr.github.ethanpod.view.util;
    exports fr.github.ethanpod.view.component.image;
    exports fr.github.ethanpod.view.component.surprise;
    exports fr.github.ethanpod.view.component.episode;
    exports fr.github.ethanpod.view.thread;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.graphics;
    requires org.apache.logging.log4j;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign2;
    requires ethanpod.core;
    requires ethanpod.service;
    requires ethanpod.util;
}