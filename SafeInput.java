import java.util.Scanner;
import java.util.regex.Pattern;

public class SafeInput {
    public static int getRangedInt(Scanner console, String prompt, int low, int high) {
        int result;
        do {
            System.out.print(prompt);
            while (!console.hasNextInt()) {
                System.out.println("Invalid input. Please enter an integer.");
                console.next();
            }
            result = console.nextInt();
        } while (result < low || result > high);
        return result;
    }

    public static boolean getYNConfirm(Scanner console, String prompt) {
        System.out.print(prompt + " (Y/N): ");
        String response = console.next().toUpperCase();
        return response.equals("Y");
    }

    public static String getRegExString(Scanner console, String prompt, String regEx) {
        System.out.print(prompt);
        String response;
        while (true) {
            response = console.next();
            if (Pattern.matches(regEx, response)) {
                break;
            } else {
                System.out.println("Invalid input. Please enter a value matching the pattern: " + regEx);
            }
        }
        return response;
    }
}
