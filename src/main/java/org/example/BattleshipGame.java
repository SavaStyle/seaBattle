package org.example;

import java.util.Random;
import java.util.Scanner;

import static com.sun.tools.javac.util.StringUtils.toUpperCase;
import static org.example.Util.*;

public class BattleshipGame {
    private final char[][] playerBoard;
    private final char[][] computerBoard;
    private final int[] shipLengths;
    private final Scanner scanner;
    private final Random random;
    PlaceShips ps;

    public BattleshipGame() {
        playerBoard = new char[BOARD_SIZE][BOARD_SIZE];
        computerBoard = new char[BOARD_SIZE][BOARD_SIZE];
        shipLengths = new int[]{4, 3, 3, 2, 2, 2, 1, 1, 1, 1};
        scanner = new Scanner(System.in);
        random = new Random();
        ps = new PlaceShips();
        initializeBoard(playerBoard);
        initializeBoard(computerBoard);
    }

    private void initializeBoard(char[][] board) {
        // заполнение пустыми полями
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }
    }

    private void printBoards(char[][] playerBoard, char[][] computerBoard) {
        // печать полей игрока и компьютера
        System.out.println("  Ваше поле                       Поле компютера");
        System.out.print("  ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.print("            ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print(alph.charAt(i) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(playerBoard[i][j] + " ");
            }
            System.out.print("          ");
            System.out.print(alph.charAt(i) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (computerBoard[i][j] == SHIP_CELL) {
                    System.out.print(EMPTY_CELL + " ");
                } else {
                    System.out.print(computerBoard[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    private boolean isGameOver(char[][] board) {
        // проверка оставшихся кораблей
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == SHIP_CELL) {
                    return false;
                }
            }
        }
        return true;
    }

    private void playerTurn() {
        // ход игрока
        int x, y;
        while (true) {
            System.out.print("Введите координаты выстрела (A и 0): ");
            x = alph.indexOf(toUpperCase(scanner.next()));
            String v = scanner.next();
            try {
                y = Integer.parseInt(v);
            } catch (NumberFormatException e) {
                y = -1;
            }
            if (x < 0 || x >= BOARD_SIZE || y < 0 || y >= BOARD_SIZE) {
                System.out.println("Неверные координаты. Попробуйте еще раз.");
                continue;
            }
            if (computerBoard[x][y] == MISS_CELL || computerBoard[x][y] == HIT_CELL) {
                System.out.println("Вы уже стреляли по данным координатам, попробуйте ещё раз!");
                continue;
            }
            break;
        }

        if (computerBoard[x][y] == SHIP_CELL) {
            System.out.println("Попадание!");
            computerBoard[x][y] = HIT_CELL;
            System.out.println("Вы стреляете ещё раз!");
            printBoards(playerBoard, computerBoard);
            playerTurn();
        } else {
            System.out.println("Промах!");
            computerBoard[x][y] = MISS_CELL;
        }
    }

    private void computerTurn() {
        //ход компьютера
        int x, y;
        do {
            x = random.nextInt(BOARD_SIZE);
            y = random.nextInt(BOARD_SIZE);
        } while (playerBoard[x][y] == HIT_CELL || playerBoard[x][y] == MISS_CELL);
        System.out.println("Компьютер стреляет: " + alph.charAt(x) + " " + y);
        if (playerBoard[x][y] == SHIP_CELL) {
            System.out.println("Компьютер попал!");
            playerBoard[x][y] = HIT_CELL;
            System.out.println("Компьютер стреляет ещё раз!");
            try {
                Thread.sleep(2500);    // задержка, что бы чат не убегал сразу
            } catch (InterruptedException ex) {
            }
            printBoards(playerBoard, computerBoard);
            computerTurn();
        } else {
            System.out.println("Компьютер промахнулся!");
            playerBoard[x][y] = MISS_CELL;
        }
    }

    public void play() {
        //запуск игры
        System.out.println("Добро пожаловать в игру Морской бой!");
        while (true) {
            System.out.println("Выберите вариант расстановки кораблей: 1 - ручная, 2 - случайная");
            String k = (scanner.next());
            if (k.equals("1")) {
                ps.placeShip(playerBoard, shipLengths);
                break;
            } else if (k.equals("2")) {
                for (int length : shipLengths) {
                    ps.placeShipRandom(playerBoard, length);
                }
                break;
            } else {
                System.out.println("Ошибка ввода, попробуйте ещё раз");
            }
        }

        System.out.println("Расстановка кораблей завершена:");
        printBoards(playerBoard, computerBoard);
        ps.computerPlaceShips(computerBoard, shipLengths);
        while (!isGameOver(playerBoard) && !isGameOver(computerBoard)) {
            System.out.println("Ваш ход:");
            playerTurn();
            printBoards(playerBoard, computerBoard);

            if (isGameOver(computerBoard)) {
                System.out.println("Вы выиграли!");
                break;
            }
            System.out.println("Ход компьютера:");
            try {
                Thread.sleep(2500);
            } catch (InterruptedException ex) {
            }
            computerTurn();
            printBoards(playerBoard, computerBoard);
            if (isGameOver(playerBoard)) {
                System.out.println("Компьютер выиграл!");
                break;
            }
        }
        System.out.println("Игра завершена.");
        scanner.close();
    }
}

