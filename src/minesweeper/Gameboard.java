package minesweeper;

import java.util.Random;

public class Gameboard {
    private int height;
    private int width;
    private int mines;
    private Cell[][] matrix;

    private Gameboard(int height, int width, int mines, Cell[][] matrix) {
        this.setHeight(height);
        this.setWidth(width);
        this.setMines(mines);
        this.setMatrix(matrix);
    }

    public static Gameboard generateRandomGameboard(int height, int width, int mines, int y, int x) {
        Cell[][] matrix = new Cell[height][width];
        // initialize game metrix with empty cells
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                matrix[row][col] = new Cell(row,col,0,false,false,false);
            }
        }
        // put mines randomly into the matrix
        for (int i=0;i<mines;i++) {
            int row = getRandomInt(height);
            int col = getRandomInt(width);
            if (!matrix[row][col].isMine() && row!=y && row!=x) {
               // System.out.println(i + " mine in cell " + col +"," + row);
                matrix[row][col].setMine(true);
            }
            else i--;
        }
        // find the number of adjacent mines for each cell, checking all 8 directions (if possible)
        for (int row=0;row<height;row++) {
            for (int col=0;col<width;col++) {
                if (!matrix[row][col].isMine()) {
                    if (row>0 && col>0 && matrix[row-1][col-1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (col>0 && matrix[row][col-1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (row<height-1 && col>0 && matrix[row+1][col-1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (row>0 && matrix[row-1][col].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (row<height-1 && matrix[row+1][col].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (row>0 && col<width-1 && matrix[row-1][col+1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (col<width-1 && matrix[row][col+1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                    if (row<height-1 && col<width-1 && matrix[row+1][col+1].isMine()) matrix[row][col].setAdjMines(matrix[row][col].getAdjMines()+1);
                }
            }
        }
        return new Gameboard(height,width,mines,matrix);
    }

    private static int getRandomInt(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public Cell[][] getMatrix() {
        return matrix;
    }

    private void setMatrix(Cell[][] matrix) {
        this.matrix = matrix;
    }

    public int getMines() {
        return mines;
    }

    private void setMines(int mines) {
        this.mines = mines;
    }
}
