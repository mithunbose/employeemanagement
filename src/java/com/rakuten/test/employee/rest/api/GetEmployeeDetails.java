package com.rakuten.test.employee.rest.api;

import com.google.gson.Gson;
import com.rakuten.test.employee.persistence.PersistanceHelper;
import java.text.ParseException;
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
@Path("/getEmployeeDetails")
public class GetEmployeeDetails {

    PersistanceHelper persistanceHelper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmployeeDetails(@QueryParam("page") String page) throws ParseException {
        if (null == persistanceHelper) {
            persistanceHelper = new PersistanceHelper();
        }
        Gson gson = new Gson();
        String respJson = gson.toJson(persistanceHelper.getPaginatedEmployeeDetails(page));
        return Response.ok(respJson, MediaType.APPLICATION_JSON).build();

    }
}
