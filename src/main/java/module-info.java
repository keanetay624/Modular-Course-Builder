module ModularCourseBuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;
    
    opens ModularCourseBuilder to javafx.fxml;
    exports ModularCourseBuilder;
}
