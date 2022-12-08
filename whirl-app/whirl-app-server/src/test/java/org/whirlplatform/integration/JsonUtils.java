package org.whirlplatform.integration;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class JsonUtils {
    URL resource = getClass().getClassLoader().getResource("reports2.zip");

    private String stringJson = new String();

    public JsonUtils() {
        this.stringJson = stringJson;
    }

    @Test
    public void getLog() {

        String jsonResult = parseUrl(resource);
////        System.out.println(jsonResult);
        getImage();
//        Map<JSONObject, List<JSONObject>> suitsMap = getSuits(jsonResult);
////        suitsMap.forEach((jsonObject, jsonObjects) -> System.out.println(jsonObject.get("idText").toString()+ "\n"+jsonObjects.toString()));
//        System.out.println(parseCurrentJsonLog());
    }

    public String parseUrl(URL url) {
        if (url == null) {
            return "";
        }
        StringBuilder inputLine = new StringBuilder();

        try (ZipInputStream zin = new ZipInputStream(Files.newInputStream(Paths.get(url.toURI())))) {

            ZipEntry entry = zin.getNextEntry();
            System.out.printf("File name: %s \t File size: %d \n", entry.getName(), entry.getSize());


            for (int c = zin.read(); c != -1; c = zin.read()) {
                inputLine.append((char) c);

            }
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        stringJson = inputLine.toString();

        return stringJson;
    }



    public String parseCurrentJsonLog() {
        JSONObject jsonObject = new JSONObject(stringJson);
        StringBuilder resultString = new StringBuilder();

        String failureCaseNum = jsonObject.get("failureCaseNum").toString();
        String successCaseNum = jsonObject.get("successCaseNum").toString();

        resultString.append("Failure case num: ").append(failureCaseNum).append("\n");
        resultString.append("Success case num: ").append(successCaseNum).append("\n");
        resultString.append(getLogsMassage(jsonObject));
        resultString.append(getFailedOperation(jsonObject));

        return resultString.toString();
    }

    private String getLogsMassage(JSONObject jsonObject) {
        StringBuilder resultString = new StringBuilder();
        JSONArray logsArray = jsonObject.getJSONArray("logs");
        JSONObject logsData;

        for (int i = 0; i < logsArray.length(); i++) {
            logsData = (JSONObject) logsArray.get(i);

            if (logsData.getString("type").equals("info") &&
                    logsData.getString("message").contains("Playing test case")) {
                resultString.append(logsData.getString("message")).append("\n");

            } else if (logsData.getString("type").equals("info") &&
                    logsData.getString("message").contains("Test case")) {
                resultString.append(logsData.getString("message")).append("\n");

            } else if (logsData.getString("type").equals("error") &&
                    logsData.getString("message").contains("Test case")) {
                resultString.append(logsData.getString("message")).append("\n");
            }
        }
        return resultString.toString();
    }

    private String getFailedOperation(JSONObject jsonObject) {

        StringBuilder resultString = new StringBuilder();
        JSONArray cases = jsonObject.getJSONArray("cases");
        JSONObject caseObj;

        for (int i = 0; i < cases.length(); i++) {
            caseObj = (JSONObject) cases.get(i);
            JSONArray records = caseObj.getJSONArray("records");

            for (int j = 0; j < records.length(); j++) {
                JSONObject recordObj = (JSONObject) records.get(j);

                if (recordObj.getString("status").equals("fail")) {

                    resultString.append("Suite title: ").append(getSuiteTitle(caseObj.getString("suiteIdText"))).append("\n");
                    resultString.append("Case title: ").append(caseObj.getString("title")).append("\n");
                    resultString.append("Operation: ").append(recordObj.getString("name")).append("\n");
                    resultString.append("Target: ").append(recordObj.getString("target").trim()).append("\n");
                    resultString.append("Status: ").append(recordObj.getString("status")).append("\n");
                }
            }
        }
        return resultString.toString();
    }

    private String getSuiteTitle(String caseSuiteId) {
        JSONObject jsonObject = new JSONObject(stringJson);
        JSONArray suites = jsonObject.getJSONArray("suites");

        String suiteTitle="";

        for (int i = 0; i <suites.length() ; i++) {
            if (suites.getJSONObject(i).get("idText").toString().equals(caseSuiteId)) {
                suiteTitle = suites.getJSONObject(i).get("title").toString();
            }
        }
        return suiteTitle;
    }
    public Map<JSONObject, List<JSONObject>> getSuits(String resultJson) {
        JSONObject jsonObject = new JSONObject(stringJson);
        JSONArray suites = jsonObject.getJSONArray("suites");
        Map<JSONObject, List<JSONObject>> suitsMap = new HashMap<>();


        JSONArray cases = jsonObject.getJSONArray("cases");


        for (int i = 0; i < suites.length(); i++) {
            String idSuit = suites.getJSONObject(i).get("idText").toString();
            List<JSONObject> casesList = new ArrayList<>();

            for (int j = 0; j < cases.length(); j++) {
                String idSuitInCase = cases.getJSONObject(j).get("suiteIdText").toString();
                if (idSuit.equals(idSuitInCase)) {
                    casesList.add(cases.getJSONObject(j));
                }

            }
            suitsMap.put(suites.getJSONObject(i), casesList);
        }

        return suitsMap;
    }

    public void getFailedCase(String resultJson) {

        Map<JSONObject, List<JSONObject>> allSuites = new HashMap<>();
        getSuits(resultJson);
        StringBuilder stringBuilder = new StringBuilder();
        List<JSONObject> casesList = new ArrayList<>();

        Iterator<List<JSONObject>> iterator = allSuites.values().iterator();
        while (iterator.hasNext()) {
            casesList = iterator.next();

            for (JSONObject js : casesList
            ) {
                if (js.getJSONObject("records").getString("status").equals("fail")) {
                    stringBuilder.append(js.getJSONObject("records").toString());
                    System.out.println(stringBuilder.toString());
                }

            }


        }


    }

    public BufferedImage getImage() {
        JSONObject jsonObject = new JSONObject(stringJson);
        String jobj= jsonObject.getJSONObject("snapshot").getJSONObject("image-1").getString("url");
        System.out.println(jobj);

        String base64ImageString = jobj.replaceFirst("data:image/png;base64,", "");

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(base64ImageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }




}
