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
 * class for locl best and global best
 */
public class PSOBestParticle {

    RescourceLocation[] _pos;
    double _cost;

    public PSOBestParticle() {
        _pos = new RescourceLocation[ApplicationManager.getNUMTASK()];
        _cost = Double.POSITIVE_INFINITY; // default value for minimization propelm 
    }

    public RescourceLocation[] getPos() {
        return _pos;
    }

    public void setPos(RescourceLocation[] _pos) {
        //System.out.println("Value changed... new POS ");
//        for (RescourceLocation loc : _pos) {
//            System.out.print(loc.name() + " ");
//        }
         System.arraycopy(_pos, 0, this._pos, 0, _pos.length);
    }

    public void setPos(RescourceLocation[] _pos, int index) {
        //System.out.println("Value changed... new POS ");
//        for (RescourceLocation loc : _pos) {
//            System.out.print(loc.name() + " ");
//        }
        //System.out.println("gbest pos...Index.." + index);
        this._pos = new RescourceLocation[ApplicationManager.getNUMTASK()];
       
        System.arraycopy(_pos, 0, this._pos, 0, _pos.length);
         
    }

    public double getCost() {
        return _cost;
    }

    public void setCost(double _cost) {
        //System.out.println("gbest value..." + _cost);
        this._cost = _cost;
    }

    public void setCost(double _cost, int index) {
        //System.out.println("gbest value..." + _cost + " Index.." + index);
        this._cost = _cost;
    }

}
