public class NonfictionBook extends Book {

    private String topic;

    public NonfictionBook(String isbn, String name, String author, int numPages, String topic) {
        super(isbn, name, author, numPages);
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public String toString() {
        return "NonfictionBook{" +
                "isbn='" + getIsbn() + '\'' +
                ", name='" + getName() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", numPages=" + getNumPages() +
                ", topic=''" + topic + '\'' +
                '}';
    }
}
