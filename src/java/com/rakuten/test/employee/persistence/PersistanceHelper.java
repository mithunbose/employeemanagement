package com.rakuten.test.employee.persistence;

import com.rakuten.test.employee.dto.EmployeeDto;
import com.rakuten.test.employee.rest.api.FileUploader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mithbose
 */
public class PersistanceHelper {

    public boolean persistEmployeeDetails(final EmployeeDto employee, boolean isNewEmployee) {
        try {
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/EmployeeManager", "root", "root");
            String statement = "insert into EMPLOYEE \n"
                    + "(employeeid, name, department, designation, salary, joiningdate)\n"
                    + "values(?,?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(statement);
            if (isNewEmployee) {
                stmt.setString(1, UUID.randomUUID().toString());
            } else {
                stmt.setString(1, employee.getEmployeeId());
            }
            stmt.setString(2, employee.getName());
            stmt.setString(3, employee.getDepartment());
            stmt.setString(4, employee.getDesignation());
            stmt.setDouble(5, employee.getSalary());
            stmt.setDate(6, new java.sql.Date(employee.getJoiningDate().getTime()));
            stmt.executeUpdate();
            stmt.close();
            con.close();
            return true;
        } catch (SQLException ex) {
            // Did not persist employee information. Throw this in the UI
            Logger.getLogger(FileUploader.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<EmployeeDto> getPaginatedEmployeeDetails(final String page) {
        try {
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/EmployeeManager", "root", "root");
            String statement = "select * from EMPLOYEE \n"
                    + "order by employeeid offset ? rows \n"
                    + "fetch first 10 rows only";
            PreparedStatement stmt = con.prepareStatement(statement);
            String limit = String.valueOf(Integer.parseInt(page) * 10);
            stmt.setString(1, limit);
            ResultSet set = stmt.executeQuery();
            List<EmployeeDto> edList = getEmployeeDetailsFromResult(set);
            set.close();
            stmt.close();
            con.close();
            return edList;
        } catch (SQLException | ParseException ex) {
            Logger.getLogger(PersistanceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return new ArrayList<EmployeeDto>();
        }
    }

    public List<EmployeeDto> getEmployeeDetailsFromResult(ResultSet set) throws SQLException, ParseException {
        List<EmployeeDto> edList = new ArrayList<EmployeeDto>();
        while (set.next()) {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(set.getString("joiningdate"));
            EmployeeDto employee = EmployeeDto.builder().employeeId(set.getString("employeeid"))
                    .name(set.getString("name")).department(set.getString("department"))
                    .designation(set.getString("designation"))
                    .salary(Double.parseDouble(set.getString("salary")))
                    .joiningDate(date)
                    .build();
            edList.add(employee);
        }
        return edList;
    }

    public boolean deleteRecord(final String employeeId) {
        try {
            Connection con = DriverManager.getConnection("jdbc:derby://localhost:1527/EmployeeManager", "root", "root");
            String statement = "delete from EMPLOYEE \n"
                    + "where employeeId = ?";
            PreparedStatement stmt = con.prepareStatement(statement);
            stmt.setString(1, employeeId);
            stmt.executeUpdate();
            stmt.close();
            con.close();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PersistanceHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

}
