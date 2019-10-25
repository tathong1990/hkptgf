package face;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "faceCheck" ,value = "/faceCheck")
public class faceCheck extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     //  faceDetectAction fd = new faceDetectAction();
        //String faceId1 = fd.detect("https://storage.googleapis.com/ptgfdatabase.appspot.com/cc/cc260.jpg");
       // String faceId2 = fd.detect("https://storage.googleapis.com/ptgfdatabase.appspot.com/cc/cc208.jpg");
       // faceVerifyAction fva = new faceVerifyAction();
       // fva.verify(faceId1,faceId2);



       //createFaceListAction cFL = new createFaceListAction();
       // cFL.createFaceList();
        addFaceToFaceListAction fl = new addFaceToFaceListAction();
        for(int a = 1020; a<=1027;a++){
          //  String faceUrl = "https://storage.googleapis.com/face-comparison.appspot.com/cc/hkcc"+a+".png";
            String faceUrl = "https://storage.googleapis.com/face-comparison.appspot.com/ptgf/hkptgf"+a+".jpg";
            System.out.println(faceUrl);
            fl.addFaceToFaceList(faceUrl);
        }
        /*
        String faceUrl = "https://storage.googleapis.com/face-comparison.appspot.com/ptgf/hkptgf1011.JPG";
        fl.addFaceToFaceList(faceUrl);

        faceFindSimilarAction fsa = new faceFindSimilarAction();
        fsa.findSimilar("10f2d400-f145-4329-b966-f3d295fd629a");

*/
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
     //   faceVerifyAction fva = new faceVerifyAction();
      //  fva.detect();
    }
}
