package minesweeper;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class Cell extends Button {
    private int row;
    private int col;
    private int adjMines;
    private boolean mine;
    private boolean flag;
    private boolean open;

    public Cell(int row, int col, int adjMines, boolean mine, boolean flag, boolean open) {
        this.setRow(row);
        this.setCol(col);
        this.setAdjMines(adjMines);
        this.setMine(mine);
        this.setFlag(flag);
        this.setOpen(open);
    }

    public Cell(String s, int row, int col, int adjMines, boolean mine, boolean flag, boolean open) {
        super(s);
        this.setRow(row);
        this.setCol(col);
        this.setAdjMines(adjMines);
        this.setMine(mine);
        this.setFlag(flag);
        this.setOpen(open);
    }

    public Cell(String s, Node node, int row, int col, int adjMines, boolean mine, boolean flag, boolean open) {
        super(s, node);
        this.setRow(row);
        this.setCol(col);
        this.setAdjMines(adjMines);
        this.setMine(mine);
        this.setFlag(flag);
        this.setOpen(open);
    }

    public int getAdjMines() {
        return adjMines;
    }

    public void setAdjMines(int adjMines) {
        this.adjMines = adjMines;
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public int getCol() { return col; }

    public void setCol(int col) { this.col = col; }

    public int getRow() { return row; }

    public void setRow(int row) { this.row = row; }

    public boolean isOpen() { return open; }

    public void setOpen(boolean open) { this.open = open; }
}
