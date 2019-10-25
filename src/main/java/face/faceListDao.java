package face;

import database.databaseHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class faceListDao {
    private static final Logger log = Logger.getLogger(databaseHandler.class.getName());
    Connection conn = null;
    private String url;
    public faceListDao() {
        String tempurl = "";
        if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
            tempurl = System.getProperty("ae-cloudsql.cloudsql-database-url");
            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
            } catch (ClassNotFoundException e) {
                log.warning("Error loading Google JDBC Driver"+e);
            }
        } else {
            tempurl = System.getProperty("ae-cloudsql.local2-database-url");
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                log.warning("Error loading mysql JDBC Driver"+e);
            }

        }
        //return tempurl;
        this.url = tempurl;
    }
    public List<String> getNonMarkedFaceList(){
        List<String> list = new ArrayList<String>();
        try{
            conn = DriverManager.getConnection(url);
            String getNonMarkedFaceList = "SELECT srcPath from faceComparison where persistedFaceId is null";
            ResultSet rs = conn.prepareStatement(getNonMarkedFaceList).executeQuery();
            while (rs.next()) {
                list.add(rs.getString("srcPath"));
            }


        }catch (Exception e){
            log.warning("getUploadedImage Exception" +e.getMessage());
        }finally {
            try {
                conn.close();
            }catch (SQLException e){
                log.warning("getUploadedImage Exception" +e.getMessage());
            }
            return list;
        }


    }

}
