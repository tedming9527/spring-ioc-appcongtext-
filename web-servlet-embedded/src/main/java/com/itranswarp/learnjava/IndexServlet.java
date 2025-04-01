package com.itranswarp.learnjava;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = "/")
public class IndexServlet extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String user = (String) req.getSession().getAttribute("user");
    resp.setContentType("text/html");
    resp.setCharacterEncoding(StandardCharsets.UTF_8);
    resp.setHeader("X-Powered-By", "JavaEE Servlet");
    PrintWriter pw = resp.getWriter();
    pw.write("<h1>Welcome, " + (user != null ? user : "guest") + "</h1>");
    String name = req.getParameter("name");
    if (user != null) {
      pw.write("<p><a href=\"signout\">Sign Out</a></p>");
    } else {
      pw.write("<p><a href=\"/signin\">Sign In</a></p>");
    }
    pw.write("<p><a href=\"/pref?lang=en\">English</a>|<a href=\"/pref?lang=zh\">中文</a></p>");
    pw.write("<p>当前偏好语言: " + this.parseLanguageFromCookie(req) + "</p>");
    pw.flush();
  }
  private String parseLanguageFromCookie(HttpServletRequest req) {
    Cookie[] cookies = req.getCookies();
    if (cookies != null) {
      for (Cookie cookie: cookies)  {
        if (cookie.getName().equals("lang")) {
          return cookie.getValue();
        }
      }
    }
    return "en";
  }
}
