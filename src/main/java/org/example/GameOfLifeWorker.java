package org.example;

import org.example.GUI.Board;
import javax.swing.*;
import java.util.concurrent.CyclicBarrier;

public class GameOfLifeWorker implements Runnable {
    private final int startingColumn;
    private final int endingColumn;
    private final CyclicBarrier startBarrier;
    private final CyclicBarrier endBarrier;
    private final int threadNumber;
    private final Board board;
    public static final int TIME_DELAY = 1300;

    public GameOfLifeWorker(int startingColumn, int endingColumn, CyclicBarrier startBarrier, CyclicBarrier endBarrier, int threadNumber, Board board) {
        this.startingColumn = startingColumn;
        this.endingColumn = endingColumn;
        this.startBarrier = startBarrier;
        this.endBarrier = endBarrier;
        this.threadNumber = threadNumber;
        this.board = board;
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
                board.nextBoard(newBoard, startingColumn, endingColumn);
                board.updateUI();
                Thread.sleep(TIME_DELAY);
                endBarrier.await();

            } catch (Exception e)   {
                e.printStackTrace();
            }
        }
    }

    public void GameOfLife(boolean[][] newBoard)  {

    //    System.out.println("Thread " + threadNumber + " start Col: " + startingColumn + " endCol: " + endingColumn + " endRow: " + Board.ROWS);
        
        

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


            //   System.out.println(threadNumber + ": " + " iteracja");
            }
        }
    }

    public int calculateNeighbours(int row, int column)    {       //dealing with edges
        int numberOfCells = 0;

        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = column - 1; j <= column + 1; j++) {

                if (i >= 0 && i < Board.COLUMNS && j >= 0 && j < Board.ROWS && !(i == row && j == column) && Board.board[i][j])  {
                    numberOfCells += 1;
                }
            }
        }
   //    System.out.println("Thread: " + threadNumber + " column: " + column + " row " + row + "  cells: " + numberOfCells + Board.board[row][column]);
        return numberOfCells;
    }

    public int checkAdditional()    {

    }

    public void displayStatus() {
        System.out.printf("tid  %d: rows:  0:%d (%d) cols:  %d:%d (%d) \n", threadNumber, Board.ROWS - 1, Board.ROWS, startingColumn, endingColumn, endingColumn - startingColumn + 1);
    }
}
