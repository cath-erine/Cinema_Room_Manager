package cinema;

import java.util.Scanner;

public class Cinema {
    private String[][] room;
    private final int rows;
    private final int cols;
    private final int total_seats;
    private final int[][] prices;
    private final int total_income;
    private int current_income = 0;
    private int purchased_tickets = 0;
    private int temp_row;

    private enum States {
        IDLE,
        BUY_CHOOSE_ROW,
        BUY_CHOOSE_COL,
        EXIT
    }

    private States state;

    private void menu(int input) {
        switch (this.state) {
            case IDLE: {
                switch (input) {
                    case 0: {
                        this.state = States.EXIT;
                        break;
                    }
                    case 1: {
                        this.printRoom();
                        this.printMenu();
                        break;
                    }
                    case 2: {
                        this.state = States.BUY_CHOOSE_ROW;
                        System.out.println("Enter a row number:");
                        break;
                    }
                    case 3: {
                        this.printStats();
                        this.printMenu();
                        break;
                    }
                }
                break;
            }
            case BUY_CHOOSE_ROW: {
                this.temp_row = input;
                System.out.println("Enter a seat number in that row:");
                this.state=States.BUY_CHOOSE_COL;
                break;
            }
            case BUY_CHOOSE_COL: {
                int seat = input;
                int row = this.temp_row;
                if (row > this.rows || seat > this.cols || row < 1 || seat < 1) {
                    System.out.println("Wrong input!");
                    System.out.println();
                    System.out.println("Enter a row number:");
                    this.state=States.BUY_CHOOSE_ROW;
                } else if (!seatEmpty(row, seat)) {
                    System.out.println("That ticket has already been purchased!");
                    System.out.println();
                    System.out.println("Enter a row number:");
                    this.state=States.BUY_CHOOSE_ROW;
                } else {
                    this.bookSeat(row, seat);
                    this.state = States.IDLE;
                    this.printMenu();
                }
                break;
            }
        }
    }

    public Cinema(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.total_seats = this.rows * this.cols;
        this.room = new String[rows][cols];
        this.prices = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                room[i][j] = "S";
                prices[i][j] = total_seats > 60 ? i + 1 > rows / 2 ? 8 : 10 : 10;
            }
        }
        this.total_income = total_seats > 60 ? cols * (rows / 2 * 10 + (rows - rows / 2) * 8) : 10 * total_seats;
        this.state = States.IDLE;
    }

    private void bookSeat(int row, int col) {
        System.out.println("Ticket price: $" + this.getPrice(row, col));
        this.room[row - 1][col - 1] = "B";
        this.purchased_tickets++;
        this.current_income += this.getPrice(row, col);
    }

    private boolean seatEmpty(int row, int col) {
        return this.room[row - 1][col - 1] == "S" ? true : false;
    }

    private int getPrice(int row, int col) {
        return this.prices[row - 1][col - 1];
    }

    private void printRoom() {
        System.out.println();
        System.out.println("Cinema:");
        System.out.println(" ");
        for (int i = -1; i < this.rows; i++) {
            System.out.print(i == -1 ? " " + " " : i + 1 + " "); //row number
            for (int j = 0; j < this.cols; j++) {
                System.out.print(i == -1 ? j + 1 + " " : this.room[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printStats() {
        System.out.println();
        System.out.println("Number of purchased tickets: " + this.purchased_tickets);
        System.out.println("Percentage: " + String.format("%.02f",(double) this.purchased_tickets / this.total_seats * 100) + "%");
        System.out.println("Current income: $" + this.current_income);
        System.out.println("Total income: $" + this.total_income);
        System.out.println();
    }

    private void printMenu() {
        System.out.println();
        System.out.println("1. Show the seats");
        System.out.println("2. Buy a ticket");
        System.out.println("3. Statistics");
        System.out.println("0. Exit");
        System.out.println();
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the number of rows:");
        int rows = sc.nextByte();
        System.out.println("Enter the number of seats in each row:");
        int cols = sc.nextByte();

        Cinema cinema = new Cinema(rows, cols);
        byte input = 0;
        cinema.printMenu();
        while (cinema.state != States.EXIT) {
            input = sc.nextByte();
            cinema.menu(input);
        }


    }
}