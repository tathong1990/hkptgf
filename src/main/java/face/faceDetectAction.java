package face;
// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;

import database.databaseHandler;
import org.json.JSONObject;
import org.json.JSONArray;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;


public class faceDetectAction
{
    private static final Logger log = Logger.getLogger(faceDetectAction.class.getName());

    public String[] detect(String url, String errorMessage)
    {
        databaseHandler databaseHandler = new databaseHandler();
        HttpClient httpclient = HttpClients.createDefault();
        String[] detect = new String[2];
        String faceId = "";
        String gender = "";
        String faceAttributes ="";
        try
        {
            URIBuilder builder = new URIBuilder(System.getProperty("azureFaceURI") + "/detect");

            builder.setParameter("returnFaceId", "true");
            builder.setParameter("returnFaceLandmarks", "false");
            builder.setParameter("returnFaceAttributes", "age,gender,noise");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", System.getProperty("azureFaceIdKey"));


            // Request body
            log.info("faceDetectAction " + url);
            StringEntity reqEntity = new StringEntity("{\"url\":\""+url+"\"}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                //System.out.println(EntityUtils.toString(entity));
                String retSrc = EntityUtils.toString(entity);
                if(retSrc.contains("[")){
                    retSrc =  retSrc.replace("[","");
                }
                if(retSrc.contains("]")){
                    retSrc = retSrc.replace("]","");
                }
                Map<String, String> faceDetectLogLog = new LinkedHashMap<String, String>();
                faceDetectLogLog.put("cloudStoragePath",url);
                faceDetectLogLog.put("faceDetectJSON",retSrc);
                databaseHandler.faceDetectLog(faceDetectLogLog);
                // parsing JSON
                JSONObject result = new JSONObject(retSrc); //Convert String to JSON Object
                if(result.has("faceId")&& !result.isNull("faceId")) {
                    faceId = result.getString("faceId");
                    result = result.getJSONObject("faceAttributes");
                    gender = result.getString("gender");
                    log.info("faceId" + faceId + "gender:" + gender);
                    if (gender.equalsIgnoreCase("male")) {
                        errorMessage = "屌你唔好UPLOAD男人相啦!";
                        faceId = "";
                    }
                }else{
                    errorMessage = "Not a valid image";
                }
            }
        }
        catch (Exception e)
        {
            log.warning(e.getMessage());
            errorMessage = "Not a valid image";
        }
        finally {
            log.info("faceId" + faceId);
            detect[0] = faceId;
            detect[1] = errorMessage;
            return detect;
        }
    }
}