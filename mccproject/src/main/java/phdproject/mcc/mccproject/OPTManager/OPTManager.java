/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.OPTManager;

//import com.mathworks.engine.MatlabEngine;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.application.ApplicationRun;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.psooptimizer.PSOBestParticle;
import phdproject.mcc.mccproject.psooptimizer.PSOCONFIG;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizationResult;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizer;
import phdproject.mcc.mccproject.psooptimizer.PSOParticle;
import phdproject.mcc.mccproject.resource.Cloudlet;
import phdproject.mcc.mccproject.resource.MobileDevice;
import phdproject.mcc.mccproject.resource.PublicCloud;
import phdproject.mcc.mccproject.resource.RescourceAPI;
import phdproject.mcc.mccproject.resource.RescourceLocation;
import static phdproject.mcc.mccproject.resource.RescourceLocation.CLOUDLET;
import static phdproject.mcc.mccproject.resource.RescourceLocation.PUBLICCLOUD;
import phdproject.mcc.mccproject.resource.Resource;
import phdproject.mcc.mccproject.resource.resourcecharacteristics.ResourceCharacteristic;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class OPTManager {

    int execution_scenario;
    int number_exper;
    int exper_id;
    int MAXIteratiosn;
    int number_tasks;
    double Latency; // s/20MB
    double bandwidth;

    String path = "C:\\Users\\90930015\\Dropbox\\Muhmmad Nour (2)\\latest_work\\5-May-2019\\Results\\data";
    FileWriter TaskCSVWriter = null;
    ArrayList<ArrayList<Double>> results;
    MobileDevice mobileDevice;
    ArrayList<Double> PSO_Data = new ArrayList<>();
    ArrayList<Double> Random_Data = new ArrayList<>();
    ArrayList<Double> Mobile_Data = new ArrayList<>();
    ArrayList<Double> MLIP_Data = new ArrayList<>();
    int[] moreIndexs = null;
    double[] deadline_factors = null;

    public OPTManager() throws IOException {
        results = new ArrayList<>();
        ApplicationManager.init();
        mobileDevice = new MobileDevice("NEXUS", "NEXUS", RescourceLocation.MOBILE);
        bandwidth = 0;
    }

    // experiment section
    public void runExpermient(int execution_scenario, int number_exper, int exper_id, double Latency, int MAXIteratiosn, int numTasks) throws IOException {

        this.exper_id = exper_id;
        this.execution_scenario = execution_scenario;
        this.number_exper = number_exper;
        this.Latency = Latency;
        this.MAXIteratiosn = MAXIteratiosn;
        this.number_tasks = numTasks;
        String fileName = "results_" + exper_id + "_" + number_exper + "_" + execution_scenario + "_" + numTasks + ".csv";
        TaskCSVWriter = new FileWriter(path + "/" + fileName);
        TaskCSVWriter.append("ntasks,bandwidth,b_model,d_model,scenario,TT,TE,TC,PT,OPT").append('\n');
        for (int b_case = 1; b_case <= 3; b_case++) {
            for (int d_case = 4; d_case >= 1; d_case--) {
                int experIter = 1;
                while (experIter <= number_exper) {
                    if (runExpermientIteration(d_case, b_case, numTasks)) {
                        WriteToFile();
                        experIter++;
                    }
                }
            }
        }
        TaskCSVWriter.flush();

    }

    void WriteToFile() throws IOException {
        String line = "";
        results.add(MLIP_Data);
        results.add(PSO_Data);
        results.add(Random_Data);
        results.add(Mobile_Data);

        for (ArrayList<Double> list : results) {
            line = "";
            for (int entryIndex = 0; entryIndex < list.size() - 1; entryIndex++) {
                //System.out.print(list.get(entryIndex) + ",");
                line = line + list.get(entryIndex) + ",";
            }
            //System.out.print(list.get(list.size() - 1));
            line = line + list.get(list.size() - 1) + "\n";
            TaskCSVWriter.append(line);
            //System.out.println("");
        }

        MLIP_Data = new ArrayList<>();
        PSO_Data = new ArrayList<>();
        Random_Data = new ArrayList<>();
        Mobile_Data = new ArrayList<>();
        results = new ArrayList<>();
    }

    private boolean runExpermientIteration(int d_case, int b_case, int numTasks) throws IOException {
        setupExperiment(d_case, b_case, numTasks);
        Instant start_pso = Instant.now();
        // 1. PSO
        double dsize[] = new double[30];
        ArrayList<Double> values = new ArrayList<>();
        // 2. MLIP
        ApplicationRun application = generateApplication(d_case, dsize);
        double et = 0;
//        et = runMLIP(d_case, dsize, b_case, application, null, et);
//        if (et == 0) {
//            return false;
//        }
       
        System.out.println("Print MLIP");
        ArrayList<Double> valuesMLIP = printAndGetApplicationValues(application);
        MLIP_Data.add(valuesMLIP.get(0) / 3600);
        MLIP_Data.add(valuesMLIP.get(1) / 1000);
        MLIP_Data.add(valuesMLIP.get(2));
        MLIP_Data.add(et);

        application = generateApplication(d_case, dsize);
        runPSO(application);
        System.out.println("Print PSO");
        ArrayList<Double> valuesPSO = printAndGetApplicationValues(application);
        MLIP_Data.add(((valuesMLIP.get(2) * valuesMLIP.get(1)) / (valuesMLIP.get(2) * valuesMLIP.get(1))) * 100);
        PSO_Data.add(((valuesMLIP.get(2) * valuesMLIP.get(1)) / (PSO_Data.get(2) * PSO_Data.get(1))) * 100);
        // 3. Random
        //application.resetApplication();
        application = generateApplication(d_case, dsize);
        double startTime = System.currentTimeMillis();
        PSOParticle resultParticle = runRandom(application);
        double endTime = System.currentTimeMillis();
        System.out.println("Print Random");
        values = printAndGetApplicationValues(application);
//        Random_Data.add(values.get(0) / 3600);
//        Random_Data.add(values.get(1) / 1000);
//        Random_Data.add(values.get(2));
        Random_Data.add(((valuesMLIP.get(2) * valuesMLIP.get(1)) / (Random_Data.get(2) * Random_Data.get(1))) * 100);
        Random_Data.add((endTime - startTime) / 1000);

        // 4. Mbile 
        //application.resetApplication();
        application = generateApplication(d_case, dsize);
        startTime = System.currentTimeMillis();
        runMobile(application);
        endTime = System.currentTimeMillis();
        System.out.println("Print Mobile");
        values = new ArrayList<>();
        values = printAndGetApplicationValues(application);
        Mobile_Data.add(values.get(0) / 3600);
        Mobile_Data.add(values.get(1) / 1000);
        Mobile_Data.add(values.get(2));
        Mobile_Data.add(((valuesMLIP.get(2) * valuesMLIP.get(1)) / (values.get(2) * values.get(1))) * 100);
        Mobile_Data.add((endTime - startTime) / 1000);
        //Measure execution time for the random
        // Start priniting results 
        return true;

    }

    void setupExperiment(int d_case, int b_case, int numTasks) {
        ApplicationManager.generateBoTWorkloadModel(d_case);
        mobileDevice.setAvailNetwork(b_case);
        switch (mobileDevice.getAvailNetwork()) {
            case 1:
                mobileDevice.setAvailBandwidth(2 + Math.random() * 3);
                break;
            case 2:
                mobileDevice.setAvailBandwidth(8 + Math.random() * 4);
                break;
            case 3:
                // double f=(4 + Math.random() * 30);
                mobileDevice.setAvailBandwidth(25 + Math.random() * 5);
                break;
            case 4:
                mobileDevice.setAvailBandwidth(20.65);
                break;
            default:
                mobileDevice.setAvailBandwidth(5 + Math.random() * 20);
                break;
        }

        bandwidth = mobileDevice.getAvailBandwidth();
        PSO_Data.add(numTasks * 1.0);
        PSO_Data.add(bandwidth);
        PSO_Data.add(b_case * 1.0);
        PSO_Data.add(d_case * 1.0);
        PSO_Data.add(1.0);

        Random_Data.add(numTasks * 1.0);
        Random_Data.add(bandwidth);
        Random_Data.add(b_case * 1.0);
        Random_Data.add(d_case * 1.0);
        Random_Data.add(3.0);

        Mobile_Data.add(numTasks * 1.0);
        Mobile_Data.add(bandwidth);
        Mobile_Data.add(b_case * 1.0);
        Mobile_Data.add(d_case * 1.0);
        Mobile_Data.add(4.0);

        MLIP_Data.add(numTasks * 1.0);
        MLIP_Data.add(bandwidth);
        MLIP_Data.add(b_case * 1.0);
        MLIP_Data.add(d_case * 1.0);
        MLIP_Data.add(2.0);
        moreIndexs = new int[number_tasks];
        for (int tIndex = 0; tIndex < number_tasks; tIndex++) {

            if (tIndex < 30) {
                moreIndexs[tIndex] = tIndex + 1;
            } else {
                int index = ThreadLocalRandom.current().nextInt(0, 30);
                if (index <= 0) {
                    index = index + 1;
                }
                moreIndexs[tIndex] = index;
            }

        }
        // change range
        deadline_factors = new double[30];
        for (int tIndex = 0; tIndex < 30; tIndex++) {
            deadline_factors[tIndex] = 1 ; // ThreadLocalRandom.current().nextInt(2, 5);
        }
    }

    // Run experiement
    private void runPSO(ApplicationRun application) {
        double startTime = System.currentTimeMillis();
        PSOParticle[] population = new PSOParticle[PSOCONFIG.NUMPARTICALS];
        List<Double> PSO_Time_Value = new ArrayList<>();
        List<Double> PSO_Cost_Value = new ArrayList<>();
        List<Double> PSO_Energy_Value = new ArrayList<>();
        for (int partIndex = 0; partIndex < population.length; partIndex++) {
            population[partIndex] = new PSOParticle();
        }
        PSOBestParticle gbest = new PSOBestParticle();
        PSOParticle targetParticle = new PSOParticle();
        int iteration = 1;
        while (iteration <= MAXIteratiosn) {
            PSOOptimizationResult optResult = initParticles(population, gbest, application);
            optResult = runOptimizationIteration(population, gbest, mobileDevice, application);
            System.out.println("PSO Solution...");
            int tIndex = 0;
            ArrayList timeValues = callculateTimes(optResult.getParticale(), application, 1);
            double Total_PT = (double) timeValues.get(0);
            double Total_TT = (double) timeValues.get(4);
            double TT = Total_PT + Total_TT;
            double TC = (double) timeValues.get(12);
            double TE = (double) timeValues.get(15);
            double OPTVAL = (double) timeValues.get(16);
            double validDeadline = (int) timeValues.get(17);
            double validE = (int) timeValues.get(18);
            if (optResult.getTotalETime() != 0) {
                for (RescourceLocation loc : optResult.getParticale().getPos()) {
                    //System.out.println(loc.name() + " ");
                    targetParticle = optResult.getParticale();
                    timeValues = callculateTimes(optResult.getParticale(), application, 1);
                    tIndex++;
                }
                ArrayList<Double> values = printAndGetApplicationValues(application);
                PSO_Time_Value.add(values.get(0));
                PSO_Cost_Value.add(values.get(2));
                PSO_Energy_Value.add(values.get(1));
                iteration++;
                population = new PSOParticle[PSOCONFIG.NUMPARTICALS];
                for (int partIndex = 0; partIndex < population.length; partIndex++) {
                    population[partIndex] = new PSOParticle();
                }
                gbest = new PSOBestParticle();
            }

        }
        double endTime = System.currentTimeMillis();
        PSO_Data.add((PSO_Time_Value.stream().mapToDouble((x) -> x).max().getAsDouble()) / 3600);
        PSO_Data.add((PSO_Energy_Value.stream().mapToDouble((x) -> x).max().getAsDouble()) / 1000);
        PSO_Data.add(PSO_Cost_Value.stream().mapToDouble((x) -> x).max().getAsDouble());
        PSO_Data.add((endTime - startTime) / (1000 * (MAXIteratiosn - 1)));

    }

//    private double runMLIP(int d_case, double[] dsize, int b_case, ApplicationRun application, double[] PSO_Sol, double et) {
//        double Total_PT_MLIP = 0;
//        double Total_TT_MLIP = 0;
//        double TT_MLIP = 0;
//        double TC_MLIP = 0;
//        double TE_MLIP = 0;
////        int[] moreIndexs = new int[application.getUserAppTask().size()];
////        for (int tIndex = 0; tIndex < application.getUserAppTask().size(); tIndex++) {
////            moreIndexs[tIndex] = application.getUserAppTask().get(tIndex).associatedIndex;
////        }
//        Future<MatlabEngine> eng = MatlabEngine.startMatlabAsync();
//        try {
//            //Start MATLAB asynchronously
//            MatlabEngine ml = eng.get();
//            // Get engine instance from the future result 
//            //ml.eval("cd 'C:\\Users\\90930034\\Dropbox\\PHD Study\\Muhmmad Nour\\latest_work\\3 March 2019\\New Code\\GA'");
//            //ml.eval("cd 'C:\\Users\\90930034\\Dropbox\\PHD Study\\Muhmmad Nour\\latest_work\\1 January 2019\\Run Bag Of Task\\Run Bag Of Task\\GA'");
//            ml.eval("cd 'C:\\Users\\90930015\\Dropbox\\Muhmmad Nour (2)\\latest_work\\5-May-2019\\matlab\\matlab_latest'");
//
//            Object[] resultTuble = null;
//            resultTuble = ml.feval(9, "CallFromJava", d_case, dsize, b_case, bandwidth, PSO_Sol, Latency, moreIndexs,deadline_factors);
//            double opt = (double) resultTuble[7];
//            et = (double) resultTuble[8];
//            if (opt == 0) {
//                return 0;
//            } else {
//                double sol[] = (double[]) resultTuble[3];
//                PSOParticle mlipParticle = new PSOParticle();
//                System.out.println("MLIP Solution...");
//                for (int tIndex = 0; tIndex < ApplicationManager.getNUMTASK(); tIndex++) {
//                    switch ((int) sol[tIndex]) {
//                        case 1:
//                            mlipParticle.getPos()[tIndex] = RescourceLocation.PUBLICCLOUD;
//                            // System.out.println("PUBLICCLOUD");
//                            break;
//                        case 2:
//                            mlipParticle.getPos()[tIndex] = RescourceLocation.CLOUDLET;
//                            //System.out.println("CLOUDLET");
//                            break;
//                        case 3:
//                            mlipParticle.getPos()[tIndex] = RescourceLocation.MOBILE;
//                            //System.out.println("Mobile");
//                            break;
//                    }
//                }
//                ArrayList timeValues = callculateTimes(mlipParticle, application, 1);
//                Total_PT_MLIP = (double) timeValues.get(0);
//                Total_TT_MLIP = (double) timeValues.get(4);
//                TT_MLIP = Total_PT_MLIP + Total_TT_MLIP;
//                TC_MLIP = (double) timeValues.get(12);
//                TE_MLIP = (double) timeValues.get(15);
//                double OPTVAL = (double) timeValues.get(16);
//                int validDeadline = (int) timeValues.get(17);
//                int validE = (int) timeValues.get(18);
//                ml.close();
//                return et;
//            }
//        } catch (Exception ex) {
//            System.out.println("Exxpetion " + ex.getMessage());
//
//        }
//        return et;
//
//    }

    private PSOParticle runRandom(ApplicationRun application) {
        PSOParticle randomParticle = null;
        List<Double> Random_Time_Value = new ArrayList<>();
        List<Double> Random_Cost_Value = new ArrayList<>();
        List<Double> Random_Energy_Value = new ArrayList<>();
        double Total_PT = 0, TT = 0, TC = 0, TE = 0;
        int iteration = 1;
        while (iteration <= MAXIteratiosn) {
            while (Total_PT == 0) {
                randomParticle = getRandomParticle();
                ArrayList timeValues = callculateTimes(randomParticle, application, 0);
                Total_PT = (double) timeValues.get(0);
                double Total_TT = (double) timeValues.get(4);
                TT = Total_PT + Total_TT;
                TC = (double) timeValues.get(12);
                TE = (double) timeValues.get(15);
                double OPTVAL = (double) timeValues.get(16);
                double validDeadline = (int) timeValues.get(17);
                double validE = (int) timeValues.get(18);
            }
            ArrayList<Double> values = printAndGetApplicationValues(application);
            Random_Time_Value.add(values.get(0));
            Random_Cost_Value.add(values.get(2));
            Random_Energy_Value.add(values.get(1));
            iteration++;
            Total_PT = 0;
        }
        Random_Data.add((Random_Time_Value.stream().mapToDouble((x) -> x).max().getAsDouble()) / 3600);
        Random_Data.add((Random_Energy_Value.stream().mapToDouble((x) -> x).max().getAsDouble()) / 1000);
        Random_Data.add(Random_Cost_Value.stream().mapToDouble((x) -> x).max().getAsDouble());

        return randomParticle;
    }

    private void runMobile(ApplicationRun application) {
        PSOParticle targetParticle = new PSOParticle();
        for (int tIndex = 0; tIndex < targetParticle.getPos().length; tIndex++) {
            targetParticle.getPos()[tIndex] = RescourceLocation.MOBILE;
        }
        //printSolution(targetParticle);
        ArrayList timeValues = callculateTimes(targetParticle, application, 0);
        double Total_PT = (double) timeValues.get(0);
        double Total_TT = (double) timeValues.get(4);
        double TT = Total_PT + Total_TT;
        double TC = (double) timeValues.get(12);
        double TE = (double) timeValues.get(15);
        double OPTVAL = (double) timeValues.get(16);
        int validDeadline = (int) timeValues.get(17);
        int validE = (int) timeValues.get(18);
    }

    // utilities 
    private ApplicationRun generateApplication(int d_case, double dsize[]) throws IOException {
        String filename = "mixapp_1";
        double[] large_d = {407, 407, 407, 1725, 1839, 1839, 1839, 1725, 407, 407, 407, 407, 407, 1725, 1839, 1839, 1839, 1725, 407, 1475, 1725, 407, 407, 1725, 1839, 1839, 1839, 1725, 407, 407};
        double[] medium_d = {535, 223, 463, 535, 198, 223, 463, 463, 535, 535, 535, 223, 463, 535, 198, 223, 463, 463, 535, 535, 535, 223, 463, 535, 198, 223, 463, 463, 535, 535};
        double[] small_d = {156, 20, 144, 223, 156, 144, 122, 122, 42, 42, 156, 20, 144, 223, 156, 144, 122, 122, 42, 42, 156, 20, 144, 223, 156, 144, 122, 122, 42, 42};
        double[] x_large_d = {1407, 1407, 1407, 2725, 2839, 2839, 2839, 2725, 1407, 1407, 1407, 1407, 1407, 2725, 2839, 2839, 2839, 2725, 1407, 2475, 2725, 1407, 1407, 2725, 2839, 2839, 2839, 2725, 1407, 1407};
        switch (d_case) {
            case 1:
                filename = "mixapp_1";
                break;
            case 2:
                filename = "mixapp_2";
                // dsize[tindex] = ThreadLocalRandom.current().nextDouble(200, 500);
                break;
            case 3:
                filename = "mixapp_1";
                //dsize[tindex] = ThreadLocalRandom.current().nextDouble(20, 200);
                break;
            case 4:
                filename = "mixapp_1";
                //dsize[tindex] = ThreadLocalRandom.current().nextDouble(2000, 4000);
                break;
        }
        // System.out.println("Daat size :" + dsize.length);
        ApplicationRun application = new ApplicationRun(1, filename, 1, 1);
        for (int tindex = 0; tindex < dsize.length; tindex++) {
            double currentDeadline =  application.getUserAppTask().get(tindex).getDeadline();
            application.getUserAppTask().get(tindex).setDeadline(currentDeadline / deadline_factors[tindex]);
            switch (d_case) {
                case 1:
                    dsize[tindex] = large_d[tindex];//ThreadLocalRandom.current().nextDouble(500, 2000);
                    // dsize[tindex] =ThreadLocalRandom.current().nextDouble(500, 2000);
                    break;
                case 2:
                    dsize[tindex] = medium_d[tindex];
                    // dsize[tindex] = ThreadLocalRandom.current().nextDouble(200, 500);
                    break;
                case 3:
                    dsize[tindex] = small_d[tindex];
                    //dsize[tindex] = ThreadLocalRandom.current().nextDouble(20, 200);
                    break;
                case 4:
                    dsize[tindex] = x_large_d[tindex];
                    //dsize[tindex] = ThreadLocalRandom.current().nextDouble(2000, 4000);
                    break;
            }
            //System.out.print(dsize[tindex] + " ");
            application.getUserAppTask().get(tindex).getTaskres().setInputDataSize(dsize[tindex]);
        }
        ApplicationManager.uploadBoTWorkloadModelToApplication(application);
        for (int index = application.getUserAppTask().size(); index < number_tasks; index++) {
            addRandomTaskFromApplication(index, application);
        }

        ApplicationManager.NUMTASK = this.number_tasks;
        return application;

    }

    private void addRandomTaskFromApplication(int tIndex, ApplicationRun application) {
        int t = moreIndexs.length;
        Task copy = application.getUserAppTask().get(moreIndexs[tIndex] - 1);
        application.getUserAppTask().add(new Task(copy, tIndex + 1));
    }

    private void resetAplication(ApplicationRun application) {
        application.resetApplication();
    }

    private ArrayList<Double> printAndGetApplicationValues(ApplicationRun applicationRun) {
        ArrayList<Double> values = new ArrayList<>();
        double energy = 0, time = 0, cost = 0;
        for (Task t : applicationRun.getUserAppTask()) {
            //System.out.println(t.getTaskID() + " " + t.getTaskres().getProcssingTime() + " " + t.getTaskres().getDtTime() + " "
            //       + t.getTaskres().getEnergy() + " " + t.getTaskres().getDtEnergy());
            energy = energy + t.getTaskres().getEnergy() + t.getTaskres().getDtEnergy();
            time = time + t.getTaskres().getProcssingTime() + t.getTaskres().getDtTime();
            cost = cost + t.getTaskres().getDtCost() + t.getTaskres().getPcost();
        }
//        System.out.println("Energy.." + energy);
//        System.out.println("Cost.." + cost);
//        System.out.println("Time.." + time);
        values.add(time);
        values.add(energy);
        values.add(cost);
        return values;

    }

    private void printSolution(PSOParticle bestParticle) {
        for (RescourceLocation loc : bestParticle.getPos()) {
            System.out.println(loc.name() + " ");
        }
    }

    private PSOParticle getRandomParticle() {
        PSOParticle randomParticle = new PSOParticle();
        for (int iteration = 1; iteration <= MAXIteratiosn; iteration++) {
            for (int tIndex = 0; tIndex < ApplicationManager.getNUMTASK(); tIndex++) {
                double loc = ThreadLocalRandom.current().nextDouble(0, 3);
                RescourceLocation locFromDouble = PSOParticle.getLocFromDouble(loc);
                switch (locFromDouble) {
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
        }
        return randomParticle;
    }

    private PSOOptimizationResult initParticles(PSOParticle[] population,
            PSOBestParticle gbest, ApplicationRun applicationRun) {
        //System.out.println("Call initParticles..");
        PSOBestParticle gbest2 = new PSOBestParticle();
        PSOOptimizationResult optRes = new PSOOptimizationResult();
        optRes.setAppID(1);
        optRes.setUserID(1);
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
                if (locFromDouble == RescourceLocation.CLOUDLET && mobileDevice.getConnectedToCloudlet() == 0) {
                    if (mobileDevice.getConnectedToPCloud() == 0) {
                        population[partIndex].getPos()[tIndex] = RescourceLocation.MOBILE;
                    } else {
                        population[partIndex].getPos()[tIndex] = RescourceLocation.PUBLICCLOUD;
                    }

                } else if (locFromDouble == RescourceLocation.PUBLICCLOUD && mobileDevice.getConnectedToPCloud() == 0) {
                    if (mobileDevice.getConnectedToCloudlet() == 0) {
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
            ArrayList timeValues = callculateTimes(p, applicationRun, 0);
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
                            PSOOptimizer.updatePSOOptimizationResult(optRes, timeValues, p.getPos(),
                                    applicationRun);

                            //System.out.println("New Best Particle " + partIndex);
                            for (RescourceLocation loc2 : gbest.getPos()) {
                                //System.out.print(loc2.name() + " ");
                            }
                            //System.out.println("OPTVAL..." + OPTVAL);
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
        //System.out.println("");
        return optRes;
    }

    private PSOOptimizationResult runOptimizationIteration(PSOParticle[] population,
            PSOBestParticle gbest, MobileDevice mobile, ApplicationRun applicationRun) {
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
                    ArrayList timeValues = callculateTimes(population[partIndex], applicationRun, 0);
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
                                        applicationRun);
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
                                //System.out.println("***OPTVAL..." + OPTVAL);
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
            //System.out.println("gbest cost..." + gbest.getCost());
            for (RescourceLocation loc2 : gbest.getPos()) {
                //System.out.print(loc2.name() + " ");
            }
        } catch (Exception ex) {

        }
        //System.out.println("");
        return optResult;
    }

    private ArrayList callculateTimes(PSOParticle targetParticle, ApplicationRun applicationRun, int checkSOL) {

        PublicCloud pubCloud = (PublicCloud) Resource.getResourcesList().stream().filter(res -> res.getResName().equals("PCLOUD")).findFirst().get();
        Cloudlet cloudlet = (Cloudlet) Resource.getResourcesList().stream().filter(res -> res.getResName().equals("CLOUDLET")).findFirst().get();
        List<Task> appTasks = applicationRun.getUserAppTask();
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
        double Latency = 0.877; // Milli/20MB
        // add comulatives 
        double total_cost_cum = 0;
        double total_energy_cum = 0;
        int numTasks = applicationRun.getUserAppTask().size();
        for (int tIndex = 0; tIndex < numTasks; tIndex++) { // itrate all tasks   
            PT = 0;

            Task task = applicationRun.getUserAppTask().get(tIndex);
            double preE = task.getTaskres().getEnergy();
            Resource inputFileRes = task.getInputFileResLocation();
            if (tIndex == 0) {
                //System.out.println("Res LOC task 1: " + inputFileRes.getResLoc().toString());
            }
            //System.out.println("Data location.." + inputFileRes.getResLoc().name() );
            if ((inputFileRes.getResLoc() == RescourceLocation.OTHER || inputFileRes.getResLoc() == RescourceLocation.PUBLICCLOUD)
                    && mobileDevice.getConnectedToPCloud() == 0) {
                targetParticle.setValidSol(0);
            } else {
                targetParticle.setValidSol(1);
            }
//
//            double dataFromSrcbw = (double) inputFileRes.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);;
//            if (tIndex == 0) {
//                //System.out.println("Res LOC task 1, Bandwidth: " + mobile.getAvailBandwidth());
//            }
//            if (inputFileRes.getResLoc() == RescourceLocation.MOBILE) {
//
//            }
            Resource outFileRes = task.getOutputFileResLocation();
            RescourceLocation computLocation = targetParticle.getPos()[tIndex];
            double dsize = task.getTaskres().get_inputDataSize();
            PSOOptimizer.PRINT = 0;

            //System.out.println("Data Size..." + dsize + " Task " + task.getTaskID());
            switch (targetParticle.getPos()[tIndex]) {
                case MOBILE:
                    computResBw = (double) mobileDevice.getAvailBandwidth();
                    PT = mobileDevice.estimateProcessingTime(1, task);
                    if (checkSOL == 1) {
                        //System.out.println("");
                    }
                    // System.out.println("Processing time on mobile.." + PT +" Task " + task.getTaskID());
                    Total_PT_Mobile = Total_PT_Mobile + PT;
                    //System.out.println("Total Processing time on mobile.."+ Total_PT_Mobile);
                    double dtCost = 0;  //updated 20/6/2018
                    if (inputFileRes.getResLoc() == RescourceLocation.MOBILE) {
                        // change to local data server  
                        TT = (dsize / mobileDevice.getAvailBandwidth());
                        TT = TT + ((dsize / 20) * (Latency * 0.05));
                        //task.getTaskres().setDtCost(0);
                    } else {
                        TT = (dsize / mobileDevice.getAvailBandwidth());
                        TT = TT + ((dsize / 20) * (Latency));
                        Total_TT_Mobile = Total_TT_Mobile + TT;
                        TotalDataTMobile = TotalDataTMobile + dsize;
                        // download from cloud cost 
                        dtCost = (dsize / 1024) * 0.02;
                    }

                    PTC = PTC + 0;

                    //System.out.println("Processing Cost Mobile.." + PTC + " Task "+ task.getTaskID());
                    //TTC = TTC + (TT * 0.009);
                    //System.out.println("Transfer Cost Mobile.." + TTC + " Task "+ task.getTaskID());
                    //task.getTaskres().setEnergy(mobile.getAvergePTEEstimationFromProfiler() * PT, RescourceLocation.MOBILE);
                    task.getTaskres().setEnergy(0.196 * PT, RescourceLocation.MOBILE);
                    // System.out.println("Mobile Processing Energy.." + task.getTaskres().getEnergy() + " Task "+task.getTaskID());
                    // System.out.println("Mobile Network .." + mobile.getAvailNetwork());
                    //task.getTaskres().setDtEnergy(mobile.getAvailNetwork(), TT);
                    // System.out.println("Mobile DT Energy.." + task.getTaskres().getDtEnergy()+ " Task "+task.getTaskID());
                    task.getTaskres().setPcost(0);
                    //if (inputFileRes.getResLoc() != RescourceLocation.MOBILE) {
                    task.getTaskres().setDtEnergy(mobileDevice.getAvailNetwork(), TT);
                    // network cost 
                    if (mobileDevice.getAvailNetwork() == 3) { // data transfer cost updated 21/6 $/GB
                        dtCost = dtCost + ((dsize / 1024) * 0.05);

                    } else if (mobileDevice.getAvailNetwork() == 2) {
                        dtCost = dtCost + ((dsize / 1024) * 1);
                        //task.getTaskres().setDtCost((dsize * 0.001024));
                    } else {
                        dtCost = dtCost + ((dsize / 1024) * 1);
                        //task.getTaskres().setDtCost((dsize * 0.001024));
                    }
//                    } else { // 
//                        task.getTaskres().setDtCost(0);
//                    }

                    task.getTaskres().setDtCost(dtCost);
                    TTC = TTC + (task.getTaskres().getDtCost());
                    if (PSOOptimizer.PRINT == 1) {
                        System.out.println("Task " + task.getTaskID() + " DTCOst on mobile = " + (task.getTaskres().getDtCost()));
                        System.out.println("dtCost .." + dtCost + " Task " + task.getTaskID());
                        System.out.println("TT .." + TT + " Task " + task.getTaskID());
                        System.out.println("PT .." + PT + " Task " + task.getTaskID());
                    }
                    double postE = task.getTaskres().getEnergy();
                    RescourceLocation tloc = targetParticle.getPos()[tIndex];
                    RescourceLocation rloc = inputFileRes.getResLoc();
                    Total_ET_Mobile = Total_ET_Mobile + PT + TT;
                    //System.out.println("***"+task.getTaskID() + ": "+"Energy: " + preE + "---" + postE + 
                    // rloc + "---" +tloc);
                    break;
                case PUBLICCLOUD:
                    computResBw = (double) pubCloud.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);
                    PT = pubCloud.estimateProcessingTime(1, task);
                    if (checkSOL == 1) {
                        //System.out.println("");
                    }
                    // System.out.println("Processing time on public cloud.." + PT + " Task " + task.getTaskID());
                    Total_PT_PCloud = Total_PT_PCloud + PT;
                    //System.out.println("Total Processing time on public cloud.." + Total_PT_PCloud);
                    dtCost = 0;
                    if (inputFileRes.getResLoc() == RescourceLocation.PUBLICCLOUD) {
                        TT = 0;
                        dtCost = (dsize / 1024) * 0.02;
                        //task.getTaskres().setDtCost((dsize / 1024) * 0.02 );          //Transfer cost within same region
                    } else {
                        TT = (dsize / RescourceAPI.callBandwidth(PUBLICCLOUD, inputFileRes.getResName(), 0));
                        //  Math.min(dataFromSrcbw, computResBw));
                        if (TT == Double.POSITIVE_INFINITY) {
                            TT = (dsize / mobileDevice.getAvailBandwidth());
                            Total_TT_Mobile = Total_TT_Mobile + TT;
                            TotalDataTMobile = TotalDataTMobile + dsize;
                            dtCost = dtCost + ((dsize / 1024) * 0.05);
                        } else {
                            dtCost = (dsize / 1024) * 0.02;
                        }
                        TT = TT + ((dsize / 20) * (Latency));
                        //task.getTaskres().setDtCost((dsize / 1024) * 0.02);             // https://aws.amazon.com/s3/pricing/ 
                        //TT = (dsize / Math.min(dataFromSrcbw, computResBw) / 1000);
                    }
                    Total_ET_PCloud = Total_ET_PCloud + PT + TT;
                    //The Cost for Task Processing Time 
                    PTC = PTC + (PT / (3600.0) * 0.3712);
                    //  System.out.println("Transfer time public cloud .." + TT + " Task "+ task.getTaskID());
                    //  System.out.println("Total execution time public cloud.."+ Total_ET_PCloud);                                        
                    //  System.out.println("Processing Cost Public Cloud.." + PTC + " Task "+ task.getTaskID());

                    task.getTaskres().setPcost(PT / (3600.0) * 0.3712);
                    task.getTaskres().setEnergy(0, RescourceLocation.MOBILE);
                    task.getTaskres().setDtCost(dtCost);
                    task.getTaskres().setDtEnergy(0);
                    //task.getTaskres().setDtCost(dtCost);
                    TTC = TTC + (task.getTaskres().getDtCost());
                    if (PSOOptimizer.PRINT == 1) {
                        System.out.println("Task " + task.getTaskID() + " DTCOst on pcloud = " + (task.getTaskres().getDtCost()));
                        System.out.println("Public Cloud");
                        System.out.println("TT .." + TT + " Task " + task.getTaskID());
                        System.out.println("PT .." + PT + " Task " + task.getTaskID());
                    }
                    break;
                case CLOUDLET:
                    computResBw = (double) cloudlet.getResourceCharacteristicEntryCurValue(ResourceCharacteristic.BANDWIDTH);
                    PT = cloudlet.estimateProcessingTime(1, task);
                    if (checkSOL == 1) {
                        //System.out.println("");
                    }
                    Total_PT_Cloudlet = Total_PT_Cloudlet + PT;
                    dtCost = 0;
                    if (inputFileRes.getResLoc() == RescourceLocation.CLOUDLET) {
                        TT = 0;
                        dtCost = (dsize / 1024) * 0;
                        //task.getTaskres().setDtCost((dsize * 0.001) / 1024);
                    } else {
                        TT = (dsize / RescourceAPI.callBandwidth(CLOUDLET, inputFileRes.getResName(), 0)); //  Math.min(dataFromSrcbw, computResBw));
                        if (TT == Double.POSITIVE_INFINITY) {
                            TT = (dsize / mobileDevice.getAvailBandwidth());
                            Total_TT_Mobile = Total_TT_Mobile + TT;
                            TotalDataTMobile = TotalDataTMobile + dsize;
                            TT = TT + ((dsize / 20) * (Latency * 0.05));
                            //dtCost = dtCost + ((dsize / 1024) * 0.05);
                        } else {
                            dtCost = (dsize / 1024) * 0.02;
                            TT = TT + ((dsize / 20) * (Latency));
                        }

                        //TT = (dsize / Math.min(dataFromSrcbw, computResBw) / 1000);
                    }
                    Total_ET_Cloudlet = Total_ET_Cloudlet + PT + TT;
                    task.getTaskres().setPcost((PT / (3600) * 0.144)); // updated 20/6/2018
                    PTC = PTC + (PT / (3600) * 0.144);

                    task.getTaskres().setEnergy(0, RescourceLocation.MOBILE);
                    //dtCost = dtCost + ((dsize / 1024) * 1); 
                    task.getTaskres().setDtCost(dtCost);
                    task.getTaskres().setDtEnergy(0);
                    TTC = TTC + (task.getTaskres().getDtCost());

                    if (PSOOptimizer.PRINT == 1) {
                        System.out.println("Task " + task.getTaskID() + " DTCOst on cloudlet = " + (task.getTaskres().getDtCost()));
                        System.out.println("Cloudlet");
                        System.out.println("TT .." + TT + " Task " + task.getTaskID());
                        System.out.println("PT .." + PT + " Task " + task.getTaskID());
                    }
                    break;
                default:
                    PT = 0;
                    task.getTaskres().setEnergy(0, RescourceLocation.PUBLICCLOUD);
                    task.getTaskres().setDtEnergy(0);
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
            double postE = task.getTaskres().getEnergy();
            RescourceLocation tloc = targetParticle.getPos()[tIndex];
            RescourceLocation rloc = inputFileRes.getResLoc();
            if (tloc != RescourceLocation.MOBILE && rloc != RescourceLocation.MOBILE) {
                // System.out.println(task.getTaskID() + ": "+"Energy: " + preE + "---" + postE + 
                //       rloc + "---" +tloc);
            }

            // Cost 
            double Task_Time = (task.getTaskres().getProcssingTime() + task.getTaskres().getDtTime());
            // add task waiting energy 
            if ((task.getTaskres().getDtEnergy() + task.getTaskres().getEnergy()) == 0) {
                task.getTaskres().setDtEnergy(0.05 * Task_Time);//, RescourceLocation.MOBILE);
            }

            double Task_Cost = (task.getTaskres().getDtCost()) + (task.getTaskres().getPcost());
            Task_Cost = Math.max(0.001, Task_Cost);

            double Task_Energy = (task.getTaskres().getDtEnergy()) + (task.getTaskres().getEnergy());
            Task_Energy = Math.max(0.001, Task_Energy);

            //   total_cost_cum += Task_Cost * (Task_Energy / 1000);             // Energy and Cost
            //   total_cost_cum += Task_Time * Task_Energy / 1000;               // Energy and Time
            //  total_cost_cum += Task_Time * (Task_Energy / 1000)* Task_Cost;  // Energy and Time and Cost
            
            total_cost_cum += (Task_Energy / 1000)+Task_Time; 
            //total_energy_cum +=Task_Energy; 
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
        PE = 0.196 * Total_PT_Mobile;
        if (mobileDevice.getAvailNetwork() == 3) {
            TTE = 0.264 * Total_TT_Mobile;
        }
        if (mobileDevice.getAvailNetwork() == 2) {
            TTE = 0.501 * Total_TT_Mobile;
        }

        if (mobileDevice.getAvailNetwork() == 1) {
            TTE = 0.550 * Total_TT_Mobile;
        }
        double ET = Double.max(Total_ET_Mobile, Double.max(Total_ET_PCloud, Total_ET_Cloudlet));
        double WE = Math.max(0, (ET - Total_PT_Mobile)) * 0.05;

        timeValues.add(PE);
        timeValues.add(TTE);
        TE = PE + TTE;//+ WE;
//        if (Math.max(0, (ET - Total_PT_Mobile)) == 0) {
//            for (RescourceLocation loc2 : targetParticle.getPos()) {
//                System.out.print(loc2.name() + " ");
//            }
        PSOOptimizer.PRINT = 3;
//        if (PSOOptimizer.PRINT == 3) {
//            System.out.println("Total Time Cloud.." + Total_ET_PCloud);
//            System.out.println("Total Cloudlet.." + Total_ET_Cloudlet);
//            System.out.println("Total Mobile.." + Total_PT_Mobile);
//            System.out.println("Total Cost.." + TC);
//            System.out.println("Total Energy.." + TE);
//            System.out.println("Total Time.." + Math.max(Total_PT_Mobile, (ET)));
//        }
//        }

//        System.out.println("Total Cost.." + TC);
//        System.out.println("Total Energy.." + TE);
//        System.out.println("Total Time.." + Math.max(Total_PT_Mobile, (ET)));
        timeValues.add(TE);
        //timeValues.add((TE / 1000) * (TC));
        // update OPT value 
        timeValues.add(total_cost_cum);
        timeValues.add(validDeadline);
        int validE = mobileDevice.getEnergyStatus(TE);  // check mobile energy constraint 
//        System.out.println("Valid Deadline: " + validDeadline);
//        System.out.println("Valid E Cost: " + validE);
//        System.out.println("OPT: " + (TE / 1000) * (TC));
//        System.out.println("Total Energy.." + TE);
//        System.out.println("Total Time Cloud.." + Total_ET_PCloud);
//        System.out.println("Total Cloudlet.." + Total_ET_Cloudlet);
//        System.out.println("Total Mobile.." + Total_PT_Mobile);
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

}
