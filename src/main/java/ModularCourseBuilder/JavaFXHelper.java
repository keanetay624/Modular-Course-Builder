/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModularCourseBuilder;

import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 *
 * @author Keane Local
 */
public class JavaFXHelper {
    // Lifted from Blair's Personbook
    // https://stackoverflow.com/questions/28558165/javafx-setvisible-hides-the-element-but-doesnt-rearrange-adjacent-nodes
    public static void setNodeHidden(Node node, boolean isHidden) {
        node.setVisible(!isHidden);
        node.setManaged(!isHidden);
    }
    public static void setNodesHidden(Node[] nodes, boolean isHidden) {
        for (Node node : nodes) {
            setNodeHidden(node, isHidden);
        }
    }
    public static void setButtonActive(Button button, boolean isActive) {
        if (isActive) {
            button.getStyleClass().add("active");
        } else {
            button.getStyleClass().removeAll("active");
        }
        
    }
    public static void setButtonsActive(Button[] buttons, boolean isActive) {
        for (Button button : buttons) {
            setButtonActive(button, isActive);
        }
    }
    
}
