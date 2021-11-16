/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.profiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * a general profiler structure
 */
@Service
public class Profiler {

    private static List<Profiler> ProfilersLIst = new ArrayList<>();
    private int profilerID = 0;
    private ProfilerType profType;
    /**
     * HashMap based on taskID and values
     */
    private HashMap<Integer, List<ProfilerEntry>> profilerDataMap;
    int _rescourceID;

    public Profiler() {
        profilerDataMap = new HashMap<>();
    }

    public Profiler(int _rescourceID, ProfilerType profType) {
        profilerDataMap = new HashMap<>();
        this._rescourceID = _rescourceID;
        this.profType = profType;
    }

    public void addProfilerEntry(int taskID, double VAL1, double VAL2) {
        getProfilerEntriesList(taskID).add(new ProfilerEntry(VAL1, VAL2));
    }

    public List<ProfilerEntry> getProfilerEntriesList(int taskID) {
        List<ProfilerEntry> listEntry = profilerDataMap.get(taskID);
        if (listEntry == null) {
            List<ProfilerEntry> toAddList = new ArrayList<>();
            profilerDataMap.put(taskID, toAddList);
            return profilerDataMap.get(taskID);
        }
        return listEntry;
    }

    public double getProfilerValue1Average(int taskID) {
        List<ProfilerEntry> list = getProfilerEntriesList(taskID);
        return getProfilerEntriesList(taskID).stream().mapToDouble(e -> e.VAL1).average().getAsDouble();
    }

    public double getProfilerValue2Average(int taskID) {
        return getProfilerEntriesList(taskID).stream().mapToDouble(e -> e.VAL2).average().getAsDouble();
    }

    public double getProfilerValueMin(int taskID) {
        if (getProfilerEntriesList(taskID).isEmpty()) {
            return 0;
        }
        return getProfilerEntriesList(taskID).stream().mapToDouble(e -> e.VAL2).min().getAsDouble();
    }

    public double getProfilerValueMax(int taskID) {
        if (getProfilerEntriesList(taskID).isEmpty()) {
            return 0;
        }
        return getProfilerEntriesList(taskID).stream().mapToDouble(e -> e.VAL2).max().getAsDouble();
    }

    public static List<Profiler> getProfilersLIst() {
        return ProfilersLIst;
    }

    public int getProfilerID() {
        return profilerID;
    }

    public int getRescourceID() {
        return _rescourceID;
    }

    public ProfilerType getProfType() {
        return profType;
    }

    public HashMap<Integer, List<ProfilerEntry>> getProfilerDataMap() {
        return profilerDataMap;
    }

    public void setProfilerID(int profilerID) {
        this.profilerID = profilerID;
    }

    public void setProfType(ProfilerType profType) {
        this.profType = profType;
    }

    public void setProfilerDataMap(HashMap<Integer, List<ProfilerEntry>> profilerDataMap) {
        this.profilerDataMap = profilerDataMap;
    }
    

}
