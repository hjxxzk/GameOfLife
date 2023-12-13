package org.example;

import org.example.GUI.Board;

import java.awt.*;
import java.util.concurrent.CyclicBarrier;

public class GameOfLifeWorker implements Runnable {
    private final int startingColumn;
    private final int endingColumn;
    private final CyclicBarrier startBarrier;
    private final CyclicBarrier endBarrier;
    private final int threadNumber;
    private final Board board;
    public static final int TIME_DELAY = 1300;
    Color color;

    public GameOfLifeWorker(int startingColumn, int endingColumn, CyclicBarrier startBarrier, CyclicBarrier endBarrier, int threadNumber, Color color, Board board) {
        this.startingColumn = startingColumn;
        this.endingColumn = endingColumn;
        this.startBarrier = startBarrier;
        this.endBarrier = endBarrier;
        this.threadNumber = threadNumber;
        this.board = board;
        this.color = color;
    }

    @Override
    public void run() {

        displayStatus();

        try {
            Thread.sleep(TIME_DELAY);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (int i = 0; i < Board.ITERATIONS; i++) {

            try {
                boolean[][] newBoard = new boolean[Board.ROWS][Board.COLUMNS];
                GameOfLife(newBoard);
                startBarrier.await();
                board.nextBoard(newBoard, startingColumn, endingColumn, color);
                Thread.sleep(TIME_DELAY);
                endBarrier.await();

            } catch (Exception e)   {
                e.printStackTrace();
            }
        }
    }

    public void GameOfLife(boolean[][] newBoard)  {
        
        for (int column = startingColumn; column <= endingColumn; column++)  {
            for(int row = 0; row <= Board.ROWS - 1; row++)   {
                int numOfCells = calculateNeighbours(row, column);

                if(Board.board[row][column] && (numOfCells == 2 || numOfCells == 3))   {
                    newBoard[row][column] = true;

                } else if(!Board.board[row][column] && numOfCells == 3) {
                    newBoard[row][column] = true;

                }  else {
                    newBoard[row][column] = false;
                }
            }
        }
    }

    public int calculateNeighbours(int row, int column)    {       //dealing with edges
        int numberOfCells = 0;

        if (row == 0 && column == 0)    {                                           //00        01
            numberOfCells = checkEdges00();

        }   else if (row == 0 && column == (Board.COLUMNS - 1)) {                   //10        11
            numberOfCells = checkEdges01();

        }   else if ((row == (Board.ROWS - 1)) && column == 0)  {
            numberOfCells = checkEdges10();

        }   else if (row == (Board.ROWS - 1) && column == (Board.COLUMNS - 1))  {
            numberOfCells = checkEdges11();

        }   else {

            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = column - 1; j <= column + 1; j++) {

                    if (i < 0)  {
                        numberOfCells += checkAdditionalRow(column, Board.ROWS - 1);    //sprawdzone
                        j += 3; //skip 3 iterations

                    }   else if (i == Board.ROWS)  {
                        numberOfCells += checkAdditionalRow(column, 0);
                        j += 3; //leave the loop

                    }   else if (j < 0)    {
                        numberOfCells += checkAdditionalColumn(i, Board.COLUMNS - 1);


                    }   else if (j == Board.COLUMNS)   {
                        numberOfCells += checkAdditionalColumn(i, 0);


                    }   else if (!(i == row && j == column) && Board.board[i][j]) {
                        numberOfCells += 1;
                    }
                }
            }
        }
        return numberOfCells;
    }
    public int checkAdditionalRow(int constantColumn, int upOrDown)    {
        int cellCount = 0;

        for (int j = constantColumn - 1; j <= constantColumn + 1; j++) {

            if (Board.board[upOrDown][j])  {
                cellCount += 1;
            }
        }
        return cellCount;
    }

    public int checkAdditionalColumn(int constantRow, int upOrDown)    {
        int cellCount = 0;

            if (Board.board[constantRow][upOrDown])
                cellCount += 1;

        return cellCount;
    }

    public int checkEdges00() {
        int cellCount = 0;

        cellCount += Board.board[0][1] ? 1 : 0;
        cellCount += Board.board[1][0] ? 1 : 0;
        cellCount += Board.board[1][1] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 1][0] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 1][1] ? 1 : 0;
        cellCount += Board.board[0][Board.COLUMNS - 1] ? 1 : 0;
        cellCount += Board.board[1][Board.COLUMNS - 1] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 1][Board.COLUMNS - 1] ? 1 : 0;

        return cellCount;
    }

    public int checkEdges01() {
        int cellCount = 0;

        cellCount += Board.board[0][Board.COLUMNS - 2] ? 1 : 0;
        cellCount += Board.board[1][Board.COLUMNS - 2] ? 1 : 0;
        cellCount += Board.board[1][Board.COLUMNS - 1] ? 1 : 0;
        cellCount += Board.board[0][0] ? 1 : 0;
        cellCount += Board.board[1][0] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 1][0] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 1][Board.COLUMNS - 2] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 1][Board.COLUMNS - 1] ? 1 : 0;

        return cellCount;
    }

    public int checkEdges11() {
        int cellCount = 0;

        cellCount += Board.board[0][0] ? 1 : 0;
        cellCount += Board.board[0][Board.COLUMNS - 2] ? 1 : 0;
        cellCount += Board.board[0][Board.COLUMNS - 1] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 1][0] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 2][0] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 2][Board.COLUMNS - 1] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 2][Board.COLUMNS - 2] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 1][Board.COLUMNS - 2] ? 1 : 0;

        return cellCount;
    }

    public int checkEdges10() {
        int cellCount = 0;

        cellCount += Board.board[0][0] ? 1 : 0;
        cellCount += Board.board[0][1] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 2][0] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 2][1] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 1][1] ? 1 : 0;
        cellCount += Board.board[0][Board.COLUMNS - 1] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 1][Board.COLUMNS - 1] ? 1 : 0;
        cellCount += Board.board[Board.ROWS - 2][Board.COLUMNS - 1] ? 1 : 0;

        return cellCount;
    }

    public void displayStatus() {
        System.out.printf("tid  %d: rows:  0:%d (%d) cols:  %d:%d (%d) \n", threadNumber, Board.ROWS - 1, Board.ROWS, startingColumn, endingColumn, endingColumn - startingColumn + 1);
        }
}
