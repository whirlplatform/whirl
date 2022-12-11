package org.whirlplatform.integration;

import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import sun.misc.BASE64Decoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JsonUtils implements AutoCloseable {
    private URL resource;
    private String stringJson = new String();
    private String token;
    BufferedInputStream bis;
    BufferedOutputStream bos;
    ZipInputStream zin;

    public JsonUtils(URL resource, String token) {
        this.resource = resource;
        this.token = token;
        // перенести скачивание + парсинг
    }

    //    @Test
    public String getLog() throws IOException {
        File zipFile = downloadZip();
        parseUrl(zipFile);
        getImage();
        return parseCurrentJsonLog();
    }


    private File downloadZip() throws IOException {
        String dowUrl = resource + "downloadReports?token=" + token + "&file=reports.zip";

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet downloadGet = new HttpGet(dowUrl);
        File zipFile = new File("C:\\Projects\\whirl_\\whirl-app\\whirl-app-server\\reports.zip");

        CloseableHttpResponse respons = httpclient.execute(downloadGet);
        HttpEntity httpEntity = respons.getEntity();

        bis = new BufferedInputStream(httpEntity.getContent());
        bos = new BufferedOutputStream(Files.newOutputStream(zipFile.toPath()));
        int inByte;
        while ((inByte = bis.read()) != -1) bos.write(inByte);
        bos.flush();
        bis.close();
        bos.close();

        return zipFile;
    }


    private void parseUrl(File url) {
        StringBuilder inputLine = new StringBuilder();

        try {
            zin = new ZipInputStream(Files.newInputStream(Paths.get(url.toURI())));
            ZipEntry entry = zin.getNextEntry();
            assert entry != null;
            for (int c = zin.read(); c != -1; c = zin.read()) {
                inputLine.append((char) c);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stringJson = inputLine.toString();

    }

    private String parseCurrentJsonLog() {
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

        String suiteTitle = "";

        for (int i = 0; i < suites.length(); i++) {
            if (suites.getJSONObject(i).get("idText").toString().equals(caseSuiteId)) {
                suiteTitle = suites.getJSONObject(i).get("title").toString();
            }
        }
        return suiteTitle;
    }

    private void getImage() {
        String pathToImage = "C:\\\\Projects\\\\whirl_\\\\whirl-app\\\\whirl-app-server\\\\reportImage.jpg";
        JSONObject jsonObject = new JSONObject(stringJson);

        String jobj = "";
        // добавить  проверку if
        try {
            jobj = jsonObject.getJSONObject("snapshot").getJSONObject("image-1").getString("url");
        } catch (JSONException e) {
            System.out.println("No image");
        }

//        System.out.println(jobj);

        String base64ImageString = jobj.replaceFirst("data:image/jpeg;base64,", "");

//        System.out.println(base64ImageString);

        BufferedImage image = null;
        byte[] imageByte;
        try {
            bos = new BufferedOutputStream(Files.newOutputStream(Paths.get(pathToImage)));
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(base64ImageString);
            bos.write(imageByte);
            bos.flush();

//            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
//            image = ImageIO.read(bis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void close() throws Exception {
        // удалить врременные файлы
//        bis.close();
//        bos.close();
//        zin.close();
    }
}
