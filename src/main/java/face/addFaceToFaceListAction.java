package face;

// // This sample uses the Apache HTTP client from HTTP Components (http://hc.apache.org/httpcomponents-client-ga/)
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

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

public class addFaceToFaceListAction
{
    public void addFaceToFaceList(String faceUrl)
    {
        HttpClient httpclient = HttpClients.createDefault();

        try
        {
            URIBuilder builder = new URIBuilder( System.getProperty("azureFaceURI")+"/facelists/"+System.getProperty("ptgfFaceList")+"/persistedFaces");
            databaseHandler databaseHandler = new databaseHandler();
           // builder.setParameter("userData", "{string}");
           // builder.setParameter("targetFace", "{string}");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Ocp-Apim-Subscription-Key", System.getProperty("azureFaceIdKey"));


            // Request body
            StringEntity reqEntity = new StringEntity("{\"url\":\""+faceUrl+"\"}");
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null)
            {
                Map<String, String> faceListLog = new LinkedHashMap<String, String>();
                String resultText ="";
                String retSrc = EntityUtils.toString(entity);
                if(retSrc.contains("[")){
                    retSrc =  retSrc.replace("[","");
                }
                if(retSrc.contains("]")){
                    retSrc = retSrc.replace("]","");
                }
                JSONObject result = new JSONObject(retSrc); //Convert String to JSON Object
                if(result.has("persistedFaceId")&& !result.isNull("persistedFaceId")) {
                    resultText =  result.getString("persistedFaceId");
                }else if(result.has("error")&& !result.isNull("error")){
                    result = result.getJSONObject("error");
                    resultText = result.getString("message");
                }
                faceListLog.put("srcPath",faceUrl);
                faceListLog.put("persistedFaceId",resultText);
                databaseHandler.faceListIdUpdate(faceListLog);
              //  System.out.println(faceUrl);
              //  System.out.println(EntityUtils.toString(entity));
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}