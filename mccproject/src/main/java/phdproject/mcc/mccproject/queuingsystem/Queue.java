/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.queuingsystem;

import java.util.List;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class Queue implements IQueue{

    String qname; // queue name 
    double s; // number of servers 
    double p; // queue utilization 
    double waitingtime; // waiting time in the queue 
    QueueVal arrivaleTime; // queue arrivale time 
    QueueVal serviceTime; // queue service time 
    QueueType queutype; // Queue type

    /**
     *
     * @param qname
     * @param queutype
     * @param paramNamesArrivalTime
     * @param paramValuesArrivalTime
     * @param distributiontypeArrivalTime
     * @param paramNamesServiceTime
     * @param paramValuesServiceTime
     * @param distributiontypeServiceTime 
     */
    public Queue(String qname, QueueType queutype,
            List<String> paramNamesArrivalTime, List<Double> paramValuesArrivalTime, DistributionType distributiontypeArrivalTime,
            List<String> paramNamesServiceTime, List<Double> paramValuesServiceTime, DistributionType distributiontypeServiceTime) {
        this.qname = qname;
        this.queutype = queutype;
        arrivaleTime=new QueueVal("ArrivalTime", paramNamesArrivalTime, paramValuesArrivalTime, distributiontypeArrivalTime);
        serviceTime=new QueueVal("ServiceTime", paramNamesServiceTime, paramValuesServiceTime, distributiontypeServiceTime);
        s=1;
        processQueue();
    }
    /**
     * calculate waiting time
     */
    @Override
    public void callculateWaitingTime() {
        switch(queutype){
            
            case GG1:
                waitingtime=(((1/arrivaleTime.getDistribution().getMean()) 
                        * (arrivaleTime.getDistribution().getCv() + serviceTime.getDistribution().getCv()))
                        / (2*(1-p))); 
                break;
            default:
                break;
        }
    }

    /**
     * calculate utilization factor
     */
    @Override
    public void calculateUtlization() {
        p=((1/arrivaleTime.getDistribution().getMean()) /
                ((1/serviceTime.getDistribution().getMean())*s));
    }

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
    }

    public double getS() {
        return s;
    }

    public void setS(double s) {
        this.s = s;
    }

    public double getP() {
        return p;
    }

    public void setP(double p) {
        this.p = p;
    }

    public double getWaitingtime() {
        return waitingtime;
    }

    public void setWaitingtime(double waitingtime) {
        this.waitingtime = waitingtime;
    }

    public QueueVal getArrivaleTime() {
        return arrivaleTime;
    }

    public void setArrivaleTime(QueueVal arrivaleTime) {
        this.arrivaleTime = arrivaleTime;
    }

    public QueueVal getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(QueueVal serviceTime) {
        this.serviceTime = serviceTime;
    }

    public QueueType getQueutype() {
        return queutype;
    }

    public void setQueutype(QueueType queutype) {
        this.queutype = queutype;
    }

    @Override
    public void processQueue() {
        this.calculateUtlization();
        this.callculateWaitingTime();
    }

    @Override
    public void printQueue() {
        System.out.println("Queue Utilization : " + getP() );
        System.out.println("Queue Service Time : " + serviceTime.getDistribution().getMean() );
        System.out.println("Queue Arrivale Time : " + arrivaleTime.getDistribution().getMean() );
        System.out.println("Queue Waiting Time : " + getWaitingtime() );
    } 
}
