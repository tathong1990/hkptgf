package upload;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import database.databaseHandler;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class uploadFileAction {
    private static final Logger log = Logger.getLogger(uploadFileAction.class.getName());
    private static Storage storage = null;
    static {
        storage = StorageOptions.getDefaultInstance().getService();
    }

    public String uploadNewGirlImage(Part filePart, final String bucketName, String ipAddress, String location) throws IOException {
        uploadFileDao ufd = new uploadFileDao();
        databaseHandler databaseHandler = new databaseHandler();
        String newfileName = ufd.getImageNewNameSeq(location);
        final String fileName = location +"/" + newfileName;
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
        log.info("uploadFile  ipAddress: " +ipAddress+" uploadtime:"+"N/A" +" orignal_filename:"+ filePart.getSubmittedFileName()+
                "new_filename:" + fileName +" cloudStoragePath:" + link);
        Map<String, String> imageUploadLog = new LinkedHashMap<String, String>();
        imageUploadLog.put("ipaddress",ipAddress);
        imageUploadLog.put("uploadtime","N/A");
        imageUploadLog.put("orignal_filename",filePart.getSubmittedFileName());
        imageUploadLog.put("new_filename",fileName);
        imageUploadLog.put("cloudStoragePath",link);
        databaseHandler.imageUploadLog(imageUploadLog);
        imageUploadLog.clear();
        ufd.insertNewGirlToPtgf(link);
        return link;
    }
    public void similarFaceLog(String imgLink, Map<String,String> result){
        uploadFileDao ufd = new uploadFileDao();
        ufd.saveSimilarFaceLog(imgLink,result);
    }
}
