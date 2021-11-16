/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.queuingsystem;

import java.util.List;

/**
 *
 * Entry for the queuing system / service time, arrival time, ...
 * parameters different according to distribution type
 */
public class QueueVal implements IQueuVal {
    
    String QueueValName;
    List<String> paramNames; // parametrs names for Queue parameter distribution 
    List<Double> paramValues; // parametrs values for Queue parameter distribution 
    Distribution distribution; 
    DistributionType distributiontype;

    /**
     *
     * @param queueValName
     * @param paramNames
     * @param paramValues
     * @param distributiontype
     */
    public QueueVal(String queueValName,List<String> paramNames, List<Double> paramValues, DistributionType distributiontype) {
        this.paramNames = paramNames;
        this.paramValues = paramValues;
        this.distributiontype = distributiontype;
        this.QueueValName=queueValName;
        distribution=new Distribution(paramNames, paramValues, distributiontype);   // initlize distribution
        distribution.calculateDsitributionValues();
    } 

    public String getQueueValName() {
        return QueueValName;
    }

    public void setQueueValName(String QueueValName) {
        this.QueueValName = QueueValName;
    }

    public List<String> getParamNames() {
        return paramNames;
    }

    public void setParamNames(List<String> paramNames) {
        this.paramNames = paramNames;
    }

    public List<Double> getParamValues() {
        return paramValues;
    }

    public void setParamValues(List<Double> paramValues) {
        this.paramValues = paramValues;
    }

    public Distribution getDistribution() {
        return distribution;
    }

    public void setDistribution(Distribution distribution) {
        this.distribution = distribution;
    }

    public DistributionType getDistributiontype() {
        return distributiontype;
    }

    public void setDistributiontype(DistributionType distributiontype) {
        this.distributiontype = distributiontype;
    }
    
    
}
