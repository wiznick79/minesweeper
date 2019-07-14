package minesweeper;

import java.util.Random;

class Gameboard {
    private int rows;
    private int columns;
    private int mines;
    private Tile[][] matrix;
    private String difficulty;

    private Gameboard(int rows, int columns, int mines, Tile[][] matrix, String difficulty) {
        this.setRows(rows);
        this.setColumns(columns);
        this.setMines(mines);
        this.setMatrix(matrix);
        this.setDifficulty(difficulty);
    }

    static Gameboard generateGameboard(int rows, int columns, int mines, String difficulty, int y, int x) {
        Tile[][] matrix = new Tile[rows][columns];
        // initialize game metrix with empty tiles
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                matrix[row][col] = new Tile(row,col,0,false,false,false, false);
            }
        }
        // put mines randomly into the matrix
        for (int i=0; i < mines; i++) {
            int row = getRandomInt(rows);
            int col = getRandomInt(columns);
            if (!matrix[row][col].isMine() && row!=y && row!=x)     // don't put mine on top of an existing one
                matrix[row][col].setMine(true);                     // also exclude the opening tile (first tile the player clicked)
            else i--;
        }
        // find the number of adjacent mines for each tile, checking all 8 directions (if possible)
        for (int row=0; row < rows ; row++) {
            for (int col=0;  col < columns; col++) {
                if (!matrix[row][col].isMine()) {
                    if (row>0 && col>0 && matrix[row-1][col-1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (col>0 && matrix[row][col-1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (row<rows-1 && col>0 && matrix[row+1][col-1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (row>0 && matrix[row-1][col].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (row<rows-1 && matrix[row+1][col].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (row>0 && col<columns-1 && matrix[row-1][col+1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (col<columns-1 && matrix[row][col+1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (row<rows-1 && col<columns-1 && matrix[row+1][col+1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                }
            }
        }
        return new Gameboard(rows,columns,mines,matrix,difficulty);
    }

    private static int getRandomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    int getColumns() {
        return columns;
    }

    private void setColumns(int columns) {
        this.columns = columns;
    }

    int getRows() {
        return rows;
    }

    private void setRows(int rows) {
        this.rows = rows;
    }

    Tile[][] getMatrix() {
        return matrix;
    }

    private void setMatrix(Tile[][] matrix) {
        this.matrix = matrix;
    }

    int getMines() {
        return mines;
    }

    private void setMines(int mines) {
        this.mines = mines;
    }

    String getDifficulty() { return difficulty; }

    private void setDifficulty(String difficulty) { this.difficulty = difficulty; }
}