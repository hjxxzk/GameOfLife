package org.example;

public class ValidateFile {
    private final int numberOfThreads;

    public ValidateFile(int numberOfThreads)    {
        this.numberOfThreads = numberOfThreads;
    }

    public boolean validate(int columns)    {

        return columns < numberOfThreads;
    }

}
