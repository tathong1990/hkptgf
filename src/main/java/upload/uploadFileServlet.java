package upload;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.sql.*;
import java.util.*;

import com.google.api.client.util.IOUtils;
import com.google.appengine.api.utils.SystemProperty;
import com.google.cloud.storage.*;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;
import database.databaseHandler;
import face.*;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.NumberFormat;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import validation.validateIPAddress;


@MultipartConfig
@WebServlet(name = "uploadFileServlet" ,value = "/uploadFileServlet")
public class uploadFileServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(uploadFileServlet.class.getName());
    final String bucket = System.getProperty("bucket");
    private static final String table = System.getProperty("ptgfTable");

    private final GcsService gcsService = GcsServiceFactory.createGcsService(new RetryParams.Builder()
            .initialRetryDelayMillis(10)
            .retryMaxAttempts(10)
            .totalRetryPeriodMillis(15000)
            .build());
    private static final int BUFFER_SIZE = 2 * 1024 * 1024;
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        validateIPAddress vi = new validateIPAddress();
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        boolean canDoUpload =  vi.validIPforUpload(remoteAddr);
        Map<String, Double> courses;
        Map<String, String> courses3 = null;
        String errorMessage = "恭喜你 有個好女朋友";
        try {
            if(canDoUpload && !(request.getContentLength()>BUFFER_SIZE)) {
                Part filePart = request.getPart("image");
                String mimeType = filePart.getContentType();
                System.out.println(mimeType);
                if (!mimeType.startsWith("image/")) {
                    throw new Exception();
                }
                log.info("UploadFileServlet remoteAddr: " + remoteAddr);
                String templink = this.uploadFile(filePart, bucket, remoteAddr);

                faceDetectAction fd = new faceDetectAction();
                String[] faceId = fd.detect(templink, errorMessage);
                log.info("UploadFileServlet faceId: " + faceId[0] + "error :" + faceId[1]);
                if (!faceId[0].equalsIgnoreCase("")) {
                    faceFindSimilarAction fsa = new faceFindSimilarAction();
                    String ccresult = fsa.findSimilar(faceId[0],System.getProperty("ccFaceList"));
                    String ptgfresult = fsa.findSimilar(faceId[0],System.getProperty("ptgfFaceList"));

                    JSONArray ccjsonarray = new JSONArray(ccresult);
                    JSONArray ptgfjsonarray = new JSONArray(ptgfresult);
                    courses = this.loadData(ccjsonarray,ptgfjsonarray);
                    log.info("UploadFileServlet result: " +courses.size());
                    if(courses.size()>0) {
                        courses = courses.entrySet().stream()
                                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
                        courses3 = this.sortResult(courses);
                        errorMessage = "大吉大利，今晚吃雞！";

                        uploadFileAction ufa = new uploadFileAction();
                        ufa.similarFaceLog(templink, courses3);
                    }
                  //  log.warning("uploadFileServlet FaceID :" + templink +"ccFaceResult :" +ccresult+"ptgfFaceResult :" +ptgfresult);
                } else {
                    errorMessage = faceId[1];
                }
            }else{
                if(!canDoUpload) {
                    errorMessage = "你已經超出UPLOAD次數的上限";
                }else if(request.getContentLength()>BUFFER_SIZE){
                    errorMessage = "你上載既檔案太大啦";
                }
            }
        }catch (Exception e){
            errorMessage = "不是有效的圖像";
            e.printStackTrace();
        }finally {
            log.info("UploadFileServlet errorMessage: " + errorMessage);
            request.setAttribute("courses", courses3);
            request.setAttribute("errorMessage", errorMessage);
            request.setAttribute("remoteAddr", remoteAddr);

            RequestDispatcher dispatcher = request.getRequestDispatcher("/result.jsp");
            dispatcher.forward(request, response);

        }


    }

    private static Storage storage = null;
    static {
        storage = StorageOptions.getDefaultInstance().getService();
    }
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       // System.out.println(request.getParameterMap());
       // System.out.println(request.getRequestURI());
    }


    private void copy(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buffer = new byte[BUFFER_SIZE];
            int bytesRead = input.read(buffer);
            while (bytesRead != -1) {
                output.write(buffer, 0, bytesRead);
                bytesRead = input.read(buffer);
            }
        } finally {
            input.close();
            output.close();
        }
    }
    public String uploadFile(Part filePart, final String bucketName,String ipAddress) throws IOException {
        databaseHandler databaseHandler = new databaseHandler();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("YYYY-MM-dd-HHmmssSSS-");
        DateTime dt = DateTime.now(DateTimeZone.UTC);
        String dtString = dt.toString(dtf);
        String filetype = FilenameUtils.getExtension(filePart.getSubmittedFileName());
        String fileipAddress = ipAddress.replace(".","_");
        String fileName = "tempImage/"+dtString +fileipAddress+"."+filetype;
        List<Acl> acls = new ArrayList<>();
        acls.add(Acl.of(Acl.User.ofAllUsers(), Acl.Role.OWNER));
        // the inputstream is closed by default, so we don't need to close it here
        BlobInfo blobInfo;
        blobInfo = storage.create(
                BlobInfo
                        .newBuilder(bucketName, fileName)
                        // Modify access list to allow all users with link to read file
                        .setAcl(acls).setContentType("image/jpeg")
                        .build(),
                filePart.getInputStream());
        // return the public download link
       String link = "https://storage.googleapis.com/"+bucketName+"/"+fileName;
        log.info("uploadFile  ipAddress: " +ipAddress+" uploadtime:"+dtString +" orignal_filename:"+ filePart.getSubmittedFileName()+
        "new_filename:" + fileName +" cloudStoragePath:" + link);
        Map<String, String> imageUploadLog = new LinkedHashMap<String, String>();
        imageUploadLog.put("ipaddress",ipAddress);
        imageUploadLog.put("uploadtime",dtString);
        imageUploadLog.put("orignal_filename",filePart.getSubmittedFileName());
        imageUploadLog.put("new_filename",fileName);
        imageUploadLog.put("cloudStoragePath",link);
        databaseHandler.imageUploadLog(imageUploadLog);
        imageUploadLog.clear();
        return link;
    }
    private Map<String, Double> loadData(JSONArray ccjsonarray,JSONArray ptgfjsonarray){
        Map<String, Double> courses = new LinkedHashMap<String, Double>();


        for (int i = 0; i < ccjsonarray.length(); i++) {
            JSONObject jsonobject = ccjsonarray.getJSONObject(i);
            if(jsonobject.getDouble("confidence")>=0.6) {
                String persistedFaceId = this.getURLByPerisitedfaceID(jsonobject.getString("persistedFaceId"));
                double confidence = jsonobject.getDouble("confidence");
                if(!"".equalsIgnoreCase(persistedFaceId)){
                    courses.put(persistedFaceId, confidence);
                }
            }
        }
        for (int i = 0; i < ptgfjsonarray.length(); i++) {
            JSONObject jsonobject = ptgfjsonarray.getJSONObject(i);
            if(jsonobject.getDouble("confidence")>=0.6) {
                String persistedFaceId = this.getURLByPerisitedfaceID(jsonobject.getString("persistedFaceId"));
                double confidence = jsonobject.getDouble("confidence");
                if(!"".equalsIgnoreCase(persistedFaceId)){
                    courses.put(persistedFaceId, confidence);
                }
            }
        }
        return courses;
    }
    private  Map<String, String> sortResult(Map<String, Double> nonSortResult){
        NumberFormat defaultFormat = NumberFormat.getPercentInstance();
        defaultFormat.setMinimumFractionDigits(1);
        Map<String, String> result = new LinkedHashMap<String, String>() ;
        int i =0;
        for(Map.Entry<String, Double> entry : nonSortResult.entrySet()){
            result.put(entry.getKey(),defaultFormat.format(entry.getValue()));
            i++;
            if(i==8){
                break;
            }
        }
        return result;
    }
    private String getURLByPerisitedfaceID(String persistedFaceID){
        String srcPath ="";
        Connection con = null;
        PreparedStatement preparedStm = null;
        try {
        con = DriverManager.getConnection(System.getProperty("ae-cloudsql.cloudsql-database-url"));
        String selectsql = "select compressedImages from "+table +" where persistedFaceID =  ?";
        preparedStm = con.prepareStatement(selectsql);
        preparedStm.setString (1, persistedFaceID);
        ResultSet rs =  preparedStm.executeQuery();
            while (rs.next()) {
                srcPath = rs.getString("compressedImages");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
            if (preparedStm != null) {
                preparedStm.close();
            }
            if (con != null) {
                con.close();
            }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return srcPath;
    }
}
