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
import java.util.Random;
import java.util.stream.Collectors;
import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.application.RunnableTask;
import static phdproject.mcc.mccproject.application.RunnableTask.generatePTProfileEntries;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.profiler.Profiler;
import phdproject.mcc.mccproject.profiler.ProfilerEntry;
import phdproject.mcc.mccproject.profiler.ProfilerType;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizationResult;
import phdproject.mcc.mccproject.psooptimizer.PSOOptimizer;
import phdproject.mcc.mccproject.resource.resourcecharacteristics.ResourceCharacteristic;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class MobileDevice extends Resource {

    Profiler ptProfiler;
    Profiler pcProfiler;
    Profiler dttProfiler;
    Profiler dtcProfiler;
    Profiler ePTProfiler;
    Profiler eTTProfiler;
    int _connectedToCloudlet;
    int _connectedToPCloud;
    int _availNetwork;
    double _availBandwidth;
    double _availEnergy;
    List<Profiler> profilerList;

    public MobileDevice(String name, String resourcePropFileName, RescourceLocation resLoc) throws FileNotFoundException, IOException {
        super(name, resourcePropFileName, resLoc);
        System.out.println("Call mobile conts..2");
        profilerList = new ArrayList<>();
        ptProfiler = new Profiler(this.getResID(), ProfilerType.PT);
        getProfilerList().add(ptProfiler);
        pcProfiler = new Profiler(RESOURCEID, ProfilerType.PC);
        Profiler.getProfilersLIst().add(pcProfiler);
        dttProfiler = new Profiler(this.getResID(), ProfilerType.DTT);
        getProfilerList().add(dttProfiler);
        dtcProfiler = new Profiler(RESOURCEID, ProfilerType.DTC);
        Profiler.getProfilersLIst().add(dtcProfiler);
        ePTProfiler = new Profiler(RESOURCEID, ProfilerType.E);
        getProfilerList().add(ePTProfiler);
        eTTProfiler = new Profiler(RESOURCEID, ProfilerType.E);
        getProfilerList().add(eTTProfiler);
        for (int t = 0; t < ApplicationManager.getNUMTASK(); t++) {
            ptProfiler.getProfilerDataMap().put(t, RunnableTask.generatePTProfileEntries(t, this));
            ePTProfiler.getProfilerDataMap().put(t, RunnableTask.generateEPTProfileEntries(t));
            eTTProfiler.getProfilerDataMap().put(t, RunnableTask.generateEIdleProfileEntries(t));
            pcProfiler.getProfilerDataMap().put(t, RunnableTask.generatePTProfileEntries(t, this));
            dttProfiler.getProfilerDataMap().put(t, RunnableTask.generateEPTProfileEntries(t));
            dtcProfiler.getProfilerDataMap().put(t, RunnableTask.generateEIdleProfileEntries(t));

        }
        randmizeConnection();
    }

    public MobileDevice(MobileDevice device) {
        System.out.println("Call mobile conts..3");
    }

    public MobileDevice() {

        super();
        ptProfiler = new Profiler();
        dttProfiler = new Profiler();
        dtcProfiler = new Profiler();
        ePTProfiler = new Profiler();
        eTTProfiler = new Profiler();
        pcProfiler = new Profiler();
        profilerList = new ArrayList<>();
    }

    public List<Profiler> getProfilerList() {
        return profilerList;
    }

    public void setProfilerList(List<Profiler> profilerList) {
        this.profilerList = profilerList;
    }

    public void allocateProfilers() {
        System.out.println("Allocate Profilers..");
        ptProfiler = new Profiler(this.getResID(), ProfilerType.PT);
        Profiler.getProfilersLIst().add(ptProfiler);
        pcProfiler = new Profiler(RESOURCEID, ProfilerType.PC);
        Profiler.getProfilersLIst().add(pcProfiler);
        dttProfiler = new Profiler(this.getResID(), ProfilerType.DTT);
        Profiler.getProfilersLIst().add(dttProfiler);
        dtcProfiler = new Profiler(RESOURCEID, ProfilerType.DTC);
        Profiler.getProfilersLIst().add(dtcProfiler);
        ePTProfiler = new Profiler(RESOURCEID, ProfilerType.E);
        Profiler.getProfilersLIst().add(ePTProfiler);
        eTTProfiler = new Profiler(RESOURCEID, ProfilerType.E);
        Profiler.getProfilersLIst().add(eTTProfiler);
        for (int t = 0; t < ApplicationManager.getNUMTASK(); t++) {
            System.out.println("Mobile ..." + this.getResName());
            List<ProfilerEntry> entries = RunnableTask.generatePTProfileEntries(t, this);
            System.out.println(ptProfiler.getProfilerDataMap().size());
            ptProfiler.getProfilerDataMap().put(t, RunnableTask.generatePTProfileEntries(t, this));
            System.out.println("PTP ..." + ptProfiler.getProfilerValue1Average(2));
            ePTProfiler.getProfilerDataMap().put(t, RunnableTask.generateEPTProfileEntries(t));
            eTTProfiler.getProfilerDataMap().put(t, RunnableTask.generateEIdleProfileEntries(t));
            pcProfiler.getProfilerDataMap().put(t, RunnableTask.generatePTProfileEntries(t, this));
            dttProfiler.getProfilerDataMap().put(t, RunnableTask.generateEPTProfileEntries(t));
            dtcProfiler.getProfilerDataMap().put(t, RunnableTask.generateEIdleProfileEntries(t));

        }
        System.out.println("Allocate Profilers..finished");
    }

    public Profiler getPtProfiler() {
        return ptProfiler;
    }

    public void setPtProfiler(Profiler ptProfiler) {
        //System.out.println("Call profiler..");
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

    public Profiler getePTProfiler() {
        return ePTProfiler;
    }

    public void setePTProfiler(Profiler ePTProfiler) {
        this.ePTProfiler = ePTProfiler;
    }

    public Profiler geteTTProfiler() {
        return eTTProfiler;
    }

    public void seteTTProfiler(Profiler eTTProfiler) {
        this.eTTProfiler = eTTProfiler;
    }

    public void randmizeConnection() {
        double connRandCloudlet = Math.random();
        if (connRandCloudlet < 0.6) {
            _connectedToCloudlet = 0;
        } else {
            _connectedToCloudlet = 1;
        }

        double connRandPCloud = Math.random();
        if (connRandPCloud < 0.4) {
            _connectedToPCloud = 0;
        } else {
            _connectedToPCloud = 1;
        }

        _connectedToCloudlet = 1;
        _connectedToPCloud = 1;

        _availNetwork = 1 + (int) (Math.random() * 3);

        _availEnergy = RescourceAPI.getAvailEnergy(this.getResID());
        _availBandwidth = RescourceAPI.getAvailBandwidth(this.getResID());
    }

    public double estimateProcessingTime(int userID, Task task) {
        //System.out.println("Task ID for energy estimatio in mobile " + taskID); 
        int cores = 2;
        double mips = task.getTASKETEST();// ApplicationManager.TASKETEST[taskID];
        double datas = task.getTASKETESTS();// ApplicationManager.TASKETESTS[taskID];
        double taskCPULOAD = task.getTASKCPULOAD();// ApplicationManager.TASKCPULOAD[taskID];
        //runTask(taskID,mips,datasize,datas,cores);
        double totalMIPS = mips + (task.getTaskres().get_inputDataSize() * datas);
        double et = (totalMIPS) / (cores * 1.2);
        if (et == 0) {
            System.out.println("Info: " + mips + " "+datas+ " "+task.getTaskres().get_inputDataSize());
        }
        return et;
    }

    public double estimatePE() {
        double PE = 0;
        if (ePTProfiler.getProfilerDataMap().isEmpty()) {

        }
        PE = getAvergePTEEstimationFromProfiler();//*dataSize;
        return PE;
    }

    private double getAvergePTEstimationFromProfiler(int taskID) {
        try {
            double v1 = ptProfiler.getProfilerValue1Average(taskID);
            double v2 = ptProfiler.getProfilerValue2Average(taskID);
            return ptProfiler.getProfilerValue2Average(taskID) / ptProfiler.getProfilerValue1Average(taskID);
        } catch (Exception ex) {
            return 1;
        }
    }

    public double getAvergePTEEstimationFromProfiler() {
        try {
            double PEAverage = 0;
            double PETotalAverage = 0;
            for (int t = 0; t < ApplicationManager.getNUMTASK(); t++) {
                PETotalAverage = PETotalAverage + ePTProfiler.getProfilerValue1Average(t) / ePTProfiler.getProfilerValue2Average(t);
            }
            return PETotalAverage / (double) ApplicationManager.getNUMTASK();
        } catch (Exception ex) {
            return 1;
        }
    }

    public double getAvergeTTEEstimationFromProfiler() {
        double TTTotalAverage = 0;
        try {
            for (int t = 0; t < ApplicationManager.getNUMTASK(); t++) {
                TTTotalAverage = TTTotalAverage + eTTProfiler.getProfilerValue1Average(t) / eTTProfiler.getProfilerValue2Average(t);
            }
            return TTTotalAverage / (double) ApplicationManager.getNUMTASK();
        } catch (Exception ex) {
            return 1;
        }
    }

    public int getEnergyStatus(double requiredE) {
        double E = (double) getResourceCharacteristicEntryCurValue(ResourceCharacteristic.ENERGY);
        double AvalE = E * (new Random(100000).nextDouble());
        if ((getAvailEnergy() > requiredE)) {
            return 1;
        }

        return -1;
    }

    public int getConnectedToCloudlet() {
        return _connectedToCloudlet;
    }

    public void setConnectedToCloudlet(int _connectedToCloudlet) {
        this._connectedToCloudlet = _connectedToCloudlet;
    }

    public int getConnectedToPCloud() {
        return _connectedToPCloud;
    }

    public void setConnectedToPCloud(int _connectedToPCloud) {
        this._connectedToPCloud = _connectedToPCloud;
    }

    public double getAvailEnergy() {
        return _availEnergy;
    }

    public void setAvailEnergy(double _availEnergy) {
        this._availEnergy = _availEnergy;
    }

    public double getAvailBandwidth() {
        return _availBandwidth;
    }

    public void setAvailBandwidth(double _availBandwidth) {
        this._availBandwidth = _availBandwidth;
    }

    public int getAvailNetwork() {
        return _availNetwork;
    }

    public void setAvailNetwork(int _availNetwork) {
        this._availNetwork = _availNetwork;
    }

}
