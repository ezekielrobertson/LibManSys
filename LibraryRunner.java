import java.util.Scanner;

public class LibraryRunner {

    private final Library library;
    private final Scanner scanner;

    public LibraryRunner() {
        this.library = new Library();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        boolean running = true;
        while (running) {
            System.out.println("Welcome to the Library Management System.");
            System.out.println("Please select one of the following options:");
            System.out.println("1. Register");
            System.out.println("2. Sort Books by ISBN");
            System.out.println("3. Search Books by ISBN");
            System.out.println("4. Borrow Book");
            System.out.println("5. Return Book");
            System.out.println("6. Show Inventory Stats");
            System.out.println("7. Quit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline character

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    sortBooks();
                    break;
                case 3:
                    searchBooks();
                    break;
                case 4:
                    borrowBook();
                    break;
                case 5:
                    returnBook();
                    break;
                case 6:
                    showInventoryStats();
                    break;
                case 7:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void register() {
        System.out.println("Enter student name:");
        String name = scanner.nextLine();
        library.registerStudent(name);
        System.out.println("Student registered successfully.");
    }

    private void sortBooks() {
        library.sortBooks();
        System.out.println("Books sorted successfully.");
    }

    private void searchBooks() {
        System.out.println("Enter ISBN:");
        String isbn = scanner.nextLine();
        Book book = library.searchBook(isbn);
        if (book != null) {
            System.out.println("Book found:");
            System.out.println(book.toString());
        } else {
            System.out.println("Book not found.");
        }
    }

    private void borrowBook() {
        System.out.println("Enter student registration number:");
        int regNum = scanner.nextInt();
        scanner.nextLine(); // consume newline character
        System.out.println("Enter book ISBN:");
        String isbn = scanner.nextLine();
        BorrowResult result = library.borrowBook(regNum, isbn);
        if (result == BorrowResult.SUCCESS) {
            System.out.println("Book borrowed successfully.");
        } else if (result == BorrowResult.BOOK_NOT_AVAILABLE) {
            System.out.println("Book not available.");
        } else if (result == BorrowResult.STUDENT_NOT_REGISTERED) {
            System.out.println("Student not registered.");
        } else if (result == BorrowResult.STUDENT_MAX_BOOKS_BORROWED) {
            System.out.println("Student has already borrowed the maximum number of books.");
        } else {
            System.out.println("An error occurred while borrowing the book.");
        }
    }

    private void returnBook() {
        System.out.println("Enter student registration number:");
        int regNum = scanner.nextInt();
        scanner.nextLine(); // consume newline character
        Student student = library.getStudent(regNum);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("List of borrowed books:");
        for (Book book : student.getBorrowedBooks()) {
            System.out.println(book.toString());
        }
        System.out.println("Enter book ISBN to return:");
        String isbn = scanner.nextLine();
        boolean success = library.returnBook(regNum, isbn);
        if (success) {
            System.out.println("Book returned successfully.");
        } else {
            System.out.println("Failed to return book.");
        }
    }

    private void showInventoryStats() {
        int numFictionBooks = library.availableBooks(Book.Category.FICTION);
        int numNonFictionBooks = library.availableBooks(Book.Category.NON_FICTION);
        String title = "Inventory Stats";
        String[] categoryLabels = {"Fiction", "Non-Fiction"};
        int[] bookCounts = {numFictionBooks, numNonFictionBooks};
        InventoryChart chart = new InventoryChart(title, categoryLabels, bookCounts);
        chart.displayGraph();
    }
}
