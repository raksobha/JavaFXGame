
package marupeke.part3;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import marupeke.part2.MarupekeGrid;
import marupeke.part2.Reason;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Candidate number: 237039
 */

public class MarupekeFX extends Application implements MarupekeGUI {

    private MarupekeGrid game;
    private int size;
    public static final int DIMENSION = 70;
    private MarupekeGrid.Mark selected;

    @Override
    public void init() throws Exception {
        super.init();
        Parameters parameters = getParameters();
        List<String> params = parameters.getRaw();
        size = Integer.parseInt(params.get(0));
        System.out.println(size);
        legalSize(size);
        this.game = MarupekeGrid.buildGameGrid(size);
        game.setGUI(this);

    }

    public static void main(String[] args) {
        MarupekeFX.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        startGame(primaryStage);

    }

    public void startGame(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(DIMENSION*size, DIMENSION*size);
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        root.setCenter(grid);

        //choose type of mark
        HBox buttonPane = new HBox();
        buttonPane.setAlignment(Pos.CENTER);
        root.setTop(buttonPane);
        ToggleGroup toggleGroup = new ToggleGroup();
        for (MarupekeGrid.Mark a : MarupekeGrid.Mark.values()) {
            ToggleButton unXO = new ToggleButton(a.toString());
            unXO.setToggleGroup(toggleGroup);

            unXO.setOnAction(e -> {
                if (toggleGroup.getSelectedToggle() == unXO) {
                    selected = a;
                }
            });
            buttonPane.getChildren().add(unXO);
        }


        // buttons for functions
        HBox bottomPane = new HBox();
        bottomPane.setAlignment(Pos.CENTER);
        root.setBottom(bottomPane);
        Button quit = new Button("QUIT");
        Button restart = new Button("RESTART");
        Button open = new Button("OPEN");
        Button save = new Button("SAVE");
        bottomPane.getChildren().addAll(quit, restart, open, save);


        //quit button press action
        quit.setOnAction(e -> {

            Integer[] sizes = new Integer[]{4, 5, 6, 7, 8, 9, 10};
            ObservableList<Integer> listSizes = FXCollections.observableArrayList(sizes);
            Dialog<ButtonType> dialog = new Dialog<ButtonType>();
            dialog.setTitle("Commiserations");
            dialog.setHeaderText("Are you sure you want to quit?");
            ButtonType quitButton = new ButtonType("Quit", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButton, quitButton);

            GridPane chooseSize = new GridPane();
            chooseSize.setHgap(10);
            chooseSize.setVgap(10);
            chooseSize.setPadding(new Insets(20, 150, 10, 10));

            ChoiceBox<Integer> choices = new ChoiceBox(listSizes);
            choices.setValue(size);
            chooseSize.add(new Label("Choose size of grid if you want to play again:"), 0, 0);
            chooseSize.add(choices, 1, 0);
            dialog.getDialogPane().setContent(chooseSize);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.get() == quitButton){
                Platform.exit();
            } else if (result.get() == okButton) {
                size = choices.getValue();
            }
        });


        //restart button action
        restart.setOnAction(e ->{
            restart(primaryStage);
        });


        //open button action
        open.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("e.g. filename.txt");
            dialog.setTitle("Enter file name");
            dialog.setHeaderText("File name");
            dialog.setContentText("Please enter file name");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(filename-> getSavedGrid(filename, primaryStage));
        });


        //save button action
        save.setOnAction(e ->{
            TextInputDialog dialog = new TextInputDialog("e.g. filename.txt");
            dialog.setTitle("Save Grid to file");
            dialog.setHeaderText("A new file will be created");
            dialog.setContentText("Please enter new file name");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(filename-> game.saveGrid(filename));
        });


        //create grid
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                //add editable tile
                if (game.get(i, j).isEditable()) {
                    GameTile a = new GameTile(i, j, game.get(i,j).toString());
                    grid.add(a, i, j);
                    //action for pressing tile
                    a.setOnMouseClicked(event -> {
                        if( toggleGroup.getSelectedToggle() !=null) {
                            int y = GridPane.getColumnIndex(a);
                            int x = GridPane.getRowIndex(a);
                            a.setText(game.pressed(selected, x, y));
                        }
                    });
                }
                //add uneditable tile
                else {
                    grid.add(new UneditableGameTile(game.get(i,j).toString()), i, j);
                }
            }
        }

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void restart(Stage stage) {
        cleanup();
        startGame(stage);
    }

    public void cleanup(){
        try {
            this.game = null;
            this.game = MarupekeGrid.buildGameGrid(size);
            game.setGUI(this);
        }
        catch (Exception e) {
            System.out.println("Size error");
        }
    }

    public void getSavedGrid(String s, Stage stage){
        MarupekeGrid tempGame;
        try {
             tempGame = MarupekeGrid.buildFromFile(s);
        }
        catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("File name does not exist");
            alert.setHeaderText("Illegal file name");
            alert.setContentText("File does not exist. Please try again");
            alert.show();
            return;
        }
        size = tempGame.getSize();
        this.game = null;
        this.game = tempGame;
        game.setGUI(this);
        startGame(stage);
    }


    private void legalSize(int i) throws IllegalGridSize {
        if(i < 4 || i > 10) {
            throw new IllegalGridSize();
        }
    }

    @Override
    public void listReasons(List<Reason> a) {
        if(!game.isLegalGrid()) {
            StringBuilder problems = new StringBuilder();
            for (Reason b : a) {
                problems.append("\n");
                problems.append(b.toString());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Illegalities");
            alert.setHeaderText("Information Alert");
            alert.setContentText(problems.toString());
            alert.show();
        }
        if(game.isPuzzleComplete() && game.isLegalGrid()){
            Integer[] sizes = new Integer[]{4, 5, 6, 7, 8, 9, 10};
            ChoiceDialog<Integer> dialog = new ChoiceDialog<>(size, sizes);
            dialog.setTitle("Winner");
            dialog.setHeaderText("Congratulations you have won");
            dialog.setContentText("Choose size of grid if you want to play again:");

            Optional<Integer> result = dialog.showAndWait();
            result.ifPresent(letter -> size = letter);
        }
    }
    @Override
    public void alert(String s){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Alert");
        alert.setHeaderText("Information Alert");
        alert.setContentText(s);
        alert.show();

    }


}
