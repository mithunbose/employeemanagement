package com.rakuten.test.employee.rest.api;

import com.google.gson.Gson;
import com.rakuten.test.employee.dto.FileDownloadResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/download")
public class FileDownloader {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String downloadFile() throws IOException {
        String contents = new String(Files.readAllBytes(Paths.get("uploadedCsv.csv")));
        Gson gson = new Gson();
        FileDownloadResponse fdr = new FileDownloadResponse();
        fdr.setCsvString(contents);
        return gson.toJson(fdr);
    }

}


