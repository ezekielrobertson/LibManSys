public abstract class Book implements Comparable<Book> {


    public enum Category {
        FICTION,
        NON_FICTION
    }
    private String isbn;
    private String name;
    private String author;
    private String category;
    private int numPages;
    private int quantity;


    public Book(String isbn, String name, String author, int numPages) {
        this.isbn = isbn;
        this.name = name;
        this.author = author;
        this.numPages = numPages;
        this.quantity = quantity;
        this.category = category;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }
    public String getCategory() {
        return category;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getNumPages() {
        return numPages;
    }

    public void setNumPages(int numPages) {
        this.numPages = numPages;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public abstract String toString();

    @Override
    public int compareTo(Book other) {
        return this.isbn.compareTo(other.getIsbn());
    }
}