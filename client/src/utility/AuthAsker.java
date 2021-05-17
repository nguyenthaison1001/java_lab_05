package utility;

import client.AppClient;
import exceptions.MustBeNotEmptyException;
import exceptions.NotInDeclaredLimitsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Asks user a username and password.
 */
public class AuthAsker {
    private final BufferedReader reader;

    public AuthAsker(BufferedReader reader) {
        this.reader = reader;
    }

    /**
     * Asks user an username.
     *
     * @return username.
     */
    public String askUsername() {
        String username;
        while (true) {
            try {
                Outputer.println("Enter username: ");
                Outputer.print(AppClient.PS2);
                username = reader.readLine().trim();
                if (username.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printError("This username does not exist!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printError("Username can not be empty!");
            } catch (IllegalStateException | IOException exception) {
                Outputer.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return username;
    }

    /**
     * Asks user a password.
     *
     * @return password.
     */
    public String askPassword() {
        String password;
        while (true) {
            try {
                Outputer.println("Enter password: ");
                Outputer.print(AppClient.PS2);
                password = reader.readLine().trim();
                if (password.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printError("This password does not exist!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printError("Password can not be empty!");
            } catch (IllegalStateException | IOException exception) {
                Outputer.printError("Unexpected error!");
                System.exit(0);
            }
        }
        return password;
    }

    /**
     * Asks a user a question.
     *
     * @param question A question.
     * @return Answer (true/false).
     */
        public boolean askQuestion(String question) {
            String finalQuestion = question + " (+/-):";
            String answer;
            while (true) {
                try {
                    Outputer.println(finalQuestion);
                    Outputer.print(AppClient.PS2);
                    answer = reader.readLine().trim();
                    if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                    break;
                } catch (NoSuchElementException exception) {
                    Outputer.printError("The answer is not recognized!");
                } catch (NotInDeclaredLimitsException exception) {
                    Outputer.printError("The answer must be represented by '+' or '-' signs!");
                } catch (IllegalStateException | IOException exception) {
                    Outputer.printError("Unexpected error!");
                    System.exit(0);
                }
            }
            return answer.equals("+");
        }
}
