package com.mihaibojescu.solvers.branch_and_bound.math;

import java.text.MessageFormat;

public class Matrix {
    private int rows;
    private int cols;
    double[][] data;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new double[rows][cols];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                this.data[i][j] = 0;
            }
        }
    }

    public Matrix(double[] data) {
        this.data = new double[1][data.length];
        this.rows = 1;
        this.cols = data.length;

        System.arraycopy(data, 0, this.data[0], 0, this.cols);
    }

    public Matrix(double[][] data) {
        this.data = data;
        this.rows = data.length;
        this.cols = data[0].length;
    }

    public Matrix clone() {
        double[][] newData = new double[this.rows][this.cols];

        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(this.data[i], 0, newData[i], 0, this.cols);
        }

        return new Matrix(newData);
    }

    public boolean isVector() {
        return this.cols == 1 || this.rows == 1;
    }

    public double[][] toRawMatrix() {
        return this.data;
    }

    public double[] toRawVector() throws IllegalStateException {
        if (!this.isVector()) {
            throw new IllegalStateException("The matrix is not a vector");
        }

        return this.data[0];
    }

    public int getNumberOfRows() {
        return this.rows;
    }

    public int getNumberOfColumns() {
        return this.cols;
    }

    public Matrix addRow(double[] data) throws IllegalArgumentException {
        if (data.length != this.cols) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "The number of elements of the new row must match the number of columns: {0} is different than {1}",
                    data.length, this.cols - 1));
        }

        double[][] newData = new double[this.rows + 1][this.cols];

        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(this.data[i], 0, newData[i], 0, this.cols);
        }

        for (int i = 0; i < data.length; i++) {
            newData[newData.length - 1][i] = data[i];
        }

        return new Matrix(newData);
    }

    public Matrix addColumn(double[] data) throws IllegalArgumentException {
        if (data.length != this.rows) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "The number of elements of the new column must match the number of rows: {0} is different than {1}",
                    data.length, this.rows - 1));
        }

        double[][] newData = new double[this.rows][this.cols + 1];

        for (int i = 0; i < this.rows; i++) {
            System.arraycopy(this.data[i], 0, newData[i], 0, this.cols);
        }

        for (int i = 0; i < data.length; i++) {
            newData[i][newData[i].length - 1] = data[i];
        }

        return new Matrix(newData);
    }

    public int findIndexOfRow(double[] data) {
        if (data.length != this.cols) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "The number of elements of the row must match the number of columns: {0} is different than {1}",
                    data.length, this.cols - 1));
        }

        boolean exists = true;

        for (int i = 0; i < this.rows; i++) {
            exists = true;

            for (int j = 0; j < this.cols; j++) {
                if (this.data[i][j] != data[j]) {
                    exists = false;
                    break;
                }
            }

            if (exists) {
                return i;
            }
        }

        return -1;
    }

    public int findIndexOfColumn(double[] data) {
        if (data.length != this.rows) {
            throw new IllegalArgumentException(MessageFormat.format(
                    "The number of elements of the column must match the number of rows: {0} is different than {1}",
                    data.length, this.rows - 1));
        }

        boolean exists = true;

        for (int i = 0; i < this.cols; i++) {
            exists = true;

            for (int j = 0; j < this.rows; j++) {
                if (this.data[j][i] != data[j]) {
                    exists = false;
                    break;
                }
            }

            if (exists) {
                return i;
            }
        }

        return -1;
    }

    public boolean doesRowExist(double[] data) {
        return this.findIndexOfRow(data) != -1;
    }

    public boolean doesColumnExist(double[] data) {
        return this.findIndexOfColumn(data) != -1;
    }

    public double get(int row, int col) throws IllegalArgumentException {
        if (row < 0 || row >= this.rows || col < 0 || col >= this.cols) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Index out of range: ({0}, {1}) must be between (0, 0) and ({2}, {3})", row,
                            col, this.rows - 1, this.cols - 1));
        }

        return this.data[row][col];
    }

    public void set(int row, int col, double value) throws IllegalArgumentException {
        if (row < 0 || row >= this.rows || col < 0 || col >= this.cols) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Index out of range: ({0}, {1}) must be between (0, 0) and ({2}, {3})", row,
                            col, this.rows - 1, this.cols - 1));
        }

        this.data[row][col] = value;
    }

    public double[] getRow(int row) throws IllegalArgumentException {
        if (row < 0 || row >= this.rows) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Index out of range: Row {0} must be between 0 and {1}", row, this.rows - 1));
        }

        double[] result = new double[this.cols];

        for (int i = 0; i < this.cols; i++) {
            result[i] = this.data[row][i];
        }

        return result;
    }

    public double[] getColumn(int col) throws IllegalArgumentException {
        if (col < 0 || col >= this.cols) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Index out of range: Column {0} must be between 0 and {1}", col,
                            this.cols - 1));
        }

        double[] result = new double[this.rows];

        for (int i = 0; i < this.rows; i++) {
            result[i] = this.data[i][col];
        }

        return result;
    }

    public Matrix transpose() {
        double[][] newData = new double[this.cols][this.rows];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                newData[j][i] = this.data[i][j];
            }
        }

        return new Matrix(newData);
    }

    public Matrix plus(Matrix other) throws IllegalArgumentException {
        if (this.cols != other.cols || this.rows != other.rows) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Invalid matrix sizes: ({0}, {1}), ({2}, {3})", this.rows,
                            this.cols, other.rows, other.cols));
        }

        double[][] newData = new double[this.rows][this.cols];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                newData[i][j] = this.data[i][j] + other.data[i][j];
            }
        }

        return new Matrix(newData);
    }

    public Matrix minus(Matrix other) throws IllegalArgumentException {
        if (this.cols != other.cols || this.rows != other.rows) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Invalid matrix sizes: ({0}, {1}), ({2}, {3})", this.rows,
                            this.cols, other.rows, other.cols));
        }

        double[][] data = new double[this.rows][this.cols];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                data[i][j] = this.data[i][j] - other.data[i][j];
            }
        }

        return new Matrix(data);
    }

    public Matrix dot(Matrix other) throws IllegalArgumentException {
        if (this.cols != other.rows || this.rows != other.cols) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Invalid matrix sizes: ({0}, {1}), ({2}, {3})", this.rows,
                            this.cols, other.rows, other.cols));
        }

        double value = 0;

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                value += this.data[i][j] * other.data[j][i];
            }
        }

        return new Matrix(new double[] { value });
    }

    public Matrix multiplyElementWise(Matrix other) throws IllegalArgumentException {
        if (this.cols != other.cols || this.rows != other.cols) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Invalid matrix sizes: ({0}, {1}), ({2}, {3})", this.rows,
                            this.cols, other.rows, other.cols));
        }

        double[][] newData = new double[this.rows][other.cols];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                newData[i][j] = this.data[i][j] * other.data[i][j];
            }
        }

        return new Matrix(newData);
    }

    public Matrix multiplyMatrixWise(Matrix other) throws IllegalArgumentException {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException(
                    MessageFormat.format("Invalid matrix sizes: ({0}, {1}), ({2}, {3})", this.rows,
                            this.cols, other.rows, other.cols));
        }

        double[][] newData = new double[this.rows][other.cols];

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                double value = 0;

                for (int k = 0; k < this.cols; k++) {
                    value += this.data[i][k] * other.data[k][j];
                }

                newData[i][j] = value;
            }
        }

        return new Matrix(newData);
    }
}
