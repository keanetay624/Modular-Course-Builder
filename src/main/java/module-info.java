module ModularCourseBuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;
    requires java.desktop;
    
    opens ModularCourseBuilder to javafx.fxml;
    exports ModularCourseBuilder;
}
