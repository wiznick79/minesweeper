package minesweeper;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

public class Tile extends Button {
    private int row;
    private int col;
    private int adjMines;
    private boolean mine;
    private boolean flag;
    private boolean qmark;
    private boolean open;

    private static Rectangle2D vBounds = Screen.getPrimary().getVisualBounds();
    private static final double TILE_SIZE = vBounds.getHeight()>1000 ? 35.0 : 27.0;

    public Tile(int row, int col, int adjMines, boolean mine, boolean flag, boolean qmark, boolean open) {
        this.setRow(row);
        this.setCol(col);
        this.setAdjMines(adjMines);
        this.setMine(mine);
        this.setFlag(flag);
        this.setQmark(qmark);
        this.setOpen(open);
    }

    public void setImage(String image) {
        ImageView tileView = new ImageView(new Image(getClass().getResourceAsStream(image)));
        tileView.setFitHeight(TILE_SIZE);
        tileView.setFitWidth(TILE_SIZE);
        this.setPadding(new Insets(0));
        this.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: transparent");
        this.setGraphic(tileView);
    }

    public int getAdjMines() { return adjMines; }

    public void setAdjMines(int adjMines) { this.adjMines = adjMines; }

    public boolean isMine() { return mine; }

    public void setMine(boolean mine) { this.mine = mine; }

    public boolean isFlag() {return flag; }

    public void setFlag(boolean flag) { this.flag = flag; }

    public int getCol() { return col; }

    public void setCol(int col) { this.col = col; }

    public int getRow() { return row; }

    public void setRow(int row) { this.row = row; }

    public boolean isOpen() { return open; }

    public void setOpen(boolean open) { this.open = open; }

    public boolean isQmark() { return qmark; }

    public void setQmark(boolean qmark) { this.qmark = qmark; }
}