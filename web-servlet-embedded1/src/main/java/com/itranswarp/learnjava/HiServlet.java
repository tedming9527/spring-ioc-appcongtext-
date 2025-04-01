package com.itranswarp.learnjava;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(urlPatterns = "/hi")
public class HiServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter("name");
    String redirectToUrl = "/hello" + (name == null ? "" : "?name=" + name);
    resp.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    // resp.sendRedirect(redirectToUrl);
    resp.setHeader("Location", redirectToUrl);
  }
}
