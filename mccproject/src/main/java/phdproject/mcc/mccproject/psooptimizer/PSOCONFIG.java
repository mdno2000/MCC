/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.psooptimizer;

import org.apache.commons.math3.distribution.UniformRealDistribution;

/**
 *
 * Setup PSO configuration 
 */
public class PSOCONFIG { 
        
    public static final int KAPPA=1;
    public static final double PHI1=2.05;
    public static final double PHI2=2.05;  
    public static final int NUMPARTICALS=100;
    public static final int MAXITERATIONS=1500;  
    public static final double PHI=PHI1+PHI2;
    public static final double CHI=(2*KAPPA)/Math.abs((2-PHI-Math.sqrt(Math.pow(PHI,2)-4*PHI)));
    public static final UniformRealDistribution c1dist=new UniformRealDistribution(0,CHI*PHI);
    public static final double C1=c1dist.sample();
    public static final UniformRealDistribution c2dist=new UniformRealDistribution(0,CHI*PHI2);    
    public static final double C2=c1dist.sample();   
    public PSOCONFIG() {
    } 

}
