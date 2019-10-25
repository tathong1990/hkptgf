package hkptgf;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.sql.*;

@WebServlet(name = "HongKongGirlGallery" , value = "/HongKongGirlGallery")
public class HongKongGirlGallery extends HttpServlet {
    private static final String table = System.getProperty("ptgfTable");
    private static final String displayLimit = System.getProperty("displayLimit");
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Map<String, String> sessionImageList = (Map<String, String>) session.getAttribute("sessionImageList");

        if(sessionImageList!=null && sessionImageList.size()>0) {
            Map<String, String> imageLink = new LinkedHashMap<String, String>();
            Map<String, Map> updateImageList =  this.updateImageList(sessionImageList);
           // System.out.println("doPost imageLinktest"+imageLinktest.size());
            imageLink = updateImageList.get("imageTempList");
            sessionImageList = updateImageList.get("sessionImageList");

            session.setAttribute("sessionImageList", sessionImageList);
            request.setAttribute("imageLink", imageLink);
            request.setAttribute("noOfRecords", "剩餘圖片總數"+sessionImageList.size());
            request.setAttribute("displayLimit", displayLimit);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
            return;
        }else{
            this.doGet(request, response);
            return;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url;
        Map<String, String> imageLink = new LinkedHashMap<String, String>();
        Map<String, String> sessionImageList;
        Map<String, Map> updateImageList;
        HttpSession session = request.getSession();
        int totalNoOfRecords = 0;
        if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
            // Check the System properties to determine if we are running on appengine or not
            // Google App Engine sets a few system properties that will reliably be present on a remote
            // instance.
            url = System.getProperty("ae-cloudsql.cloudsql-database-url");
            try {
                // Load the class that provides the new "jdbc:google:mysql://" prefix.
                Class.forName("com.mysql.jdbc.GoogleDriver");
            } catch (ClassNotFoundException e) {
                throw new ServletException("Error loading Google JDBC Driver", e);
            }
        } else {
            // Set the url with the local MySQL database connection url when running locally
            url = System.getProperty("ae-cloudsql.local2-database-url");
            try {
                // Load the class that provides the new "jdbc:google:mysql://" prefix.
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                throw new ServletException("Error loading mysql JDBC Driver", e);
            }

        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
          //  String selectsql = "select compressedImages from "+table +" where status = 'ACTIVE' order by RAND() limit "+displayLimit;
            String selectsql = "select compressedImages from "+table +" where status = 'ACTIVE' order by RAND()";
            ResultSet rs = conn.prepareStatement(selectsql).executeQuery();
            while (rs.next()) {
                imageLink.put(rs.getString("compressedImages"),rs.getString("compressedImages"));
            }
            String totalNoOfRecordsSQL = "select count(1) as total from "+table +" where status = 'ACTIVE' ";
            ResultSet rs2 = conn.prepareStatement(totalNoOfRecordsSQL).executeQuery();
            while (rs2.next()) {
                totalNoOfRecords = rs2.getInt("total");
            }
            rs = null;
            rs2 = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                if (conn != null) {
                    conn.close();
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
            updateImageList = this.updateImageList(imageLink);
            imageLink = updateImageList.get("imageTempList");
            sessionImageList = updateImageList.get("sessionImageList");


            session.setAttribute("sessionImageList", sessionImageList);
            request.setAttribute("imageLink", imageLink);
            request.setAttribute("noOfRecords", "圖片總數:"+totalNoOfRecords);
            request.setAttribute("displayLimit", displayLimit);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
            dispatcher.forward(request, response);
        }

    }
    private Map<String, Map> updateImageList(Map<String, String> sessionImage){
        Map<String, String> imageLinkTemp = new LinkedHashMap<String, String>();
        Map<String, Map> updatedImageList = new LinkedHashMap<String, Map>();
        int countImage =0;
        for(Map.Entry<String, String> entry : sessionImage.entrySet()){
            imageLinkTemp.put(entry.getKey(),entry.getValue());
            countImage++;
            if(countImage == Integer.parseInt(displayLimit)){
                break;
            }
        }
        for(Map.Entry<String, String> entry : imageLinkTemp.entrySet()) {
            sessionImage.remove(entry.getKey());
        }
        updatedImageList.put("imageTempList",imageLinkTemp);
        updatedImageList.put("sessionImageList",sessionImage);
        return updatedImageList;
    }
}
