/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.Service;

import java.io.IOException;
import org.springframework.stereotype.Service;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.applicationuser.UserProfile;

/**
 *
 * @author Administrator
 */
@Service
public class ApplicationManagerService {
    public void init() throws IOException
    {
        ApplicationManager.init();
    }
    public UserProfile getUser(int userID)
    {
        return ApplicationManager.getUserProfileByID(userID);
    }
    public void updateEstimations(double [] est, double [] ests, double [] cpuload){
        ApplicationManager.TASKETEST = est;  
        ApplicationManager.TASKETESTS = ests;  
        ApplicationManager.TASKCPULOAD = cpuload;  
   }
   public void generateBoTWorkloadModel(int model){
       ApplicationManager.generateBoTWorkloadModel(model);
   } 
    
}
