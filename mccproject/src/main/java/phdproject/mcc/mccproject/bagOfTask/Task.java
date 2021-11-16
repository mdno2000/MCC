/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.bagOfTask;

import java.io.Serializable;
import java.util.ArrayList;
import phdproject.mcc.mccproject.application.TaskResult;
import phdproject.mcc.mccproject.profiler.Profiler;
import phdproject.mcc.mccproject.resource.RescourceLocation;
import phdproject.mcc.mccproject.resource.Resource;

/**
 *
 * BoT task
 */
public class Task {

    Profiler _etInMobileProfiler; // Execution time in mobile 
    Profiler _etInPubCloudProfiler; // Execution time in cloud 
    Profiler _etInPriCloudProfiler; // Execution time in private cloud 
    int _taskID;
    String _taskName;
    double _deadline;
    String _inputFileRef; // data adress location
    RescourceLocation _outFileLocationType; // resource type
    RescourceLocation _inputFileLocationType;
    Resource _inputFileResLocation;
    Resource _outputFileResLocation;
    String _outFileRef;
    TaskResult taskres;
    double TASKETEST;
    double TASKETESTS;
    double TASKCPULOAD;
    public int associatedIndex;

    public double getTASKETEST() {
        return TASKETEST;
    }

    public void setTASKETEST(double TASKETEST) {
        this.TASKETEST = TASKETEST;
    }

    public double getTASKETESTS() {
        return TASKETESTS;
    }

    public void setTASKETESTS(double TASKETESTS) {
        this.TASKETESTS = TASKETESTS;
    }

    public double getTASKCPULOAD() {
        return TASKCPULOAD;
    }

    public void setTASKCPULOAD(double TASKCPULOAD) {
        this.TASKCPULOAD = TASKCPULOAD;
    }

    public void setInputFileResLocation(Resource _inputFileResLocation) {
        this._inputFileResLocation = _inputFileResLocation;
    }

    /**
     *
     * @param _taskID
     * @param _taskName
     * @param _deadline
     * @param _inputFileLocationType
     * @param _inputFileRef
     * @param _outFileLocationType
     * @param _outFileRef
     */
    public Task() {

    }

    public Task(int _taskID, String _taskName, double _deadline, RescourceLocation _inputFileLocationType,
            String _inputFileRef, RescourceLocation _outFileLocationType, String _outFileRef) {
        this._taskID = _taskID;
        this._taskName = _taskName;
        this._deadline = _deadline;
        this._inputFileRef = _inputFileRef;
        this._outFileRef = _outFileRef;
        taskres = new TaskResult();
        associatedIndex = _taskID;
    }

    public Task(Task copy, int _taskID) {
        this._taskID = _taskID;
        this._taskName = "Task" + _taskID;
        this._deadline = copy.getDeadline();
        this._inputFileRef = copy.getInputFileRef();
        this._outFileRef = copy.getOutFileRef();
        this._inputFileLocationType = copy.getInputFileLocationType();
        this._inputFileResLocation = copy.getInputFileResLocation();
        taskres = new TaskResult();
        taskres.setInputDataSize(copy.taskres.get_inputDataSize());
        this.TASKETEST = copy.getTASKETEST();
        this.TASKETESTS = copy.getTASKETESTS();
        this.TASKCPULOAD = copy.getTASKCPULOAD();
        associatedIndex = copy._taskID - 1;
    }

