package minesweeper;

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
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

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

    private static Rectangle2D vBounds = Screen.getPrimary().getVisualBounds();
    private static final double TILE_SIZE = vBounds.getHeight()>1000 ? 35.0 : 27.0;
    private DoubleProperty time = new SimpleDoubleProperty();
    private int[] scores = new int[]{9999,9999,9999};       // initialize scores with default of 9999
    private AnimationTimer timer = new AnimationTimer() {

        BooleanProperty running = new SimpleBooleanProperty();
        private long startTime;

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
        double decW = calcDecW();
        double decH = calcDecH();
        stage.setWidth(((TILE_SIZE+1)*columns)+19+decW);
        stage.setHeight((TILE_SIZE+1)*rows+96+decH);
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
        mLabel.setLayoutX(columns*(TILE_SIZE+1)-40);
        mLabel.setVisible(true);
        minesLabel.setLayoutX(columns*(TILE_SIZE+1)-30);
        minesLabel.setVisible(true);
        minesLabel.setText(Integer.toString(mines));
        msgLabel.setLayoutX(columns*(TILE_SIZE+1)/2 - 44 + decW);
        msgLabel.setVisible(false);
        newGameButton.setVisible(true);
        newGameButton.setLayoutX(columns*(TILE_SIZE+1)/2 - 44 + decW);
        newGameButton.setOnAction(e -> generateEmptyGameGrid(rows,columns,mines,difficulty));
        for (int row=0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile tile = new Tile(row, col, 0, false, false, false, false);
                gameGridPane.add(tile, col, row);
                Image tileImage = new Image(getClass().getResourceAsStream("/images/tile.png"));
                ImageView tileView = new ImageView(tileImage);
                tileView.setFitHeight(TILE_SIZE);
                tileView.setFitWidth(TILE_SIZE);
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
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile tile = gboard.getMatrix()[row][col];
                gameGridPane.add(tile, col, row);
                Image tileImage = new Image(getClass().getResourceAsStream("/images/tile.png"));
                ImageView tileView = new ImageView(tileImage);
                tileView.setFitHeight(TILE_SIZE);
                tileView.setFitWidth(TILE_SIZE);
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
        timer.start();
        checkTile(gboard,gboard.getMatrix()[y][x]);
    }

    private void checkTile(Gameboard gboard, Tile tile) {

        if (tile.isOpen() || tile.isFlag()) return;     // if the tile is already opened or is flagged, nothing happens

        revealTile(gboard, tile);   // show the tile

        if (tile.isMine()) {    // if tile contains a mine, then game over !
            gameOver(gboard,false);
            return;
        }

        if (tile.getAdjMines() > 0) {
            if (checkWin(gboard)) gameOver(gboard,true);  // check winning condition
            return;     // if tile has number in it, end function
        }

        int row = tile.getRow();
        int col = tile.getCol();
        Tile[][] matrix = gboard.getMatrix();
        int rows = gboard.getRows();
        int columns = gboard.getColumns();

        // if tile is empty, check its adjacent tiles, recursively
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                if (row+y >= 0 && row+y < rows && col+x >= 0 && col+x < columns && !matrix[row+y][col+x].isMine())
                    checkTile(gboard, matrix[row+y][col+x]);
            }
        }

        if (checkWin(gboard)) gameOver(gboard,true);    // check winning condition
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
        // enable the double-click to open adjacent tiles for the revealed tile
        tile.setOnMouseClicked(e -> {
            if ((e.getButton()==MouseButton.PRIMARY) && (e.getClickCount() == 2))
                openAdjacentTiles(gboard, tile);
        });
        tileView = new ImageView(tileImage);
        tileView.setFitHeight(TILE_SIZE);
        tileView.setFitWidth(TILE_SIZE);
        tile.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;" );
        tile.setGraphic(tileView);
    }

    private void openAdjacentTiles(Gameboard gboard, Tile tile) {
        int row = tile.getRow();
        int col = tile.getCol();
        int count = 0;
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                if (row+y >= 0 && row+y < gboard.getRows() && col+x >= 0 && col+x < gboard.getColumns() && gboard.getMatrix()[row+y][col+x].isFlag())
                    count++;
            }
        }
        if (count == tile.getAdjMines()) {
            for (int y = -1; y < 2; y++) {
                for (int x = -1; x < 2; x++) {
                    if (row+y >= 0 && row+y < gboard.getRows() && col+x >= 0 && col+x < gboard.getColumns() && !gboard.getMatrix()[row+y][col+x].isOpen()) {
                        checkTile(gboard, gboard.getMatrix()[row + y][col + x]);
                    }
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
            minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText())-1)); // decrease mines counter
        }
        else if (tile.isFlag()){
            tileImage = new Image(getClass().getResourceAsStream("/images/qmark.png"));
            tile.setFlag(false);
            tile.setQmark(true);
            minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText())+1)); // increase mines counter
        }
        else {
            tileImage = new Image(getClass().getResourceAsStream("/images/tile.png"));
            tile.setQmark(false);
        }
        tileView = new ImageView(tileImage);
        tileView.setFitHeight(TILE_SIZE);
        tileView.setFitWidth(TILE_SIZE);
        tile.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;" );
        tile.setGraphic(tileView);
    }

    /*
    // Alternative function
    private boolean checkWin(Gameboard gboard) {
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < columns; col++)
                if (!gboard.getMatrix()[row][col].isOpen() && !gboard.getMatrix()[row][col].isMine())
                    return false;
        return true;
    } */

    private boolean checkWin(Gameboard gboard) {
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        int count = 0;

        for (int row = 0; row < rows; row++)
            for (int col = 0; col < columns; col++)
                if (gboard.getMatrix()[row][col].isOpen())
                    count++;
        return count == (rows*columns) - gboard.getMines();
    }

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
        gameOverWindow(gboard,win);
    }

    @FXML
    private void gameOverWindow(Gameboard gboard, boolean win) {
        boolean highScore = false;
        int bestScore = 9999;
        Label l1 = new Label();
        Label l2 = new Label();
        Label l3 = new Label();
        Stage gameover = new Stage();
        double width = 300.0;
        double height = 120.0;
        gameover.initModality(Modality.APPLICATION_MODAL);
        gameover.initStyle(StageStyle.UTILITY);
        if (win) {
            msgLabel.setText("YOU WON!");
            gameover.setTitle("You won!");
            l1.setText("Congratulations! You won!");
            int score = Integer.parseInt(timeLabel.getText());
            l2.setText("Your time was " + score + " seconds.");
            switch (gboard.getDifficulty()) {
                case "easy":
                    if (score < scores[0]) {
                        highScore = true;
                        scores[0] = score;
                    }
                    bestScore = scores[0];
                    break;
                case "normal":
                    if (score < scores[1]) {
                        highScore = true;
                        scores[1] = score;
                    }
                    bestScore = scores[1];
                    break;
                case "hard":
                    if (score < scores[2]) {
                        highScore = true;
                        scores[2] = score;
                    }
                    bestScore = scores[2];
                    break;
            }
            if (highScore) {
                l3.setText("You have the fastest time for this difficulty!");
                saveScores();
            }
            else {
                l3.setText("Best time: " + bestScore + " seconds");
            }
        }
        else {
            msgLabel.setText("YOU LOST!");
            gameover.setTitle("You lost!");
            l1.setText("You lost!");
        }
        msgLabel.setVisible(true);
        Button newGameBtn = new Button("New Game");
        newGameBtn.setPrefWidth(100.0);
        newGameBtn.setOnMouseClicked(e -> {
            generateEmptyGameGrid(gboard.getRows(), gboard.getColumns(), gboard.getMines(), gboard.getDifficulty());
            gameover.close();
        });
        Button closeBtn = new Button ("Close");
        closeBtn.setPrefWidth(100.0);
        closeBtn.setOnMouseClicked(e -> gameover.close());
        VBox layout = new VBox(5);
        HBox buttons = new HBox (20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(newGameBtn,closeBtn);
        layout.getChildren().addAll(l1,l2,l3,buttons);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout,width,height);
        gameover.setResizable(false);
        gameover.setScene(scene);
        Stage primaryStage = (Stage) mainAnchorPane.getScene().getWindow();
        double xPos = primaryStage.getX() + primaryStage.getWidth()/2d;
        double yPos = primaryStage.getY() + primaryStage.getHeight()/2d;
        gameover.setX(xPos - width/2 - calcDecW()/2);
        gameover.setY(yPos - height/2 - calcDecH()/2);
        gameover.showAndWait();
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
        Stage bestScores = new Stage();
        double width = 240.0;
        double height = 110.0;
        bestScores.initModality(Modality.APPLICATION_MODAL);
        bestScores.setTitle("Best Scores");
        bestScores.initStyle(StageStyle.UTILITY);
        Label easy = new Label("Easy: " + scores[0] + " seconds");
        Label normal = new Label("Normal: " + scores[1] + " seconds");
        Label hard = new Label("Hard: " + scores[2] + " seconds");
        Button resetBtn = new Button("Reset Scores");
        resetBtn.setPrefWidth(100.0);
        resetBtn.setOnAction(e -> {
            scores[0] = 9999;
            easy.setText("Easy: " + scores[0] + " seconds");
            scores[1] = 9999;
            normal.setText("Normal: " + scores[1] + " seconds");
            scores[2] = 9999;
            hard.setText("Hard: " + scores[2] + " seconds");
            saveScores();
        });
        Button closeBtn = new Button ("Close");
        closeBtn.setPrefWidth(100.0);
        closeBtn.setOnAction(e -> bestScores.close());
        VBox layout = new VBox(5);
        HBox buttons = new HBox (20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(resetBtn,closeBtn);
        layout.getChildren().addAll(easy,normal,hard,buttons);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout,width,height);
        bestScores.setResizable(false);
        bestScores.setScene(scene);
        closeBtn.requestFocus();
        Stage primaryStage = (Stage) mainAnchorPane.getScene().getWindow();
        double xPos = primaryStage.getX() + primaryStage.getWidth()/2d;
        double yPos = primaryStage.getY() + primaryStage.getHeight()/2d;
        bestScores.setX(xPos - width/2 - calcDecW()/2);
        bestScores.setY(yPos - height/2 - calcDecH()/2);
        bestScores.showAndWait();
    }

    @FXML
    private void aboutWindow(ActionEvent actionEvent) {
        Stage about = new Stage();
        double width = 320.0;
        double height = 120.0;
        about.initModality(Modality.APPLICATION_MODAL);
        about.setTitle("About Minesweeper");
        about.initStyle(StageStyle.UTILITY);
        Label l1 = new Label("A Minesweeper clone made in JavaFX");
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
        Button closeBtn = new Button ("Close");
        closeBtn.setPrefWidth(100.0);
        closeBtn.setOnAction(e -> about.close());
        VBox layout = new VBox(3);
        layout.getChildren().addAll(l1,l2,email,l3,closeBtn);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout,width,height);
        about.setResizable(false);
        about.setScene(scene);
        Stage primaryStage = (Stage) mainAnchorPane.getScene().getWindow();
        double xPos = primaryStage.getX() + primaryStage.getWidth()/2d;
        double yPos = primaryStage.getY() + primaryStage.getHeight()/2d;
        about.setX(xPos - width/2 - calcDecW()/2);
        about.setY(yPos - height/2 - calcDecH()/2);
        about.showAndWait();
    }

    @FXML
    private void helpWindow(ActionEvent actionEvent) {
        Stage help = new Stage();
        double width = 330.0;
        double height = 140.0;
        help.initModality(Modality.APPLICATION_MODAL);
        help.setTitle("Help");
        help.initStyle(StageStyle.UTILITY);
        Label l1 = new Label("This Minesweeper clone plays just like the classic Windows Minesweeper. Choose a difficulty level or make your own custom game and have fun!");
        l1.setWrapText(true);
        l1.setPadding(new Insets(10));
        Button closeBtn = new Button ("Close");
        closeBtn.setPrefWidth(100.0);
        closeBtn.setOnAction(e -> help.close());
        VBox layout = new VBox(3);
        layout.getChildren().addAll(l1,closeBtn);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout,width,height);
        help.setScene(scene);
        Stage primaryStage = (Stage) mainAnchorPane.getScene().getWindow();
        double xPos = primaryStage.getX() + primaryStage.getWidth()/2d;
        double yPos = primaryStage.getY() + primaryStage.getHeight()/2d;
        help.setX(xPos - width/2 - calcDecW()/2);
        help.setY(yPos - height/2 - calcDecH()/2);
        help.showAndWait();
    }

    public void handleQuit(ActionEvent actionEvent) {
        System.exit(0);
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
}