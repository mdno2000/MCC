/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.psooptimizer;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import phdproject.mcc.mccproject.bagOfTask.Task;
import phdproject.mcc.mccproject.resource.RescourceLocation;
/**
 *
 * save optimization result for the entire solution
 */
public class PSOOptimizationResult {
    private double totalCost;
    private double totalEnergy;
    private double totalETime;
    private PSOParticle particale;
    public RescourceLocation[] optimizationDecision;
    int _userID;
    int _appID;

    public PSOParticle getParticale() {
        return particale;
    }

    public void setParticale(PSOParticle particale) {
        this.particale = particale;
    }
    int _partcID;
    double _ETM;
    double _WT;    
    double _PTC;
    double _TTC;
    double _TC;
    double _PE;
    double _TTE;
    double _WE;

    public double getWE() {
        return _WE;
    }

    public void setWE(double _WE) {
        this._WE = _WE;
    }
    double _TE;
    double _DSIZE;
    double _BW;
    int _NETWORK;
    double _AVAILE;
    Timestamp _recordTime;
    List<Task> userAppTask; // BoT of task to be  processed 

    public Timestamp getRecordTime() {
        return _recordTime;
    }

    public void setRecordTime(Timestamp _recordTime) {
        this._recordTime = _recordTime;
    }

    public List<Task> getUserAppTask() {
        return userAppTask;
    }

    public void setUserAppTask(List<Task> userAppTask) {
        this.userAppTask = userAppTask;
    }
    public PSOOptimizationResult(){
        this.totalCost = 0;
        this.totalEnergy = 0;
        this.totalETime = 0;        
        this._userID = 0;
        this._appID = 0;
        this._recordTime=new Timestamp(System.currentTimeMillis());
        this._partcID=0;
    }

    public double getETM() {
        return _ETM;
    }

    public void setETM(double _ETM) {
        this._ETM = _ETM;
    }

    public double getWT() {
        return _WT;
    }

    public void setWT(double _WT) {
        this._WT = _WT;
    }

    public double getPTC() {
        return _PTC;
    }

    public void setPTC(double _PTC) {
        this._PTC = _PTC;
    }

    public double getTTC() {
        return _TTC;
    }

    public void setTTC(double _TTC) {
        this._TTC = _TTC;
    }

    public double getTC() {
        return _TC;
    }

    public void setTC(double _TC) {
        this._TC = _TC;
    }

    public double getPE() {
        return _PE;
    }

    public void setPE(double _PE) {
        this._PE = _PE;
    }

    public double getTTE() {
        return _TTE;
    }

    public void setTTE(double _TTE) {
        this._TTE = _TTE;
    }

    public double getTE() {
        return _TE;
    }

    public void setTE(double _TE) {
        this._TE = _TE;
    }

    public double getDSIZE() {
        return _DSIZE;
    }

    public void setDSIZE(double _DSIZE) {
        this._DSIZE = _DSIZE;
    }

    public double getBW() {
        return _BW;
    }

    public void setBW(double _BW) {
        this._BW = _BW;
    }

    public int getNETWORK() {
        return _NETWORK;
    }

    public void setNETWORK(int _NETWORK) {
        this._NETWORK = _NETWORK;
    }

    public double getAVAILE() {
        return _AVAILE;
    }

    public void setAVAILE(double _AVAILE) {
        this._AVAILE = _AVAILE;
    }
     public PSOOptimizationResult(double totalCost, double totalEnergy, double totalETime, RescourceLocation[] optimizationDecision, int _userID, int _appID) {
        this.totalCost = totalCost;
        this.totalEnergy = totalEnergy;
        this.totalETime = totalETime;
        this.optimizationDecision = optimizationDecision;
        this._userID = _userID;
        this._appID = _appID;
        this._recordTime=new Timestamp(System.currentTimeMillis());
    }

    public int getPartcID() {
        return _partcID;
    }

    public void setPartcID(int _partcID) {
        this._partcID = _partcID;
    }
    
    
    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getTotalEnergy() {
        return totalEnergy;
    }

    public void setTotalEnergy(double totalEnergy) {
        this.totalEnergy = totalEnergy;
    }

    public double getTotalETime() {
        return totalETime;
    }

    public void setTotalETime(double totalETime) {
        this.totalETime = totalETime;
    }

    public RescourceLocation[] getOptimizationDecision() {
        return optimizationDecision;
    }

    public void setOptimizationDecision(RescourceLocation[] optimizationDecision) {
        this.optimizationDecision = optimizationDecision;
    }

    public int getUserID() {
        return _userID;
    }

    public void setUserID(int _userID) {
        this._userID = _userID;
    }

    public int getAppID() {
        return _appID;
    }

    public void setAppID(int _appID) {
        this._appID = _appID;
    }

    @Override
    public String toString() {
        String msg= "PSOOptimizationResult{" + "Totla Cost: " + totalCost + ", Total Energy: " + totalEnergy + ", Total execution Time: " + totalETime + "\n";
        msg=msg + "User ID : " + _userID + ", Application ID: " + _appID + ", Record Time: " + new Date(_recordTime.getTime()).toString() + "}\n";
        msg=msg + "Optimization Decision: \n";
        
        for(RescourceLocation loc: this.getOptimizationDecision()){
            msg=msg + loc.name() + "\n";
        }
        
        return msg;
    }
    
}
