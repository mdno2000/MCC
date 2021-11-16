/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.queuingsystem;

import java.util.List;
import org.apache.commons.math3.distribution.UniformRealDistribution;

/**
 *
 * define data distribution for service time, arrival time, and others
 */
public class Distribution implements IDistribution {
    String name; // name of queue val 
    double mean; // mean callculated based on given distribution
    double variance;    
    double cv;  // coefficient of variance
    List<String> paramNames; // parametrs names for Queue parameter distribution 
    List<Double> paramValues; // parametrs values for Queue parameter distribution 
    DistributionType distributionType;

    /**
     *
     * @param paramNames
     * @param paramValues
     * @param distributionType
     */
    public Distribution(List<String> paramNames, List<Double> paramValues, DistributionType distributionType) {
        this.paramNames = paramNames;
        this.paramValues = paramValues;
        this.distributionType = distributionType;
        mean=0;
        variance=0;
        cv=0;
        calculateDsitributionValues(); // get values of mean, STD , varieance, ..
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    public double getVariance() {
        return variance;
    }

    public void setVariance(double variance) {
        this.variance = variance;
    }

    public double getCv() {
        return cv;
    }

    public void setCv(double cv) {
        this.cv = cv;
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

    public DistributionType getDistributionType() {
        return distributionType;
    }

    public void setDistributionType(DistributionType distributionType) {
        this.distributionType = distributionType;
    }

    /**
     *
     */
    @Override
    public void calculateDsitributionValues() {
        switch(distributionType){
            case UNIFORM:
                double min=paramValues.get(0);
                double max=paramValues.get(1);
                UniformRealDistribution unif=new UniformRealDistribution(min,max);                
                setMean(unif.getNumericalMean());
                setVariance(unif.getNumericalVariance());
                setCv(Math.sqrt(getVariance())/getMean());
                break;
            case EXPONENTIAL:
                break;
            default:
                break;
        }
    }
}
