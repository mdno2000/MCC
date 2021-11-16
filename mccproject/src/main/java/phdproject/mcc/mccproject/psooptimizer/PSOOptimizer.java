/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.psooptimizer;

import com.sun.xml.bind.v2.schemagen.xmlschema.Particle;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.application.ApplicationRun;
import phdproject.mcc.mccproject.application.ExecutionScenario;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.resource.Cloudlet;
import phdproject.mcc.mccproject.resource.MobileDevice;
import phdproject.mcc.mccproject.resource.PublicCloud;
import phdproject.mcc.mccproject.resource.RescourceAPI;
import phdproject.mcc.mccproject.resource.RescourceLocation;
import phdproject.mcc.mccproject.resource.Resource;
import phdproject.mcc.mccproject.resource.resourcecharacteristics.ResourceCharacteristic;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class PSOOptimizer {

    private static ArrayList<PSOOptimizationResult> OptimizationProcessRecords = new ArrayList<>();
    public static PSOParticle[] population = null;
    public static PSOParticle optPSO = new PSOParticle();
    public static List< RescourceLocation[]> listOB = new ArrayList<>();
    public static int PRINT = 0;

    public static PSOOptimizationResult runOptimizer(ApplicationRun application, Double[] dsize, int scenario,int model) {
        // step 1: set-up the environment   
        //System.out.println("Call Run Optimizer..");
        MobileDevice mobile = ApplicationManager.getUserProfileByID(application.getUserID()).getUserMobile();
        population = new PSOParticle[PSOCONFIG.NUMPARTICALS];
        for (int partIndex = 0; partIndex < population.length; partIndex++) {
            population[partIndex] = new PSOParticle();
        }
        int index = 0;
        for (Task task : application.getUserAppTask()) {
            //task.getTaskres().setInputDataSize(dsize[index]);
            //System.out.println("Task Data Size... " + task.getTaskres().get_inputDataSize());
            index++;
        }
        PSOBestParticle gbest = new PSOBestParticle();
        PSOBestParticle gbest2 = new PSOBestParticle();
        PSOOptimizationResult optResult = null;
        optResult = PSOOptimizer.initParticles(PSOOptimizer.population, gbest, application.getAppID(), application.getUserID(), scenario,model);
        optResult = PSOOptimizer.runOptimizationIteration(PSOOptimizer.population, gbest, application.getAppID(), application.getUserID(),model);
        PSOParticle p = optResult.getParticale();
        try {
            for (int i = 0; i < p.getPos().length; i++) {
                switch (scenario) {
                    case 2:
                        p.getPos()[i] = RescourceLocation.MOBILE;
                        break;
                    case 3:
                        p.getPos()[i] = RescourceLocation.PUBLICCLOUD;
                        break;
                    case 4:
                        p.getPos()[i] = RescourceLocation.CLOUDLET;
                        break;
                    default:
                        break;
                }
            }
            //p.getPos()[0] = RescourceLocation.MOBILE;
            Random rand = new Random();
            // nextInt as provided by Random is exclusive of the top value so you need to add 1 
            System.out.println("Best Particle ");
            for (RescourceLocation loc : p.getPos()) {
                System.out.print(loc.name() + " ");
            }
            optResult = PSOOptimizer.WriteResults(p, application, optResult, mobile, 1,model);
        } catch (Exception ex) {

        }
        return optResult;
    }

    public static PSOOptimizationResult runOptimizer(ApplicationRun application, int option, double[] dsize, RescourceLocation[] source,
            RescourceLocation[] dist, int scenario, int experID,int model) throws IOException {
        // step 1: set-up the environment  

        ApplicationManager.getUserProfileByID(application.getUserID()).getUserMobile().setResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH,
                (5 + Math.random() * 20));
        MobileDevice mobile = ApplicationManager.getUserProfileByID(application.getUserID()).getUserMobile();
        PSOParticle[] population = new PSOParticle[PSOCONFIG.NUMPARTICALS];
        for (int partIndex = 0; partIndex < population.length; partIndex++) {
            population[partIndex] = new PSOParticle();
        }
        PSOBestParticle gbest = new PSOBestParticle();
        PSOOptimizationResult optResult = null;

        // step2 : get datasize from location 
        if (option == 1) { // fixed datasize and fixed resource loc
            int index = 0;
            for (Task task : application.getUserAppTask()) {
                task.getTaskres().setInputDataSize(dsize[index]);
                index++;
            }
        } else if (option == 2) { // dynamic data size and dynamic resource loc
            int index = 0;
            source = ExecutionScenario.randmizeDataLocation(source.length);
            for (Task task : application.getUserAppTask()) {
                task.getTaskres().setInputDataSize(RescourceAPI.getDataSizeInLocation(task.getInputFileResLocation()));
                task.getInputFileResLocation().setResLoc(source[index]);
                //System.out.println(task.toString());
                index++;
            }
        } else if (option == 3) { // fixed data size and dynamic rescource loc
            source = ExecutionScenario.randmizeDataLocation(source.length);
            int index = 0;
            for (Task task : application.getUserAppTask()) {
                task.getTaskres().setInputDataSize(dsize[index]);
                task.getInputFileResLocation().setResLoc(source[index]);
                //System.out.println(task.toString());
                index++;
            }
        } else if (option == 0) { // dynamic data size and fixed rescource loc
            for (Task task : application.getUserAppTask()) {
                //int index=0;
                task.getTaskres().setInputDataSize(RescourceAPI.getDataSizeInLocation(task.getInputFileResLocation()));
                //task.getInputFileResLocation().setResLoc(source[index]);
                //System.out.println(task.toString());
                //index++;    
            }
        }

        optResult = PSOOptimizer.initParticles(population, gbest, application.getAppID(), application.getUserID(), scenario,model);
        optResult = PSOOptimizer.runOptimizationIteration(population, gbest, application.getAppID(), application.getUserID(),model);
        if (optResult.getTotalETime() != 0) {

            PSOOptimizer.getOptimizationProcessRecords().add(optResult);
            PSOParticle p = population[optResult.getPartcID()];
            PSOOptimizer.WriteResults(p, application, optResult, mobile, option, scenario, experID, 0,model);
            int randomPIndex = (int) Math.random() * population.length;
            PSOParticle randomP = population[randomPIndex];
            PSOOptimizer.WriteResults(randomP, application, optResult, mobile, 10, scenario, experID, 0,model);
            PSOOptimizer.RunOnMobile(application, option, dsize, source, dist);
            for (int tIndex = 0; tIndex < optResult.getOptimizationDecision().length; tIndex++) {
                optResult.getOptimizationDecision()[tIndex] = RescourceLocation.MOBILE;
            }
            PSOOptimizer.WriteResults(p, application, optResult, mobile, 11, scenario, experID, 0,model);
            for (int tIndex = 0; tIndex < optResult.getOptimizationDecision().length; tIndex++) {
                optResult.getOptimizationDecision()[tIndex] = RescourceLocation.CLOUDLET;
            }
            PSOOptimizer.WriteResults(p, application, optResult, mobile, 12, scenario, experID, 0,model);
            for (int tIndex = 0; tIndex < optResult.getOptimizationDecision().length; tIndex++) {
                optResult.getOptimizationDecision()[tIndex] = RescourceLocation.PUBLICCLOUD;
            }
            PSOOptimizer.WriteResults(p, application, optResult, mobile, 13, scenario, experID, 0,model);

        }
        return optResult;
    }

    // step 2 : init Particles  
    private static PSOOptimizationResult initParticles(PSOParticle[] population,
            PSOBestParticle gbest, int APPID, int userID, int scenario,int model) {
        //System.out.println("Call initParticles..");
        PSOBestParticle gbest2 = new PSOBestParticle();
        MobileDevice mobile = ApplicationManager.getUserProfileByID(userID).getUserMobile();
        PSOOptimizationResult optRes = new PSOOptimizationResult();
        optRes.setAppID(APPID);
        optRes.setUserID(userID);
        int np = 0;
        int nm = 0;
        int nc = 0;
        Random randPos = new Random();
        randPos.setSeed(100000);
        double loc = 0;
        for (int partIndex = 0; partIndex < population.length; partIndex++) {
            for (int tIndex = 0; tIndex < ApplicationManager.getNUMTASK(); tIndex++) {
                loc = ThreadLocalRandom.current().nextDouble(0, 3);
                RescourceLocation locFromDouble = PSOParticle.getLocFromDouble(loc);
                if (locFromDouble == RescourceLocation.CLOUDLET && mobile.getConnectedToCloudlet() == 0) {
                    if (mobile.getConnectedToPCloud() == 0) {
                        population[partIndex].getPos()[tIndex] = RescourceLocation.MOBILE;
                    } else {
                        population[partIndex].getPos()[tIndex] = RescourceLocation.PUBLICCLOUD;
                    }

                } else if (locFromDouble == RescourceLocation.PUBLICCLOUD && mobile.getConnectedToPCloud() == 0) {
                    if (mobile.getConnectedToCloudlet() == 0) {
                        population[partIndex].getPos()[tIndex] = RescourceLocation.MOBILE;
                    } else {
                        population[partIndex].getPos()[tIndex] = RescourceLocation.CLOUDLET;
                    }
                } else {
                    population[partIndex].getPos()[tIndex] = locFromDouble;
                }

                population[partIndex].getVelocity()[tIndex] = 0;
                population[partIndex].getBestpos()[tIndex] = population[partIndex].getPos()[tIndex];
            }
            int v = 0;
        }
        for (int partIndex = 0; partIndex < population.length; partIndex++) {
            PSOParticle p = population[partIndex];
            ArrayList timeValues = PSOCostFunction.callculateTimes(p, userID, APPID,model);
            if (timeValues != null) {
                double Total_PT = (double) timeValues.get(0);
                double Total_PT_Mobile = (double) timeValues.get(1);
                double Total_PT_PCloud = (double) timeValues.get(2);
                double Total_PT_Cloudlet = (double) timeValues.get(3);
                double Total_TT = (double) timeValues.get(4);
                double TotalDataTMobile = (double) timeValues.get(5);
                double ET = Double.max(Total_PT_Mobile, Double.min(Total_PT_PCloud, Total_PT_Cloudlet));
                double Total_ET_Mobile = (double) timeValues.get(6);
                double Total_ET_PCloud = (double) timeValues.get(7);
                double Total_ET_Cloudlet = (double) timeValues.get(8);
                double Total_TT_Mobile = (double) timeValues.get(9);
                double PTC = (double) timeValues.get(10);
                double TTC = (double) timeValues.get(11);
                double TC = (double) timeValues.get(12);
                double PE = (double) timeValues.get(13);
                double TTE = (double) timeValues.get(14);
                double TE = (double) timeValues.get(15);
                double OPTVAL = (double) timeValues.get(16);
                int validDeadline = (int) timeValues.get(17);
                int validE = (int) timeValues.get(18);
                if (validDeadline == 1 && validE == 1 && OPTVAL < p.getCost()) {
                    p.setCost(OPTVAL);
                    p.setPos(p.getPos());

                    if (OPTVAL < p.getBest().getCost()) {
                        p.getBest().setCost(OPTVAL);
                        p.getBest().setPos(p.getPos());
                        if (OPTVAL < gbest.getCost()) {
                            gbest.setCost(OPTVAL);
                            gbest.setPos(p.getPos());
                            ApplicationRun app = ApplicationManager.getUserProfileByID(userID).getApplicationRunByID(APPID);
                            PSOOptimizer.updatePSOOptimizationResult(optRes, timeValues, p.getPos(),
                                    app);

                            //System.out.println("New Best Particle " + partIndex);
                            for (RescourceLocation loc2 : gbest.getPos()) {
                                System.out.print(loc2.name() + " ");
                            }
                            System.out.println("OPTVAL..." + OPTVAL);
                            PSOParticle pract = new PSOParticle();
                            pract.setPos(gbest.getPos());
                            optRes.setParticale(pract);
                            int z = 0;

                        }
                    }
                } else {
                    int x = 0;
                }
            } else {
                //p = population[partIndex];
                int c = 0;
            }
        }
        System.out.println("");
        return optRes;
    }

    private static PSOOptimizationResult runOptimizationIteration(PSOParticle[] population,
            PSOBestParticle gbest, int APPID, int userID,int model) {
        PSOOptimizationResult optResult = new PSOOptimizationResult();

        //System.out.println("Call Iterations..");
        PSOOptimizationResult res = new PSOOptimizationResult();
        PSOBestParticle gbest2 = new PSOBestParticle();
        double min = 0;
        double max = 0;
        for (int itr = 0; itr < PSOCONFIG.MAXITERATIONS; itr++) {
            for (int partIndex = 0; partIndex < population.length; partIndex++) {
                if (population[partIndex].getValidSol() == 1) {
                    //System.out.println(itr + "--" + partIndex );
                    double[] velocity1 = population[partIndex].getVelocity();
                    PSOParticle.updateParticleVelocity(population[partIndex], gbest);
                    PSOParticle.updateParticlePosition(population[partIndex]);
                    //population[partIndex].getPos()[0] = RescourceLocation.MOBILE;
                    PSOParticle p = population[partIndex];
                    ArrayList timeValues = PSOCostFunction.callculateTimes(population[partIndex], userID, APPID,model);
                    double Total_PT = (double) timeValues.get(0);
                    double Total_PT_Mobile = (double) timeValues.get(1);
                    double Total_PT_PCloud = (double) timeValues.get(2);
                    double Total_PT_Cloudlet = (double) timeValues.get(3);
                    double Total_TT = (double) timeValues.get(4);
                    double TotalDataTMobile = (double) timeValues.get(5);
                    double ET = Double.max(Total_PT_Mobile, Double.max(Total_PT_PCloud, Total_PT_Cloudlet));
                    double Total_ET_Mobile = (double) timeValues.get(6);
                    double Total_ET_PCloud = (double) timeValues.get(7);
                    double Total_ET_Cloudlet = (double) timeValues.get(8);
                    double Total_TT_Mobile = (double) timeValues.get(9);
                    double PTC = (double) timeValues.get(10);
                    double TTC = (double) timeValues.get(11);
                    double TC = (double) timeValues.get(12);
                    double PE = (double) timeValues.get(13);
                    double TTE = (double) timeValues.get(14);
                    double TE = (double) timeValues.get(15);
                    double OPTVAL = (double) timeValues.get(16);
                    int validDeadline = (int) timeValues.get(17);
                    int validE = (int) timeValues.get(18);
                    if (validDeadline == 1 && validE == 1 && OPTVAL < p.getCost()) {
//                        if (partIndex == optResult.getPartcID()) {
//                            System.out.println("Last Best Particle " + partIndex);
//                            for (RescourceLocation loc2 : p.getPos()) {
//                                System.out.print(loc2.name() + " ");
//                            }
//                        }

                        p.setCost(OPTVAL);
                        p.setPos(p.getPos());
                        if (OPTVAL < p.getBest().getCost()) {
                            p.getBest().setCost(OPTVAL);
                            p.getBest().setPos(p.getPos(), partIndex);//                             
                            if (OPTVAL < gbest.getCost()) {
                                gbest = new PSOBestParticle();
                                gbest.setCost(OPTVAL, partIndex);
                                gbest.setPos(p.getPos(), partIndex);
                                PSOOptimizer.updatePSOOptimizationResult(optResult, timeValues, p.getPos(),
                                        ApplicationManager.getUserProfileByID(userID).getApplicationRunByID(APPID));
                                optResult.setETM(Total_PT_Mobile);
                                optResult.setWT(ET - Total_PT_Mobile);
                                optResult.setPTC(PTC);
                                optResult.setTTC(TTC);
                                optResult.setTC(TC);
                                optResult.setPE(PE);
                                optResult.setTE(TE);
                                optResult.setTTE(TTE);
                                PSOParticle partc = new PSOParticle();
                                partc.setPos(gbest.getPos());
                                optResult.setParticale(partc);
                                //PSOOptimizer.optPSO = 
//                                System.out.println("New Best Particle " + partIndex);
//                                for (RescourceLocation loc2 : gbest.getPos()) {
//                                    System.out.print(loc2.name() + " ");
//                                }
                                System.out.println("***OPTVAL..." + OPTVAL);
                            }
                        }
                    }
                }
            }
        }
        /*System.out.println("List of bests..");
        for(int i=0;i<listOB.size();i++){
            for (RescourceLocation loc2 : listOB.get(i)) {
                System.out.print(loc2.name() + " ");
            }
            System.out.println("...");
        }*/
//        System.out.println("Index.." + optResult.getPartcID());
//
//         for (RescourceLocation loc2 : PSOOptimizer.population[optResult.getPartcID()].getPos()) {
//            System.out.print(loc2.name() + " ");
//        }

        //System.out.println("gbest..");
        try {
            System.out.println("gbest cost..." + gbest.getCost());
            for (RescourceLocation loc2 : gbest.getPos()) {
                System.out.print(loc2.name() + " ");
            }
        } catch (Exception ex) {

        }
        System.out.println("");
        return optResult;
    }

    public static double callcPSOCost() {
        double cost = 0;

        return cost;
    }

    public static ArrayList<PSOOptimizationResult> getOptimizationProcessRecords() {
        return OptimizationProcessRecords;
    }

    public static void setOptimizationProcessRecords(ArrayList<PSOOptimizationResult> OptimizationProcessRecords) {
        PSOOptimizer.OptimizationProcessRecords = OptimizationProcessRecords;
    }

    private static void updatePSOOptimizationResult(PSOOptimizationResult posRes, ArrayList<Double> estValues) {

    }

    public static void updatePSOOptimizationResult(PSOOptimizationResult posRes,
            ArrayList estValues,
            RescourceLocation[] pos,
            ApplicationRun app) {
        List<Task> tasks = app.getUserAppTask();
        for (Task t : tasks) {
            if (t.getTaskres().getDtEnergy() > 0) {
                Task T = t;
            }
        }
        posRes.setTotalCost((double) estValues.get(12));
        posRes.setTotalETime((double) estValues.get(0) + (double) estValues.get(4));
        posRes.setTotalEnergy((double) estValues.get(15));
        posRes.setOptimizationDecision(pos);
    }

    private static ArrayList callculateTimes(PSOParticle targetParticle, int userID, int AppID) {
        PublicCloud pubCloud = (PublicCloud) Resource.getResourcesList().stream().filter(res -> res.getResName().equals("PCLOUD")).findFirst().get();
        Cloudlet cloudlet = (Cloudlet) Resource.getResourcesList().stream().filter(res -> res.getResName().equals("CLOUDLET")).findFirst().get();
        MobileDevice mobile = ApplicationManager.getUserProfileByID(userID).getUserMobile();
        List<Task> appTasks = ApplicationManager.getUserProfileByID(userID).getApplicationRunByID(AppID).getUserAppTask();
        double computResBw = 0;
        double dataToSrcbw = 0;
        double Total_PT_Mobile = 0; // PT; processing time on mobile
        double Total_PT_PCloud = 0; // PT; processing time on public cloud
        double Total_PT_Cloudlet = 0; // PT; processing time on loudlet
        double Total_ET_Mobile = 0;  // ET: execution time = PT + TT (transfer time)
        double Total_TT_Mobile = 0;
        double Total_ET_PCloud = 0;
        double Total_ET_Cloudlet = 0;
        double Total_PT = 0;
        double TotalDataTMobile = 0;    // total data processed in mobile   
        double TPT = 0;
        double TT = 0;
        double TTT = 0;
        double PTC = 0; // PT cost
        double TTC = 0;
        double TC = 0; // Transfer Cost
        double PE = 0; // Processing Energy
        double TE = 0; // transfer Energy
        double TTE = 0;
        int validDeadline = 1;
        double PT = 0;
        for (int tIndex = 0; tIndex < ApplicationManager.getNUMTASK(); tIndex++) { // itrate all tasks            
            Task task = ApplicationManager.getUserProfileByID(userID).getApplicationRunByID(AppID).getUserAppTask().get(tIndex);
            Resource inputFileRes = task.getInputFileResLocation();
            if ((inputFileRes.getResLoc() == RescourceLocation.OTHER || inputFileRes.getResLoc() == RescourceLocation.PUBLICCLOUD) && mobile.getConnectedToPCloud() == 0) {
                targetParticle.setValidSol(0);
            } else {
                targetParticle.setValidSol(1);
            }

            double dataFromSrcbw = (double) inputFileRes.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);;
            if (inputFileRes.getResLoc() == RescourceLocation.MOBILE) {

            }
            Resource outFileRes = task.getOutputFileResLocation();
            RescourceLocation computLocation = targetParticle.getPos()[tIndex];
            double dsize = ApplicationManager.getUserProfileByID(userID).getApplicationRunByID(AppID).getUserAppTask().get(tIndex).getTaskres().get_inputDataSize();
            switch (targetParticle.getPos()[tIndex]) {
                case MOBILE:
                    computResBw = (double) mobile.getAvailBandwidth();
                    PT = mobile.estimateProcessingTime(userID, task);
                    Total_PT_Mobile = Total_PT_Mobile + PT;
                    if (inputFileRes.getResLoc() == RescourceLocation.MOBILE) {
                        TT = 0;
                    } else {
                        TT = (dsize / Math.min(dataFromSrcbw, computResBw) / 1000);
                        Total_TT_Mobile = Total_TT_Mobile + TT;
                        TotalDataTMobile = TotalDataTMobile + dsize;
                    }
                    Total_ET_Mobile = Total_ET_Mobile + PT + TT;
                    PTC = PTC + 0;
                    TTC = TTC + (TT * 0.009);
                    task.getTaskres().setEnergy(mobile.getAvergePTEEstimationFromProfiler() * PT, RescourceLocation.MOBILE);
                    task.getTaskres().setDtEnergy(mobile.getAvergeTTEEstimationFromProfiler() * TT);
                    task.getTaskres().setPcost(0);
                    task.getTaskres().setDtCost((TT * 0.009));
                    break;
                case PUBLICCLOUD:
                    computResBw = (double) pubCloud.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);
                    PT = pubCloud.estimateProcessingTime(userID, task);
                    Total_PT_PCloud = Total_PT_PCloud + PT;
                    if (inputFileRes.getResLoc() == RescourceLocation.PUBLICCLOUD) {
                        TT = 0;
                    } else {
                        TT = (dsize / Math.min(dataFromSrcbw, computResBw) / 1000);
                    }
                    Total_ET_PCloud = Total_ET_PCloud + PT + TT;
                    PTC = PTC + (PT * 0.004);
                    task.getTaskres().setPcost(PT * 0.004);
                    task.getTaskres().setDtCost((TT * 0.002));
                    task.getTaskres().setEnergy(0, RescourceLocation.PUBLICCLOUD);
                    task.getTaskres().setDtEnergy(0);

                    break;
                case CLOUDLET:
                    computResBw = (double) cloudlet.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);
                    PT = cloudlet.estimateProcessingTime(userID, task);
                    Total_PT_Cloudlet = Total_PT_Cloudlet + PT;
                    if (inputFileRes.getResLoc() == RescourceLocation.CLOUDLET) {
                        TT = 0;
                    } else {
                        TT = (dsize / Math.min(dataFromSrcbw, computResBw) / 1000);
                    }
                    Total_ET_Cloudlet = Total_ET_Cloudlet + PT + TT;
                    task.getTaskres().setPcost(PT * 0.001);
                    task.getTaskres().setDtCost((TT * 0.0005));
                    task.getTaskres().setEnergy(0, RescourceLocation.CLOUDLET);
                    task.getTaskres().setDtEnergy(0);
                    PTC = PTC + (PT * 0.001);
                    break;
                default:
                    PT = 0;
                    break;
            }
            if (PT + TT > task.getDeadline()) {          // check deadline constraint       
                validDeadline = -1;
            }

            TPT = TPT + PT;
            TTT = TTT + TT;
            task.getTaskres().setProcssingTime(PT);
            task.getTaskres().setDtTime(TT);
            if ((task.getTaskres().getEnergy() > 0 || task.getTaskres().getDtEnergy() > 0)) {
                if (targetParticle.getPos()[tIndex] != RescourceLocation.MOBILE) {
                    int x = 0;
                }

            }

        }
        // save values in arraylist
        ArrayList timeValues = new ArrayList();
        timeValues.add(TPT);
        timeValues.add(Total_PT_Mobile);
        timeValues.add(Total_PT_PCloud);
        timeValues.add(Total_PT_Cloudlet);
        timeValues.add(TTT);
        timeValues.add(TotalDataTMobile);
        timeValues.add(Total_ET_Mobile);
        timeValues.add(Total_ET_PCloud);
        timeValues.add(Total_ET_Cloudlet);
        timeValues.add(Total_TT_Mobile);
        timeValues.add(PTC);
        timeValues.add(TTC);
        TC = PTC + TTC;
        timeValues.add(TC);
        PE = mobile.getAvergePTEEstimationFromProfiler() * Total_PT_Mobile;
        TTE = mobile.getAvergeTTEEstimationFromProfiler() * Total_TT_Mobile;
        timeValues.add(PE);
        timeValues.add(TTE);
        TE = PE + TTE;
        timeValues.add(TE);
        timeValues.add(TE * TC);
        timeValues.add(validDeadline);
        int validE = mobile.getEnergyStatus(TE);  // check mobile energy constraint 
        timeValues.add(validE);
        if (PE > 0 || TTE > 0) {
            int v = 0;
        }
        if (targetParticle.getValidSol() == 1) {
            return timeValues;
        } else {
            return null;
        }

    }

    public static void RunOnMobile(ApplicationRun application, int option, double[] dsize, RescourceLocation[] source, RescourceLocation[] dist) throws IOException {
        PSOOptimizationResult optResult = null;
        PSOParticle particale = new PSOParticle();

        for (int tIndex = 0; tIndex < ApplicationManager.getNUMTASK(); tIndex++) {
            particale.getPos()[tIndex] = RescourceLocation.MOBILE;
            //   optResult.getOptimizationDecision()[tIndex]=RescourceLocation.MOBILE;
        }
    }

    private static void WriteResults(PSOParticle p, ApplicationRun application, PSOOptimizationResult optResult, MobileDevice mobile,
            int option, int scenario, int experID, int print,int model) throws IOException {

        ArrayList timeValues = PSOCostFunction.callculateTimes(p, application.getUserID(), application.getAppID(),model);
        double Total_PT = (double) timeValues.get(0);
        double Total_PT_Mobile = (double) timeValues.get(1);
        double Total_PT_PCloud = (double) timeValues.get(2);
        double Total_PT_Cloudlet = (double) timeValues.get(3);
        double Total_TT = (double) timeValues.get(4);
        double TotalDataTMobile = (double) timeValues.get(5);
        double ET = Double.max(Total_PT_Mobile, Double.max(Total_PT_PCloud, Total_PT_Cloudlet));
        double Total_ET_Mobile = (double) timeValues.get(6);
        double Total_ET_PCloud = (double) timeValues.get(7);
        double Total_ET_Cloudlet = (double) timeValues.get(8);
        double Total_TT_Mobile = (double) timeValues.get(9);
        double PTC = (double) timeValues.get(10);
        double TTC = (double) timeValues.get(11);
        double TC = (double) timeValues.get(12);
        double PE = (double) timeValues.get(13);
        double TTE = (double) timeValues.get(14);
        double WE = Math.max(0, ((Math.max(Total_PT_PCloud, Total_PT_Cloudlet)) - Total_PT_Mobile) * 0.05);
        double TE = (double) timeValues.get(15);
        TE = TE + WE;
        double OPTVAL = (double) timeValues.get(16);
        double percInMobile = 0;
        double percInCloudLet = 0;
        double percInPublic = 0;
        double totalNumber = optResult.getOptimizationDecision().length * 1.0;
        optResult.setTotalETime(Math.max(Math.max(Total_ET_PCloud, Total_ET_Cloudlet), Total_ET_Mobile));
        for (int index = 0; index < p.getPos().length; index++) {
            optResult.optimizationDecision[index] = p.getPos()[index];
        }

        for (RescourceLocation loc : optResult.getOptimizationDecision()) {
            if (loc == RescourceLocation.MOBILE) {
                percInMobile++;
            }
            if (loc == RescourceLocation.CLOUDLET) {
                percInCloudLet++;
            }
            if (loc == RescourceLocation.PUBLICCLOUD) {
                percInPublic++;
            }

        }
        optResult.setETM(Total_PT_Mobile);
        optResult.setWT(ET - Total_PT_Mobile);
        optResult.setPTC(PTC);
        optResult.setTTC(TTC);
        optResult.setTC(TC);
        optResult.setPE(PE);
        optResult.setTE(TE);
        optResult.setTTE(TTE);
        optResult.setWE(WE);
        optResult.setAVAILE(mobile.getAvailEnergy());
        optResult.setBW(mobile.getAvailBandwidth());
        optResult.setNETWORK(mobile.getAvailNetwork());
        optResult.setUserAppTask(application.getUserAppTask());
    }

    public static PSOOptimizationResult WriteResults(PSOParticle p, ApplicationRun application, PSOOptimizationResult optResult, MobileDevice mobile, int print,int model) {

        ArrayList timeValues = PSOCostFunction.callculateTimes(p, application.getUserID(), application.getAppID(),model);
        double Total_PT = (double) timeValues.get(0);
        double Total_PT_Mobile = (double) timeValues.get(1);
        double Total_PT_PCloud = (double) timeValues.get(2);
        double Total_PT_Cloudlet = (double) timeValues.get(3);
        double Total_TT = (double) timeValues.get(4);
        double TotalDataTMobile = (double) timeValues.get(5);
        double Total_ET_Mobile = (double) timeValues.get(6);
        double Total_ET_PCloud = (double) timeValues.get(7);
        double Total_ET_Cloudlet = (double) timeValues.get(8);
        double ET = Double.max(Total_ET_Mobile, Double.max(Total_ET_PCloud, Total_ET_Cloudlet));
        double Total_TT_Mobile = (double) timeValues.get(9);
        double PTC = (double) timeValues.get(10);
        double TTC = (double) timeValues.get(11);
        double TC = (double) timeValues.get(12);
        double PE = (double) timeValues.get(13);
        double TTE = (double) timeValues.get(14);
        double WE = Math.max(0, (ET - Total_PT_Mobile)) * 0.05;
//        if (print == 1) {
//            System.out.println("Total time : " + ET);
//            System.out.println("P E : " + ET);
//            System.out.println("Total time : " + ET);
//        }
        double TE = (double) timeValues.get(15);
        //TE = TE + WE;
        if (print == 1) {
            System.out.println("Total time : " + ET);
            System.out.println("Total time in Mobile : " + Total_ET_Mobile);
            System.out.println("W E : " + WE);
            System.out.println("T E : " + TE);
        }
        double OPTVAL = (double) timeValues.get(16);
        double percInMobile = 0;
        double percInCloudLet = 0;
        double percInPublic = 0;
        double totalNumber = optResult.getOptimizationDecision().length * 1.0;
        optResult.setTotalETime(Math.max(Math.max(Total_ET_PCloud, Total_ET_Cloudlet), Total_ET_Mobile));
        for (int index = 0; index < p.getPos().length; index++) {
            optResult.optimizationDecision[index] = p.getPos()[index];
        }

        for (RescourceLocation loc : optResult.getOptimizationDecision()) {
            if (loc == RescourceLocation.MOBILE) {
                percInMobile++;
            }
            if (loc == RescourceLocation.CLOUDLET) {
                percInCloudLet++;
            }
            if (loc == RescourceLocation.PUBLICCLOUD) {
                percInPublic++;
            }

        }
        System.out.println("Percenatge in mobile : " + (double) percInMobile/ ApplicationManager.getNUMTASK());
        System.out.println("Percenatge in cloudlet : " + (double) percInCloudLet/ ApplicationManager.getNUMTASK());
        System.out.println("Percenatge in public cloud : " + (double) percInPublic/ ApplicationManager.getNUMTASK());
        
        
        optResult.setETM(Total_PT_Mobile);
        optResult.setWT(ET - Total_PT_Mobile);
        optResult.setPTC(PTC);
        optResult.setTTC(TTC);
        optResult.setTC(TC);
        optResult.setPE(PE);
        optResult.setTE(TE);
        optResult.setTTE(TTE);
        optResult.setWE(WE);
        optResult.setAVAILE(mobile.getAvailEnergy());
        optResult.setBW(mobile.getAvailBandwidth());
        optResult.setNETWORK(mobile.getAvailNetwork());
        optResult.setUserAppTask(application.getUserAppTask());
        return optResult;
    }

    public PSOParticle[] getPopulation() {
        return null;
    }
}
