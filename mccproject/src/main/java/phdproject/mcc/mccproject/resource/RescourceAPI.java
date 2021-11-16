/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.resource;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.application.ApplicationRun;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizationResult;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizer;
import phdproject.mcc.mccproject.psooptimizer.PSOParticle;
import phdproject.mcc.mccproject.resource.resourcecharacteristics.ResourceCharacteristic;

/**
 *
 * class to interact to external resources / mobile device nd computation
 * resources
 */
public class RescourceAPI {

    private static final double MINSIZE = 1000;
    private static final double MAXSIZE = 1000000;
    public static BasicAWSCredentials basicAWSCredentials=new BasicAWSCredentials("AKIAITO32GEWBAKSZEKQ","2aML74i4LFjk/FIZoqOVBmivwhVxR0bXwwYcZg1K");
    public static final AmazonS3 amazonS3=new AmazonS3Client(basicAWSCredentials);    
    private static Connection dbConn = null;

    public static double getDataSizeInLocation(Resource res) {
        return ThreadLocalRandom.current().nextDouble(MINSIZE, MAXSIZE);
    }

    public static double getRandDataSize() {
        return ThreadLocalRandom.current().nextDouble(MINSIZE, MAXSIZE);
    }

    public static Object getDataTransferTime(Resource res, double dataSize) {
        Object bandwidth = res.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);
        return (double) dataSize / (double) bandwidth;
    }

    public static double getAvailEnergy(int rescID) {

        MobileDevice mobile = (MobileDevice) Resource.getRescourceByID(rescID);
        double energy = (double) mobile.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.ENERGY);
        return energy;
        //double deviceEnergy = energy * 0.3;
        //return (energy - deviceEnergy) * Math.random();
    }

    public static double getAvailBandwidth(int rescID) {

        MobileDevice mobile = (MobileDevice) Resource.getRescourceByID(rescID);
        double bandwidth = (double) mobile.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);
        switch (mobile.getAvailNetwork()) {
            case 1:
                return (1 + Math.random() * 4);
            case 2:
                return (6 + Math.random() * 5);
            case 3:
                return (15 + Math.random() * 25);
            case 4:
                return (20.65);
            default:
                return (5 + Math.random() * 20);
        }
    }
    public static double callBandwidth(RescourceLocation loc1, String loc2, int mobilenet) {
        double bandwidth = 0.0;
        if (loc1 == RescourceLocation.PUBLICCLOUD) {
            if (loc2.equals("PCLOUD")) {
                return 100.0;
            }
            if (loc2.equals("S3")) {
                return 6.7;
            }
        }
        if (loc1 == RescourceLocation.CLOUDLET) {
            if (loc2.equals("PCLOUD")) {
                return 5.8;
            }
            if (loc2.equals("S3")) {
                return 5.8;
            }
        }
        if (loc1 == RescourceLocation.MOBILE) {
            if (loc2.equals("PCLOUD")) {
                return 5.8;
            }
            if (loc2.equals("S3")) {
                return 5.6;
            }
        }
        return bandwidth;

    }

    public static Connection getDBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            RescourceAPI.dbConn = DriverManager.getConnection("jdbc:mysql:///mccdb", "root", "root");
        } catch (Exception ex) {

        }
        return RescourceAPI.dbConn;
    }

    public static void closeDBConnection() throws SQLException {
        RescourceAPI.dbConn.close();
    }

    public static PSOOptimizationResult addOPTRecord(ApplicationRun application, PSOOptimizationResult result, MobileDevice mobile,
            int scenario, int model, int random, Timestamp timestamp) {
        double totalT = result.getTotalETime();
        double totalE = result.getTE();
        double totalC = result.getTC();
        PSOParticle bestParticle = result.getParticale();
        PSOOptimizer.PRINT = 1;
        PSOOptimizationResult res2=PSOOptimizer.WriteResults(bestParticle, application, result, mobile,1,model);
        
        try {
            
            Random rand = new Random();
            if (scenario == 3) { 
                PSOParticle randomParticle = PSOOptimizer.population[0];
                for (int tIndex = 0; tIndex < ApplicationManager.getNUMTASK(); tIndex++) {
                double loc = ThreadLocalRandom.current().nextDouble(0, 3);
                RescourceLocation locFromDouble = PSOParticle.getLocFromDouble(loc);
                switch(locFromDouble){
                    case MOBILE:
                        randomParticle.getPos()[tIndex] = RescourceLocation.MOBILE;
                        break;
                    case CLOUDLET:
                        randomParticle.getPos()[tIndex] = RescourceLocation.CLOUDLET;
                        break;
                    case PUBLICCLOUD:
                        randomParticle.getPos()[tIndex] = RescourceLocation.PUBLICCLOUD;
                        break;
                }
                }
                System.out.println("Random Scenario.. ");
                result = PSOOptimizer.WriteResults(randomParticle, application, result, mobile,1,model);
                for (RescourceLocation loc : randomParticle.getPos()) {
                    System.out.print(loc.name() + " ");                    
                }
                System.out.println("");                
//                int randomNum = rand.nextInt((48) + 1);
//                PSOParticle randomParticle = PSOOptimizer.population[randomNum];
//                System.out.println("Selected Particle " + randomNum);
//                result = PSOOptimizer.WriteResults(randomParticle, application, result, mobile,1,model);
//                double totalT1 = result.getTotalETime();
//                double totalE2 = result.getTE();
//                double totalC3 = result.getTC();
//                double etP = (totalT1/totalT);
//                double etE = (totalE2/totalE);
//                double etC = (totalC3/totalC);
//            
//                while ((etP + etP + etP) < 0.3){
//                     randomNum = rand.nextInt((48) + 1);
//                     randomParticle = PSOOptimizer.population[randomNum];
//                     System.out.println("Selected Particle " + randomNum);
//                     result = PSOOptimizer.WriteResults(randomParticle, application, result, mobile,1,model);
//                     totalT1 = result.getTotalETime();
//                     totalE2 = result.getTE();
//                     totalC3 = result.getTC();
//                     etP = (totalT1/totalT);
//                     etE = (totalE2/totalE);
//                     etC = (totalC3/totalC);
//                }
//                for (RescourceLocation loc : randomParticle.getPos()) {
//                    System.out.print(loc.name() + " ");                    
//                }
//                System.out.println("");
            }
             PSOOptimizer.PRINT =3;
            if (scenario == 2) {
                int randomNum = rand.nextInt((48) + 1);
                PSOParticle mobileParticle = PSOOptimizer.population[randomNum];
                for(int i=0;i<mobileParticle.getPos().length;i++){
                    mobileParticle.getPos()[i] = RescourceLocation.MOBILE;
                }
                System.out.println("Mobile Device Scenario.. ");
                result = PSOOptimizer.WriteResults(mobileParticle, application, result, mobile,1,model);
                for (RescourceLocation loc : mobileParticle.getPos()) {
                    System.out.print(loc.name() + " ");                    
                }
                System.out.println("");
            }

            Connection conn = RescourceAPI.getDBConnection();
            PreparedStatement pstm = null;
            try {
                String sql = "insert into exper (expertype,model,scenario,TT,TC,TE,OPTVAL,mobileenergy, mobilebandwidth, mobilenetwork, logtime)";
                sql = sql + " values (?,?,?,?,?,?,?,?,?,?,?)";
                pstm = conn.prepareStatement(sql);
                pstm.setInt(1, 1);
                pstm.setInt(2, model);
                pstm.setInt(3, scenario);
                pstm.setDouble(4, result.getTotalETime());
                pstm.setDouble(5, result.getTC());
                pstm.setDouble(6, result.getTE());
                pstm.setDouble(7, result.getTC() * result.getTE());

                pstm.setDouble(8, mobile.getAvailEnergy());
                pstm.setDouble(9, mobile.getAvailBandwidth());
                pstm.setInt(10, mobile.getAvailNetwork());
                pstm.setTimestamp(11, timestamp);
                int res = pstm.executeUpdate();
            } catch (Exception ex) {
                System.out.println("Database error " + ex.getMessage());
            }
            pstm = null;

            try {
                for (int index = 0; index < result.getOptimizationDecision().length; index++) {
                    Task t = application.getUserAppTask().get(index);
                    String sql = "insert into task (logtime, taskID, DLOC, PLOC, DSize, PT, TT, ET, PC, TC ,EC, PE ,TE ,EE)";
                    sql = sql + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    pstm = conn.prepareStatement(sql);
                    pstm.setTimestamp(1, timestamp);
                    pstm.setInt(2, index + 1);
                    pstm.setString(3, t.getInputFileResLocation().getResName());
                    pstm.setString(4, result.getOptimizationDecision()[index].name());
                    pstm.setDouble(5, t.getTaskres().get_inputDataSize());
                    pstm.setDouble(6, t.getTaskres().getProcssingTime());
                    pstm.setDouble(7, t.getTaskres().getDtTime());
                    pstm.setDouble(8, t.getTaskres().getProcssingTime() + t.getTaskres().getDtTime());
                    pstm.setDouble(9, t.getTaskres().getPcost());
                    pstm.setDouble(10, t.getTaskres().getDtCost());
                    pstm.setDouble(11, t.getTaskres().getPcost() + t.getTaskres().getDtCost());
                    pstm.setDouble(12, t.getTaskres().getEnergy());
                    pstm.setDouble(13, t.getTaskres().getDtEnergy());
                    pstm.setDouble(14, t.getTaskres().getEnergy() + t.getTaskres().getDtEnergy());
                    int res = pstm.executeUpdate();
                }
            } catch (Exception ex) {
                System.out.println("Database error " + ex.getMessage());
            }
        } catch (Exception ex) {
            return res2;
        }
        PSOOptimizer.PRINT =4;
        result = PSOOptimizer.WriteResults(bestParticle, application, result, mobile,1,model);
        return res2;

    }
}
