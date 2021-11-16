/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import phdproject.mcc.mccproject.applicationuser.UserProfile;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.resource.Cloudlet;
import phdproject.mcc.mccproject.resource.MobileDevice;
import phdproject.mcc.mccproject.resource.PublicCloud;
import phdproject.mcc.mccproject.resource.RescourceLocation;
import phdproject.mcc.mccproject.resource.Resource;

/**
 * Application Manager
 *
 * @author Mohammad Alkhalaileh
 */
@Service
public class ApplicationManager {

    public static int NUMTASK = 30; // change number of tasks here
    /**
     * randomize BoT arrival rate for public cloud and cloudelt, used for
     * queuing system play with these values for expirements, but please check
     * queue values
     */
    private static final double PNUMAPPSPERHOURMIN = 0.09;
    private static final double PNUMAPPSPERHOURMAX = 0.12;
    private static final double CNUMAPPSPERHOURMIN = 0.90;
    private static final double CNUMAPPSPERHOURMAX = 0.92;
    public static double[] TASKETEST = new double[NUMTASK];
    public static double[] TASKETESTS = new double[NUMTASK];
    public static double[] TASKCPULOAD = new double[NUMTASK];

    public static double getPNUMAPPSPERHOURMIN() {
        return PNUMAPPSPERHOURMIN;
    }

    public static double getPNUMAPPSPERHOURMAX() {
        return PNUMAPPSPERHOURMAX;
    }

    public static double getCNUMAPPSPERHOURMIN() {
        return CNUMAPPSPERHOURMIN;
    }

    public static double getCNUMAPPSPERHOURMAX() {
        return CNUMAPPSPERHOURMAX;
    }
    private static List<UserProfile> userProfileList = new ArrayList<>();
    private static int USERPROFILECOUNT = 100;
    public static int APPID = 1;

    public static void init() throws IOException { // inilize the model
        PublicCloud publiccloud = new PublicCloud("PCLOUD", "PCLOUD", RescourceLocation.PUBLICCLOUD);
        Cloudlet cloudlet = new Cloudlet("CLOUDLET", "CLOUDLET", RescourceLocation.CLOUDLET);
        Resource s3 = new Resource("S3", "S3", RescourceLocation.OTHER);
    }

    public static void regUserProfile(UserProfile newuser) {
        ApplicationManager.userProfileList.add(newuser);
    }

    public static UserProfile getUserProfileByID(int userid) {
        return userProfileList.stream().filter(p -> p.getUserID() == userid).findFirst().get();
    }

    public static List<UserProfile> getUserProfileList() {
        return ApplicationManager.userProfileList;
    }

    public static int requestUserPofileID() {
        ApplicationManager.USERPROFILECOUNT++;
        return ApplicationManager.USERPROFILECOUNT;
    }

    public void runBoTApplication(ApplicationRun applicationRun) {
        //UserProfile userProfile=applicationRun.getUserProfile();  
    }

    public static void runBoT(int userID, int mobileDeviceID, List<Task> userTasks) {
        MobileDevice userMobile = (MobileDevice) Resource.getRescourceByID(mobileDeviceID);
    }

    public static int getNUMTASK() {
        return NUMTASK;
    }

    public static int getUSERPROFILECOUNT() {
        return USERPROFILECOUNT;
    }

    public static int getAPPID() {
        return APPID;
    }

