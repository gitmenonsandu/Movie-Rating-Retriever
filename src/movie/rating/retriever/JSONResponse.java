/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package movie.rating.retriever;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
/**
 *
 * @author sandukuttan
 */
public class JSONResponse {
        InputStream is = null;
	JSONObject jObj = null;
	String json = "";
        public JSONResponse(){
            
        }
        public JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params)throws Exception {

		// Making HTTP request

			// check for request method
			if(method == "POST"){
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
				
			}else if(method == "GET"){
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				if(params != null){
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}			
			


		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 20);
			StringBuilder sb = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
			
		} catch (Exception e) {
			System.out.println( "Error converting result " + e.toString());
		}

		// try parse the string to a JSON object

		//System.out.println("JSON String"+ json);
		try {
			jObj = new JSONObject(json);
                        
		} catch (Exception e) {
			System.out.println( "Error parsing data " + e.toString());
		}

		// return JSON String
		return jObj;

	}
                
    
}