    /**
     * get data from input string (in user task schema csv file)
     *
     * @param _taskInputLine
     * @param delim
     */
    public Task(String _taskInputLine, String delim) {

        String[] taskProp = _taskInputLine.split(delim);
        this._taskID = Integer.parseInt(taskProp[0]);
        this._taskName = taskProp[1];
        // change deadline 
        double deadline_factor = 1;
        this._deadline = Double.parseDouble(taskProp[6]) / deadline_factor;
        // Input file location 
        this._inputFileLocationType = Resource.getLocationFromID(Integer.parseInt(taskProp[2]));
        this._inputFileRef = taskProp[3];

        Resource dataInputLocRescource = Resource.getRescourceByLocationURL(this._inputFileRef);
        if (dataInputLocRescource != null) {
            _inputFileResLocation = dataInputLocRescource;
        } else {
            _inputFileResLocation = Resource.getRescourceByID(Integer.parseInt(taskProp[2]));
        }
        int fileLocID = Integer.parseInt(taskProp[2]);
        if (fileLocID == 9) {
            _inputFileResLocation = Resource.getResourcesList().get(3);
        }
        if (fileLocID == 0) {
            _inputFileResLocation = Resource.getResourcesList().get(0);
        }
        if (fileLocID == 2) {
            _inputFileResLocation = Resource.getResourcesList().get(2);
        }

        this._outFileRef = taskProp[5];
        Resource dataOutputLocRescource = Resource.getRescourceByLocationURL(this._outFileRef);
        if (dataInputLocRescource != null) {
            _outputFileResLocation = dataOutputLocRescource;
        }
        // Input file location 
        this._outFileLocationType = Resource.getLocationFromID(Integer.parseInt(taskProp[4]));
        taskres = new TaskResult();
        associatedIndex = _taskID;
    }

    /**
     *
     * @return
     */
    public Profiler getEtInMobileProfiler() {
        return _etInMobileProfiler;
    }

    /**
     *
     * @return
     */
    public Profiler getEtInPubCloudProfiler() {
        return _etInPubCloudProfiler;
    }

    /**
     *
     * @return
     */
    public Profiler getEtInPriCloudProfiler() {
        return _etInPriCloudProfiler;
    }

    /**
     *
     * @return
     */
    public TaskResult runTask() {
        return null;
    }

    public int getTaskID() {
        return _taskID;
    }

    public void setTaskID(int _taskID) {
        this._taskID = _taskID;
    }

    @Override
    public String toString() {
        return "Task{"
                + "ID: " + _taskID
                + ", Name: " + _taskName
                + ", Deadline=" + _deadline
                + ", _inputFileLocationType=" + _inputFileLocationType
                + ", inputdata loc: " + _inputFileResLocation.getResName()
                + ", _inputFileRef=" + _inputFileRef
                + ", _outFileLocationType=" + _outFileLocationType
                + ", _outFileRef=" + _outFileRef
                + ", outdata loc: " + _outputFileResLocation.getResName()
                + ", data size: " + taskres.get_inputDataSize() + " }";
    }

    public String getTaskName() {
        return _taskName;
    }

    public void setTaskName(String _taskName) {
        this._taskName = _taskName;
    }

    public double getDeadline() {
        return _deadline;
    }

    public void setDeadline(double _deadline) {
        this._deadline = _deadline;
    }

    public RescourceLocation getInputFileLocationType() {
        return _inputFileLocationType;
    }

    public void setInputFileLocationType(RescourceLocation _inputFileLocationType) {
        this._inputFileLocationType = _inputFileLocationType;
    }

    public String getInputFileRef() {
        return _inputFileRef;
    }

    public void setInputFileRef(String _inputFileRef) {
        this._inputFileRef = _inputFileRef;
    }

    public RescourceLocation getOutFileLocationType() {
        return _outFileLocationType;
    }

    public void setOutFileLocationType(RescourceLocation _outFileLocationType) {
        this._outFileLocationType = _outFileLocationType;
    }

    public String getOutFileRef() {
        return _outFileRef;
    }

    public void setOutFileRef(String _outFileRef) {
        this._outFileRef = _outFileRef;
    }

    public Resource getInputFileResLocation() {
        return _inputFileResLocation;
    }

    public Resource getOutputFileResLocation() {
        return _outputFileResLocation;
    }

    public TaskResult getTaskres() {
        return taskres;
    }

    public void setTaskres(TaskResult taskres) {
        this.taskres = taskres;
    }

    public void resetTaskInfo() {
        getTaskres().resetTaskResult();
    }

}
