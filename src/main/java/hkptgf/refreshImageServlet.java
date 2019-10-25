package hkptgf;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "refreshImageServlet" , value = "/refreshImageServlet")
public class refreshImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, String> imageLink = (Map<String, String>) session.getAttribute("imageLink2");

        if(imageLink.size()>0) {
            StringBuilder result = new StringBuilder();
            result.append("<div id=\"fh5co-board\" data-columns =\"4\">");
          //  result.append("<c:forEach items=\"${imageLink2}\" var=\"imageLink2\">");
            result.append("<div class=\"item\">");
            result.append("<div class=\"animate-box\">");
            result.append("<a href=\"https://storage.googleapis.com/face-comparison.appspot.com/tempImage/2017-10-22-162228809-newptgf12.jpg\" class=\"image-popup fh5co-board-img\">");
            result.append("<img src=\"https://storage.googleapis.com/face-comparison.appspot.com/tempImage/2017-10-22-162228809-newptgf12.jpg\" alt=\"SP\"></a>");
            result.append("  </div>");
            result.append("</div>");
           // result.append(" </c:forEach>");
            result.append(" </div>");
            response.setContentType("text/plain");
            response.getWriter().write(result.toString());
        }else{
            response.setContentType("text/plain");
            response.getWriter().write("NO SESSION");
        }
    }
}
