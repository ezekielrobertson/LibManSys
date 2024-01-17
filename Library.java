import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Library implements LibraryManagementSystem {
    ArrayList<FictionBook> fictionBooks;

    ArrayList<NonfictionBook> nonfictionBooks;
    ArrayList<Student> students;
    private Iterable<? extends Book> inventory;


    public Library(ArrayList<FictionBook> fictionBooks, HashMap<String, Integer> inventory, ArrayList<NonfictionBook> nonfictionBooks, ArrayList<Student> students) {
        this.fictionBooks = fictionBooks;
        this.inventory = (Iterable<? extends Book>) inventory;
        this.nonfictionBooks = nonfictionBooks;
        this.students = students;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public HashMap<String, Integer> getInventory() {
        return (HashMap<String, Integer>) inventory;
    }

    public ArrayList<NonfictionBook> getNonfictionBooks() {
        return nonfictionBooks;
    }

    public ArrayList<FictionBook> getFictionBooks() {
        return fictionBooks;
    }

    public void setFictionBooks(ArrayList<FictionBook> fictionBooks) {
        this.fictionBooks = fictionBooks;
    }

    private List<Student> registeredStudents;

    public Library() {

        registeredStudents = new ArrayList<>();
    }

    public int[] availableBooks() {
        int numFiction = 0;
        int numNonfiction = 0;

        for (Book book : fictionBooks) {
            if (book.getQuantity() > 0) {
                numFiction += book.getQuantity();
            }
        }

        for (Book book : nonfictionBooks) {
            if (book.getQuantity() > 0) {
                numNonfiction += book.getQuantity();
            }
        }

        return new int[]{numFiction, numNonfiction};
    }

    @Override
    public void inventory(String filePath) {
        try {
            File inventoryFile = new File("inventory.txt");
            Scanner scanner = new Scanner(inventoryFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String isbn = parts[0].trim();
                String title = parts[1].trim();
                String author = parts[2].trim();
                int quantity = Integer.parseInt(parts[3].trim());
                String type = parts[4].trim();
                Book book = new Book(isbn, title, author, quantity) {
                    @Override
                    public String toString() {
                        return null;
                    }
                };
                if (type.equalsIgnoreCase("fiction")) {
                    fictionBooks.add((FictionBook) book);
                } else {
                    nonfictionBooks.add((NonfictionBook) book);
                }
                inventory.put(isbn, quantity);
            }
            scanner.close();
            System.out.println("Inventory loaded successfully from file: " + filePath);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity found in file: " + filePath);
        }
    }
    // This method updates the borrowed books file with the given borrowedBooks map
    private void updateBorrowedFile(Map<String, List<String>> borrowedBooks) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("borrowed_books.txt"))) {
            for (Map.Entry<String, List<String>> entry : borrowedBooks.entrySet()) {
                String regNo = entry.getKey();
                List<String> books = entry.getValue();
                for (String book : books) {
                    writer.write(regNo + "," + book);
                    writer.newLine();
                }
            }
        }
    }

    // This method updates the inventory file with the current inventory of books
    private void updateInventoryFile() throws IOException {
        Iterable<? extends Book> inventory = this.inventory;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory.txt"))) {
            for (Book book : inventory) {
                writer.write(book.getIsbn() + "," + book.getName() + "," + book.getAuthor() + "," + book.getNumPages() + "," + book.getQuantity() + "," + book.getCategory().name());
                writer.newLine();
            }
        }
    }

    @Override
    public void lend(String isbn) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter student registration number:");
        String regNo = scanner.nextLine().trim();

        try {
            // Read borrowed books file into memory
            HashMap<String, List<String>> borrowedBooks = new HashMap<>();
            File borrowedFile = new File("borrowed_books.txt");
            if (borrowedFile.exists()) {
                Scanner borrowedScanner = new Scanner(borrowedFile);
                while (borrowedScanner.hasNextLine()) {
                    String line = borrowedScanner.nextLine().trim();
                    String[] parts = line.split(",");
                    if (parts.length == 2) {
                        String key = parts[0];
                        String value = parts[1];
                        if (borrowedBooks.containsKey(key)) {
                            borrowedBooks.get(key).add(value);
                        } else {
                            List<String> newList = new ArrayList<>();
                            newList.add(value);
                            borrowedBooks.put(key, newList);
                        }
                    }
                }
                borrowedScanner.close();
            }

            // Check if the student has already borrowed the maximum number of books (3)
            if (borrowedBooks.containsKey(regNo) && borrowedBooks.get(regNo).size() >= 3) {
                System.out.println("This student has already borrowed the maximum number of books (3).");
                return;
            }

            // Check if the book is available in the inventory
            Book book = search(isbn);
            if (book == null) {
                System.out.println("Book not found in inventory.");
                return;
            }
            if (book.getQuantity() == 0) {
                System.out.println("This book is not available in the inventory.");
                return;
            }


            book.setQuantity(book.getQuantity() - 1);
            updateInventoryFile();

            // Update the borrowed books file
            if (borrowedBooks.containsKey(regNo)) {
                borrowedBooks.get(regNo).add(isbn);
            } else {
                List<String> newList = new ArrayList<>();
                newList.add(isbn);
                borrowedBooks.put(regNo, newList);
            }
            updateBorrowedFile(borrowedBooks);

            System.out.println("Book lent to student.");
        } catch (FileNotFoundException e) {
            System.out.println("Error: borrowed_books.txt file not found.");
        } catch (IOException e) {
            System.out.println("Error updating borrowed_books.txt file.");
        }
    }

    @Override
    public void putBack(String isbn) {
        for (Book book : borrowedBooks) {
            if (book.getIsbn().equals(isbn)) {
                borrowedBooks.remove(book);
                availableBooks.add(book);
                System.out.println("Book with ISBN " + isbn + " has been returned.");
                return;
            }
        }
        System.out.println("Book with ISBN " + isbn + " was not found in the borrowed books list.");
    }

    @Override
    public void registerStudent(Student student) {
        registeredStudents.add(student);
    }

    @Override
    public Book search(String isbn) {
        List<Book> books = new ArrayList<Book>();
        books.addAll(fictionBooks);
        books.addAll(nonfictionBooks);
        Collections.sort(books);

        int left = 0;
        int right = books.size() - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            Book book = books.get(mid);
            int cmp = book.getIsbn().compareTo(isbn);
            if (cmp < 0) {
                left = mid + 1;
            } else if (cmp > 0) {
                right = mid - 1;
            } else {
                return book;
            }
        }

        return null;
    }

    @Override
    public ArrayList<Book> sort(int mode) {
        // Create a list of all books from the inventory
        ArrayList<Book> allBooks = new ArrayList<>();
        allBooks.addAll(fictionBooks);
        allBooks.addAll(nonfictionBooks);

        // Sort the list based on the selected mode
        if (mode == 1) {
            Collections.sort(allBooks, new Comparator<Book>() {
                @Override
                public int compare(Book b1, Book b2) {
                    return b1.getIsbn().compareTo(b2.getIsbn());
                }
            });
        } else if (mode == 2) {
            Collections.sort(allBooks, new Comparator<Book>() {
                @Override
                public int compare(Book b1, Book b2) {
                    return Integer.compare(b2.getQuantity(), b1.getQuantity());
                }
            });
        }

        // Create a new list of the top 10 books
        ArrayList<Book> top10 = new ArrayList<>();
        for (int i = 0; i < Math.min(10, allBooks.size()); i++) {
            top10.add(search(allBooks.get(i).getIsbn()));
        }
        return top10;
    }
}
