module com.soma.view {
	
    exports com.soma.view;
    exports com.soma.view.window to javafx.graphics;

    // TODO to remove
    exports com.soma.view.window.panel.stack to javafx.graphics;
    
    // Only for scenic view
    requires javafx.swing;

	requires javafx.base;
	requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;

    requires transitive reactor.fx;

    opens com.soma.view.window to javafx.fxml;
    opens com.soma.view.window.panel.stack to javafx.fxml;
}