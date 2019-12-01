package lukuvinkkikirjasto.data_access;

import java.sql.*;
import java.util.*;
import lukuvinkkikirjasto.domain.*;

public class BookDao implements LukuvinkkiDao {
    private final String DELETE = "DELETE FROM Book WHERE lukuvinkki_id = ?";
    private final String FIND_BY_ID = "SELECT * FROM Book WHERE lukuvinkki_id = ?";
    private final String FIND_ALL = "SELECT * FROM Book ORDER BY book_id";
    private final String INSERT = "INSERT INTO Book (title, author) VALUES (?,?)";
    private final String UPDATE = "UPDATE Book SET title = ?, author = ?, lukuvinkki_id = ? WHERE id = ?";

    Database database;

    public BookDao(Database db) throws Exception {
        this.database = db;
        checkDatabaseConnection();
    }

    private void checkDatabaseConnection() throws Exception {
        Connection conn = null;

        try {
            conn = database.getConnection();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //System.exit(0);
        }
    }

    public Book findByLukuvinkkiId(String lukuvinkkiId) {
        Book book = null;
        try {
            Connection connection = database.getConnection();
            PreparedStatement stmt = connection.prepareStatement(FIND_BY_ID);
            stmt.setInt(1, Integer.parseInt(lukuvinkkiId));

            ResultSet rs = stmt.executeQuery();
            boolean hasOne = rs.next();
            if (!hasOne) {
                return null;
            }

            book.id = rs.getInt("book_id");
            book.title = rs.getString("title");
            book.author = rs.getString("author");
            System.out.println("status: " + rs.getInt("status"));

            stmt.close();
            rs.close();
            connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return book;
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try {
            Connection conn = database.getConnection();
            PreparedStatement stmt = conn.prepareStatement(FIND_ALL);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Book book = new Book();
                book.id = rs.getInt("book_id");
                book.title = rs.getString("title");
                book.author = rs.getString("author");

                books.add(book);
            }
            stmt.close();
            rs.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return books;
    }

    public void insert(String title, String author) throws Exception {
        Connection conn = database.getConnection();

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO Lukuvinkki (name, type) VALUES (?,?)",
                                  Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, title);
        stmt.setString(2, "book");

        if (stmt.executeUpdate() == 0) {
            throw new SQLException("Creating lukuvinkki failed, no rows affected.");
        }

        int lukuvinkkiId;

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                lukuvinkkiId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Creating lukuvinkki failed, no ID obtained");
            }
        }

        stmt.clearParameters();
        stmt = conn.prepareStatement("INSERT INTO Book (title, author, lukuvinkki_id) VALUES (?,?,?)");
        stmt.setString(1, title);
        stmt.setString(2, author);
        stmt.setInt(3,lukuvinkkiId);
        stmt.execute();

        stmt.close();
        conn.close();
    }

    public void delete(String lukuvinkkiId) {
        try {
            Connection conn = database.getConnection();

            PreparedStatement stmt = conn.prepareStatement(DELETE);
            stmt.setInt(1, Integer.parseInt(lukuvinkkiId));
            stmt.executeUpdate();
            stmt.close();

            stmt = conn.prepareStatement("DELETE FROM Lukuvinkki WHERE id = ?");
            stmt.setInt(1, Integer.parseInt(lukuvinkkiId));
            stmt.executeUpdate();

            stmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
