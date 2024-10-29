package net.codejava.javaee.bookstore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
DAO.java
This DAO class provides CRUD database operations for the table book
in the database
 */
public class DAO {
    private String jdbcURL;
    private String jdbcUsername;
    private String jdbcPassword;
    private Connection jdbcConnection;

    public DAO(String jdbcURL, String jdbcUsername, String jdbcPassword){
        this.jdbcURL = jdbcURL;
        this.jdbcUsername = jdbcUsername;
        this.jdbcPassword = jdbcPassword;
    }

    protected void connect() throws SQLException {
        if(jdbcConnection == null || jdbcConnection.isClosed()){
            try{
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch(ClassNotFoundException e){
                throw new SQLException(e);
            }
            jdbcConnection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        }
    }

    protected void disconnect() throws SQLException{
        if(jdbcConnection != null && !jdbcConnection.isClosed()){
            jdbcConnection.close();
        }
    }

    public boolean insertBook(Book book) throws SQLException {
        String sql = "INSERT INTO book (title, author, price) VALUE (?, ?, ?)";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setDouble(3, book.getPrice());

        boolean rowInserted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowInserted;
    }

    public List<Book> listAllBooks() throws SQLException{
        List<Book> listbook = new ArrayList<Book>();
        String sql = "SELECT * FROM book";

        connect();

        Statement statement = jdbcConnection.createStatement();
        ResultSet resultset = statement.executeQuery(sql);

        while(resultset.next()){
            int id = resultset.getInt("book_id");
            String title = resultset.getString("title");
            String author = resultset.getString("author");
            float price = resultset.getFloat("price");

            Book book = new Book(id, title, author, price);
            listbook.add(book);
        }
        resultset.close();
        statement.close();

        disconnect();
        return listbook;
    }

    public boolean deleteBook(Book book) throws SQLException {
        String sql = "DELETE FROM book WHERE book_id = ?";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, book.getId());

        boolean rowDeleted = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowDeleted;
    }

    public boolean updateBook(Book book) throws SQLException{
        String sql = "UPDATE book SET title = ?, author = ?, price = ?";
        sql = sql + " WHERE book_id = ?";
        connect();

        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setString(1, book.getTitle());
        statement.setString(2, book.getAuthor());
        statement.setFloat(3, book.getPrice());
        statement.setInt(4, book.getId());

        boolean rowUpdated = statement.executeUpdate() > 0;
        statement.close();
        disconnect();
        return rowUpdated;
    }

    public Book getBook(int id) throws SQLException {
        Book book = null;
        String sql = "SELECT * FROM book WHERE book_id = ?";
        connect();
        PreparedStatement statement = jdbcConnection.prepareStatement(sql);
        statement.setInt(1, id);

        ResultSet resultset = statement.executeQuery();
        if(resultset.next()){
            String title = resultset.getString("title");
            String author = resultset.getString("author");
            float price = resultset.getFloat("price");

            book = new Book(id, title, author, price);
        }

        resultset.close();
        statement.close();

        return book;
    }
}