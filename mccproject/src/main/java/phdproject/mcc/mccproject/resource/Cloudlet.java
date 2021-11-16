/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.application.RunnableTask;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.profiler.Profiler;
import phdproject.mcc.mccproject.profiler.ProfilerType;
import phdproject.mcc.mccproject.queuingsystem.DistributionType;
import phdproject.mcc.mccproject.queuingsystem.Queue;
import phdproject.mcc.mccproject.queuingsystem.QueueType;
import static phdproject.mcc.mccproject.resource.Resource.RESOURCEID;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class Cloudlet extends Resource {

    Profiler ptProfiler;
    Profiler pcProfiler;
    Profiler dttProfiler;
    Profiler dtcProfiler;
    Queue gg1;

    public Cloudlet(String name, String resourcePropFileName, RescourceLocation resLoc) throws FileNotFoundException, IOException {
        super(name, resourcePropFileName, resLoc);
        ptProfiler = new Profiler(this.getResID(), ProfilerType.PT);
        Profiler.getProfilersLIst().add(ptProfiler);
        pcProfiler = new Profiler(RESOURCEID, ProfilerType.PC);
        Profiler.getProfilersLIst().add(pcProfiler);
        dttProfiler = new Profiler(this.getResID(), ProfilerType.DTT);
        Profiler.getProfilersLIst().add(dttProfiler);
        dtcProfiler = new Profiler(RESOURCEID, ProfilerType.DTC);
        Profiler.getProfilersLIst().add(dtcProfiler);
        for (int t = 0; t < ApplicationManager.getNUMTASK(); t++) {
            ptProfiler.getProfilerDataMap().put(t, RunnableTask.generatePTProfileEntries(t, this));
        }
        List<String> paramNamesArrivalTime = new ArrayList<>();
        paramNamesArrivalTime.add("MIN");
        paramNamesArrivalTime.add("MAX");
        List<Double> paramValuesArrivalTime = new ArrayList<>();
        //paramValuesArrivalTime.add(ApplicationManager.getCNUMAPPSPERHOURMIN());
        //paramValuesArrivalTime.add(ApplicationManager.getCNUMAPPSPERHOURMAX());
        List<String> paramNamesServiceTime = new ArrayList<>();
        List<Double> paramValuesServiceTime = new ArrayList<>();
        paramNamesServiceTime.add("MIN");
        paramNamesServiceTime.add("MAX");
        paramValuesServiceTime.add(estimateMinProcessingTime());
        paramValuesServiceTime.add(estimateMaxProcessingTime());

        paramValuesArrivalTime.add(estimateMinProcessingTime() * 2.2);
        paramValuesArrivalTime.add(estimateMaxProcessingTime() * 2.9);
        gg1 = new Queue("PUBLICCLOUDQUEUE", QueueType.GG1,
                paramNamesArrivalTime, paramValuesArrivalTime, DistributionType.UNIFORM,
                paramNamesServiceTime, paramValuesServiceTime, DistributionType.UNIFORM);

    }

    public Profiler getPtProfiler() {
        return ptProfiler;
    }

    public void setPtProfiler(Profiler ptProfiler) {
        this.ptProfiler = ptProfiler;
    }

    public Profiler getPcProfiler() {
        return pcProfiler;
    }

    public void setPcProfiler(Profiler pcProfiler) {
        this.pcProfiler = pcProfiler;
    }

    public Profiler getDttProfiler() {
        return dttProfiler;
    }

    public void setDttProfiler(Profiler dttProfiler) {
        this.dttProfiler = dttProfiler;
    }

    public Profiler getDtcProfiler() {
        return dtcProfiler;
    }

    public void setDtcProfiler(Profiler dtcProfiler) {
        this.dtcProfiler = dtcProfiler;
    }

    public Queue getGg1() {
        return gg1;
    }

    public void setGg1(Queue gg1) {
        this.gg1 = gg1;
    }

    public double estimateProcessingTime(int userID, Task task) {
        double serviceTime = getGg1().getServiceTime().getDistribution().getMean();
        double waitingTime = getGg1().getWaitingtime();
        double PT = 0;
        if (ptProfiler.getProfilerDataMap().isEmpty()) {

        }
        //System.out.println("Task ID for energy estimatio in cloudlet " + taskID); 
        int cores = 16;
        double mips = task.getTASKETEST();// ApplicationManager.TASKETEST[taskID];
        double datas = task.getTASKETESTS();// ApplicationManager.TASKETESTS[taskID];
        double taskCPULOAD = task.getTASKCPULOAD();// ApplicationManager.TASKCPULOAD[taskID];
        //runTask(taskID,mips,datasize,datas,cores);
        double totalMIPS = mips + (task.getTaskres().get_inputDataSize() * datas);
        double et = (totalMIPS) / (cores * 1.2);
        if (et == 0) {
            System.out.println("");
        }
        //runTask(taskID,mips,datasize,datas,cores);
        waitingTime = 0;
        if (et + waitingTime == 0) {
            System.out.println("");
        }
        return et + waitingTime;
    }

    private double getAvergePTEstimationFromProfiler(int taskID) {
        double val1 = ptProfiler.getProfilerValue1Average(taskID);

        return ptProfiler.getProfilerValue1Average(taskID) / ptProfiler.getProfilerValue2Average(taskID);
    }

    private double estimateMinProcessingTime() {
        double minPT = 0;
        double totalMin = 0;
        for (int t = 0; t < ApplicationManager.getNUMTASK(); t++) {
            totalMin = totalMin + ptProfiler.getProfilerValueMin(t);
        }
        return totalMin / ApplicationManager.getNUMTASK();
    }

    private double estimateMaxProcessingTime() {
        double totalMAX = 0;
        for (int t = 0; t < ApplicationManager.getNUMTASK(); t++) {
            totalMAX = totalMAX + ptProfiler.getProfilerValueMax(t);
        }
        return totalMAX / ApplicationManager.getNUMTASK();
    }

}
