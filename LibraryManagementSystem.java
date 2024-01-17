import java.util.ArrayList;

public interface LibraryManagementSystem {
    abstract void inventory(String s);
    abstract void lend(String s);
    abstract void putBack(String s);
    abstract void registerStudent(Student student);
    abstract Book search(String s);
    abstract ArrayList<Book> sort(int a);
}
