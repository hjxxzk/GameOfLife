package org.example;

import org.example.GUI.Board;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TXTFileReader {

    public static Board raedFile(String filePath, int threads)   {

        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);

                int row = scanner.nextInt();
                int col = scanner.nextInt();
                int iterations = scanner.nextInt();
                int size = scanner.nextInt();

                if (col < threads) {       // 1st error
                    throwError();
                }

                int[][] coordinates = new int[size][2];         // x y
                                                                // x y...
            for (int i = 0; i < size; i++) {
                coordinates[i][0] = scanner.nextInt();
                coordinates[i][1] = scanner.nextInt();

                if (coordinates[i][0] >= row || coordinates[i][1] >= col) {     // 2nd error
                    throwError();
                }
            }

            return new Board(row, col, size, coordinates, iterations);

        } catch (FileNotFoundException | InputMismatchException e)    {
            System.out.println("Incorrect input");
        }

        return null;
    }

    public static void throwError() {
        throw new IllegalArgumentException("Incorrect input");
    }
}
