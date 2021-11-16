/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.queuingsystem;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class testqueue {
    public static void main(String args []){
        
        List<String> paramNamesArrivalTime=new ArrayList<> ();
        paramNamesArrivalTime.add("MIN");
        paramNamesArrivalTime.add("MAX");
        List<Double> paramValuesArrivalTime=new ArrayList<> ();
        paramValuesArrivalTime.add(12.0);
        paramValuesArrivalTime.add(14.0);
        List<String> paramNamesServiceTime=new ArrayList<> ();
        List<Double> paramValuesServiceTime=new ArrayList<> ();
        paramNamesServiceTime.add("MIN");
        paramNamesServiceTime.add("MAX");        
        paramValuesServiceTime.add(5.6);
        paramValuesServiceTime.add(7.2);
        Queue q=new Queue("GG1Queue", QueueType.GG1, paramNamesArrivalTime, paramValuesArrivalTime, 
                DistributionType.UNIFORM, paramNamesServiceTime, paramValuesServiceTime, DistributionType.UNIFORM);        
        
        q.printQueue();               
    }       
}
