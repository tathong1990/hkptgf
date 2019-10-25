package database;

import org.apache.commons.lang3.ArrayUtils;
import upload.uploadFileServlet;

import javax.servlet.ServletException;
import java.sql.*;
import java.util.Map;
import java.util.logging.Logger;

public class databaseHandler {
    private static final Logger log = Logger.getLogger(databaseHandler.class.getName());
    Connection conn = null;
    public static final String url = loadSystem();
    public static final String[] errorFaceID = { "No face detected in the image.", "There is more than 1 face in the image.", "Invalid image URL or error downloading from target server." };
    public static final  String loadSystem() {
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
        return tempurl;
    }

    public void imageUploadLog(Map<String, String> imageUploadLog){
        PreparedStatement preparedStmt = null;
        try{
            conn = DriverManager.getConnection(url);
       //     log.info(url);
            String insertImageUploadLog = " insert into imageUploadLog (ipaddress,uploadtime, orignal_filename, new_filename, cloudStoragePath)"
                    + " values (?, ?, ?, ?, ?)";
            preparedStmt = conn.prepareStatement(insertImageUploadLog);
            preparedStmt.setString (1, imageUploadLog.get("ipaddress"));
            preparedStmt.setString (2, imageUploadLog.get("uploadtime"));
            preparedStmt.setString   (3, imageUploadLog.get("orignal_filename"));
            preparedStmt.setString(4, imageUploadLog.get("new_filename"));
            preparedStmt.setString    (5, imageUploadLog.get("cloudStoragePath"));
            preparedStmt.execute();

        }catch (Exception e){
            log.warning("imageUploadLog Exception" +e.getMessage());
        }finally {
            try {
                preparedStmt.close();
                conn.close();
            }catch (SQLException e){
                log.warning("imageUploadLog Exception" +e.getMessage());
            }

        }
    }
    public void faceDetectLog(Map<String, String> faceDetectLog){
        PreparedStatement preparedStmt = null;
        try{
            conn = DriverManager.getConnection(url);
         //   log.info(url);
            String insertImageUploadLog = " insert into faceDetectLog (cloudStoragePath, faceDetectJSON)"
                    + " values (?, ?)";
            preparedStmt = conn.prepareStatement(insertImageUploadLog);
            preparedStmt.setString (1, faceDetectLog.get("cloudStoragePath"));
            preparedStmt.setString (2, faceDetectLog.get("faceDetectJSON"));
            preparedStmt.execute();

        }catch (Exception e){
            log.warning("faceDetectLog Exception" +e.getMessage());
        }finally {
            try {
                preparedStmt.close();
                conn.close();
            }catch (SQLException e){
                log.warning("faceDetectLog Exception" +e.getMessage());
            }

        }
    }
    public void faceListIdUpdate(Map<String, String> faceListILog){
        PreparedStatement preparedStmt = null;
        try{
            conn = DriverManager.getConnection(url);
            String status ="ACTIVE";
            if(ArrayUtils.contains(errorFaceID,faceListILog.get("persistedFaceId"))){
                status ="INACTIVE";
            }
           // log.info(url);
            String updatefaceListId = "update ptgf set persistedFaceId = ? ,status = ? where srcPath = ?";
            preparedStmt = conn.prepareStatement(updatefaceListId);
            preparedStmt.setString (1, faceListILog.get("persistedFaceId"));
            preparedStmt.setString (2, status);
            preparedStmt.setString (3, faceListILog.get("srcPath"));
            preparedStmt.executeUpdate();

        }catch (Exception e){
            log.warning("faceListIdUpdate Exception" +e.getMessage());
        }finally {
            try {
                preparedStmt.close();
                conn.close();
            }catch (SQLException e){
                log.warning("faceListIdUpdate Exception" +e.getMessage());
            }
        }
    }
    public void createUploadLimit(String ip_address){
        PreparedStatement preparedStmt = null;
        try{
            conn = DriverManager.getConnection(url);
       //     log.info(url);
            String insertUploadLimit = " insert into UploadLimit (ip_address, uploadlimit)"
                    + " values (?, ?)";
            preparedStmt = conn.prepareStatement(insertUploadLimit);
            preparedStmt.setString (1, ip_address);
            preparedStmt.setInt (2, 4);
            preparedStmt.execute();

        }catch (Exception e){
            log.warning("createUploadLimit Exception" +e.getMessage());
        }finally {
            try {
                preparedStmt.close();
                conn.close();
            }catch (SQLException e){
                log.warning("createUploadLimit Exception" +e.getMessage());
            }

        }
    }
    public int getUploadLimit(String ip_address){
        PreparedStatement preparedStmt = null;
        int limit = 0;
        try{
            conn = DriverManager.getConnection(url);
            //log.info(url);
            String getUploadLimit = "SELECT uploadlimit from UploadLimit where ip_address = ?";
            preparedStmt = conn.prepareStatement(getUploadLimit);
            preparedStmt.setString (1, ip_address);
            log.info(getUploadLimit + ip_address);
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                do {
                    limit = rs.getInt("uploadlimit");
                    log.info("limit:" + limit);
                } while(rs.next());
            } else {
                limit = -1;
            }


        }catch (Exception e){
            log.warning("createUploadLimit Exception" +e.getMessage());
        }finally {
            try {
                preparedStmt.close();
                conn.close();
            }catch (SQLException e){
                log.warning("createUploadLimit Exception" +e.getMessage());
            }
            return limit;
        }
    }
    public void updateUploadLimit(String ip_address, int newLimit){
        PreparedStatement preparedStmt = null;
        int limit = 0;
        try{
            conn = DriverManager.getConnection(url);
            //log.info(url);
            String updateUploadLimit = "update UploadLimit set uploadlimit = ? where ip_address = ?";
            preparedStmt = conn.prepareStatement(updateUploadLimit);
            preparedStmt.setInt (1, newLimit);
            preparedStmt.setString (2, ip_address);
            preparedStmt.executeUpdate();

        }catch (Exception e){
            log.warning("createUploadLimit Exception" +e.getMessage());
        }finally {
            try {
                preparedStmt.close();
                conn.close();
            }catch (SQLException e){
                log.warning("createUploadLimit Exception" +e.getMessage());
            }
        }
    }


}
