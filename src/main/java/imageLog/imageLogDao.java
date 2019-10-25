package imageLog;

import database.databaseHandler;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class imageLogDao {
    private static final Logger log = Logger.getLogger(databaseHandler.class.getName());
    Connection conn = null;
    // private static final String url = loadSystem();
    private String url;
    public imageLogDao() {
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
    public Map<String, String> getUploadedImage(){
        Map<String, String> imageLink = new LinkedHashMap<String, String>();

        try{
            conn = DriverManager.getConnection(url);
            //log.info(url);
            String getUploadLimit = "SELECT ipaddress,cloudStoragePath from imageUploadLog order by id DESC limit 50";
           // preparedStmt = conn.prepareStatement(getUploadLimit);
           // preparedStmt.setString (1, name);
           // ResultSet rs = preparedStmt.executeQuery();
            ResultSet rs = conn.prepareStatement(getUploadLimit).executeQuery();
            while (rs.next()) {
                imageLink.put(rs.getString("cloudStoragePath"),rs.getString("cloudStoragePath"));
            }


        }catch (Exception e){
            log.warning("getUploadedImage Exception" +e.getMessage());
        }finally {
            try {
                conn.close();
            }catch (SQLException e){
                log.warning("getUploadedImage Exception" +e.getMessage());
            }
            return imageLink;
        }
    }

}

