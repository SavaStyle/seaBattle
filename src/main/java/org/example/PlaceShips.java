package org.example;

import java.util.Random;
import java.util.Scanner;

import static com.sun.tools.javac.util.StringUtils.toUpperCase;
import static org.example.Util.*;

public class PlaceShips {

    private final Random random = new Random();
    private final Scanner scanner = new Scanner(System.in);

    void placeShip(char[][] board, int[] shipLengths) {
        // ручная расстановка кораблей
        int x, y;
        int k = 0;
        boolean horizontal = true;
        for (int length : shipLengths) {
            boolean placed = false;
            while (!placed) {
                System.out.println("Осталось расставить " + (shipLengths.length - k) + " кораблей");
                System.out.println("Укажите начальную точку расположения " + length + " палубного корабля: ");
                x = alph.indexOf(toUpperCase(scanner.next()));
                String v = scanner.next();
                try {
                    y = Integer.parseInt(v);
                } catch (NumberFormatException e) {
                    y = -1;
                }
                if (length > 1) {
                    System.out.println("Вертикальное расположение - 1, Горизонтальное расположение - 2.");
                    while (true) {
                        String h = scanner.next();
                        if (h.equals("1")) {
                            break;
                        } else if (h.equals("2")) {
                            horizontal = false;
                            break;
                        } else {
                            System.out.println("Ошибка ввода, попробуйте ещё раз.");
                        }
                    }
                }
                placed = isPlaced(board, length, placed, x, y, horizontal);
                if (!placed) {
                    System.out.println("Данные координаты недопустимы либо заняты, попробуйте ещё раз.");
                } else {
                    printBoard(board);
                    k++;
                }
            }
        }
        scanner.close();
    }

    void placeShipRandom(char[][] board, int length) {
        // случайная расстановка кораблей
        boolean placed = false;
        while (!placed) {
            int x = random.nextInt(BOARD_SIZE);
            int y = random.nextInt(BOARD_SIZE);
            boolean horizontal = random.nextBoolean();
            placed = isPlaced(board, length, placed, x, y, horizontal);
        }
    }

    private boolean isPlaced(char[][] board, int length, boolean placed, int x, int y, boolean horizontal) {
        //проверка доступности места расположения и отрисовка корабля
        //выносил дублирующийся код
        if (isValidPlacement(board, x, y, length, horizontal)) {
            if (horizontal) {
                for (int i = x; i < x + length; i++) {
                    board[i][y] = SHIP_CELL;
                }
            } else {
                for (int i = y; i < y + length; i++) {
                    board[x][i] = SHIP_CELL;
                }
            }
            placed = true;
        }
        return placed;
    }

    boolean checkNear(char[][] board, int x, int y) {
       /* проверка кораблей на соседних клетках
       вижу что получилось не красиво и местами проверки перекрываются
       но как ещё проверить наличие соседних кораблей не выходя за границы массива я не придумал, буду благодарен за совет*/
        StringBuilder sb = new StringBuilder();
        if (x == 0 && y == 0) {
            sb.append((board[x][y]));
            sb.append((board[x][y + 1]));
            sb.append((board[x + 1][y]));
            sb.append((board[x + 1][y + 1]));
        }
        if (x == BOARD_SIZE - 1 && y == BOARD_SIZE - 1) {
            sb.append((board[x - 1][y - 1]));
            sb.append((board[x - 1][y]));
            sb.append((board[x][y - 1]));
            sb.append((board[x][y]));
        }
        if (x == BOARD_SIZE - 1 && y != BOARD_SIZE - 1 && y != 0) {
            sb.append((board[x - 1][y - 1]));
            sb.append((board[x - 1][y]));
            sb.append((board[x - 1][y + 1]));
            sb.append((board[x][y - 1]));
            sb.append((board[x][y]));
            sb.append((board[x][y + 1]));
        }
        if (x == 0 && y != BOARD_SIZE - 1 && y != 0) {
            sb.append((board[x][y - 1]));
            sb.append((board[x][y]));
            sb.append((board[x][y + 1]));
            sb.append((board[x + 1][y - 1]));
            sb.append((board[x + 1][y]));
            sb.append((board[x + 1][y + 1]));
        }
        if (x != BOARD_SIZE - 1 && x != 0 && y == BOARD_SIZE - 1) {
            sb.append((board[x - 1][y - 1]));
            sb.append((board[x - 1][y]));
            sb.append((board[x][y - 1]));
            sb.append((board[x][y]));
            sb.append((board[x + 1][y - 1]));
            sb.append((board[x + 1][y]));
        }
        if (x != BOARD_SIZE - 1 && x != 0 && y == 0) {
            sb.append((board[x - 1][y]));
            sb.append((board[x - 1][y + 1]));
            sb.append((board[x][y]));
            sb.append((board[x][y + 1]));
            sb.append((board[x + 1][y]));
            sb.append((board[x + 1][y + 1]));
        }
        if (x != BOARD_SIZE - 1 && y != BOARD_SIZE - 1 && x != 0 && y != 0) {
            sb.append((board[x - 1][y - 1]));
            sb.append((board[x - 1][y]));
            sb.append((board[x - 1][y + 1]));
            sb.append((board[x][y - 1]));
            sb.append((board[x][y]));
            sb.append((board[x][y + 1]));
            sb.append((board[x + 1][y - 1]));
            sb.append((board[x + 1][y]));
            sb.append((board[x + 1][y + 1]));
        }
        return sb.toString().contains(SHIP_CELL.toString());
    }

    boolean checkBoard(int x, int y) {
        // проверка расположения корабля в границах массива
        return x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE;
    }

    boolean isValidPlacement(char[][] board, int x, int y, int length, boolean horizontal) {
        //проверка доступности места расположения
        boolean valid = false;
        int endX = x;
        int endY = y;
        if (horizontal) {
            endX += length - 1;
        } else {
            endY += length - 1;
        }
        switch (length) {
            case (4):
                if (checkBoard(x, y) && checkBoard(endX, endY)) {
                    valid = true;
                }
                break;
            case (3):
            case (2):
                if (checkBoard(x, y) && checkBoard(endX, endY) && (!checkNear(board, x, y)) && (!checkNear(board, endX, endY))) {
                    valid = true;
                }
                break;
            case (1):
                if (checkBoard(x, y) && !checkNear(board, x, y)) {
                    valid = true;
                }
                break;
        }
        return valid;
    }

    void computerPlaceShips(char[][] computerBoard, int[] shipLengths) {
        //расстановка кораблей компьютера
        for (int length : shipLengths) {
            placeShipRandom(computerBoard, length);
        }
    }

    private void printBoard(char[][] board) {
        // печать поля игрока (при ручной расстановке кораблей)
        System.out.print("  ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(alph.charAt(i) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
}
