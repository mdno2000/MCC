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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import phdproject.mcc.mccproject.Service.MLIPService;
import phdproject.mcc.mccproject.Service.PSOService;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.application.ApplicationRun;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizationResult;
import phdproject.mcc.mccproject.psooptimizer.PSOParticle;
import phdproject.mcc.mccproject.resource.MobileDevice;
import phdproject.mcc.mccproject.resource.RescourceAPI;
import phdproject.mcc.mccproject.resource.RescourceLocation;

/**
 *
 * @author malkhalaileh
 */
@RestController
@RequestMapping("/mccproject/mlip/")

public class MLIPController {

    @Autowired
    MLIPService psoservice;

    @ResponseBody
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test() {
         return "test";
    }
    
    @ResponseBody
    @RequestMapping(value = "run/{model}", method = RequestMethod.POST)//, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PSOParticle runPSOOptimization(@RequestBody ApplicationRun application,
           @PathVariable Integer model) throws IOException, SQLException {
        //System.out.println(application.getUserID());
        PSOParticle result = psoservice.runMLIP(model, application);     
       
        return result;
        //MobileDevice mobile = ApplicationManager.getUserProfileByID(application.getUserID()).getUserMobile();
        //ApplicationManager.generateBoTWorkloadModel(model);
        
//        System.out.println("Average Data..." + datalist.stream().mapToDouble(Double::doubleValue).average());
//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//        Connection conn = RescourceAPI.getDBConnection();
//        PreparedStatement pstm = null;
//        PSOOptimizationResult result = new PSOOptimizationResult();
////        System.out.println("Recived appID" + application.getAppID());
////        application=ApplicationManager.getUserProfileByID(application.getUserID()).getApplicationRunByID(application.getAppID());
////        System.out.println("Processed appID" + application.getAppID()); 
//        mobile.setAvailNetwork(network);
//        mobile.setAvailBandwidth(RescourceAPI.getAvailBandwidth(mobile.getResID()));
//        result = psoservice.runPSOOptimization(application, r, scenario, model);
//        PSOOptimizationResult result2 = new PSOOptimizationResult();
//        try {
//
//            result2 = RescourceAPI.addOPTRecord(application, result, mobile, scenario, model, 0, timestamp);
//            for (RescourceLocation loc : result2.getOptimizationDecision()) {
//                //System.out.print("* " + loc + ", ");
//            }
//
//            RescourceAPI.addOPTRecord(application, result, mobile, 3, model, 1, timestamp);
//
//            for (RescourceLocation loc : result2.getOptimizationDecision()) {
//                // System.out.print("** " + loc + ", ");
//            }
//            RescourceAPI.addOPTRecord(application, result, mobile, 2, model, 1, timestamp);
//            for (RescourceLocation loc : result2.getOptimizationDecision()) {
//                //System.out.print("*** " + loc + ", ");
//            }
//        } catch (Exception ex) {
//            System.out.println(ex.getMessage());
//        } 

    }

}
