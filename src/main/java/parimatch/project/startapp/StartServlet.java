package parimatch.project.startapp;

import parimatch.project.startapp.utilStart.StartHelperDB;
import parimatch.project.startapp.utilStart.StartHelperDBTables;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "start", urlPatterns = "/start")
public class StartServlet extends HttpServlet {

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String command = req.getParameter("command");
    if (command.equals("createDb")) {
      resp.getWriter().write("Creating data base... \n");
      if (StartHelperDB.createDataBase()) {
        resp.getWriter().write("SUCCESS! Data Base is created. \n");
      } else {
        resp.getWriter().write("FAIL! Data Base already exists. \n");
      }
    }
    if (command.equals("dropDb")) {
      resp.getWriter().write("Dropping the data base...\n");
      if (StartHelperDB.dropDataBase()) {
        resp.getWriter().write("SUCCESS! Data Base is dropped. \n");
      } else {
        resp.getWriter().write("FAIL! Data Base is not dropped. \n");
      }
    }
    if (command.equals("createTables")) {
      resp.getWriter().write("Creating the tables...\n");
      if (StartHelperDBTables.createTables("dbTable.sql")) {
        resp.getWriter().write("SUCCESS! Tables are created. \n");
      } else {
        resp.getWriter().write("FAIL! Tables are not created. \n");
      }
    }
    if (command.equals("insertData")) {
      resp.getWriter().write("Inserting data...\n");
      if (StartHelperDBTables.createTables("data.sql")) {
        resp.getWriter().write("SUCCESS! Data inserted. \n");
      } else {
        resp.getWriter().write("FAIL! No data inserted. \n");
      }
    }
    if (command.equals("dropTables")) {
      resp.getWriter().write("Dropping tables...\n");
      if (StartHelperDBTables.dropAllTables()) {
        resp.getWriter().write("SUCCESS! Tables are dropped. \n");
      } else {
        resp.getWriter().write("FAIL! Tables are not dropped. \n");
      }
    }
    if (command.equals("addTrigger")) {
      resp.getWriter().write("Adding a trigger to the DB...\n");
      if (StartHelperDBTables.addRoundTrigger()) {
        resp.getWriter().write("SUCCESS! Trigger added. \n");
      } else {
        resp.getWriter().write("FAIL! Trigger NOT added :(. \n");
      }
    }

  }
}
