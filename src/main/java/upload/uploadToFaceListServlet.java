package upload;

import face.addFaceToFaceListAction;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.util.logging.Logger;

@MultipartConfig
@WebServlet(name = "uploadToFaceListServlet" ,value = "/uploadToFaceListServlet")
public class uploadToFaceListServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(uploadToFaceListServlet.class.getName());
    private String bucket = System.getProperty("bucket");
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        addFaceToFaceListAction fl = new addFaceToFaceListAction();
        uploadFileAction ufa = new uploadFileAction();
        Part filePart = request.getPart("image");
        String faceUrl =  ufa.uploadNewGirlImage(filePart, bucket, "","newptgf");
        fl.addFaceToFaceList(faceUrl);
        log.info(faceUrl);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }


}
