package minesweeper;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable  {

    public Label labelRows;
    public Label labelColumns;
    public Label labelMines;
    public Button createButton;
    public Button newGameButton;
    public Button easyGameButton;
    public Button normalGameButton;
    public Button hardGameButton;
    public Button customGameButton;
    public Button pauseButton;
    public GridPane gameGridPane;
    public AnchorPane mainAnchorPane;
    public Text title;
    public Label timeLabel;
    public Label tLabel;
    public Label mLabel;
    public Label minesLabel;
    public Label msgLabel;
    public ComboBox<Integer> comboRows;
    public ComboBox<Integer> comboCols;
    public ComboBox<Integer> comboMines;

    private static boolean questionMark;
    private static final Rectangle2D vBounds = Screen.getPrimary().getVisualBounds();
    private static final double TILE_SIZE = vBounds.getHeight()>1000 ? 35.0 : 27.0;
    private static int[] scores = new int[]{9999,9999,9999};       // initialize scores with default of 9999
    private static double averageTimeEasy,averageTimeNormal,averageTimeHard;
    private static int gamesPlayedEasy,gamesPlayedNormal,gamesPlayedHard;
    private static int gamesWonEasy,gamesWonNormal,gamesWonHard;
    private final IntegerProperty time = new SimpleIntegerProperty();
    private final Timeline timer = new Timeline (
            new KeyFrame(
                    Duration.seconds(1),
                    event -> time.set(time.get() + 1)
            )
    );

    @Override
    public void initialize (URL location, ResourceBundle resources){

        for (int i = 9; i <= 25; i++)
            comboRows.getItems().add(i);
        for (int i = 9; i <= 40; i++)
            comboCols.getItems().add(i);
        comboRows.setValue(9);
        comboCols.setValue(9);
        for (int i = 5; i <= 40; i++)
            comboMines.getItems().add(i);
        comboMines.setValue(10);

        comboRows.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            comboMines.getItems().clear();
            int m = comboCols.getValue()*newValue;
            for (int i = m/20; i <= m/2; i++)
                comboMines.getItems().add(i);
            comboMines.setValue(m/10);
        });

        comboCols.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            comboMines.getItems().clear();
            int m = comboRows.getValue()*newValue;
            for (int i = m/20; i <= m/2; i++)
                comboMines.getItems().add(i);
            comboMines.setValue(m/10);
        });

        mainAnchorPane.setStyle("-fx-font-family: Arial; -fx-font-size: 12");
        tLabel.setStyle("-fx-font-size: 14");
        timeLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: red");
        timer.setCycleCount(Animation.INDEFINITE);
        timeLabel.textProperty().bind(time.asString("%03d"));
        mLabel.setStyle("-fx-font-size: 14");
        minesLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold; -fx-text-fill: red");
        msgLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold");
        loadScores();
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

    public void customGame(ActionEvent actionEvent) {
        title.setVisible(false);
        easyGameButton.setVisible(false);
        normalGameButton.setVisible(false);
        hardGameButton.setVisible(false);
        customGameButton.setVisible(false);
        tLabel.setVisible(false);
        timeLabel.setVisible(false);
        mLabel.setVisible(false);
        minesLabel.setVisible(false);
        newGameButton.setVisible(false);
        pauseButton.setVisible(false);
        msgLabel.setVisible(false);
        labelRows.setVisible(true);
        labelColumns.setVisible(true);
        labelMines.setVisible(true);
        comboRows.setVisible(true);
        comboCols.setVisible(true);
        comboMines.setVisible(true);
        createButton.setVisible(true);
    }

    public void createCustomGame(ActionEvent actionEvent) {
        generateEmptyGameGrid(comboRows.getValue(),comboCols.getValue(),comboMines.getValue(),"custom");
    }

    private void generateEmptyGameGrid(int rows, int columns, int mines, String difficulty) {
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.setWidth(((TILE_SIZE+1)*columns)+20+calcDecW());
        stage.setHeight((TILE_SIZE+1)*rows+96+calcDecH());
        title.setVisible(false);
        labelRows.setVisible(false);
        labelColumns.setVisible(false);
        labelMines.setVisible(false);
        comboRows.setVisible(false);
        comboCols.setVisible(false);
        comboMines.setVisible(false);
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
        newGameButton.setLayoutX(columns*(TILE_SIZE+1)/2 - 41);
        newGameButton.setOnAction(e -> generateEmptyGameGrid(rows,columns,mines,difficulty));
        newGameButton.setVisible(true);
        pauseButton.setLayoutX(columns*(TILE_SIZE+1)/2 - 41);
        pauseButton.setVisible(true);
        msgLabel.setLayoutX(columns*(TILE_SIZE+1)/2 - 39);
        msgLabel.setVisible(false);
        for (int row=0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile tile = new Tile(row, col, 0, false, false, false, false);
                gameGridPane.add(tile, col, row);
                tile.setImage("/images/tile.png");
                tile.setOnAction(e -> generateGameGrid(rows, columns, mines, difficulty, tile.getRow(), tile.getCol()));
            }
        }
    }

    private void generateGameGrid(int rows, int columns, int mines, String difficulty, int y, int x) {
        Gameboard gboard = Gameboard.generateGameboard(rows, columns, mines, difficulty, y, x);
        gameGridPane.getChildren().clear();
        pauseButton.setOnAction(e -> pauseGame(gboard));
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile tile = gboard.getMatrix()[row][col];
                gameGridPane.add(tile, col, row);
                tile.setImage("/images/tile.png");
                tile.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY)
                        checkTile(gboard, tile);
                    else if (e.getButton() == MouseButton.SECONDARY)
                        flagTile(tile);
                });
            }
        }
        timer.play();
        checkTile(gboard,gboard.getMatrix()[y][x]);
        // the following lines enable the Shift+P 'cheat'
        mainAnchorPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.P && e.isShiftDown())
                revealBoard(gboard);
        });
        mainAnchorPane.setOnKeyReleased(e -> hideBoard(gboard));
        switch (gboard.getDifficulty()) {
            case "easy":
                gamesPlayedEasy++;
                break;
            case "normal":
                gamesPlayedNormal++;
                break;
            case "hard":
                gamesPlayedHard++;
                break;
        }
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
        tile.setOpen(true);
        if (tile.isMine())
            tile.setImage("/images/mine.png");
        else if ((tile.getAdjMines()==1))
            tile.setImage("/images/1.png");
        else if ((tile.getAdjMines()==2))
            tile.setImage("/images/2.png");
        else if ((tile.getAdjMines()==3))
            tile.setImage("/images/3.png");
        else if ((tile.getAdjMines()==4))
            tile.setImage("/images/4.png");
        else if ((tile.getAdjMines()==5))
            tile.setImage("/images/5.png");
        else if ((tile.getAdjMines()==6))
            tile.setImage("/images/6.png");
        else if ((tile.getAdjMines()==7))
            tile.setImage("/images/7.png");
        else if ((tile.getAdjMines()==8))
            tile.setImage("/images/8.png");
        else if ((tile.getAdjMines()==0))
            tile.setImage("/images/opentile.png");
        // enable the double-click to open adjacent tiles for the revealed tile
        tile.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            if ((e.getButton()==MouseButton.PRIMARY) && (e.getClickCount() == 2)
                || (e.isPrimaryButtonDown() && e.isSecondaryButtonDown()))
                openAdjacentTiles(gboard, tile);
        });
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
                        if (gboard.getMatrix()[row + y][col + x].isMine() && !gboard.getMatrix()[row + y][col + x].isFlag()) {
                            gameOver(gboard,false);
                            return;
                        }
                        else checkTile(gboard, gboard.getMatrix()[row + y][col + x]);
                    }
                }
            }
        }
    }

    private void flagTile(Tile tile) {
        if (tile.isOpen()) return;
        if (!tile.isFlag() && !tile.isQmark()) {
            tile.setImage("/images/flag.png");
            tile.setFlag(true);
            minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText())-1)); // decrease mines counter
        }
        else if (tile.isFlag() && questionMark){
            tile.setImage("/images/qmark.png");
            tile.setFlag(false);
            tile.setQmark(true);
            minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText())+1)); // increase mines counter
        }
        else if (tile.isQmark()) {
            tile.setImage("/images/tile.png");
            tile.setQmark(false);
        } else {
            tile.setImage("/images/tile.png");
            tile.setFlag(false);
            minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText())+1)); // increase mines counter
        }
    }

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
        double width = 400.0;
        double height = 120.0;
        gameover.initModality(Modality.APPLICATION_MODAL);
        gameover.initStyle(StageStyle.UTILITY);
        if (win) {
            gameover.setTitle("You won!");
            l1.setText("Congratulations! You won!");
            l1.setTextFill(Color.BLUE);
            msgLabel.setText("YOU WON!");
            msgLabel.setTextFill(Color.BLUE);
            int score = Integer.parseInt(timeLabel.getText());
            l2.setText("Your time was " + score + " seconds.");
            switch (gboard.getDifficulty()) {
                case "easy":
                    averageTimeEasy = ((averageTimeEasy*gamesWonEasy)+score)/++gamesWonEasy;
                    if (score < scores[0] && gboard.usedCheat() && !gboard.isRepeatedBoard()) {
                        highScore = true;
                        scores[0] = score;
                    }
                    bestScore = scores[0];
                    break;
                case "normal":
                    averageTimeNormal = ((averageTimeNormal*gamesWonNormal)+score)/++gamesWonNormal;
                    if (score < scores[1] && gboard.usedCheat() && !gboard.isRepeatedBoard()) {
                        highScore = true;
                        scores[1] = score;
                    }
                    bestScore = scores[1];
                    break;
                case "hard":
                    averageTimeHard = ((averageTimeHard*gamesWonHard)+score)/++gamesWonHard;
                    if (score < scores[2] && gboard.usedCheat() && !gboard.isRepeatedBoard()) {
                        highScore = true;
                        scores[2] = score;
                    }
                    bestScore = scores[2];
                    break;
            }
            if (highScore) {
                l3.setText("You have the new fastest time for this difficulty!");
            }
            else if (!gboard.getDifficulty().equals("custom")) {
                l3.setText("Best time: " + bestScore + " seconds");
            }
        }
        else {
            gameover.setTitle("You lost!");
            l1.setText("You lost!");
            l1.setTextFill(Color.RED);
            msgLabel.setText("YOU LOST!");
            msgLabel.setTextFill(Color.RED);
        }
        saveStats();
        pauseButton.setVisible(false);
        msgLabel.setVisible(true);
        Button newGameBtn = new Button("New Game");
        newGameBtn.setPrefWidth(120.0);
        newGameBtn.setOnMouseClicked(e -> {
            generateEmptyGameGrid(gboard.getRows(), gboard.getColumns(), gboard.getMines(), gboard.getDifficulty());
            gameover.close();
        });
        Button repeatGameBtn = new Button("Repeat board");
        repeatGameBtn.setPrefWidth(120.0);
        repeatGameBtn.setOnMouseClicked(e -> {
            repeatGame(gboard);
            gameover.close();
        });
        Button closeBtn = new Button ("Close");
        closeBtn.setPrefWidth(120.0);
        closeBtn.setOnMouseClicked(e -> gameover.close());
        VBox layout = new VBox(5);
        HBox buttons = new HBox (10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(newGameBtn,repeatGameBtn,closeBtn);
        layout.getChildren().addAll(l1,l2,l3,buttons);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout,width,height);
        gameover.setResizable(false);
        gameover.setScene(scene);
        Stage primaryStage = (Stage) mainAnchorPane.getScene().getWindow();
        double xPos = primaryStage.getX() + primaryStage.getWidth()/2d;
        double yPos = primaryStage.getY() + primaryStage.getHeight()/2d;
        gameover.setX(xPos - (width+calcDecW())/2);
        gameover.setY(yPos - (height+calcDecH())/2);
        gameover.showAndWait();
    }

    private void repeatGame(Gameboard gboard) {
        gboard.setRepeatedBoard(true);
        time.setValue(0);
        msgLabel.setVisible(false);
        pauseButton.setVisible(true);
        minesLabel.setText(Integer.toString(gboard.getMines()));
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        for (int row=0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile tile = gboard.getMatrix()[row][col];
                tile.setImage("/images/tile.png");
                tile.setOpen(false);
                tile.setFlag(false);
                tile.setQmark(false);
                tile.setOnMouseClicked(e -> {
                    timer.play();
                    if (e.getButton() == MouseButton.PRIMARY)
                        checkTile(gboard, tile);
                    else if (e.getButton() == MouseButton.SECONDARY)
                        flagTile(tile);
                });
            }
        }
    }

    @FXML
    private void pauseGame(Gameboard gboard) {
        timer.pause();
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        for (int row=0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                gboard.getMatrix()[row][col].setDisable(true);
                if (gboard.getMatrix()[row][col].isOpen() || gboard.getMatrix()[row][col].isFlag()) {
                    gboard.getMatrix()[row][col].setImage("/images/tile.png");
                }
            }
        }
        pauseButton.setText("Resume");
        pauseButton.setOnAction(e -> resumeGame(gboard));
    }

    private void resumeGame(Gameboard gboard) {
        timer.play();
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        for (int row=0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                gboard.getMatrix()[row][col].setDisable(false);
                if (gboard.getMatrix()[row][col].isFlag())
                    gboard.getMatrix()[row][col].setImage("/images/flag.png");
                else if (gboard.getMatrix()[row][col].isOpen()) {
                    revealTile(gboard, gboard.getMatrix()[row][col]);
                }
            }
        }
        pauseButton.setText("Pause");
        pauseButton.setOnAction(e -> pauseGame(gboard));
    }

    @FXML
    private void statsWindow(ActionEvent actionEvent) {
        Stage statistics = new Stage();
        double width = 570.0;
        double height = 170.0;
        statistics.initModality(Modality.APPLICATION_MODAL);
        statistics.setTitle("Statistics");
        statistics.initStyle(StageStyle.UTILITY);
        GridPane stats = new GridPane();
        stats.setPadding(new Insets(10, 10, 10, 10));
        stats.setVgap(5);
        stats.setHgap(15);
        stats.add(new Label("Best time"),1,0);
        stats.add(new Label("Average time"),2,0);
        stats.add(new Label("Games won"),3,0);
        stats.add(new Label("Games played"),4,0);
        stats.add(new Label("Percentage"),5,0);
        stats.add(new Label("Easy:"),0,1);
        stats.add(new Label("Normal:"),0,2);
        stats.add(new Label("Hard:"),0,3);
        Label easy = new Label(scores[0] + " secs");
        Label normal = new Label( scores[1] + " secs");
        Label hard = new Label(scores[2] + " secs");
        stats.add(easy,1,1);
        stats.add(normal,1,2);
        stats.add(hard,1,3);
        DecimalFormat df = new DecimalFormat("0.0");
        stats.add(new Text(df.format(averageTimeEasy) + " secs"),2,1);
        stats.add(new Text(df.format(averageTimeNormal) + " secs"),2,2);
        stats.add(new Text(df.format(averageTimeHard) + " secs"),2,3);
        Text gamesWonEasyText = new Text(Integer.toString(gamesWonEasy));
        stats.add(gamesWonEasyText,3,1);
        GridPane.setHalignment(gamesWonEasyText, HPos.CENTER);
        Text gamesWonNormalText = new Text(Integer.toString(gamesWonNormal));
        stats.add(gamesWonNormalText,3,2);
        GridPane.setHalignment(gamesWonNormalText, HPos.CENTER);
        Text gamesWonHardText = new Text(Integer.toString(gamesWonHard));
        stats.add(gamesWonHardText,3,3);
        GridPane.setHalignment(gamesWonHardText, HPos.CENTER);
        Text gamesPlayedEasyText = new Text(Integer.toString(gamesPlayedEasy));
        stats.add(gamesPlayedEasyText,4,1);
        GridPane.setHalignment(gamesPlayedEasyText, HPos.CENTER);
        Text gamesPlayedNormalText = new Text(Integer.toString(gamesPlayedNormal));
        stats.add(gamesPlayedNormalText,4,2);
        GridPane.setHalignment(gamesPlayedNormalText, HPos.CENTER);
        Text gamesPlayedHardText = new Text(Integer.toString(gamesPlayedHard));
        stats.add(gamesPlayedHardText,4,3);
        GridPane.setHalignment(gamesPlayedHardText, HPos.CENTER);
        double percentageEasy = gamesPlayedEasy > 0 ? (double) gamesWonEasy*100/gamesPlayedEasy : 0;
        Text percentageEasyText = new Text(df.format(percentageEasy)+"%");
        stats.add(percentageEasyText,5,1);
        GridPane.setHalignment(percentageEasyText, HPos.CENTER);
        double percentageNormal = gamesPlayedNormal > 0 ? (double) gamesWonNormal*100/gamesPlayedNormal : 0;
        Text percentageNormalText = new Text(df.format(percentageNormal)+"%");
        stats.add(percentageNormalText,5,2);
        GridPane.setHalignment(percentageNormalText, HPos.CENTER);
        double percentageHard = gamesPlayedHard > 0 ? (double) gamesWonHard*100/gamesPlayedHard : 0;
        Text percentageHardText = new Text(df.format(percentageHard)+"%");
        stats.add(percentageHardText,5,3);
        GridPane.setHalignment(percentageHardText, HPos.CENTER);
        stats.setAlignment(Pos.CENTER);
        Button resetBtn = new Button("Reset Stats");
        resetBtn.setPrefWidth(110.0);
        resetBtn.setOnAction(e -> {
            scores[0] = 9999;
            easy.setText(scores[0] + " secs");
            scores[1] = 9999;
            normal.setText(scores[1] + " secs");
            scores[2] = 9999;
            hard.setText(scores[2] + " secs");
            saveStats();
        });
        Button closeBtn = new Button ("Close");
        closeBtn.setPrefWidth(110.0);
        closeBtn.setOnAction(e -> statistics.close());
        VBox layout = new VBox(5);
        HBox buttons = new HBox (20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(resetBtn,closeBtn);
        layout.getChildren().addAll(stats,buttons);
        layout.setAlignment(Pos.CENTER);
        Scene scene = new Scene(layout,width,height);
        statistics.setResizable(false);
        statistics.setScene(scene);
        closeBtn.requestFocus();
        Stage primaryStage = (Stage) mainAnchorPane.getScene().getWindow();
        double xPos = primaryStage.getX() + primaryStage.getWidth()/2d;
        double yPos = primaryStage.getY() + primaryStage.getHeight()/2d;
        statistics.setX(xPos - (width+calcDecW())/2);
        statistics.setY(yPos - (height+calcDecH())/2);
        statistics.showAndWait();
    }

    @FXML
    private void optionsWindow(ActionEvent actionEvent) {
        Stage settings = new Stage();
        double width = 320.0;
        double height = 120.0;
        settings.initModality(Modality.APPLICATION_MODAL);
        settings.setTitle("Settings");
        settings.initStyle(StageStyle.UTILITY);
        Label l1 = new Label(" ");
        l1.setPadding(new Insets(10,10,0,10));
        CheckBox c1 = new CheckBox("Use question marks");
        c1.setSelected(questionMark);
        c1.setPadding(new Insets(10,10,10,10));
        Button okBtn = new Button ("OK");
        okBtn.setPrefWidth(100.0);
        okBtn.setOnAction(e -> {
            questionMark = c1.isSelected();
            settings.close();
        });
        Button cancelBtn = new Button ("Cancel");
        cancelBtn.setPrefWidth(100.0);
        cancelBtn.setOnAction(e -> settings.close());
        HBox buttons = new HBox (20);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(okBtn,cancelBtn);
        VBox layout = new VBox(5);
        layout.getChildren().addAll(l1,c1, buttons);
        Scene scene = new Scene(layout,width,height);
        settings.setResizable(false);
        settings.setScene(scene);
        Stage primaryStage = (Stage) mainAnchorPane.getScene().getWindow();
        double xPos = primaryStage.getX() + primaryStage.getWidth()/2d;
        double yPos = primaryStage.getY() + primaryStage.getHeight()/2d;
        settings.setX(xPos - (width+calcDecW())/2);
        settings.setY(yPos - (height+calcDecH())/2);
        settings.showAndWait();
    }

    @FXML
    private void aboutWindow(ActionEvent actionEvent) {
        Stage about = new Stage();
        double width = 320.0;
        double height = 150.0;
        about.initModality(Modality.APPLICATION_MODAL);
        about.setTitle("About Minesweeper");
        about.initStyle(StageStyle.UTILITY);
        Label l1 = new Label("A Minesweeper clone made in Java/JavaFX");
        Label l2 = new Label("by Nikolaos Perris");
        Hyperlink email = new Hyperlink("nperris@gmail.com");
        email.setStyle("-fx-border-style: hidden;");
        Application a = new Application() {
            @Override
            public void start(Stage stage) {}
        };
        email.setOnAction(e -> a.getHostServices().showDocument("mailto:"+email.getText()));
        Label l3 = new Label("© 2019");
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
        about.setX(xPos - (width+calcDecW())/2);
        about.setY(yPos - (height+calcDecH())/2);
        about.showAndWait();
    }

    @FXML
    private void helpWindow(ActionEvent actionEvent) {
        Stage help = new Stage();
        double width = 500.0;
        double height = 420.0;
        help.initModality(Modality.APPLICATION_MODAL);
        help.setTitle("Help");
        help.initStyle(StageStyle.UTILITY);
        Label l1 = new Label("Minesweeper help");
        l1.setStyle("-fx-font-family: Arial; -fx-font-size: 18");
        TextArea t1 = new TextArea(loadHelpText());
        t1.setWrapText(true);
        t1.setPrefHeight(330);
        Button closeBtn = new Button ("Close");
        closeBtn.setPrefWidth(100.0);
        closeBtn.setOnAction(e -> help.close());
        VBox layout = new VBox(10);
        layout.getChildren().addAll(l1,t1,closeBtn);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(10));
        Scene scene = new Scene(layout,width,height);
        help.setScene(scene);
        Stage primaryStage = (Stage) mainAnchorPane.getScene().getWindow();
        double xPos = primaryStage.getX() + primaryStage.getWidth()/2d;
        double yPos = primaryStage.getY() + primaryStage.getHeight()/2d;
        help.setX(xPos - (width+calcDecW())/2);
        help.setY(yPos - (height+calcDecH())/2);
        help.showAndWait();
    }

    private String loadHelpText() {
        StringBuilder helpText = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(Objects.requireNonNull(getClass().getResourceAsStream("/data/help.txt"))))) {
            String line = br.readLine();
            while (line != null) {
                helpText.append(line).append("\n");
                line = br.readLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Help file not found.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return helpText.toString();
    }

    private void saveStats() {
        try (PrintWriter pw = new PrintWriter("stats.txt")) {
            for (int i = 0; i < 3; i++) {
                pw.println(scores[i]);
            }
            pw.println(averageTimeEasy);
            pw.println(averageTimeNormal);
            pw.println(averageTimeHard);
            pw.println(gamesPlayedEasy);
            pw.println(gamesWonEasy);
            pw.println(gamesPlayedNormal);
            pw.println(gamesWonNormal);
            pw.println(gamesPlayedHard);
            pw.println(gamesWonHard);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void loadScores() {
        try (BufferedReader br = new BufferedReader(new FileReader(new File("stats.txt")))) {
            for (int i=0; i<3; i++) {
                scores[i] = Integer.parseInt(br.readLine());
            }
            averageTimeEasy = Double.parseDouble(br.readLine());
            averageTimeNormal = Double.parseDouble(br.readLine());
            averageTimeHard = Double.parseDouble(br.readLine());
            gamesPlayedEasy = Integer.parseInt(br.readLine());
            gamesWonEasy = Integer.parseInt(br.readLine());
            gamesPlayedNormal = Integer.parseInt(br.readLine());
            gamesWonNormal = Integer.parseInt(br.readLine());
            gamesPlayedHard = Integer.parseInt(br.readLine());
            gamesWonHard = Integer.parseInt(br.readLine());

        } catch (FileNotFoundException ex) {
            System.out.println("Scores file not found.");
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

    public void handleQuit(ActionEvent actionEvent) {
        System.exit(0);
    }

    /* this function is activated by Shift+P. It reveals the board to the player */
    private void revealBoard(Gameboard gboard) {
        gboard.setCheat(true);
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                if (!gboard.getMatrix()[row][col].isOpen()) {
                    revealTile(gboard, gboard.getMatrix()[row][col]);
                    gboard.getMatrix()[row][col].setOpen(false);
                }
            }
        }
    }
    /* this function hides the board again when player stops pressing Shift+P */
    private void hideBoard(Gameboard gboard) {
        int rows = gboard.getRows();
        int columns = gboard.getColumns();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Tile tile = gboard.getMatrix()[row][col];
                if (!tile.isOpen()) {
                    if (tile.isFlag())
                        tile.setImage("/images/flag.png");
                    else tile.setImage("/images/tile.png");
                    tile.setOnMouseClicked(e -> {
                        if (e.getButton() == MouseButton.PRIMARY)
                            checkTile(gboard, tile);
                        else if (e.getButton() == MouseButton.SECONDARY)
                            flagTile(tile);
                    });
                }
            }
        }
    }

}