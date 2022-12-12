package org.whirlplatform.integration;

import org.json.JSONArray;
import org.json.JSONObject;
import sun.misc.BASE64Decoder;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.file.StandardOpenOption.*;


public class JsonUtils implements AutoCloseable {
    private final URL resource;
    private String stringJson;
    private final String token;
    private Path toPath;
    private final JSONObject jsonObject;

    public JsonUtils(URL resource, String token) {
        this.resource = resource;
        this.token = token;
        unzip();
        parseJsonFile();
        this.jsonObject = new JSONObject(stringJson);
    }

    public File createTempDirectory() throws IOException {
        File tmpdir = new File(Files.createTempDirectory("tmpDir-").toFile().getAbsolutePath());
        return tmpdir;
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
        System.out.println(toPath.toAbsolutePath());
        try ( BufferedInputStream bis = new BufferedInputStream(Files.newInputStream(toPath))) {
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

    public Integer getCountSuitSuccess(){
        return jsonObject.getInt("successSuiteNum");
    }

    public Integer getCountSuitFailed(){
        return jsonObject.getInt("failureSuiteNum");
    }

    public String getLog(){
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

    public Path getImage() {
        Path pathToImage;
        try {
            String tmpPath = toPath.toAbsolutePath().toString().replace(toPath.getFileName().toString(), "");
            pathToImage = Paths.get(tmpPath).resolve("images");
            Files.createDirectory(pathToImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String base64ImageString = "";
        JSONObject snapshots = jsonObject.getJSONObject("snapshot");

        if (snapshots.isNull("image-1")) return null;

        byte[] imageByte;

        try (BufferedOutputStream bos = new BufferedOutputStream(Files.newOutputStream(pathToImage))) {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(base64ImageString);
            bos.write(imageByte);
            bos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return pathToImage;
    }


    @Override
    public void close() {
        String tmpPath = toPath.toAbsolutePath().toString().replace(toPath.getFileName().toString(), "");
        Path directory = Paths.get(tmpPath);
        try {
            Files.walk(directory)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
