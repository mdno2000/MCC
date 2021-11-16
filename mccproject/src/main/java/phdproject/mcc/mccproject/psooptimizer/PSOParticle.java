/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.psooptimizer;

import phdproject.mcc.mccproject.application.ApplicationManager;
import phdproject.mcc.mccproject.resource.RescourceLocation;

/**
 *
 * Particle has: position, velocity, best local position, and cost 
 */
public class PSOParticle {

    RescourceLocation[] _pos;
    RescourceLocation[] _bestpos;
    double _cost;
    double[] _velocity;
    PSOBestParticle _best; // has different structure as no need to have a local best
    int _validSol;
    public PSOParticle() {
        _pos = new RescourceLocation[ApplicationManager.getNUMTASK()];
        _bestpos = new RescourceLocation[ApplicationManager.getNUMTASK()];
        _velocity = new double[ApplicationManager.getNUMTASK()];
        _cost =Double.POSITIVE_INFINITY;
        _best=new PSOBestParticle(); 
        _validSol=0;
    }

    /**
     * see matlab code for calculating new velocity 
     * PSOCONFIG has values for PSO model
     * @param targetPrticle
     * @param globalePrticle 
     */
    public static void updateParticleVelocity(PSOParticle targetPrticle, PSOBestParticle globalePrticle) {         
        for (int tIndex = 0; tIndex < ApplicationManager.getNUMTASK(); tIndex++) {
            
//            double x1= targetPrticle.getVelocity()[tIndex] = (targetPrticle.getVelocity()[tIndex] * PSOCONFIG.CHI);
//            double x2=((PSOCONFIG.C1 * Math.random() * (PSOParticle.getIntFromLoc(targetPrticle.getBest().getPos()[tIndex]))));
//            double x3=(PSOParticle.getIntFromLoc(targetPrticle.getPos()[tIndex]));
//            double x4=(PSOCONFIG.C2 * Math.random() * (PSOParticle.getIntFromLoc(globalePrticle.getPos()[tIndex])));
//            double x5=(PSOParticle.getIntFromLoc(targetPrticle.getPos()[tIndex]));
//            System.out.println("Term1 " + (targetPrticle.getVelocity()[tIndex] * PSOCONFIG.CHI));
//            System.out.println("Term2 " + ((PSOCONFIG.C1 * Math.random() * (PSOParticle.getIntFromLoc(targetPrticle.getBestpos()[tIndex])))));
//            System.out.println("Term3 " + (PSOParticle.getIntFromLoc(targetPrticle.getPos()[tIndex])));
//            System.out.println("Term4 " + ((PSOCONFIG.C2 * Math.random() * (PSOParticle.getIntFromLoc(globalePrticle.getPos()[tIndex])))));
//            System.out.println("Term5"  + ((PSOParticle.getIntFromLoc(targetPrticle.getPos()[tIndex]))));
//            
            try {
                targetPrticle.getVelocity()[tIndex] = (targetPrticle.getVelocity()[tIndex] * PSOCONFIG.CHI)
                    + ((PSOCONFIG.C1 * Math.random() * (PSOParticle.getIntFromLoc(targetPrticle.getBestpos()[tIndex])))
                    - (PSOParticle.getIntFromLoc(targetPrticle.getPos()[tIndex])))
                    + ((PSOCONFIG.C2 * Math.random() * (PSOParticle.getIntFromLoc(globalePrticle.getPos()[tIndex])))
                    - (PSOParticle.getIntFromLoc(targetPrticle.getPos()[tIndex])));
                //System.out.println("task Index " + tIndex+" Callculate Velocity.." + targetPrticle.getVelocity()[tIndex] );
            }
            catch(Exception ex){
                ex.getStackTrace();
            }
        }
    }
    /**
     * see matlab code of how update position
     * @param targetPrticle 
     */
    public static void updateParticlePosition(PSOParticle targetPrticle){
        for (int tIndex = 0; tIndex < ApplicationManager.getNUMTASK(); tIndex++) {
            RescourceLocation prevLoc=targetPrticle.getPos()[tIndex];
            targetPrticle.getPos()[tIndex] = PSOParticle.getLocFromDouble(PSOParticle.getIntFromLoc(targetPrticle.getPos()[tIndex])
                    + targetPrticle.getVelocity()[tIndex]);
            double velocity = targetPrticle.getVelocity()[tIndex];
            RescourceLocation postLoc=targetPrticle.getPos()[tIndex];
            //System.out.println("Task : " + tIndex + "Prev Pos : " + prevLoc + " Post Pos "+ postLoc + " Velocity: " + velocity);
        }
    }
    public RescourceLocation getRescourceLocationFromDouble(double val) {
        long round = Math.round(val);
        switch ((int) round) {
            case 1:
                return RescourceLocation.MOBILE;
            case 2:
                return RescourceLocation.PUBLICCLOUD;
            case 3:
                return RescourceLocation.CLOUDLET;
            default:
                return RescourceLocation.NONE;
        }
    }

    public void moveParticle() throws Exception {

        for (int tIndex = 0; tIndex < ApplicationManager.getNUMTASK(); tIndex++) {
            int locToInt = PSOParticle.getIntFromLoc(getPos()[tIndex]);
            if (locToInt != 0) {
                double newVelocity = getVelocity()[tIndex] + locToInt;

            } else {
                throw new Exception();
            }
        }
    }
    public static int getIntFromLoc(RescourceLocation loc) {         
        switch (loc) {
            case MOBILE:
                return 1;
            case PUBLICCLOUD:
                return 2;
            case CLOUDLET:
                return 3;
            default:
                return 0;
        }
    }

    public static RescourceLocation getLocFromDouble(double newLocVal) {

        if (Math.abs(newLocVal) >= 0 && Math.abs(newLocVal) <= 1) {
            return RescourceLocation.MOBILE;
        }

        if (Math.abs(newLocVal) > 1 && Math.abs(newLocVal) <= 2) {
            return RescourceLocation.PUBLICCLOUD;
        }

        if (Math.abs(newLocVal) > 2 && Math.abs(newLocVal) <= 3) {
            return RescourceLocation.CLOUDLET;
        } 
        return RescourceLocation.PUBLICCLOUD;
    }

    public RescourceLocation[] getPos() {
        return _pos;
    }

    public void setPos(RescourceLocation[] _pos) {
        this._pos = _pos;
    }

    public double getCost() {
        return _cost;
    }

    public void setCost(double _cost) {
        this._cost = _cost;
    }

    public double[] getVelocity() {
        return _velocity;
    }

    public void setVelocity(double[] _velocity) {
        this._velocity = _velocity;
    }

    public PSOBestParticle getBest() {
        return _best;
    }

    public void setBest(PSOBestParticle _best) {
        this._best = _best;
    }

    public RescourceLocation[] getBestpos() {
        return _bestpos;
    }

    public void setBestpos(RescourceLocation[] _bestpos) {
        this._bestpos = _bestpos;
    }

    public int getValidSol() {
        return _validSol;
    }

    public void setValidSol(int _validSol) {
        this._validSol = _validSol;
    }

}
