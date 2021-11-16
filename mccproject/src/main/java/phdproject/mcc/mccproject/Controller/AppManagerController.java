/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.Controller;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import phdproject.mcc.mccproject.Service.ApplicationManagerService;
import phdproject.mcc.mccproject.Service.ResourceService;
import phdproject.mcc.mccproject.Service.UserProfileService;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.application.ApplicationRun;
import phdproject.mcc.mccproject.applicationuser.UserProfile;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizationResult;
import phdproject.mcc.mccproject.resource.MobileDevice;
import phdproject.mcc.mccproject.resource.PublicCloud;
import phdproject.mcc.mccproject.resource.RescourceAPI;
import phdproject.mcc.mccproject.resource.Resource;

/**
 *
 * @author Mohammad Alkhalaileh
 */
@RestController
@RequestMapping("/mccproject/app/")
public class AppManagerController {

    @Autowired
    ApplicationManagerService appmanager;
    @Autowired
    ResourceService resourceservice;
    @Autowired
    UserProfileService userprofileservice;

    @ResponseBody
    @RequestMapping(value = "init", method = RequestMethod.GET)
    public String init() {
        try {
            appmanager.init();
            appmanager.generateBoTWorkloadModel(1);
            return "OK";
        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    @ResponseBody
    @RequestMapping(value = "publiccloud/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PublicCloud getpubliccloud() {
        return resourceservice.getPublicCloud();
    }

    @ResponseBody
    @RequestMapping(value = "getmobile/{id}/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public MobileDevice getmobile(@PathVariable Integer id) {
        return userprofileservice.getProfile(id).getUserMobile();
    }

    @ResponseBody
    @RequestMapping(value = "updateEstimations", method = RequestMethod.GET)
    public String updateEstimations(Double est, Double[] ests, Double[] cpuload) {
        try {
            System.out.println("EST..." + est);
            //appmanager.updateEstimations(est, ests, cpuload);
            return String.valueOf(est);
        } catch (Exception ex) {
            return ex.getMessage();
        }

    }

    @ResponseBody
    @RequestMapping(value = "runbot", method = RequestMethod.POST)//, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Object> runBoT(@RequestBody List<Task> BoT) throws IOException, MalformedURLException, InterruptedException, URISyntaxException {
        appmanager.generateBoTWorkloadModel(3);
        long startTime = System.currentTimeMillis();
        double totalTime = 0.0;
        List<Object> allvalues = new ArrayList<>();
        allvalues.add(new Long(0));
        allvalues.add(new Double(0.0));
        allvalues.add(new Long(0));
        allvalues.add(new Double(0.0));

        for (int tIndex = 0; tIndex < BoT.size(); tIndex++) {
            Task task = BoT.get(tIndex);
            List<Object> values = runTask(task);
            System.out.println("Value: " + values.get(0));
            System.out.println("Value: " + values.get(1));
            System.out.println("Value: " + values.get(2));
            System.out.println("Value: " + values.get(3));

            allvalues.set(0, (long) allvalues.get(0) + (long) values.get(0));
            allvalues.set(1, (double) allvalues.get(1) + (double) values.get(1));
            allvalues.set(2, (long) allvalues.get(2) + (long) values.get(2));
            allvalues.set(3, (double) allvalues.get(3) + (double) values.get(3));
        }
        //long finishTime = System.currentTimeMillis();
        System.out.println("Public cloud");
        System.out.println("Value: downlaod time  " + (long) allvalues.get(0));
        System.out.println("Value: downlaod cost" + (double) allvalues.get(1));
        System.out.println("Value: exeution time" + (long) allvalues.get(2));
        System.out.println("Value: execution cost" + (double) allvalues.get(3));
        
        return allvalues;
    }

    private List<Object> runTask(Task t) throws MalformedURLException, IOException, InterruptedException, URISyntaxException {
        long startTime = System.currentTimeMillis();
        System.out.println("Start Task.." + t.getTaskID());
        System.out.println("Start Dowloding file..");
        String filePath = "";
        //String file=t.getTaskres().get//
        //long startTime = System.currentTimeMillis();
        double datasize = 0;
        //URL url = new URL(t.getInputFileRef());
        byte[] bytes = null;

        switch (t.getInputFileLocationType()) {
            case PUBLICCLOUD:
                filePath = t.getInputFileRef();
                filePath = "/data/"+filePath ;
                String packetName = "mohammadphd";
                String objectName = filePath.split("/data")[1];
                String filename = filePath.split("/data/")[1];
                filename = "c://"+filename;
                objectName = "data" + objectName;
                //System.out.println("File path (public cloud): " + objectName);
                ObjectMetadata objectMetadata = RescourceAPI.amazonS3.getObjectMetadata(packetName, objectName);
                long maxLength = objectMetadata.getContentLength();
                int length = (int) (objectMetadata.getContentLength() / 20);

                S3ObjectInputStream s3ObjectInputStream = RescourceAPI.amazonS3.getObject(packetName, objectName).getObjectContent();
                byte[] data = new byte[length];
                int offset = 0;
                int count = 0;
                long totalDataRead = 0;
                int c = 1;
                long fileSize = 0;
                File file = new File(filename);                 
                file.createNewFile();
                
                //File file = new File(objectName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                Files.copy(s3ObjectInputStream, new File(filename).toPath(),StandardCopyOption.REPLACE_EXISTING); //location to local path
//                try {
//                    while ((count = s3ObjectInputStream.available()) >= 1 && fileSize <= maxLength) {
//                        data = new byte[Math.min(s3ObjectInputStream.available(), length)];
//                        s3ObjectInputStream.read(data);
//                        System.out.println("Avaliable data: "+s3ObjectInputStream.available());
//                        //outputStream.write(data);
//                        //publishProgress("--Available is " + s3ObjectInputStream.available(), "");
//                        c++;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    filePath = t.getInputFileRef();
//                }
                break;

            case S3:
                filePath = t.getInputFileRef();
                filePath = "/data/"+filePath ;
                packetName = "mohammadphd2";
                objectName = filePath.split("/data")[1];
                filename = filePath.split("/data/")[1];
                filename = "c://"+filename;
                objectName = "data" + objectName;
                //System.out.println("File path (S3): " + objectName);
                objectMetadata = RescourceAPI.amazonS3.getObjectMetadata(packetName, objectName);
                maxLength = objectMetadata.getContentLength();
                length = (int) (objectMetadata.getContentLength() / 20);
                s3ObjectInputStream = RescourceAPI.amazonS3.getObject(packetName, objectName).getObjectContent();
                data = new byte[length];
                offset = 0;
                count = 0;
                totalDataRead = 0;
                c = 1;
                fileSize = 0;
                file = new File(filename);                
                file.createNewFile();
                
                //file = new File(objectName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                 Files.copy(s3ObjectInputStream, new File(filename).toPath(),StandardCopyOption.REPLACE_EXISTING); //location to local path
//                try {
//                    while ((count = s3ObjectInputStream.available()) > 1 && fileSize <= maxLength) {
//                        data = new byte[Math.min(s3ObjectInputStream.available(), length)];
//                        s3ObjectInputStream.read(data);
//                        System.out.println("Avaliable data: "+s3ObjectInputStream.available());
//                        //outputStream.write(data);
//                        //publishProgress("--Available is " + s3ObjectInputStream.available(), "");
//                        c++;
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    filePath = t.getInputFileRef();
//                }
                break;
            default:
                System.out.println("Path: (mobile) " + t.getInputFileRef());
               //s3.amazonaws.com/mohammadphd2/data/trip_data_12.zip

                filePath = t.getInputFileRef(); 
                filePath = "/data/"+filePath ;
                try {
                    packetName = "mohammadphd2";
                    objectName = filePath.split("/data")[1];
                    filename = filePath.split("/data/")[1];
                    filename = "c://"+filename;
                    objectName = "data" + objectName;
                    System.out.println("Path: () " + objectName);
                    objectMetadata = RescourceAPI.amazonS3.getObjectMetadata(packetName, objectName);
                    maxLength = objectMetadata.getContentLength();
                    length = (int) (objectMetadata.getContentLength() / 20);
                    s3ObjectInputStream = RescourceAPI.amazonS3.getObject(packetName, objectName).getObjectContent();
                    data = new byte[length];
                    offset = 0;
                    count = 0;
                    totalDataRead = 0;
                    c = 1;
                    fileSize = 0;
                    file = new File(filename);            
                    file.createNewFile();
                
                    //file = new File(objectName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                     Files.copy(s3ObjectInputStream, new File(filename).toPath(),StandardCopyOption.REPLACE_EXISTING); //location to local path
                } catch (Exception ex) {
                    System.out.println("Exception " + ex.getMessage());
                }
                try {
                    packetName = "mohammadphd";
                    objectName = filePath.split("/data")[1];
                    filename = filePath.split("/data/")[1];
                    filename = "c://"+filename;
                    objectName = "data" + objectName;
                    System.out.println("Path: " + objectName);
                    objectMetadata = RescourceAPI.amazonS3.getObjectMetadata(packetName, objectName);
                    maxLength = objectMetadata.getContentLength();
                    length = (int) (objectMetadata.getContentLength() / 20);
                    s3ObjectInputStream = RescourceAPI.amazonS3.getObject(packetName, objectName).getObjectContent();
                    data = new byte[length];
                    offset = 0;
                    count = 0;
                    totalDataRead = 0;
                    c = 1;
                    fileSize = 0;
                    file = new File(filename);               
                    file.createNewFile();
                    
                    //file = new File(objectName);
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                     Files.copy(s3ObjectInputStream, new File(filename).toPath(),StandardCopyOption.REPLACE_EXISTING); //location to local path
                } catch (Exception ex) {
                    System.out.println("Exception " + ex.getMessage());
                }

                break;
        }
        //long startTime = System.currentTimeMillis();
        List<Object> values = new ArrayList<>();
        double downloadtime = (t.getTaskres().get_inputDataSize() / 12.9);
        values.add(((System.currentTimeMillis() - startTime) / 1000)); // download time 
        values.add((t.getTaskres().get_inputDataSize() * 0.02) / 1024); // download cost $
        Thread.sleep((long) (1 * 1000));
        System.out.println("Finish dowonloading file..." + (System.currentTimeMillis() - startTime) / 1000);
        startTime = System.currentTimeMillis();
        //System.out.println("Public Cloud: Run Task " + t.getTaskID());
        double mips = ApplicationManager.TASKETEST[t.getTaskID() - 1];
        double datas = ApplicationManager.TASKETESTS[t.getTaskID() - 1];
        double taskCPULOAD = ApplicationManager.TASKCPULOAD[t.getTaskID() - 1];
        datasize = t.getTaskres().get_inputDataSize();
        //System.out.println("info: " + mips + ", " + datas + ", " + taskCPULOAD + ", " + datasize);
        int cores = 16;
        try {
            //runTask(taskID,mips,datasize,datas,cores);
            double totalMIPS = mips + (datasize * datas);
            double et = (totalMIPS * 1000) / (cores * 1.2);
            //System.out.println("et: " + et);
            while (System.currentTimeMillis() - startTime < et) {
                // Every 100ms, sleep for the percentage of unladen time
                if (System.currentTimeMillis() % 100 == 0) {
                    Thread.sleep((long) Math.floor((1 - taskCPULOAD) * 100));
                }
                if (System.currentTimeMillis() % 1000 == 0) {
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // load data file
        double totaltime = 0;
        long finishTime = System.currentTimeMillis();
        //System.out.println("Task (" + t.getTaskID() + ") execution time " + (finishTime - startTime) / 1000.0);
        long et = finishTime - startTime;
        values.add(et / 1000);
        values.add((et / (3600) * 0.0742));
        //System.out.println("values..." + values.size());
        return values;
    }

    @ResponseBody
    @RequestMapping(value = "updatest", method = RequestMethod.POST)//, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public double updateEst(@RequestBody Double[] estVals) throws IOException {

        for (int i = 0; i < estVals.length; i++) {
            ApplicationManager.TASKETEST[i] = estVals[i].doubleValue();
        }
//        }
//        for(int i=0;i<estVals.get(1).length;i++ ){
//             ApplicationManager.TASKETESTS[i] = estVals.get(1)[i].doubleValue();
//        }
//        for(int i=0;i<estVals.get(2).length;i++ ){
//             ApplicationManager.TASKCPULOAD[i] = estVals.get(2)[i].doubleValue();
//        }
        return ApplicationManager.TASKETEST[0];
    }
}
