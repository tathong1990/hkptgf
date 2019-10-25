package validation;

import database.databaseHandler;
import upload.uploadFileServlet;

import java.util.logging.Logger;

public class validateIPAddress {
    private static final Logger log = Logger.getLogger(uploadFileServlet.class.getName());
    public boolean validIPforUpload(String ipAddress){
        databaseHandler dh = new databaseHandler();
        int uploadLimit = dh.getUploadLimit(ipAddress);
        log.info("ipAddress:" + ipAddress + " upload Limit"+ uploadLimit);
        switch(uploadLimit){
            case -1:
                dh.createUploadLimit(ipAddress);
                break;
            case 0:
                return  false;
            default:
                dh.updateUploadLimit(ipAddress,uploadLimit-1);
                break;
        }
        return true;
    }
}
