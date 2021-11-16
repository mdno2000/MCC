/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import phdproject.mcc.mccproject.Service.PSOService;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.application.ApplicationRun;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizationResult;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizer;
import phdproject.mcc.mccproject.resource.MobileDevice;
import phdproject.mcc.mccproject.resource.RescourceAPI;
import phdproject.mcc.mccproject.resource.RescourceLocation;

/**
 *
 * @author Administrator
 */
@RestController
@RequestMapping("/mccproject/pso/")
public class PSOController {

    @Autowired
    PSOService psoservice;

    @ResponseBody
    @RequestMapping(value = "run/{d}/{model}/{scenario}/{network}", method = RequestMethod.POST)//, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PSOOptimizationResult runPSOOptimization(@RequestBody ApplicationRun application,
            @PathVariable String d, @PathVariable Integer model, @PathVariable Integer scenario, @PathVariable Integer network) throws IOException, SQLException {
        for (ApplicationRun applicationRun : ApplicationManager.getUserProfileByID(application.getUserID()).getUserApplications()) {
            System.out.println("USER ID: " + application.getUserID() + " Application ID: " + applicationRun.getAppID());
        }
        ObjectMapper mapper = new ObjectMapper();
        d = d + "]";
        Double[] r = mapper.readValue(d, Double[].class);
        ArrayList<Double> datalist = new ArrayList<>();
        for (Double val : r) {
            datalist.add(val);
        }

        ApplicationManager.generateBoTWorkloadModel(model);
        System.out.println("Average Data..." + datalist.stream().mapToDouble(Double::doubleValue).average());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Connection conn = RescourceAPI.getDBConnection();
        PreparedStatement pstm = null;
        PSOOptimizationResult result = new PSOOptimizationResult();
//        System.out.println("Recived appID" + application.getAppID());
//        application=ApplicationManager.getUserProfileByID(application.getUserID()).getApplicationRunByID(application.getAppID());
//        System.out.println("Processed appID" + application.getAppID()); 
        MobileDevice mobile = ApplicationManager.getUserProfileByID(application.getUserID()).getUserMobile();
        mobile.setAvailNetwork(network);
        mobile.setAvailBandwidth(RescourceAPI.getAvailBandwidth(mobile.getResID()));
        result = psoservice.runPSOOptimization(application, r, scenario,model);
        PSOOptimizationResult result2=new PSOOptimizationResult();
        try {
        
        result2=RescourceAPI.addOPTRecord(application, result, mobile, scenario, model, 0, timestamp);
         for (RescourceLocation loc : result2.getOptimizationDecision()) {
                //System.out.print("* " + loc + ", ");
            }
        
        RescourceAPI.addOPTRecord(application, result, mobile, 3, model, 1, timestamp); 
        
        for (RescourceLocation loc : result2.getOptimizationDecision()) {
               // System.out.print("** " + loc + ", ");
            }
        RescourceAPI.addOPTRecord(application, result, mobile, 2, model, 1, timestamp); 
        for (RescourceLocation loc : result2.getOptimizationDecision()) {
                //System.out.print("*** " + loc + ", ");
            }
        }
        catch(Exception ex) {
            System.out.println(ex.getMessage());
        }

        return result2;

    }

}
