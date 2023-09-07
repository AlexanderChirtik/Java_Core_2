package org.example;

import java.util.Random;
import java.util.Scanner;

public class GameXO {

    private static final int WIN_COUNT = 3; // Количество клеток необходимых для победы
    private static final char DOT_FIRST = 'X'; // Фишка первого игрока
    private static final char DOT_SECOND = 'O'; // Фишка второго игрока
    private static final char DOT_EMPTY = ' '; // Пустое поле
    private static final Scanner scanner = new Scanner(System.in); // Ввод данных
    private static final Random random = new Random(); // Использование случайных чисел
    private static char[][] field; // Двумерный массив для создания игрового поля
    private static int fieldSizeX; // Размер поля по оси X
    private static int fieldSizeY; // Размер поля по оси Y


    public static void main(String[] args) {
        while (true){
            initialize();
            printField();
            int choice = choiceOrder();
            if (choice == 1){
                System.out.println("Вы ходите первым");
                while (true){
                    int [] coordinatesFirst = humanTurn(DOT_FIRST);
                    printField();
                    if (checkGameState(coordinatesFirst, DOT_FIRST, "Человек победил")) break;
                    int [] coordinatesSecond = aiTurn(DOT_SECOND);
                    printField();
                    if (checkGameState(coordinatesSecond, DOT_SECOND, "Победил компьтер!")) break;
                }
            }
            else if (choice == 2){
                System.out.println("Первым ходит компьютер");
                while (true){
                    int [] coordinatesFirst = aiTurn(DOT_FIRST);
                    printField();
                    if (checkGameState(coordinatesFirst, DOT_FIRST, "Компьютер победил")) break;
                    int [] coordinatesSecond = humanTurn(DOT_SECOND);
                    printField();
                    if (checkGameState(coordinatesSecond, DOT_SECOND, "Победил человек!")) break;
                }
            }
            else {
                System.out.println("Вы ввели неправильное значение");
            }
            System.out.print("Хотите сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    /**
     * Создание игрового поля
     */
    private static void initialize(){
        fieldSizeX = 5;
        fieldSizeY = 5;
        field = new char[fieldSizeX][fieldSizeY];
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                field[x][y] = DOT_EMPTY;
            }
        }
    }

    /**
     * Отрисовка игравого поля
     */
    private static void printField(){
        System.out.print("+");
        for (int x = 0; x < fieldSizeX * 2 + 1; x++) {
            System.out.print((x % 2 == 0) ? "-" : x / 2 + 1);
        }
        System.out.println();

        for (int x = 0; x < fieldSizeX; x++) {
            System.out.print(x + 1 + "|");
            for (int y = 0; y < fieldSizeY; y++) {
                System.out.print(field[x][y] + "|");
            }
            System.out.println();
        }

        for (int x = 0; x < fieldSizeX * 2 + 2; x++) {
            System.out.print("-");
        }
        System.out.println();
    }

    /**
     * Выбор очередности (человек ходит первым или компьютер)
     * @return
     */
    private static int choiceOrder(){
        System.out.print("Хотите ходить первым или вторым (введите число 1 или 2) ");
        int order = scanner.nextInt();
        return order;
    }


    /**
     * Ход игрока и его проверка
     */
    private static int[] humanTurn(char c){
        int x,y;

        do {
            System.out.print("Введите координаты X и Y через пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        }
        while (!isCellValid(x, y) || !isCellEmpty(x, y));
        field[x][y] = c;
        int [] coordinates = {x, y};
        return coordinates;
    }

    /**
     * Проверка вбранной ячейки на пустоту
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellEmpty(int x, int y){
        return field[x][y] == DOT_EMPTY;
    }

    /**
     * Проверка на то, что в ячейка поместили валидные данные
     * @param x
     * @param y
     * @return
     */
    private static boolean isCellValid(int x, int y){
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    /**
     * Проверка хода компьютера
     */
    private static int[] aiTurn(char c){
        int x,y;
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        }
        while (!isCellEmpty(x, y));
        field[x][y] = c;
        int [] coordinates = {x, y};
        return coordinates;
    }

    /**
     * Проверка результата игры
     * @param c Проверка фишки игрока (X или O)
     * @param s Текст в случае победы
     * @return
     */
    private static boolean checkGameState(int [] coordinates, char c, String s){
        if (checkWin(coordinates)){
            System.out.println(s);
            return true;
        }
        if (checkDraw()){
            System.out.println("Ничья");
            return true;
        }

        return false; // Игра продолжается
    }

    /**
     * Проверка игрового поля на победителя.
     * @return
     */
    private static boolean checkWin(int [] coordinates){
        int winCount = WIN_COUNT;

        // проверка оси X на наличие идущих подряд одинаковых знаков в количестве WIN_COUNT
        for (int x = coordinates[0]; x == coordinates[0]; x++) {
            for (int y = 0; y < fieldSizeY - winCount; y++) {
                int count = 0;
                for (int j = y; j < y + winCount; j++) {
                    if (field[x][j] == field[x][j + 1] && field[x][j + 1] != DOT_EMPTY){
                        count++;
                        if (count == winCount - 1) return true;
                    }
                }
            }
        }

        // проверка оси Y на наличие идущих подряд одинаковых знаков в количестве WIN_COUNT
        for (int y = coordinates[1]; y == coordinates[1]; y++) {
            for (int x = 0; x < fieldSizeX - winCount; x++) {
                int count = 0;
                for (int j = x; j < x + winCount; j++) {
                    if (field[j][y] == field[j + 1][y] && field[j + 1][y] != DOT_EMPTY){
                        count++;
                        if (count == winCount - 1) return true;
                    }
                }
            }
        }

        return false;

        /*
        if (field[0][0] == c && field[0][1] == c && field[0][2] == c) return true;
        if (field[1][0] == c && field[1][1] == c && field[1][2] == c) return true;
        if (field[2][0] == c && field[2][1] == c && field[2][2] == c) return true;

        if (field[0][0] == c && field[1][0] == c && field[2][0] == c) return true;
        if (field[0][1] == c && field[1][1] == c && field[2][1] == c) return true;
        if (field[0][2] == c && field[1][2] == c && field[2][2] == c) return true;

        if (field[0][0] == c && field[1][1] == c && field[2][2] == c) return true;
        if (field[0][2] == c && field[1][1] == c && field[2][0] == c) return true;

        return false;*/
    }

    /**
     * Проверка ничьи
     * @return
     */
    private static boolean checkDraw(){
        for (int x = 0; x < fieldSizeX; x++) {
            for (int y = 0; y < fieldSizeY; y++) {
                if (isCellEmpty(x, y)) return false;
            }
        }
        return true;
    }
}