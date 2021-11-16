/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.application;

import phdproject.mcc.mccproject.resource.IResource;
import phdproject.mcc.mccproject.resource.Resource;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class TaskRunInput implements ITaskRunInput{
     Resource _inputFileLocation;
     Resource _outFileLocation;
     Resource _cmputaationResource; 
     double _inputDataSize;
     double _outputDataSize;
     double _prcoessingTime;
     double _prcoessingCost;
     double _prcoessingEnergy;     
     double _dataTransferTime;
     double _dataTransferCost;
     double _dataTransferEnergy;

    public TaskRunInput(Resource _inputFileLocation, Resource _outFileLocation, Resource _cmputaationResource){
        this._inputFileLocation = _inputFileLocation;
        this._outFileLocation = _outFileLocation;
        this._cmputaationResource = _cmputaationResource;
        this._inputDataSize = 0;
        this._outputDataSize = 0;
        this._prcoessingTime = 0;
        this._prcoessingCost = 0;
        this._prcoessingEnergy = 0;
        this._dataTransferTime = 0;
        this._dataTransferCost = 0;
        this._dataTransferEnergy = 0;
    } 
     
    public IResource getInputFileLocation() {
        return _inputFileLocation;
    }

    public void setInputFileLocation(Resource _inputFileLocation) {
        this._inputFileLocation = _inputFileLocation;
    }

    public IResource getOutFileLocation() {
        return _outFileLocation;
    }

    public void setOutFileLocation(Resource _outFileLocation) {
        this._outFileLocation = _outFileLocation;
    }

    public IResource getCmputaationResource() {
        return _cmputaationResource;
    }

    public void setCmputaationResource(Resource _cmputaationResource) {
        this._cmputaationResource = _cmputaationResource;
    }

    public double getInputDataSize() {
        return _inputDataSize;
    }

    public void setInputDataSize(double _inputDataSize) {
        this._inputDataSize = _inputDataSize;
    }

    public double getOutputDataSize() {
        return _outputDataSize;
    }

    public void setOutputDataSize(double _outputDataSize) {
        this._outputDataSize = _outputDataSize;
    }

    public double getPrcoessingTime() {
        return _prcoessingTime;
    }

    public void setPrcoessingTime(double _prcoessingTime) {
        this._prcoessingTime = _prcoessingTime;
    }

    public double getPrcoessingCost() {
        return _prcoessingCost;
    }

    public void setPrcoessingCost(double _prcoessingCost) {
        this._prcoessingCost = _prcoessingCost;
    }

    public double getPrcoessingEnergy() {
        return _prcoessingEnergy;
    }

    public void setPrcoessingEnergy(double _prcoessingEnergy) {
        this._prcoessingEnergy = _prcoessingEnergy;
    }

    public double getDataTransferTime() {
        return _dataTransferTime;
    }

    public void setDataTransferTime(double _dataTransferTime) {
        this._dataTransferTime = _dataTransferTime;
    }

    public double getDataTransferCost() {
        return _dataTransferCost;
    }

    public void setDataTransferCost(double _dataTransferCost) {
        this._dataTransferCost = _dataTransferCost;
    }

    public double getDataTransferEnergy() {
        return _dataTransferEnergy;
    }

    public void setDataTransferEnergy(double _dataTransferEnergy) {
        this._dataTransferEnergy = _dataTransferEnergy;
    } 
    
}
