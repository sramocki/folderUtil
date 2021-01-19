module org.tagUtil {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires org.apache.commons.lang3;
    requires jaudiotagger;

    opens org.tagUtil to javafx.fxml;
    exports org.tagUtil;
}