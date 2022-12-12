package org.whirlplatform.integration;

import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.file.StandardOpenOption.*;


public class JsonUtils implements AutoCloseable {
    private final URL resource;
    private String stringJson;
    private final String token;
    private Path toPath;

    private File tmpDir;
    private final JSONObject jsonObject;

    public JsonUtils(URL resource, String token) {
        this.resource = resource;
        this.token = token;
        unzip();
        parseJsonFile();
        this.jsonObject = new JSONObject(stringJson);
    }

    public File createTempDirectory() throws IOException {
        this.tmpDir = new File(Files.createTempDirectory("tmpDir-").toFile().getAbsolutePath());
        return tmpDir;
    }

    public void unzip() {
        URL url;
        try {
            url = new URL(resource + "downloadReports?token=" + token + "&file=reports.zip");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try (ZipInputStream zipInputStream = new ZipInputStream(Channels.newInputStream(Channels.newChannel(url.openStream())))) {
            for (ZipEntry entry = zipInputStream.getNextEntry(); entry != null; entry = zipInputStream.getNextEntry()) {
                toPath = createTempDirectory().toPath().resolve(entry.getName());
                if (entry.isDirectory()) {
                    Files.createDirectory(toPath);
                } else try (FileChannel fileChannel = FileChannel.open(toPath, WRITE, CREATE)) {
                    fileChannel.transferFrom(Channels.newChannel(zipInputStream), 0, Long.MAX_VALUE);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void parseJsonFile() {
        StringBuilder inputLine = new StringBuilder();
        try (BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(toPath))) {
            for (int c = bis.read(); c != -1; c = bis.read()) {
                inputLine.append((char) c);
            }
            stringJson = inputLine.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getCountCaseSuccess() {
        return jsonObject.getInt("successCaseNum");
    }

    public Integer getCountCaseFailed() {
        return jsonObject.getInt("failureCaseNum");

    }

    public Integer getCountSuitSuccess() {
        return jsonObject.getInt("successSuiteNum");
    }

    public Integer getCountSuitFailed() {
        return jsonObject.getInt("failureSuiteNum");
    }

    public String getLog() {
        StringBuilder resultString = new StringBuilder();
        JSONArray logsArray = jsonObject.getJSONArray("logs");
        JSONObject logsData;

        for (int i = 0; i < logsArray.length(); i++) {
            logsData = (JSONObject) logsArray.get(i);
            resultString.append(logsData.toString()).append("\n");
        }

        return resultString.toString();
    }

    public String getLogsErrorMassage() {
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

    public String getFailedOperation() {
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
                    if (!recordObj.isNull("snapshot")) {
                        resultString.append("Image name: ").append(recordObj.getString("snapshot")).append("\n");
                    }
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

    private Map<String, String> getImagesName() {
        JSONArray cases = jsonObject.getJSONArray("cases");
        JSONObject caseObj;
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < cases.length(); i++) {
            caseObj = (JSONObject) cases.get(i);
            JSONArray records = caseObj.getJSONArray("records");

            for (int j = 0; j < records.length(); j++) {
                JSONObject recordObj = (JSONObject) records.get(j);

                if (!recordObj.isNull("snapshot")) {
                    String imageId = recordObj.getString("snapshot");
                    map.put(imageId, caseObj.getString("title"));
                }
            }
        }
        return map;
    }

    public Path getImage() {
        Map<String, String> mapIm = getImagesName();

        if (mapIm.isEmpty()) return null;

        Path pathToImage;
        try {
            pathToImage = tmpDir.toPath().resolve("images");
            Files.createDirectory(pathToImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String base64ImageString;
        JSONObject snapshots = jsonObject.getJSONObject("snapshot");
        byte[] imageByte;

        for (Map.Entry<String, String> imId : mapIm.entrySet()) {
            String p = String.format("%s-%s.jpeg", imId.getKey(), imId.getValue());
            File file = new File(pathToImage.toString(), p);

            try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(file.toPath()))) {
                BASE64Decoder decoder = new BASE64Decoder();
                base64ImageString = snapshots.getJSONObject(imId.getKey()).getString("url");
                base64ImageString = base64ImageString.replaceFirst("data:image/jpeg;base64,", "");
                imageByte = decoder.decodeBuffer(base64ImageString);
                bos.write(imageByte);
                bos.flush();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return pathToImage;
    }

    @Override
    public void close() {
        try {
            Files.walk(tmpDir.toPath())
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
