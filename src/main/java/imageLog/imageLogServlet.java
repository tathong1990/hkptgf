package imageLog;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet(name = "imageLogServlet" , value = "/imageLogServlet")
public class imageLogServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            imageLogDao imgDao = new imageLogDao();
            String url;
            Map<String, String> imageLink = imgDao.getUploadedImage();

            int totalNoOfRecords = 0;

                request.setAttribute("imageLink", imageLink);
                request.setAttribute("noOfRecords", "圖片總數:"+totalNoOfRecords);
                request.setAttribute("displayLimit", "10000");

                RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
                dispatcher.forward(request, response);



    }
}
