package com.rakuten.test.employee.rest.api;

import com.rakuten.test.employee.dto.EmployeeDto;
import com.google.gson.Gson;
import com.rakuten.test.employee.dto.UpdateDetailsResponse;
import com.rakuten.test.employee.utils.ValidationUtils;
import com.rakuten.test.employee.persistence.PersistanceHelper;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author mithbose
 */
@Path("/update")
@Consumes(MediaType.APPLICATION_JSON)
public class UpdateEmployeeDetails {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private PersistanceHelper persistanceHelper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateDetails(@QueryParam("name") String name,
            @QueryParam("employeeId") String employeeId,
            @QueryParam("department") String department,
            @QueryParam("designation") String designation,
            @QueryParam("salary") String salary,
            @QueryParam("date") String date
    ) throws ParseException, SQLException {
        if (persistanceHelper == null) {
            persistanceHelper = new PersistanceHelper();
        }
        String validationResult = ValidationUtils.validateInputs(name, department, designation, salary, date);
        if (validationResult.length() > 0) {
            return handleValidationErrorResponse(validationResult);
        }
        boolean isNewEmployee = true;
        if (null != employeeId && employeeId.length() != 0) {
            // TODO: handle exception when emplyee is not present in db, but request for updating came
            boolean isDeleted = persistanceHelper.deleteRecord(employeeId);
            if (!isDeleted) {
                return handleDataDeletionNotPossible();
            }
            isNewEmployee = false;
        }
        UpdateDetailsResponse udr = new UpdateDetailsResponse();
        udr.setStatus(persistEmployeeDetails(employeeId, name, department, designation, salary, date, isNewEmployee));
        Gson gson = new Gson();
        return Response.ok(gson.toJson(udr), MediaType.APPLICATION_JSON).build();

    }

    private Response handleValidationErrorResponse(final String validationResult) {
        UpdateDetailsResponse udr = new UpdateDetailsResponse();
        udr.setStatus(false);
        udr.setErrorDetails(validationResult);
        Gson gson = new Gson();
        return Response.ok(gson.toJson(udr), MediaType.APPLICATION_JSON).build();
    }

    private Response handleDataDeletionNotPossible() {
        return Response.serverError().build();
    }

    private boolean persistEmployeeDetails(String employeeId, String name, String department, String designation, String salary, String date, boolean isNewEmployee) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            EmployeeDto employee = EmployeeDto.builder().employeeId(employeeId).name(name).department(department)
                    .designation(designation)
                    .salary(Double.parseDouble(salary))
                    .joiningDate(formatter.parse(date))
                    .build();
            return persistanceHelper.persistEmployeeDetails(employee, isNewEmployee);
        } catch (ParseException ex) {
            Logger.getLogger(UpdateEmployeeDetails.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

}
