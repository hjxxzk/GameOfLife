package org.example.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CellClickListener implements ActionListener {
    private final int row;
    private final int column;
    private final JButton button;
    private final boolean[][] board;

    public CellClickListener(int row, int column, JButton button, boolean[][] board)   {
        this.row = row;
        this.column = column;
        this.button = button;
        this.board = board;
    }

    @Override
    public void actionPerformed(ActionEvent e)  {
        board[row][column] = !board[row][column];
        button.setBackground(board[row][column] ? Color.BLACK : Color.WHITE);
    }

}
