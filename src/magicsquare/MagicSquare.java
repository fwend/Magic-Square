package magicsquare;

import static java.lang.Math.*;

public class MagicSquare {

    public static void main(String[] args) {
        printResult(magicSquareDoublyEven(4), 4);
        printResult(magicSquareOdd(5), 5);
        printResult(magicSquareSinglyEven(6), 6);
        printResult(magicSquareDoublyEven(8), 8);
    }

    public static int[][] magicSquareOdd(final int n) {
        if (n < 3 || n % 2 == 0)
            throw new IllegalArgumentException("base must be odd and > 2");

        int value = 0;
        int gridSize = n * n;
        int c = n / 2, r = 0;

        int[][] result = new int[n][n];

        while (++value <= gridSize) {
            result[r][c] = value;
            if (r == 0) {
                if (c == n - 1) {
                    r++;
                } else {
                    r = n - 1;
                    c++;
                }
            } else if (c == n - 1) {
                r--;
                c = 0;
            } else if (result[r - 1][c + 1] == 0) {
                r--;
                c++;
            } else {
                r++;
            }
        }
        return result;
    }

    static int[][] magicSquareDoublyEven(final int n) {
        if (n < 4 || n % 4 != 0)
            throw new IllegalArgumentException("base must be a positive "
                    + "multiple of 4");

        // pattern of count-up vs count-down zones
        int bits = 0b1001_0110_0110_1001;
        int size = n * n;
        int mult = n / 4;  // how many multiples of 4

        int[][] result = new int[n][n];

        for (int r = 0, i = 0; r < n; r++) {
            for (int c = 0; c < n; c++, i++) {
                int bitPos = c / mult + (r / mult) * 4;
                result[r][c] = (bits & (1 << bitPos)) != 0 ? i + 1 : size - i;
            }
        }
        return result;
    }

    static int[][] magicSquareSinglyEven(final int n) {
        if (n < 6 || (n - 2) % 4 != 0)
            throw new IllegalArgumentException("base must be a positive "
                    + "multiple of 4 plus 2");

        int size = n * n;
        int halfN = n / 2;
        int subSquareSize = size / 4;

        int[][] subSquare = magicSquareOdd(halfN);
        int[] quadrantFactors = {0, 2, 3, 1};
        int[][] result = new int[n][n];

        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                int quadrant = (r / halfN) * 2 + (c / halfN);
                result[r][c] = subSquare[r % halfN][c % halfN];
                result[r][c] += quadrantFactors[quadrant] * subSquareSize;
            }
        }

        int nColsLeft = halfN / 2;
        int nColsRight = nColsLeft - 1;

        for (int r = 0; r < halfN; r++)
            for (int c = 0; c < n; c++) {
                if (c < nColsLeft || c >= n - nColsRight
                        || (c == nColsLeft && r == nColsLeft)) {

                    if (c == 0 && r == nColsLeft)
                        continue;

                    int tmp = result[r][c];
                    result[r][c] = result[r + halfN][c];
                    result[r + halfN][c] = tmp;
                }
            }

        return result;
    }

    static void printResult(int[][] grid, final int n) {
        if (!check(grid, n))
            System.out.println("ERROR");

        System.out.printf("%nMagic square order: %d%n", n);
        System.out.printf("Constant: %d %n%n", (n * n + 1) * n / 2);

        int width = (int) floor(log10(n * n)) + 1;

        for (int[] row : grid) {
            for (int x : row)
                System.out.printf("%" + width + "s ", x);
            System.out.println();
        }
    }

    static boolean check(int[][] grid, final int n) {
        int sum = (n * n + 1) * n / 2;
        int sumDiagUp = 0, sumDiagDown = 0, sumRow = 0, sumCol = 0;

        for (int r = 0; r < n; r++) {

            for (int c = 0; c < n; c++) {
                sumRow += grid[r][c];
                sumCol += grid[c][r];
            }

            if (sumRow != sum || sumCol != sum)
                return false;
            
            sumRow = sumCol = 0;

            sumDiagDown += grid[r][r];
            sumDiagUp += grid[n - 1 - r][n - 1 - r];
        }
        return (sumDiagUp == sum && sumDiagDown == sum);
    }
}
