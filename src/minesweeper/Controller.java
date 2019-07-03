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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Controller implements Initializable  {

    public TextField boardHeight;
    public TextField boardWidth;
    public TextField boardMines;
    public Label labelHeight;
    public Label labelWidth;
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
        boardHeight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}?") || Integer.parseInt(newValue)>30)
                    boardHeight.setText(oldValue);
            }
        });

        boardWidth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d{0,2}?") || Integer.parseInt(newValue)>40)
                    boardWidth.setText(oldValue);
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
        System.out.println("DecW: " + dec);
        return dec;
    }

    private double calcDecH () {
        double dec;
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        Scene scene = mainAnchorPane.getScene();
        dec = stage.getHeight() - scene.getHeight();
        System.out.println("DecH: " + dec);
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
        labelHeight.setVisible(true);
        labelWidth.setVisible(true);
        labelMines.setVisible(true);
        boardHeight.setVisible(true);
        boardWidth.setVisible(true);
        boardMines.setVisible(true);
        createButton.setVisible(true);
    }

    public void createCustomGame(ActionEvent actionEvent) {
        int height,width,mines;
        if (boardHeight.getText().isEmpty()) height=9;
        else height = Integer.parseInt(boardHeight.getText());
        if (boardWidth.getText().isEmpty()) width=9;
        else width = Integer.parseInt(boardWidth.getText());
        if (boardMines.getText().isEmpty()) mines=10;
        else mines = Integer.parseInt(boardMines.getText());
        generateEmptyGameGrid(height,width,mines,"custom");
    }

    private void generateEmptyGameGrid(int height, int width, int mines, String difficulty) {
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.setWidth((36*width)+19+calcDecW());
        stage.setHeight(36*height+96+calcDecH());
        title.setVisible(false);
        labelHeight.setVisible(false);
        labelWidth.setVisible(false);
        labelMines.setVisible(false);
        boardHeight.setVisible(false);
        boardWidth.setVisible(false);
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
        mLabel.setLayoutX(width*36-40);
        mLabel.setVisible(true);
        minesLabel.setLayoutX(width*36-30);
        minesLabel.setVisible(true);
        minesLabel.setText(Integer.toString(mines));
        msgLabel.setVisible(false);
        newGameButton.setVisible(true);
        newGameButton.setLayoutX(width*18 - 28);
        newGameButton.setOnAction(e -> generateEmptyGameGrid(height,width,mines,difficulty));
        for (int row=0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = new Cell(row, col, 0, false, false, false, false);
                gameGridPane.add(cell, col, row);
                Image tile = new Image(getClass().getResourceAsStream("/images/tile.png"));
                ImageView tileV = new ImageView(tile);
                tileV.setFitHeight(35.0);
                tileV.setFitWidth(35.0);
                cell.setPadding(new Insets(0));
                cell.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                cell.setGraphic(tileV);
                cell.setOnAction(e -> generateGameGrid(height, width, mines, difficulty, cell.getRow(), cell.getCol()));
            }
        }
    }

    private void generateGameGrid(int height, int width, int mines, String difficulty, int y, int x) {
        Gameboard gboard = Gameboard.generateGameboard(height, width, mines, difficulty, y, x);
        gameGridPane.getChildren().clear();
        timeLabel.setVisible(true);
        timer.start();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell cell = gboard.getMatrix()[row][col];
                gameGridPane.add(cell, col, row);
                Image tile = new Image(getClass().getResourceAsStream("/images/tile.png"));
                ImageView tileV = new ImageView(tile);
                tileV.setFitHeight(35.0);
                tileV.setFitWidth(35.0);
                cell.setPadding(new Insets(0));
                cell.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
                cell.setGraphic(tileV);
                cell.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY)
                        checkCell(gboard, cell);
                    else if (e.getButton() == MouseButton.SECONDARY)
                        flagCell(cell);
                });
            }
        }
        checkCell(gboard,gboard.getMatrix()[y][x]);
        //printGameBoard(gboard,height,width); // for testing
    }

    private void checkCell (Gameboard gboard, Cell cell) {
        if (cell.isOpen() || cell.isFlag()) return;     // if the cell is already opened or is flagged, nothing happens

        int row = cell.getRow();
        int col = cell.getCol();

        Cell[][] matrix = gboard.getMatrix();
        int height = gboard.getHeight();
        int width = gboard.getWidth();

        showCell(gboard,cell);   // show the cell

        if (cell.isMine()) {    // if cell contains a mine, then game over !
            gameOver(gboard,false);
            return;
        }

        if (check_win(gboard)) {    // check winning condition
            gameOver(gboard,true);
            return;
        }

        if (cell.getAdjMines() > 0) return;     // if cell has number in it, end function
        // if cell is empty, check it's adjacent cells, recursively
        if (row>0 && col>0 && matrix[row-1][col-1].getAdjMines()==0)
            checkCell(gboard,matrix[row-1][col-1]);
        else if (row>0 && col>0 && matrix[row-1][col-1].getAdjMines()>0)
            showCell(gboard,matrix[row-1][col-1]);
        if (col>0 && matrix[row][col-1].getAdjMines()==0)
            checkCell(gboard,matrix[row][col-1]);
        else if (col>0 && matrix[row][col-1].getAdjMines()>0)
            showCell(gboard,matrix[row][col-1]);
        if (col>0 && row<height-1 && matrix[row+1][col-1].getAdjMines()==0)
            checkCell(gboard,matrix[row+1][col-1]);
        else if (row<height-1 && col>0 && matrix[row+1][col-1].getAdjMines()>0)
            showCell(gboard,matrix[row+1][col-1]);
        if (row>0 && matrix[row-1][col].getAdjMines()==0)
            checkCell(gboard,matrix[row-1][col]);
        else if (row>0 && matrix[row-1][col].getAdjMines()>0)
            showCell(gboard,matrix[row-1][col]);
        if (row<height-1 && matrix[row+1][col].getAdjMines()==0)
            checkCell(gboard,matrix[row+1][col]);
        else if (row<height-1 && matrix[row+1][col].getAdjMines()>0)
            showCell(gboard,matrix[row+1][col]);
        if (row>0 && col<width-1 && matrix[row-1][col+1].getAdjMines()==0)
            checkCell(gboard,matrix[row-1][col+1]);
        else if (row>0 && col<width-1 && matrix[row-1][col+1].getAdjMines()>0)
            showCell(gboard,matrix[row-1][col+1]);
        if (col<width-1 && matrix[row][col+1].getAdjMines()==0)
            checkCell(gboard,matrix[row][col+1]);
        else if (col<width-1 && matrix[row][col+1].getAdjMines()>0)
           showCell(gboard,matrix[row][col+1]);
        if (row<height-1 && col<width-1 && matrix[row+1][col+1].getAdjMines()==0)
            checkCell(gboard,matrix[row+1][col+1]);
        else if (row<height-1 && col<width-1 && matrix[row+1][col+1].getAdjMines()>0)
            showCell(gboard,matrix[row+1][col+1]);
    }

    private void showCell(Gameboard gboard, Cell cell) {
        Image tile = null;
        ImageView tileV;
        cell.setOpen(true);
        if (cell.isMine())
            tile = new Image(getClass().getResourceAsStream("/images/mine.png"));
        else if ((cell.getAdjMines()==1))
            tile = new Image(getClass().getResourceAsStream("/images/1.png"));
        else if ((cell.getAdjMines()==2))
            tile = new Image(getClass().getResourceAsStream("/images/2.png"));
        else if ((cell.getAdjMines()==3))
            tile = new Image(getClass().getResourceAsStream("/images/3.png"));
        else if ((cell.getAdjMines()==4))
            tile = new Image(getClass().getResourceAsStream("/images/4.png"));
        else if ((cell.getAdjMines()==5))
            tile = new Image(getClass().getResourceAsStream("/images/5.png"));
        else if ((cell.getAdjMines()==6))
            tile = new Image(getClass().getResourceAsStream("/images/6.png"));
        else if ((cell.getAdjMines()==7))
            tile = new Image(getClass().getResourceAsStream("/images/7.png"));
        else if ((cell.getAdjMines()==8))
            tile = new Image(getClass().getResourceAsStream("/images/8.png"));
        else if ((cell.getAdjMines()==0))
            tile = new Image(getClass().getResourceAsStream("/images/opentile.png"));
        cell.setOnMouseClicked(e -> {
            if ((e.getButton()==MouseButton.PRIMARY) && (e.getClickCount() == 2))
                openAdjacentTiles(gboard,cell);
        });
        tileV = new ImageView(tile);
        tileV.setFitHeight(35.0);
        tileV.setFitWidth(35.0);
        cell.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;" );
        cell.setGraphic(tileV);
    }

    private void openAdjacentTiles(Gameboard gboard, Cell cell) {
        int row = cell.getRow();
        int col = cell.getCol();
        int count = 0;
        for (int y = -1; y < 2; y++) {
            for (int x = -1; x < 2; x++) {
                if (row + y >= 0 && row + y < gboard.getWidth() && col + x >= 0 && col + x < gboard.getHeight() && gboard.getMatrix()[row + y][col + x].isFlag())
                    count++;
            }
        }
        if (count == cell.getAdjMines()) {
            for (int y = -1; y < 2; y++) {
                for (int x = -1; x < 2; x++) {
                    if (row + y >= 0 && row + y < gboard.getWidth() && col + x >= 0 && col + x < gboard.getHeight() && !gboard.getMatrix()[row + y][col + x].isOpen())
                        checkCell(gboard, gboard.getMatrix()[row + y][col + x]);
                }
            }
        }
    }

    private void flagCell(Cell cell) {
        if (cell.isOpen()) return;
        Image tile;
        ImageView tileV;
        if (!cell.isFlag() && !cell.isQmark()) {
            tile = new Image(getClass().getResourceAsStream("/images/flag.png"));
            cell.setFlag(true);
            minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText())-1));
        }
        else if (cell.isFlag()){
            tile = new Image(getClass().getResourceAsStream("/images/qmark.png"));
            cell.setFlag(false);
            cell.setQmark(true);
            minesLabel.setText(Integer.toString(Integer.parseInt(minesLabel.getText())+1));
        }
        else {
            tile = new Image(getClass().getResourceAsStream("/images/tile.png"));
            cell.setQmark(false);
        }
        tileV = new ImageView(tile);
        tileV.setFitHeight(35.0);
        tileV.setFitWidth(35.0);
        cell.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;" );
        cell.setGraphic(tileV);
    }

    private boolean check_win(Gameboard gboard) {
        int height = gboard.getHeight();
        int width = gboard.getWidth();
        int count = 0;

        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                if (gboard.getMatrix()[row][col].isOpen())
                    count++;

        return count == (height*width) - gboard.getMines();
    }

    /* this function rarely was not detecting the win condition
    private boolean check_win(Gameboard gboard) {
        int height = gboard.getHeight();
        int width = gboard.getWidth();
        for (int row = 0; row < height; row++)
            for (int col = 0; col < width; col++)
                if (!gboard.getMatrix()[row][col].isOpen() && !gboard.getMatrix()[row][col].isMine())
                    return false;
        return true;
    }
    */

    private void gameOver(Gameboard gboard, boolean win) {
        timer.stop();
        int height = gboard.getHeight();
        int width = gboard.getWidth();
        for (int row=0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                gboard.getMatrix()[row][col].setOpen(true);
                if (gboard.getMatrix()[row][col].isMine())
                    showCell(gboard, gboard.getMatrix()[row][col]);
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

        msgLabel.setLayoutX(width*18 - 28);
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
        int height = gboard.getHeight();
        int width = gboard.getWidth();
        for (int row=0; row < height; row++)
            for (int col=0; col < width; col++)
                if (gboard.getMatrix()[row][col].isFlag()) count++;
        return count;
    }

    private void printGameBoard (Gameboard gboard, int height, int width) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
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
