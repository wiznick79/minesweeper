package minesweeper;

import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Screen;

import java.util.Objects;

class Tile extends Button {
    private int row;
    private int col;
    private int adjMines;
    private boolean mine;
    private boolean flag;
    private boolean qmark;
    private boolean open;

    private static final Rectangle2D vBounds = Screen.getPrimary().getVisualBounds();
    private static final double TILE_SIZE = vBounds.getHeight()>1000 ? 35.0 : 27.0;

    Tile(int row, int col, int adjMines, boolean mine, boolean flag, boolean qmark, boolean open) {
        this.setRow(row);
        this.setCol(col);
        this.setAdjMines(adjMines);
        this.setMine(mine);
        this.setFlag(flag);
        this.setQmark(qmark);
        this.setOpen(open);
    }

    /**
     *
     * @param image
     */
    void setImage(String image) {
        ImageView tileView = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream(image))));
        tileView.setFitHeight(TILE_SIZE);
        tileView.setFitWidth(TILE_SIZE);
        this.setPadding(new Insets(0));
        this.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent; -fx-background-color: transparent");
        this.setGraphic(tileView);
    }

    int getAdjMines() { return adjMines; }

    void setAdjMines(int adjMines) { this.adjMines = adjMines; }

    boolean isMine() { return mine; }

    void setMine(boolean mine) { this.mine = mine; }

    boolean isFlag() {return flag; }

    void setFlag(boolean flag) { this.flag = flag; }

    int getCol() { return col; }

    private void setCol(int col) { this.col = col; }

    int getRow() { return row; }

    private void setRow(int row) { this.row = row; }

    boolean isOpen() { return open; }

    void setOpen(boolean open) { this.open = open; }

    boolean isQmark() { return qmark; }

    void setQmark(boolean qmark) { this.qmark = qmark; }
}