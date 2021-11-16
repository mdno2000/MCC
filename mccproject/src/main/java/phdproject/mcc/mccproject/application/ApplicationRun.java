/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.application;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import phdproject.mcc.mccproject.applicationuser.UserProfile;
import phdproject.mcc.mccproject.bagOfTask.Task;

/**
 *
 * it has all configuration to run a user application
 */
public class ApplicationRun {

    int _appID;
    int _userID; // user profile ID 
    int _appType;
    ApplicationRunStatus _applicationStatus;
    List<Task> userAppTask; // BoT of task to be  processed 

    public int getAppType() {
        return _appType;
    }

    /**
     *
     * @param _userID
     * @param filename; get data from file
     * @param appID
     * @param appType
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void setAppType(int _appType) {
        this._appType = _appType;
    }

    public ApplicationRun() {
        //userAppTask=new ArrayList<Task>();
    }

    public ApplicationRun(int _userID, String filename, int appID, int appType) throws FileNotFoundException, IOException {
        this._userID = _userID;
        this._appID = appID;
        this._appType = appType;
        this._applicationStatus = ApplicationRunStatus.WAITING;
        userAppTask = new ArrayList<>();
        String path = "src\\resources\\" + filename + ".csv";
        FileReader reader = new FileReader(path);
        BufferedReader br = new BufferedReader(reader);
        String line = "";
        while ((line = br.readLine()) != null) {
            userAppTask.add(new Task(line, ","));  // refer to task class to see how add a task from a csv line
        }
    }

    public int getAppID() {
        return _appID;
    }

    public void setAppID(int _appID) {
        this._appID = _appID;
    }

    public int getUserID() {
        return _userID;
    }

    public void setUserID(int _userID) {
        this._userID = _userID;
    }

    public ApplicationRunStatus getApplicationStatus() {
        return _applicationStatus;
    }

    public void setApplicationStatus(ApplicationRunStatus _applicationStatus) {
        this._applicationStatus = _applicationStatus;
    }

    public List<Task> getUserAppTask() {
        return userAppTask;
    }

    public void setUserAppTask(List<Task> userAppTask) {
        this.userAppTask = userAppTask;
    }

    public void resetApplication() {
        for (Task t : getUserAppTask()) {
             t.resetTaskInfo();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ApplicationRun{ App ID=").append(_appID).append(", User ID=").append(_userID).append(", Application Status=").append(_applicationStatus).append('}');
        sb.append("\nList Of Tasks\n");
        for (Task t : getUserAppTask()) {
            sb.append(t.toString()).append("\n");
        }
        return sb.toString();
    }

}
