package upload;

import database.databaseHandler;

import java.sql.*;
import java.util.Map;
import java.util.logging.Logger;

public class uploadFileDao {
    private static final Logger log = Logger.getLogger(databaseHandler.class.getName());
    Connection conn = null;
  // private static final String url = loadSystem();
    private String url;
    public uploadFileDao() {
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
    public String getImageNewNameSeq(String name){
        PreparedStatement preparedStmt = null;
        String newSeqName = "";
        try{
            conn = DriverManager.getConnection(url);
            //log.info(url);
            String getUploadLimit = "SELECT name,seq from image_seq where name = ?";
            preparedStmt = conn.prepareStatement(getUploadLimit);
            preparedStmt.setString (1, name);
            ResultSet rs = preparedStmt.executeQuery();
            if (rs.next()) {
                do {
                    int newSeq = rs.getInt("seq")+1;
                    newSeqName = rs.getString("name")+newSeq;
                    this.updateImageNameSeq(name);
                } while(rs.next());
            } else {
                newSeqName = name+"1";
            }


        }catch (Exception e){
            log.warning("getImageNewNameSeq Exception" +e.getMessage());
        }finally {
            try {
                preparedStmt.close();
                conn.close();
            }catch (Exception e){
                log.warning("getImageNewNameSeq Exception" +e.getMessage());
            }
            return newSeqName;
        }
    }
    private void updateImageNameSeq(String name){
        PreparedStatement preparedStmt = null;
        try{
            conn = DriverManager.getConnection(url);
            //log.info(url);
            String updateUploadLimit = "update image_seq set seq = seq + 1 where name = ?";
            preparedStmt = conn.prepareStatement(updateUploadLimit);
            preparedStmt.setString (1, name);
            preparedStmt.executeUpdate();

        }catch (Exception e){
            log.warning("updateImageNameSeq Exception" +e.getMessage());
        }finally {
            try {
                preparedStmt.close();
                conn.close();
            }catch (Exception e){
                log.warning("updateImageNameSeq Exception" +e.getMessage());
            }
        }
    }
    public void insertNewGirlToPtgf(String fileLink){
        PreparedStatement preparedStmt = null;
        try{
            conn = DriverManager.getConnection(url);
            //     log.info(url);
            String insertImageUploadLog = " insert into ptgf (srcPath)"
                    + " values (?)";
            preparedStmt = conn.prepareStatement(insertImageUploadLog);
            preparedStmt.setString (1, fileLink);
            preparedStmt.execute();

        }catch (Exception e){
            log.warning("insertNewGirlToPtgf Exception" +e.getMessage());
        }finally {
            try {
                preparedStmt.close();
                conn.close();
            }catch (Exception e){
                log.warning("insertNewGirlToPtgf Exception" +e.getMessage());
            }

        }
    }
    public void saveSimilarFaceLog(String imgLink, Map<String,String> result){
        PreparedStatement preparedStmt = null;
        try{
            conn = DriverManager.getConnection(url);
            for (Map.Entry<String, String> entry : result.entrySet()){
                String insertImageUploadLog = " insert into similarFaceLog (cloudStoragePath,similarFacePath,confidence) values (?,?,?)";
                preparedStmt = conn.prepareStatement(insertImageUploadLog);
                preparedStmt.setString (1, imgLink);
                preparedStmt.setString (2, entry.getKey());
                preparedStmt.setString (3, entry.getValue());
                preparedStmt.execute();
            }

        }catch (Exception e){
            log.warning("saveSimilarFaceLog Exception" +e.getMessage());
        }finally {
            try {
                preparedStmt.close();
                conn.close();
            }catch (Exception e){
                log.warning("insertNewGirlToPtgf Exception" +e.getMessage());
            }

        }
    }
}

