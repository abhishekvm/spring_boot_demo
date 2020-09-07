package com.velotio.demo1.services;

import org.springframework.stereotype.Service;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ClientApi;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

@Service("zapService")
public class ZapService {
    private static final String ADDRESS = "localhost";
    private static final int PORT = 8081;
    private static final String API_KEY = "zap_key";
    // The URL of the application to be tested
    private static final String TARGET = "https://public-firing-range.appspot.com";
    private ClientApi api = new ClientApi(ADDRESS, PORT, API_KEY);

    public String scan(String target) {
        try {
            ApiResponse resp = api.spider.scan(target, null, null, null, null);
            String scanID;
            int progress, numberOfRecords;

            // URL discovery
            scanID = ((ApiResponseElement) resp).getValue();
            while (true) {
                Thread.sleep(1000);
                progress = Integer.parseInt(((ApiResponseElement) api.spider.status(scanID)).getValue());
                System.out.println("Spider progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }
            System.out.println("Spider completed");

            // Passive scan
            while (true) {
                Thread.sleep(2000);
                api.pscan.recordsToScan();
                numberOfRecords = Integer.parseInt(((ApiResponseElement) api.pscan.recordsToScan()).getValue());
                System.out.println("Number of records left for scanning : " + numberOfRecords);
                if (numberOfRecords == 0) {
                    break;
                }
            }
            System.out.println("Passive Scan completed");

            // Generate Report
            byte[] report = api.core.htmlreport();

            return generateReport(report);
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public String report() {
        try {
            byte[] report = api.core.htmlreport();

            return generateReport(report);
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private String generateReport(byte[] content) throws IOException {
        String filename = UUID.randomUUID().toString() + ".html";
        RandomAccessFile stream = new RandomAccessFile(filename, "rw");
        FileChannel channel = stream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(content.length);
        buffer.put(content);
        buffer.flip();
        channel.write(buffer);
        stream.close();
        channel.close();

        return System.getProperty("user.dir") + "/" + filename;
    }
}
