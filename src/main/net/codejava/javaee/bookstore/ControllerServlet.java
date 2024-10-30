package net.codejava.javaee.bookstore;

import javax.servlet.http.HttpServlet;

public class ControllerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DAO bookDAO;

    public void init(){
        String jdbcURL = getServletContext().getInitParameter("jdbcURL");
        String jdbcUsername = getServletContext().getInitParameter("jdbcUsername");
        String jdbcPassword = getServletContext().getInitParameter("jdbcPassword");

        bookDAO = new DAO(jdbcURL, jdbcUsername, jdbcPassword);
    }
}
