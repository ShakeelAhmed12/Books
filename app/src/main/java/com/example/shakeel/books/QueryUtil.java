package com.example.shakeel.books;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shakeel on 19/03/2018.
 */

public class QueryUtil {

    private static final String TAG = QueryUtil.class.getSimpleName();

    private QueryUtil(){

    }

    public static List<Books> extractFeatureFromJSON(String booksJSON){

        if(TextUtils.isEmpty(booksJSON)){
            return null;
        }

        List<Books> books = new ArrayList<>();

        try{
            JSONObject baseJsonResponse = new JSONObject(booksJSON);

            JSONArray booksArray = baseJsonResponse.getJSONArray("items");

            for(int i = 0; i < booksArray.length(); i++) {
                JSONObject currentBook = booksArray.getJSONObject(i);

                JSONObject information = currentBook.getJSONObject("volumeInfo");

                String mTitle = information.getString("title");

                //Log.e("Author", "" + information.getString("authors"));
                String mAuthor = information.getString("authors").replaceAll("\"", "").replaceAll("]", "").replace("[","").trim();

                //JSONObject getURL = currentBook.getJSONObject("imageLinks");

                //String url = information.getString("imageLinks").replace("\\","");
                //String[] thumbnailSize = url.split(",");

                //Log.e("Book", thumbnailSize[0]);

                Books book = new Books(mAuthor, mTitle);

                books.add(book);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return books;
    }

    public static List<Books> fetchBooks(String requestUrl){
        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            e.printStackTrace();
        }
        List<Books> books = extractFeatureFromJSON(jsonResponse);

        return books;
    }

    private static URL createUrl(String stringURL){
        URL url = null;
        try{
            url = new URL(stringURL);
        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else{
                Log.e(TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(TAG, "Problem retrieving the earthquake JSON results", e);
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
