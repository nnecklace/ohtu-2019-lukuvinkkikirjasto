package lukuvinkkikirjasto.data_access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import lukuvinkkikirjasto.domain.Kirja;
import lukuvinkkikirjasto.domain.Lukuvinkki;


public interface LukuvinkkiDao {

    List<Kirja> getBooks() throws Exception;
    void newBook(String name, String writer) throws Exception;
    void deleteBook(String lukuvinkki_id) throws Exception; 
}