        public static void generateBoTWorkloadModel(int model) {

        // set ApplicationManager.TASKETEST and ApplicationManager.TASKETESTS manually
        int tindex = 0;

        if (model == 1 || model == 4) {
            ApplicationManager.TASKETEST[tindex] = 210.29;
            ApplicationManager.TASKETESTS[tindex] = 0.12916;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 300.12;
            ApplicationManager.TASKETESTS[tindex] = 0.18518;
            ApplicationManager.TASKCPULOAD[tindex] = 0.3;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 240.56;
            ApplicationManager.TASKETESTS[tindex] = 0.43719;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 500.56;
            ApplicationManager.TASKETESTS[tindex] = 0.13753;
            ApplicationManager.TASKCPULOAD[tindex] = 0.1;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 590.8;
            ApplicationManager.TASKETESTS[tindex] = 0.68220;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 440.56;
            ApplicationManager.TASKETESTS[tindex] = 0.231;
            ApplicationManager.TASKCPULOAD[tindex] = 0.2;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 330.78;
            ApplicationManager.TASKETESTS[tindex] = 0.4684;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 205.89;
            ApplicationManager.TASKETESTS[tindex] = 0.8416;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 360.78;
            ApplicationManager.TASKETESTS[tindex] = 0.78;
            ApplicationManager.TASKCPULOAD[tindex] = 0.85;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 560.76;
            ApplicationManager.TASKETESTS[tindex] = 0.4062;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 210.29;
            ApplicationManager.TASKETESTS[tindex] = 0.12916;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 300.12;
            ApplicationManager.TASKETESTS[tindex] = 0.18518;
            ApplicationManager.TASKCPULOAD[tindex] = 0.3;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 240.56;
            ApplicationManager.TASKETESTS[tindex] = 0.43719;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 500.56;
            ApplicationManager.TASKETESTS[tindex] = 0.13753;
            ApplicationManager.TASKCPULOAD[tindex] = 0.1;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 590.8;
            ApplicationManager.TASKETESTS[tindex] = 0.68220;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 440.56;
            ApplicationManager.TASKETESTS[tindex] = 0.231;
            ApplicationManager.TASKCPULOAD[tindex] = 0.2;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 330.78;
            ApplicationManager.TASKETESTS[tindex] = 0.4684;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 205.89;
            ApplicationManager.TASKETESTS[tindex] = 0.8416;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 360.78;
            ApplicationManager.TASKETESTS[tindex] = 0.78;
            ApplicationManager.TASKCPULOAD[tindex] = 0.85;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 560.76;
            ApplicationManager.TASKETESTS[tindex] = 0.4062;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 210.29;
            ApplicationManager.TASKETESTS[tindex] = 0.12916;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 300.12;
            ApplicationManager.TASKETESTS[tindex] = 0.18518;
            ApplicationManager.TASKCPULOAD[tindex] = 0.3;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 240.56;
            ApplicationManager.TASKETESTS[tindex] = 0.43719;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 500.56;
            ApplicationManager.TASKETESTS[tindex] = 0.13753;
            ApplicationManager.TASKCPULOAD[tindex] = 0.1;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 590.8;
            ApplicationManager.TASKETESTS[tindex] = 0.68220;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 440.56;
            ApplicationManager.TASKETESTS[tindex] = 0.231;
            ApplicationManager.TASKCPULOAD[tindex] = 0.2;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 330.78;
            ApplicationManager.TASKETESTS[tindex] = 0.4684;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 205.89;
            ApplicationManager.TASKETESTS[tindex] = 0.8416;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 360.78;
            ApplicationManager.TASKETESTS[tindex] = 0.78;
            ApplicationManager.TASKCPULOAD[tindex] = 0.85;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 560.76;
            ApplicationManager.TASKETESTS[tindex] = 0.4062;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;
        }
        if (model == 2) {
            ApplicationManager.TASKETEST[tindex] = 70.29;
            ApplicationManager.TASKETESTS[tindex] = 0.09916;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 22.12;
            ApplicationManager.TASKETESTS[tindex] = 0.18518;
            ApplicationManager.TASKCPULOAD[tindex] = 0.5;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 55.56;
            ApplicationManager.TASKETESTS[tindex] = 0.23719;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 69.56;
            ApplicationManager.TASKETESTS[tindex] = 0.13753;
            ApplicationManager.TASKCPULOAD[tindex] = 0.1;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 80.8;
            ApplicationManager.TASKETESTS[tindex] = 0.18220;
            ApplicationManager.TASKCPULOAD[tindex] = 0.7;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 52.56;
            ApplicationManager.TASKETESTS[tindex] = 0.131;
            ApplicationManager.TASKCPULOAD[tindex] = 0.2;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 61.78;
            ApplicationManager.TASKETESTS[tindex] = 0.1684;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 34.89;
            ApplicationManager.TASKETESTS[tindex] = 0.4416;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 36.78;
            ApplicationManager.TASKETESTS[tindex] = 0.78;
            ApplicationManager.TASKCPULOAD[tindex] = 0.85;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 23.76;
            ApplicationManager.TASKETESTS[tindex] = 0.1062;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 70.29;
            ApplicationManager.TASKETESTS[tindex] = 0.09916;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 22.12;
            ApplicationManager.TASKETESTS[tindex] = 0.18518;
            ApplicationManager.TASKCPULOAD[tindex] = 0.5;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 55.56;
            ApplicationManager.TASKETESTS[tindex] = 0.23719;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 69.56;
            ApplicationManager.TASKETESTS[tindex] = 0.13753;
            ApplicationManager.TASKCPULOAD[tindex] = 0.1;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 80.8;
            ApplicationManager.TASKETESTS[tindex] = 0.18220;
            ApplicationManager.TASKCPULOAD[tindex] = 0.7;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 52.56;
            ApplicationManager.TASKETESTS[tindex] = 0.131;
            ApplicationManager.TASKCPULOAD[tindex] = 0.2;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 61.78;
            ApplicationManager.TASKETESTS[tindex] = 0.1684;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 34.89;
            ApplicationManager.TASKETESTS[tindex] = 0.4416;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 36.78;
            ApplicationManager.TASKETESTS[tindex] = 0.78;
            ApplicationManager.TASKCPULOAD[tindex] = 0.85;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 23.76;
            ApplicationManager.TASKETESTS[tindex] = 0.1062;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 70.29;
            ApplicationManager.TASKETESTS[tindex] = 0.09916;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 22.12;
            ApplicationManager.TASKETESTS[tindex] = 0.18518;
            ApplicationManager.TASKCPULOAD[tindex] = 0.5;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 55.56;
            ApplicationManager.TASKETESTS[tindex] = 0.23719;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 69.56;
            ApplicationManager.TASKETESTS[tindex] = 0.13753;
            ApplicationManager.TASKCPULOAD[tindex] = 0.1;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 80.8;
            ApplicationManager.TASKETESTS[tindex] = 0.18220;
            ApplicationManager.TASKCPULOAD[tindex] = 0.7;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 52.56;
            ApplicationManager.TASKETESTS[tindex] = 0.131;
            ApplicationManager.TASKCPULOAD[tindex] = 0.2;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 61.78;
            ApplicationManager.TASKETESTS[tindex] = 0.1684;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 34.89;
            ApplicationManager.TASKETESTS[tindex] = 0.4416;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 36.78;
            ApplicationManager.TASKETESTS[tindex] = 0.78;
            ApplicationManager.TASKCPULOAD[tindex] = 0.85;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 23.76;
            ApplicationManager.TASKETESTS[tindex] = 0.1062;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;
        }
        if (model == 3) {

            ApplicationManager.TASKETEST[tindex] = 212.29;
            ApplicationManager.TASKETESTS[tindex] = 0.13453;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 160.12;
            ApplicationManager.TASKETESTS[tindex] = 0.16518;
            ApplicationManager.TASKCPULOAD[tindex] = 0.4;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 240.56;
            ApplicationManager.TASKETESTS[tindex] = 0.43719;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 190.56;
            ApplicationManager.TASKETESTS[tindex] = 0.12253;
            ApplicationManager.TASKCPULOAD[tindex] = 0.1;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 98.8;
            ApplicationManager.TASKETESTS[tindex] = 0.33220;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 44.56;
            ApplicationManager.TASKETESTS[tindex] = 0.131;
            ApplicationManager.TASKCPULOAD[tindex] = 0.4;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 112.78;
            ApplicationManager.TASKETESTS[tindex] = 0.3234;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 88.89;
            ApplicationManager.TASKETESTS[tindex] = 0.6416;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 360.78;
            ApplicationManager.TASKETESTS[tindex] = 0.78;
            ApplicationManager.TASKCPULOAD[tindex] = 0.85;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 125.76;
            ApplicationManager.TASKETESTS[tindex] = 0.2062;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 212.29;
            ApplicationManager.TASKETESTS[tindex] = 0.13453;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 160.12;
            ApplicationManager.TASKETESTS[tindex] = 0.16518;
            ApplicationManager.TASKCPULOAD[tindex] = 0.4;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 240.56;
            ApplicationManager.TASKETESTS[tindex] = 0.43719;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 190.56;
            ApplicationManager.TASKETESTS[tindex] = 0.12253;
            ApplicationManager.TASKCPULOAD[tindex] = 0.1;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 98.8;
            ApplicationManager.TASKETESTS[tindex] = 0.33220;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 44.56;
            ApplicationManager.TASKETESTS[tindex] = 0.131;
            ApplicationManager.TASKCPULOAD[tindex] = 0.4;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 112.78;
            ApplicationManager.TASKETESTS[tindex] = 0.3234;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 88.89;
            ApplicationManager.TASKETESTS[tindex] = 0.6416;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 360.78;
            ApplicationManager.TASKETESTS[tindex] = 0.78;
            ApplicationManager.TASKCPULOAD[tindex] = 0.85;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 125.76;
            ApplicationManager.TASKETESTS[tindex] = 0.2062;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 212.29;
            ApplicationManager.TASKETESTS[tindex] = 0.13453;
            ApplicationManager.TASKCPULOAD[tindex] = 0.6;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 160.12;
            ApplicationManager.TASKETESTS[tindex] = 0.16518;
            ApplicationManager.TASKCPULOAD[tindex] = 0.4;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 240.56;
            ApplicationManager.TASKETESTS[tindex] = 0.43719;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 190.56;
            ApplicationManager.TASKETESTS[tindex] = 0.12253;
            ApplicationManager.TASKCPULOAD[tindex] = 0.1;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 98.8;
            ApplicationManager.TASKETESTS[tindex] = 0.33220;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 44.56;
            ApplicationManager.TASKETESTS[tindex] = 0.131;
            ApplicationManager.TASKCPULOAD[tindex] = 0.4;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 112.78;
            ApplicationManager.TASKETESTS[tindex] = 0.3234;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 88.89;
            ApplicationManager.TASKETESTS[tindex] = 0.6416;
            ApplicationManager.TASKCPULOAD[tindex] = 0.9;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 360.78;
            ApplicationManager.TASKETESTS[tindex] = 0.78;
            ApplicationManager.TASKCPULOAD[tindex] = 0.85;

            tindex++;
            ApplicationManager.TASKETEST[tindex] = 125.76;
            ApplicationManager.TASKETESTS[tindex] = 0.2062;
            ApplicationManager.TASKCPULOAD[tindex] = 0.8;
        } 
    }

    public static void uploadBoTWorkloadModelToApplication(ApplicationRun application) {

        for (int tIndex = 0; tIndex < application.getUserAppTask().size(); tIndex++) {
            application.getUserAppTask().get(tIndex).setTASKETEST(ApplicationManager.TASKETEST[tIndex]);
            application.getUserAppTask().get(tIndex).setTASKETESTS(ApplicationManager.TASKETESTS[tIndex]);
            application.getUserAppTask().get(tIndex).setTASKCPULOAD(ApplicationManager.TASKCPULOAD[tIndex]);
        }  
    }

}