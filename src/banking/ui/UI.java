package banking.ui;

import java.util.Scanner;

import banking.logic.Banking;
import banking.domain.*;

import java.sql.*;

public class UI {
    private Banking banking;

    public void start(Scanner scanner) {
        this.banking = new Banking();
        getDatabase(banking);
        while (true) {
            System.out.println("1. Create an account\n2. Log into account\n0. Exit");
            int cmd = Integer.valueOf(scanner.nextLine());
            System.out.println();
            if (cmd == 1) {
                CreditCard card = banking.createAccount();
                System.out.println("Your card has been created");
                System.out.println("Your card number:\n" + card.getCardNumber());
                System.out.println("Your card PIN:\n" + card.getPin());
                connect(card.getCardNumber(), card.getPin());
                getDatabase(banking);
                System.out.println();

            } else if (cmd == 2) {
                System.out.println("Enter your card number:");
                String cardNumber = scanner.nextLine();
                System.out.println("Enter your PIN:");
                String pin = scanner.nextLine();
                System.out.println();
                CreditCard card = banking.logIntoAccount(cardNumber, pin);
                if (card == null) {
                    System.out.println("Wrong card number or PIN!");
                    System.out.println();
                    continue;
                } else {
                    System.out.println("You have successfully logged in!\n");
                    while (true) {

                        System.out.println("1. Balance\n2. Add income\n3. Do transfer\n4. Close account\n5. Log out\n0. Exit");
                        int command = Integer.valueOf(scanner.nextLine());
                        System.out.println();
                        if (command == 1) {
                            System.out.println("Balance: " + getBalance(card));
                            getDatabase(banking);
                            System.out.println();
                        } else if (command == 2) {
                            System.out.println("Enter income: ");
                            int income = Integer.valueOf(scanner.nextLine());
                            setBalance(card, income, 1);
                            getDatabase(banking);
                            System.out.println("Income was added!\n");
                        } else if (command == 3) {
                            System.out.println("Transfer\nEnter card number:");
                            String number = scanner.nextLine();
                            if (number.length() < 16 || number.length() > 16) {
                                System.out.println("Probably you made a mistake in the card number. Please try again!\n");
                                continue;
                            } else {
                                if (banking.logIntoAccount(number) == null) {
                                    System.out.println("Such a card does not exist.\n");
                                    continue;
                                } else {
                                    System.out.println("Enter how much money you want to transfer:");
                                    double money = Double.valueOf(scanner.nextLine());
                                    if (money <= getBalance(card)) {
                                        setBalance(card, money, 2);
                                        setBalance(banking.logIntoAccount(number), money, 1);
                                        getDatabase(banking);
                                        continue;
                                    } else {
                                        System.out.println("Not enough money!\n");
                                        continue;
                                    }
                                }
                            }
                        } else if (command == 4) {
                            deleteCard(card);
                            System.out.println("The account has been closed!\n");
                            getDatabase(banking);
                        } else if (command == 5) {
                            System.out.println("You have successfully logged out!");
                            System.out.println();
                            getDatabase(banking);
                            break;
                        } else if (command == 0) {
                            System.out.println("Bye");
                            System.exit(0);
                        }

                    }
                }
            } else if (cmd == 0) {
                System.out.println("Bye!");
                System.exit(0);
            }
        }
    }

    public static void connect(String cardNumber, String pin) {
        String url = "jdbc:mysql://localhost:3306/bank";
        String user = "root";
        String pass = "dragonul24";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            Statement statement = conn.createStatement();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO cards (Card_Number, PIN, balance) VALUES (?, ?, ?)");
            ps.setString(1, cardNumber);
            ps.setString(2, pin);
            ps.setDouble(3, 0.0);
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void getDatabase(Banking banking) {
        banking.clearCards();
        String url = "jdbc:mysql://localhost:3306/bank";
        String user = "root";
        String pass = "dragonul24";
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM cards");
            while (result.next()) {
                banking.createAccount(result.getString(2), result.getString(3));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setBalance(CreditCard card, double money, int choice) {
        String url = "jdbc:mysql://localhost:3306/bank";
        String user = "root";
        String pass = "dragonul24";

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            if (choice == 1) {
                PreparedStatement ps = conn.prepareStatement("UPDATE cards SET balance=balance+" + money + " WHERE Card_Number='" + card.getCardNumber() + "'");
                ps.execute();
            } else if (choice == 2) {
                PreparedStatement ps = conn.prepareStatement("UPDATE cards SET balance=balance-" + money + " WHERE Card_Number='" + card.getCardNumber() + "'");
                ps.execute();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public double getBalance(CreditCard card) {
        String url = "jdbc:mysql://localhost:3306/bank";
        String user = "root";
        String pass = "dragonul24";
        double balance = 0.0;

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM cards WHERE Card_Number='" + card.getCardNumber() + "'");
            while (result.next()) {
                balance = result.getDouble(4);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return balance;
    }

    public void deleteCard(CreditCard card) {
        String url = "jdbc:mysql://localhost:3306/bank";
        String user = "root";
        String pass = "dragonul24";
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM cards WHERE Card_Number='" + card.getCardNumber() + "'");
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}
