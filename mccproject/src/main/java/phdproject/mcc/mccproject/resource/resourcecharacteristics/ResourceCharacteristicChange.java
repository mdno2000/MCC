/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.resource.resourcecharacteristics;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * a new entry to a resource Characteristic
 */
public class ResourceCharacteristicChange {
    Object _curVal;
    Object _newVal;
    Timestamp _changValTimestamp; // change time

    public ResourceCharacteristicChange()
    {
        
    }
    public ResourceCharacteristicChange(Object _curVal, Object _newVal) {
        this._curVal = _curVal;
        this._newVal = _newVal;
        Date date= new Date();
        _changValTimestamp=new Timestamp(date.getTime());
    }
     public ResourceCharacteristicChange(Object _curVal) {
        this._curVal = _curVal;
        this._newVal = _curVal;
        Date date= new Date();
        _changValTimestamp=new Timestamp(date.getTime());
    }

    public Object getCurVal() {
        return _curVal;
    }

    public void setCurVal(Object _curVal) {
        this._curVal = _curVal;
    }

    public Object getNewVal() {
        return _newVal;
    }

    public void setNewVal(Object _newVal) {
        this._newVal = _newVal;
    }

    public Timestamp getChangValTimestamp() {
        return _changValTimestamp;
    }

    public void setChangValTimestamp(Timestamp _changValTimestamp) {
        this._changValTimestamp = _changValTimestamp;
    }
}
