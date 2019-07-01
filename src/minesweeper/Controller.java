package minesweeper;

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
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
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
    public GridPane gameGridPane;
    public AnchorPane mainAnchorPane;
    public VBox mainVBox;
    public Label timeLabel;
    public Label tLabel;
    public Label mLabel;

    private DoubleProperty time = new SimpleDoubleProperty();

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

        timeLabel.textProperty().bind(time.asString("%.0f"));
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

    public void easyGame(ActionEvent actionEvent) {
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.setWidth(359);
        stage.setHeight(459);
        generateEmptyGameGrid(9,9,10);
    }

    public void normalGame(ActionEvent actionEvent) {
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.setWidth(611);
        stage.setHeight(711);
        generateEmptyGameGrid(16,16,40);
    }

    public void hardGame(ActionEvent actionEvent) {
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.setWidth(935);
        stage.setHeight(855);
        generateEmptyGameGrid(20,25,99);
    }

    public void customGame (ActionEvent actionEvent) {
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
        labelHeight.setVisible(false);
        labelWidth.setVisible(false);
        labelMines.setVisible(false);
        boardHeight.setVisible(false);
        boardWidth.setVisible(false);
        boardMines.setVisible(false);
        createButton.setVisible(false);
        Stage stage = (Stage) mainAnchorPane.getScene().getWindow();
        stage.setWidth(width*36+35);
        stage.setHeight(height*36+135);
        generateEmptyGameGrid(height,width,mines);
    }

    private void generateEmptyGameGrid(int height, int width, int mines) {
        gameGridPane.getChildren().clear();
        timeLabel.setVisible(false);
        newGameButton.setVisible(true);
        newGameButton.setLayoutX(width*18 - 28);
        newGameButton.setOnAction(e -> generateEmptyGameGrid(height,width,mines));
        for (int row=0; row < height; row++)
            for (int col=0; col < width; col++) {
                Cell cell = new Cell(row,col,0,false,false,false);
                gameGridPane.add(cell,col,row);
                Image tile = new Image(getClass().getResourceAsStream("/images/tile.png"));
                ImageView tileV = new ImageView(tile);
                tileV.setFitHeight(35.0);
                tileV.setFitWidth(35.0);
                cell.setPadding(new Insets(0));
                cell.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;" );
                cell.setGraphic(tileV);
                cell.setOnAction(e -> generateGameGrid(height, width, mines, cell.getRow(), cell.getCol()));
            }
    }

    private void generateGameGrid(int height, int width, int mines,int y, int x) {
        Gameboard gboard = Gameboard.generateGameboard(height,width,mines,y,x);
        gameGridPane.getChildren().clear();
        tLabel.setVisible(true);
        timeLabel.setVisible(true);
        timer.start();
        for (int row=0; row < height; row++)
            for (int col=0; col < width; col++) {
                Cell cell = gboard.getMatrix()[row][col];
                gameGridPane.add(cell,col,row);
                Image tile = new Image(getClass().getResourceAsStream("/images/tile.png"));
                ImageView tileV = new ImageView(tile);
                tileV.setFitHeight(35.0);
                tileV.setFitWidth(35.0);
                cell.setPadding(new Insets(0));
                cell.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;" );
                cell.setGraphic(tileV);
                cell.setOnMouseClicked(e -> {
                    if (e.getButton() == MouseButton.PRIMARY)
                        checkCell(gboard, cell.getRow(), cell.getCol());
                    else if (e.getButton() == MouseButton.SECONDARY)
                        flagCell(gboard, cell.getRow(), cell.getCol());
                });
            }
        checkCell(gboard,y,x);
        //printGameBoard(gboard,height,width); // for testing
    }

    private void checkCell (Gameboard gboard, int row, int col) {
        Cell cell = gboard.getMatrix()[row][col];

        if (cell.isOpen() || cell.isFlag()) return;     // if the cell is already opened or is flagged, nothing happens

        Cell[][] matrix = gboard.getMatrix();
        int height = gboard.getHeight();
        int width = gboard.getWidth();

        if (cell.isMine()) {    // if cell contains a mine, then game over !
            gameover(gboard);
            return;
        }

        showCell(gboard,row,col);   // show the cell

        if (check_win(gboard)) {    // check winning condition
            timer.stop();
            System.out.println("Malaka you won!! Your time was " + timeLabel.getText() + " seconds.");
            for (int i=0; i < gboard.getHeight(); i++) {
                for (int j = 0; j < gboard.getWidth(); j++) {
                    gboard.getMatrix()[i][j].setOpen(true);
                }
            }
        }

        if (cell.getAdjMines() > 0) return;     // if cell has number in it, end function
        // if cell is empty, check it's adjacent cells, recursively
        if (row>0 && col>0 && matrix[row-1][col-1].getAdjMines()==0)
            checkCell(gboard,row-1,col-1);
        else if (row>0 && col>0 && matrix[row-1][col-1].getAdjMines()>0)
            showCell(gboard,row-1,col-1);
        if (col>0 && matrix[row][col-1].getAdjMines()==0)
            checkCell(gboard,row,col-1);
        else if (col>0 && matrix[row][col-1].getAdjMines()>0)
            showCell(gboard,row,col-1);
        if (col>0 && row<height-1 && matrix[row+1][col-1].getAdjMines()==0)
            checkCell(gboard,row+1,col-1);
        else if (row<height-1 && col>0 && matrix[row+1][col-1].getAdjMines()>0)
            showCell(gboard,row+1,col-1);
        if (row>0 && matrix[row-1][col].getAdjMines()==0)
            checkCell(gboard,row-1,col);
        else if (row>0 && matrix[row-1][col].getAdjMines()>0)
            showCell(gboard,row-1,col);
        if (row<height-1 && matrix[row+1][col].getAdjMines()==0)
            checkCell(gboard,row+1,col);
        else if (row<height-1 && matrix[row+1][col].getAdjMines()>0)
            showCell(gboard,row+1,col);
        if (row>0 && col<width-1 && matrix[row-1][col+1].getAdjMines()==0)
            checkCell(gboard,row-1,col+1);
        else if (row>0 && col<width-1 && matrix[row-1][col+1].getAdjMines()>0)
            showCell(gboard,row-1,col+1);
        if (col<width-1 && matrix[row][col+1].getAdjMines()==0)
            checkCell(gboard,row,col+1);
        else if (col<width-1 && matrix[row][col+1].getAdjMines()>0)
           showCell(gboard,row,col+1);
        if (row<height-1 && col<width-1 && matrix[row+1][col+1].getAdjMines()==0)
            checkCell(gboard,row+1,col+1);
        else if (row<height-1 && col<width-1 && matrix[row+1][col+1].getAdjMines()>0)
            showCell(gboard,row+1,col+1);
    }

    private void showCell(Gameboard gboard, int row, int col) {
        Cell cell = gboard.getMatrix()[row][col];
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
        tileV = new ImageView(tile);
        tileV.setFitHeight(35.0);
        tileV.setFitWidth(35.0);
        cell.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;" );
        cell.setGraphic(tileV);
    }

    private void flagCell(Gameboard gboard, int row, int col)  {
        Cell cell = gboard.getMatrix()[row][col];
        if (cell.isOpen()) return;
        Image tile = null;
        ImageView tileV;
        if (!cell.isFlag()) {
            tile = new Image(getClass().getResourceAsStream("/images/flag.png"));
            cell.setFlag(true);
        }
        else {
            tile = new Image(getClass().getResourceAsStream("/images/tile.png"));
            cell.setFlag(false);
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
        for (int i=0; i < height; i++)
            for (int j=0; j < width; j++)
                if (!gboard.getMatrix()[i][j].isOpen() && !gboard.getMatrix()[i][j].isMine())
                    return false;
        return true;
    }

    private void gameover(Gameboard gboard) {
        timer.stop();
        System.out.println("YOU CLICKED ON A MINE! GAME OVER CARALHO!");
        int height = gboard.getHeight();
        int width = gboard.getWidth();

        for (int i=0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                gboard.getMatrix()[i][j].setOpen(true);
                if (gboard.getMatrix()[i][j].isMine())
                    showCell(gboard, i, j);
            }
        }
    }

    private void printGameBoard (Gameboard gboard, int height, int width) {
        for (int row=0; row < height; row++) {
            for (int col=0; col < width; col++) {
                if (gboard.getMatrix()[row][col].isMine())
                    System.out.print("| M ");
                else System.out.print("| " + gboard.getMatrix()[row][col].getAdjMines() + " ");
            }
            System.out.println("|");
        }
    }

    public void handleQuit(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void aboutWindow(ActionEvent actionEvent) {
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

    public void helpWindow(ActionEvent actionEvent) {
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
