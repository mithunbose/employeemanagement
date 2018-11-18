package com.rakuten.test.employee.rest.api;

import com.rakuten.test.employee.dto.EmployeeDto;
import au.com.bytecode.opencsv.CSVReader;
import com.google.gson.Gson;
import com.rakuten.test.employee.dto.FileUploadResponse;
import com.rakuten.test.employee.utils.ValidationUtils;
import com.rakuten.test.employee.persistence.PersistanceHelper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.Produces;
import org.glassfish.jersey.media.multipart.FormDataParam;

/**
 *
 * @author mithbose
 */
@Path("/upload")
public class FileUploader {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private PersistanceHelper persistanceHelper;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public String uploadFile(@FormDataParam("file") InputStream uploadedInputStream) throws ParseException, SQLException {
        //TODO: manage exceptions properly
        if (persistanceHelper == null) {
            persistanceHelper = new PersistanceHelper();
        }
        if (uploadedInputStream == null) {
            return ("Invalid form data");
        }
        FileUploadResponse fur;
        try {
            fur = processFile(uploadedInputStream);
        } catch (IOException e) {
            return ("Can not process file");
        }
        Gson gson = new Gson();
        return gson.toJson(fur);
    }

    private FileUploadResponse processFile(InputStream inputStream)
            throws IOException, ParseException, SQLException {
        boolean isDbPersisted = true;
        int invalidCount = 0, validCount = 0;
        StringBuilder validatedFile = new StringBuilder();
        CSVReader br = new CSVReader(new InputStreamReader(inputStream));
        String nextLine[];
        while ((nextLine = br.readNext()) != null) {
            String name = nextLine[0], department = nextLine[1], designationString = nextLine[2],
                    salary = nextLine[3], joiningDate = nextLine[4];
            String errorMessage = ValidationUtils.validateInputs(name, department, designationString, salary, joiningDate);
            validatedFile.append(String.join(",", nextLine));
            if (!errorMessage.isEmpty()) {
                invalidCount++;
                validatedFile.append(",").append(errorMessage);
            } else {
                SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
                Date date = formatter.parse(joiningDate);
                EmployeeDto employee = EmployeeDto.builder().name(name).department(department)
                        .designation(designationString)
                        .salary(Double.parseDouble(salary))
                        .joiningDate(date)
                        .build();
                isDbPersisted = isDbPersisted && persistanceHelper.persistEmployeeDetails(employee, true);
                validCount++;
            }
            validatedFile.append("\n");
        }
        try (PrintWriter out = new PrintWriter("uploadedCsv.csv")) {
            out.println(validatedFile.toString());
            out.flush();
        }
        return getFileUploadResponse(invalidCount, validCount, isDbPersisted);
    }

    private FileUploadResponse getFileUploadResponse(int invalidCount, int validCount, boolean dbPersisted) {

        FileUploadResponse fur = new FileUploadResponse();
        fur.setInvalidRowCount(invalidCount);
        fur.setValidRowCount(validCount);
        fur.setDbPersistanceSuccess(dbPersisted);
        return fur;
    }
}
