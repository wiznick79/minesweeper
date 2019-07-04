package minesweeper;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Controller implements Initializable  {

    public TextField boardRows;
    public TextField boardColumns;
    public TextField boardMines;
    public Label labelRows;
    public Label labelColumns;
    public Label labelMines;
    public Button createButton;
    public Button newGameButton;
    public Button easyGameButton;
    public Button normalGameButton;
    public Button hardGameButton;
    public Button customGameButton;
    public GridPane gameGridPane;
    public AnchorPane mainAnchorPane;
    public AnchorPane anchorPane;
    public VBox mainVBox;
    public Text title;
    public Label timeLabel;
    public Label tLabel;
    public Label mLabel;
    public Label minesLabel;
    public Label msgLabel;

    private DoubleProperty time = new SimpleDoubleProperty();
    private int[] scores = new int[]{9999,9999,9999};       // initialize scores with default of 9999

    @Override
    public void initialize (URL location, ResourceBundle resources){
        boardRows.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}?") || Integer.parseInt(newValue)>30)
                    boardRows.setText(oldValue);
            }
        });

        boardColumns.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}?") || Integer.parseInt(newValue)>40)
                    boardColumns.setText(oldValue);
            }
        });

        boardMines.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}?") || Integer.parseInt(newValue)>99)
                    boardMines.setText(oldValue);
            }
        });

        tLabel.setFont(Font.font("Arial", 14));
        timeLabel.setFont(Font.font("Arial", FontWeight.BOLD,20));
        timeLabel.setTextFill(Color.RED);
        timeLabel.textProperty().bind(time.asString("%03.0f"));

        mLabel.setFont(Font.font("Arial",14));
        minesLabel.setFont(Font.font("Arial", FontWeight.BOLD,20));
        minesLabel.setTextFill(Color.RED);

        msgLabel.setFont(Font.font("Arial", FontWeight.BOLD,14));
        msgLabel.setTextFill(Color.BLUE);
        try {
            loadScores();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private AnimationTimer timer = new AnimationTimer() {

        private long startTime;
        BooleanProperty running = new SimpleBooleanProperty();

        @Override
        public void start() {
            startTime = System.currentTimeMillis();
            running.set(true);
            super.start();
        }

        @Override
        public void stop() {
            running.set(false);
            super.stop();
        }

        @Override
        public void handle (long timestamp) {
            long now = System.currentTimeMillis();
            time.set((now - startTime) / 1000.0);
        }
    };

    private double calcDecW () {
        double dec;
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        Scene scene = mainAnchorPane.getScene();
        dec = stage.getWidth() - scene.getWidth();
        return dec;
    }

    private double calcDecH () {
        double dec;
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        Scene scene = mainAnchorPane.getScene();
        dec = stage.getHeight() - scene.getHeight();
        return dec;
    }

    public void easyGame(ActionEvent actionEvent) {
        generateEmptyGameGrid(9,9,10,"easy");
    }

    public void normalGame(ActionEvent actionEvent) {
        generateEmptyGameGrid(16,16,40,"normal");
    }

    public void hardGame(ActionEvent actionEvent) {
        generateEmptyGameGrid(20,25,99,"hard");
    }

    public void customGame (ActionEvent actionEvent) {
        title.setVisible(false);
        easyGameButton.setVisible(false);
        normalGameButton.setVisible(false);
        hardGameButton.setVisible(false);
        customGameButton.setVisible(false);
        tLabel.setVisible(false);
        timeLabel.setVisible(false);
        mLabel.setVisible(false);
        minesLabel.setVisible(false);
        msgLabel.setVisible(false);
        newGameButton.setVisible(false);
        labelRows.setVisible(true);
        labelColumns.setVisible(true);
        labelMines.setVisible(true);
        boardRows.setVisible(true);
        boardColumns.setVisible(true);
        boardMines.setVisible(true);
        createButton.setVisible(true);
    }

    public void createCustomGame(ActionEvent actionEvent) {
        int rows,columns,mines;
        if (boardRows.getText().isEmpty()) rows=9;
        else rows = Integer.parseInt(boardRows.getText());
        if (boardColumns.getText().isEmpty()) columns=9;
        else columns = Integer.parseInt(boardColumns.getText());
        if (boardMines.getText().isEmpty()) mines=10;
        else mines = Integer.parseInt(boardMines.getText());
        generateEmptyGameGrid(rows,columns,mines,"custom");
    }

    private void generateEmptyGameGrid(int rows, int columns, int mines, String difficulty) {
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.setWidth((36*columns)+19+calcDecW());
        stage.setHeight(36*rows+96+calcDecH());
        title.setVisible(false);
        labelRows.setVisible(false);
        labelColumns.setVisible(false);
        labelMines.setVisible(false);
        boardRows.setVisible(false);
        boardColumns.setVisible(false);
        boardMines.setVisible(false);
        easyGameButton.setVisible(false);
        normalGameButton.setVisible(false);
        hardGameButton.setVisible(false);
        customGameButton.setVisible(false);
        createButton.setVisible(false);
        gameGridPane.getChildren().clear();
        tLabel.setVisible(true);
        timer.stop();
        time.setValue(0);
        timeLabel.setVisible(true);
        mLabel.setLayoutX(columns*36-40);
        mLabel.setVisible(true);
        minesLabel.setLayoutX(columns*36-30);
        minesLabel.setVisible(true);
        minesLabel.setText(Integer.toString(mines));
        msgLabel.setVisible(false);
        newGameButton.setVisible(true);
        newGameButton.setLayoutX(columns*18 - 28);
        newGameButton.setOnAction(e -> generateEmptyGameGrid(rows,columns,mines,difficulty));
        for (int row=0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile tile = new Tile(row, col, 0, false, false, false, false);
                gameGridPane.add(tile, col, row);
                Image tileImage = new Image(getClass().getResourceAsStream("/images/tile.png"));
                ImageView tileView = new ImageView(tileImage);
                tileView.setFitHeight(35.0);
                tileView.setFitWidth(35.0);
                tile.setPadding(new Insets(0));
                tile.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                tile.setGraphic(tileView);
                tile.setOnAction(e -> generateGameGrid(rows, columns, mines, difficulty, tile.getRow(), tile.getCol()));
            }
        }
    }

    private void generateGameGrid(int rows, int columns, int mines, String difficulty, int y, int x) {
        Gameboard gboard = Gameboard.generateGameboard(rows, columns, mines, difficulty, y, x);
        gameGridPane.getChildren().clear();
        timeLabel.setVisible(true);
        timer.start();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile tile = gboard.getMatrix()[row][col];
                gameGridPane.add(tile, col, row);
                Image tileImage = new Image(getClass().getResourceAsStream("/images/tile.png"));
                ImageView tileView = new ImageView(tileImage);
                tileView.setFitHeight(35.0);
                tileView.setFitWidth(35.0);
                tile.setPadding(new Insets(0));
                tile.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                tile.setGraphic(tileView);
                tile.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY)
                        checkTile(gboard, tile);
                    else if (e.getButton() == MouseButton.SECONDARY)
                        flagTile(tile);
                });
            }
        }
        checkTile(gboard,gboard.getMatrix()[y][x]);
        //printGameBoard(gboard,rows,columns); // for testing
    }

    private void checkTile(Gameboard gboard, Tile tile) {
        if (tile.isOpen() || tile.isFlag()) return;     // if the tile is already opened or is flagged, nothing happens

        int row = tile.getRow();
        int col = tile.getCol();

        Tile[][] matrix = gboard.getMatrix();
        int rows = gboard.getRows();
        int columns = gboard.getColumns();

        tile.setOpen(true);
        revealTile(gboard, tile);   // show the tile

        if (tile.isMine()) {    // if tile contains a mine, then game over !
            gameOver(gboard,false);
            return;
        }

        if (check_win(gboard)) {    // check winning condition
            gameOver(gboard,true);
            return;
        }

        if (tile.getAdjMines() > 0) return;     // if tile has number in it, end function
        // if tile is empty, check it's adjacent tiless, recursively
        if (row>0 && col>0 && matrix[row-1][col-1].getAdjMines()==0)
            checkTile(gboard,matrix[row-1][col-1]);
        else if (row>0 && col>0 && matrix[row-1][col-1].getAdjMines()>0)
            revealTile(gboard,matrix[row-1][col-1]);
        if (col>0 && matrix[row][col-1].getAdjMines()==0)
            checkTile(gboard,matrix[row][col-1]);
        else if (col>0 && matrix[row][col-1].getAdjMines()>0)
            revealTile(gboard,matrix[row][col-1]);
        if (col>0 && row<rows-1 && matrix[row+1][col-1].getAdjMines()==0)
            checkTile(gboard,matrix[row+1][col-1]);
        else if (row<rows-1 && col>0 && matrix[row+1][col-1].getAdjMines()>0)
            revealTile(gboard,matrix[row+1][col-1]);
        if (row>0 && matrix[row-1][col].getAdjMines()==0)
            checkTile(gboard,matrix[row-1][col]);
        else if (row>0 && matrix[row-1][col].getAdjMines()>0)
            revealTile(gboard,matrix[row-1][col]);
        if (row<rows-1 && matrix[row+1][col].getAdjMines()==0)
            checkTile(gboard,matrix[row+1][col]);
        else if (row<rows-1 && matrix[row+1][col].getAdjMines()>0)
            revealTile(gboard,matrix[row+1][col]);
        if (row>0 && col<columns-1 && matrix[row-1][col+1].getAdjMines()==0)
            checkTile(gboard,matrix[row-1][col+1]);
        else if (row>0 && col<columns-1 && matrix[row-1][col+1].getAdjMines()>0)
            revealTile(gboard,matrix[row-1][col+1]);
        if (col<columns-1 && matrix[row][col+1].getAdjMines()==0)
            checkTile(gboard,matrix[row][col+1]);
        else if (col<columns-1 && matrix[row][col+1].getAdjMines()>0)
           revealTile(gboard,matrix[row][col+1]);
        if (row<rows-1 && col<columns-1 && matrix[row+1][col+1].getAdjMines()==0)
            checkTile(gboard,matrix[row+1][col+1]);
        else if (row<rows-1 && col<columns-1 && matrix[row+1][col+1].getAdjMines()>0)
            revealTile(gboard,matrix[row+1][col+1]);
    }

    private void revealTile(Gameboard gboard, Tile tile) {
        Image tileImage = null;
        ImageView tileView;
        tile.setOpen(true);
        if (tile.isMine())
            tileImage = new Image(getClass().getResourceAsStream("/images/mine.png"));
        else if ((tile.getAdjMines()==1))
            tileImage = new Image(getClass().getResourceAsStream("/images/1.png"));
        else if ((tile.getAdjMines()==2))
            tileImage = new Image(getClass().getResourceAsStream("/images/2.png"));
        else if ((tile.getAdjMines()==3))
            tileImage = new Image(getClass().getResourceAsStream("/images/3.png"));
        else if ((tile.getAdjMines()==4))
            tileImage = new Image(getClass().getResourceAsStream("/images/4.png"));
        else if ((tile.getAdjMines()==5))
            tileImage = new Image(getClass().getResourceAsStream("/images/5.png"));
        else if ((tile.getAdjMines()==6))
            tileImage = new Image(getClass().getResourceAsStream("/images/6.png"));
        else if ((tile.getAdjMines()==7))
            tileImage = new Image(getClass().getResourceAsStream("/images/7.png"));
        else if ((tile.getAdjMines()==8))
            tileImage = new Image(getClass().getResourceAsStream("/images/8.png"));
        else if ((tile.getAdjMines()==0))
            tileImage = new Image(getClass().getResourceAsStream("/images/opentile.png"));
        tile.setOnMouseClicked(e -> {
            if ((e.getButton()==MouseButton.PRIMARY) && (e.getClickCount() == 2))
                openAdjacentTiles(gboard,tile);
        });
        tileView = new ImageView(tileImage);
        tileView.setFitHeight(35.0);
        tileView.setFitWidth(35.0);
        tile.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;" );
        tile.setGraphic(tileView);
    }

    private void openAdjacentTiles(Gameboard gboard, Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        int count = 0;
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                if (row + y >= 0 && row + y < gboard.getColumns() && col + x >= 0 && col + x < gboard.getRows() && gboard.getMatrix()[row + y][col + x].isFlag())
                    count++;
            }
        }
        if (count == tile.getAdjMines()) {
            for (int y = -1; y < 2; y++) {
                for (int x = -1; x < 2; x++) {
                    if (row + y >= 0 && row + y < gboard.getColumns() && col + x >= 0 && col + x < gboard.getRows() && !gboard.getMatrix()[row + y][col + x].isOpen())
                        checkTile(gboard, gboard.getMatrix()[row + y][col + x]);
                }
            }
        }
    }

    private void flagTile(Tile tile) {
        if (tile.isOpen()) return;
        Image tileImage;
        ImageView tileView;
        if (!tile.isFlag() && !tile.isQmark()) {
            tileImage = new Image(getClass().getResourceAsStream("/images/flag.png"));
            tile.setFlag(true);
            minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText())-1));
        }
        else if (tile.isFlag()){
            tileImage = new Image(getClass().getResourceAsStream("/images/qmark.png"));
            tile.setFlag(false);
            tile.setQmark(true);
            minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText())+1));
        }
        else {
            tileImage = new Image(getClass().getResourceAsStream("/images/tile.png"));
            tile.setQmark(false);
        }
        tileView = new ImageView(tileImage);
        tileView.setFitHeight(35.0);
        tileView.setFitWidth(35.0);
        tile.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;" );
        tile.setGraphic(tileView);
    }

    private boolean check_win(Gameboard gboard) {
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        int count = 0;

        for (int row = 0; row < rows; row++)
            for (int col = 0; col < columns; col++)
                if (gboard.getMatrix()[row][col].isOpen())
                    count++;

        System.out.println("count: " + count);
        return count == (rows*columns) - gboard.getMines();
    }

    /*
    private boolean check_win(Gameboard gboard) {
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < columns; col++)
                if (!gboard.getMatrix()[row][col].isOpen() && !gboard.getMatrix()[row][col].isMine())
                    return false;
        return true;
    }
    */

    private void gameOver(Gameboard gboard, boolean win) {
        timer.stop();
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        for (int row=0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                gboard.getMatrix()[row][col].setOpen(true);
                if (gboard.getMatrix()[row][col].isMine())
                    revealTile(gboard, gboard.getMatrix()[row][col]);
            }
        }

        if (win) {
            msgLabel.setText("YOU WON!");
            int score = Integer.parseInt(timeLabel.getText());
            if (gboard.getDifficulty().equals("easy") && score<scores[0])
                scores[0]=score;
            else if (gboard.getDifficulty().equals("normal") && score<scores[1])
                scores[1]=score;
            else if (gboard.getDifficulty().equals("hard") && score<scores[2])
                scores[2]=score;
            saveScores();
        }
        else msgLabel.setText("YOU LOST!");

        msgLabel.setLayoutX(columns*18 - 28);
        msgLabel.setVisible(true);
    }

    private void saveScores() {
        try (PrintWriter pw = new PrintWriter("scores.txt")) {
            for (int i = 0; i < 3; i++)
                pw.println(scores[i]);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadScores() throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(new File("scores.txt")))) {
            int i=0;
            String line = br.readLine();
            while (line != null) {
                scores[i] = Integer.parseInt(line);
                i++;
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Scores file not found.");
        }
    }

    @FXML
    private void scoresWindow(ActionEvent actionEvent) {
        Stage scores = new Stage();
        scores.initModality(Modality.APPLICATION_MODAL);
        scores.setTitle("Best Scores");
        scores.initStyle(StageStyle.UTILITY);
        Label easy = new Label("Easy: " + this.scores[0] + " seconds");
        Label normal = new Label("Normal: " + this.scores[1] + " seconds");
        Label hard = new Label("Hard: " + this.scores[2] + " seconds");
        Button btn = new Button ("CLOSE");
        btn.setOnAction(e->scores.close());
        VBox layout = new VBox(5);
        layout.getChildren().addAll(easy,normal,hard,btn);
        layout.setAlignment(Pos.CENTER);
        Scene scn = new Scene(layout,200,100);
        scores.setResizable(false);
        scores.setScene(scn);
        scores.showAndWait();
    }

    private int countFlags(Gameboard gboard) {
        int count=0;
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        for (int row=0; row < rows; row++)
            for (int col=0; col < columns; col++)
                if (gboard.getMatrix()[row][col].isFlag()) count++;
        return count;
    }

    private void printGameBoard (Gameboard gboard, int rows, int columns) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (gboard.getMatrix()[row][col].isMine())
                    System.out.print("| M ");
                else System.out.print("| " + gboard.getMatrix()[row][col].getAdjMines() + " ");
            }
            System.out.println("|");
        }
    }

    public void printBestScores () {
        System.out.println("Easy: " + scores[0] + " seconds");
        System.out.println("Normal: " + scores[1] + " seconds");
        System.out.println("Hard: " + scores[2] + " seconds");
    }

    public void handleQuit(ActionEvent actionEvent) {
        System.exit(0);
    }

    @FXML
    private void aboutWindow(ActionEvent actionEvent) {
        Stage about = new Stage();
        about.initModality(Modality.APPLICATION_MODAL);
        about.setTitle("About Minesweeper");
        about.initStyle(StageStyle.UTILITY);
        Label l1 = new Label("A Minesweeper clone made in Java and JavaFX");
        Label l2 = new Label("by Nikolaos Perris");
        Hyperlink email = new Hyperlink("nperris@gmail.com");
        email.setStyle("-fx-border-style: hidden;");
        Application a = new Application() {
            @Override
            public void start(Stage stage) {}
        };
        email.setOnAction(e -> a.getHostServices().showDocument("mailto:"+email.getText()));
        Label l3 = new Label("Copyright (c) 2019");
        l3.setPadding(new Insets(0,0,5,0));
        Button btn = new Button ("CLOSE");
        btn.setOnAction(e->about.close());
        VBox layout = new VBox(3);
        layout.getChildren().addAll(l1,l2,email,l3,btn);
        layout.setAlignment(Pos.CENTER);
        Scene scn = new Scene(layout,300,130);
        about.setResizable(false);
        about.setScene(scn);
        about.showAndWait();
    }

    @FXML
    private void helpWindow(ActionEvent actionEvent) {
        Stage help = new Stage();
        help.setTitle("Help");
        help.initStyle(StageStyle.UTILITY);
        Label l1 = new Label("This Minesweeper clone plays just like the classic Windows Minesweeper. Choose a difficulty level or make your own custom game and have fun!");
        l1.setWrapText(true);
        l1.setPadding(new Insets(10));
        Button btn = new Button ("CLOSE");
        btn.setOnAction(e->help.close());
        VBox layout = new VBox(3);
        layout.getChildren().addAll(l1,btn);
        layout.setAlignment(Pos.CENTER);
        Scene scn = new Scene(layout,330,140);
        help.setScene(scn);
        help.showAndWait();
    }
}