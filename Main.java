import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final String FILE_EXTENSION = ".txt";
    private static boolean needsToBeSaved = false;
    private static ArrayList<String> list = new ArrayList<>();
    private static String currentFileName = null;

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String command;
        do {
            displayMenu();
            command = SafeInput.getRegExString(in, "Enter a command: ", "[AaDdIiPpQqOoSsCcMmVv]");
            switch (command.toUpperCase()) {
                case "A":
                    addItem(in);
                    break;
                case "D":
                    deleteItem(in);
                    break;
                case "I":
                    insertItem(in);
                    break;
                case "P":
                case "V":
                    printList();
                    break;
                case "Q":
                    if (promptSaveChanges(in)) {
                        if (needsToBeSaved) {
                            saveToFile(in);
                        }
                        System.out.println("Exiting program...");
                        System.exit(0);
                    }
                    break;
                case "O":
                    if (promptSaveChanges(in)) {
                        if (needsToBeSaved) {
                            saveToFile(in);
                        }
                        openFile(in);
                    }
                    break;
                case "S":
                    saveToFile(in);
                    break;
                case "C":
                    if (promptSaveChanges(in)) {
                        clearList(in);
                    }
                    break;
                case "M":
                    moveItem(in);
                    break;
                default:
                    System.out.println("Invalid command. Please try again.");
            }
        } while (!command.equalsIgnoreCase("Q"));
    }

    private static void displayMenu() {
        System.out.println("\nMenu:");
        System.out.println("A - Add an item to the list");
        System.out.println("D - Delete an item from the list");
        System.out.println("I - Insert an item into the list");
        System.out.println("P/V - View the list");
        System.out.println("O - Open a list file from disk");
        System.out.println("S - Save the current list file to disk");
        System.out.println("C - Clear the list");
        System.out.println("M - Move an item in the list");
        System.out.println("Q - Quit the program");
    }

    private static void addItem(Scanner in) {
        System.out.print("Enter an item to add: ");
        in.nextLine();  // Clear the buffer
        String item = in.nextLine();
        list.add(item);
        needsToBeSaved = true;
    }

    private static void deleteItem(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("The list is empty. Nothing to delete.");
            return;
        }
        int index = SafeInput.getRangedInt(in, "Enter the index of the item to delete: ", 1, list.size()) - 1;
        list.remove(index);
        needsToBeSaved = true;
    }

    private static void insertItem(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("The list is empty. Use Add to add the first item.");
            return;
        }
        int index = SafeInput.getRangedInt(in, "Enter the index to insert the item at: ", 1, list.size()) - 1;
        System.out.print("Enter an item to insert: ");
        in.nextLine();  // Clear the buffer
        String item = in.nextLine();
        list.add(index, item);
        needsToBeSaved = true;
    }

    private static void printList() {
        if (list.isEmpty()) {
            System.out.println("The list is empty.");
        } else {
            System.out.println("Current list:");
            for (int i = 0; i < list.size(); i++) {
                System.out.println((i + 1) + ". " + list.get(i));
            }
        }
    }

    private static void openFile(Scanner in) {
        System.out.print("Enter the file name to open: ");
        String fileName = in.next() + FILE_EXTENSION;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            list.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
            currentFileName = fileName;
            needsToBeSaved = false;
            System.out.println("List loaded from " + fileName);
        } catch (IOException e) {
            System.out.println("Error opening file: " + e.getMessage());
        }
    }

    private static void saveToFile(Scanner in) {
        if (currentFileName == null) {
            System.out.print("Enter a file name to save: ");
            currentFileName = in.next() + FILE_EXTENSION;
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(currentFileName))) {
            for (String item : list) {
                writer.println(item);
            }
            needsToBeSaved = false;
            System.out.println("List saved to " + currentFileName);
        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

    private static void clearList(Scanner in) {
        if (SafeInput.getYNConfirm(in, "Are you sure you want to clear the list?")) {
            list.clear();
            needsToBeSaved = true;
            System.out.println("List cleared.");
        }
    }

    private static void moveItem(Scanner in) {
        if (list.isEmpty()) {
            System.out.println("The list is empty. Nothing to move.");
            return;
        }
        int fromIndex = SafeInput.getRangedInt(in, "Enter the index of the item to move: ", 1, list.size()) - 1;
        int toIndex = SafeInput.getRangedInt(in, "Enter the new index for the item: ", 1, list.size()) - 1;
        String item = list.remove(fromIndex);
        list.add(toIndex, item);
        needsToBeSaved = true;
    }

    private static boolean promptSaveChanges(Scanner in) {
        if (needsToBeSaved) {
            return SafeInput.getYNConfirm(in, "You have unsaved changes. Do you want to save them?");
        }
        return true; // No unsaved changes, it's safe to proceed
    }
}
