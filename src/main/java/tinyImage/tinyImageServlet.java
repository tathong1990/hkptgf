package tinyImage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.tinify.*;

@WebServlet(name = "tinyImageServlet" ,value = "/tinyImageServlet")
public class tinyImageServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Tinify.setKey("iH_1OYGy9C1iKxyNNQIq4QVDa3MsKalv");
        for (int i =511; i<=1010;i++) {
            Source source = Tinify.fromFile("D:\\tat\\python\\face\\cloud\\ptgfjpg\\hkptgf"+i+".jpg");
            source.toFile("D:\\tat\\python\\face\\cloud\\ptgfmin\\hkptgf"+i+".jpg");
            System.out.println(" process " + i);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
