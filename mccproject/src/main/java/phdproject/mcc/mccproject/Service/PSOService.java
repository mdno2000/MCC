/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;
import phdproject.mcc.mccproject.application.ApplicationRun;
import phdproject.mcc.mccproject.application.ExecutionScenario;
import phdproject.mcc.mccproject.applicationuser.UserProfile;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizationResult;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizer;

/**
 *
 * @author Administrator
 */
@Service
public class PSOService {
    @Autowired
    ApplicationManagerService appservice;
    public PSOOptimizationResult runPSOOptimization(ApplicationRun application,Double d[],int scenario,int model)
    {
        PSOOptimizationResult result=new PSOOptimizationResult();        
        int numtasks =10;//application.getUserAppTask().size();
        //double d[] = ExecutionScenario.randmizeDataSize(numtasks); 
        UserProfile profile=appservice.getUser(application.getUserID()); 
        profile.getUserAppCount();
        profile.setUserAppCount(profile.getUserAppCount()+1);
        application.setAppID( profile.getUserAppCount());        
        appservice.getUser(application.getUserID()).getUserApplications().add(application);
        // System.out.println("Application ID "+application.getAppID());

        //System.out.println("User ID "+profile.getUserID());
        //System.out.println("#Applications "+profile.getUserApplications().size());
        result=PSOOptimizer.runOptimizer(application,d,scenario,model);        
        return result;
    }
}
