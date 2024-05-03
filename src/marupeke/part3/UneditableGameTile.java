package marupeke.part3;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


public class UneditableGameTile extends StackPane {
    private Text text = new Text();

    /**
     * Creates a StackPane layout with default CENTER alignment.
     */
    public UneditableGameTile(String s) {
        Rectangle tile = new Rectangle(70,70);
        tile.setFill(Color.GRAY);
        text.setText(s);
        text.setFont(Font.font(50));
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(tile, text);

    }
}
