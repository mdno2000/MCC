/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.profiler;

import java.sql.Timestamp;

/**
 *
 * profiler entry value
 */
public class ProfilerEntry {
    public Timestamp _entryTime;
    public double VAL1; // value type
    public double VAL2; // real value
    
    public ProfilerEntry(double _v1,double _v2) {
        this.VAL1=_v1;
        this.VAL2=_v2;
    }
     public ProfilerEntry() {
        this.VAL1=0;
        this.VAL2=0;
    }
}
