public class FictionBook extends Book {

    //fictionbook extends book

    private String genre;

    public FictionBook(String isbn, String name, String author, int numPages, String genre) {
        super(isbn, name, author, numPages);
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "FictionBook{" +
                "isbn='" + getIsbn() + '\'' +
                ", name='" + getName() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", numPages=" + getNumPages() +
                ", genre='" + genre + '\'' +
                '}';
    }
}
