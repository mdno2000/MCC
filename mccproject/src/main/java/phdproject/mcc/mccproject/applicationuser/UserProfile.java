/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.applicationuser;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.application.ApplicationRun;
import phdproject.mcc.mccproject.application.ApplicationRunStatus;
import phdproject.mcc.mccproject.application.ExecutionScenario;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizationResult;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizer;
import phdproject.mcc.mccproject.resource.MobileDevice;
import phdproject.mcc.mccproject.resource.RescourceLocation;

/**
 *
 * User Profile includes mobile device data and list of application to run
 */
public class UserProfile {

    int _userID;
    String username;
    MobileDevice userMobile;
     List<ApplicationRun> userApplications;
    int userAppCount;
    int appID;

    public int getUserAppCount() {
        return userAppCount;
    }

    public void setUserAppCount(int userAppCount) {
        this.userAppCount = userAppCount;
    }

    public UserProfile(String username, String resFile) throws IOException {
//        userMobile = new MobileDevice(resFile, resFile, RescourceLocation.MOBILE);
//        userApplications = new ArrayList<>();
//        userAppCount = 0;
//        _userID = ApplicationManager.requestUserPofileID();
//        this.username = username;
//        ApplicationManager.regUserProfile(this); // add user profile o user lists
    }

    public UserProfile() {
        userApplications = new ArrayList<>();
    }

    public void loadApplicationTasks() {

    }

    /**
     *
     * @param filename : csv file for application definition
     * @param appID : type of application structure , many applications can have
     * the same structure
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public int registerBoTApplication(String filename, int appID) throws FileNotFoundException, IOException {
        int UserAPPID = 0;
        try {
            if (appID == 0) {
                UserAPPID = ApplicationManager.APPID;
                ApplicationManager.APPID++;
            } else {
                UserAPPID = appID;
            }
            getUserApplications().add(new ApplicationRun(this.getUserID(), filename, userAppCount, UserAPPID));
            userAppCount++; // number of user applications
        } catch (Exception e) {

        }
        this.appID = UserAPPID;
        return UserAPPID;
    }

    public int getAppID() {
        return appID;
    }

    public void setAppID(int appID) {
        this.appID = appID;
    }

    public void runNextWaitingApplication(int option, double[] dsize, RescourceLocation[] source, RescourceLocation[] dist) throws IOException {
        for (ApplicationRun application : getUserApplications()) {
            if (application.getApplicationStatus() == ApplicationRunStatus.WAITING) { // check application status     
                this.getUserMobile().randmizeConnection();
                //PSOOptimizationResult optRes = PSOOptimizer.runOptimizer(application,option,dsize,source,dist); // run optimizer                
                application.setApplicationStatus(ApplicationRunStatus.FINISHED); // set to finished 
            }
        }
    }

    public ApplicationRun getApplicationRunByID(int appID) {
        return getUserApplications().stream().filter(p -> p.getAppID() == appID).findFirst().get();

    }

    //public 
    public int getUserID() {
        return _userID;
    }

    public void setUserID(int _userID) {
        this._userID = _userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public MobileDevice getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(MobileDevice userMobile) {
        this.userMobile = userMobile;
    }

    public List<ApplicationRun> getUserApplications() {
        return userApplications;
    }

    public void setUserApplications(List<ApplicationRun> userApplications) {
        this.userApplications = userApplications;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UserProfile{ID: ").append(_userID).append(", Name: ").append(username).append(", userMobile: ").append(userMobile).append(", Number of Applications=").append(userAppCount).append('}');
        sb.append("\nList of Applications:\n");
        for (ApplicationRun app : userApplications) {
            sb.append(app.toString()).append("\n");
        }
        return sb.toString();
    }

}
