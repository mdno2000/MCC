/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.application;

import java.sql.Timestamp;
import java.time.Instant;

import phdproject.mcc.mccproject.resource.RescourceAPI;
import phdproject.mcc.mccproject.resource.RescourceLocation;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class TaskResult implements ITaskResult {

    double _procssingTime;
    double _dtTime;
    double energy;
    double _pcost;
    double _dtCost;
    double _dtEnergy;
    double _dataInTime;
    double _dataOutTime;
    double _inputDataSize;
    double _outDataSize;

    Timestamp _startExeTime;
    Timestamp _finishExeTime;

    /**
     * Log Task Execution Result
     */
    public TaskResult() {
        _procssingTime = 0.0;
        _dataInTime = 0.0;
        _dataOutTime = 0.0;
        _inputDataSize = 0.0;
        _outDataSize = 0.0;
        _dtTime = 0;
        energy = 0;
        _pcost = 0;
        _dtCost = 0;
        _dtEnergy = 0;
//        Instant instant = Instant.now();
        _startExeTime = new Timestamp(System.currentTimeMillis());
        _finishExeTime = new Timestamp(System.currentTimeMillis());
    }

    public void resetTaskResult() {
        _procssingTime = 0.0;
        _dataInTime = 0.0;
        _dataOutTime = 0.0;
        _inputDataSize = 0.0;
        _outDataSize = 0.0;
        _dtTime = 0;
        energy = 0;
        _pcost = 0;
        _dtCost = 0;
        _dtEnergy = 0;
//        Instant instant = Instant.now();
        _startExeTime = new Timestamp(System.currentTimeMillis());
        _finishExeTime = new Timestamp(System.currentTimeMillis());
    }

    /**
     *
     * @param _procssingTime
     * @param _dataInTime
     * @param _dataOutTime
     * @param _inputDataSize
     * @param _outDataSize
     * @param _startExeTime
     * @param _finishExeTime
     */
    public TaskResult(double _procssingTime, double _dataInTime, double _dataOutTime, double _inputDataSize, double _outDataSize, Timestamp _startExeTime, Timestamp _finishExeTime) {
        this._procssingTime = _procssingTime;
        this._dataInTime = _dataInTime;
        this._dataOutTime = _dataOutTime;
        this._inputDataSize = _inputDataSize;
        this._outDataSize = _outDataSize;
        this._startExeTime = _startExeTime;
        this._finishExeTime = _finishExeTime;
    }

    public double getProcssingTime() {
        return _procssingTime;
    }

    public void setProcssingTime(double _procssingTime) {
        this._procssingTime = _procssingTime;
    }

    public double getDataInTime() {
        return _dataInTime;
    }

    public double getDtTime() {
        return _dtTime;
    }

    public void setDtTime(double _dtTime) {
        this._dtTime = _dtTime;
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double _energy, RescourceLocation loc) {
        if ((loc == RescourceLocation.CLOUDLET || loc == RescourceLocation.PUBLICCLOUD) && _energy > 0) {
            int c = 0;
        }
        this.energy = _energy;
    }

    public double getPcost() {
        return _pcost;
    }

    public void setPcost(double _pcost) {
        this._pcost = _pcost;
    }

    public double getDtCost() {
        return _dtCost;
    }

    public void setDtCost(double _dtCost) {
        this._dtCost = _dtCost;
    }

    public double getDtEnergy() {
        return _dtEnergy;
    }

    public void setDtEnergy(int network, double TT) {
        if (network == 3) {
            this._dtEnergy = TT * 0.264;
        }

        if (network == 2) {
            this._dtEnergy = TT * 0.501;
        }

        if (network == 1) {
            this._dtEnergy = TT * 0.550;
        }

        //this._dtEnergy = 0;
    }

    public void setDtEnergy(double energy) {
        this._dtEnergy = energy;
    }

    public void setDataInTime(double _dataInTime) {
        this._dataInTime = _dataInTime;
    }

    public double getDataOutTime() {
        return _dataOutTime;
    }

    public void setDataOutTime(double _dataOutTime) {
        this._dataOutTime = _dataOutTime;
    }

    public double getInputDataSize(String loc, String loctype) {

        return 0;
    }

    public void setInputDataSize(double _inputDataSize) {
        this._inputDataSize = _inputDataSize;
    }

    public double getOutDataSize() {
        return _outDataSize;
    }

    public void setOutDataSize(double _outDataSize) {
        this._outDataSize = _outDataSize;
    }

    public Timestamp getStartExeTime() {
        return _startExeTime;
    }

    public void setStartExeTime(Timestamp _startExeTime) {
        this._startExeTime = _startExeTime;
    }

    public Timestamp getFinishExeTime() {
        return _finishExeTime;
    }

    public void setFinishExeTime(Timestamp _finishExeTime) {
        this._finishExeTime = _finishExeTime;
    }

    public double get_procssingTime() {
        return _procssingTime;
    }

    public double get_dtTime() {
        return _dtTime;
    }

    public double get_pcost() {
        return _pcost;
    }

    public double get_dtCost() {
        return _dtCost;
    }

    public double get_dtEnergy() {
        return _dtEnergy;
    }

    public double get_dataInTime() {
        return _dataInTime;
    }

    public double get_dataOutTime() {
        return _dataOutTime;
    }

    public double get_inputDataSize() {
        return _inputDataSize;
    }

    public double get_outDataSize() {
        return _outDataSize;
    }

    public Timestamp get_startExeTime() {
        return _startExeTime;
    }

    public Timestamp get_finishExeTime() {
        return _finishExeTime;
    }
}
