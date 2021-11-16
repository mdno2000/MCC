/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package phdproject.mcc.mccproject.resource.resourcecharacteristics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mohammad Alkhalaileh
 */
public class ResourceCharacteristicEntry {

    public static int ResourceCharacteristicEntryID = 1;
    int resourceCharacteristicEntryID;
    int resourceCharacteristicOwnerID;
    ResourceCharacteristic _resChar;
    Object _curVal; // can take any value type
    Object _costPerUnit;
    List<ResourceCharacteristicChange> resourceCharacteristicChangeList;

    public ResourceCharacteristicEntry()
    {
        this.resourceCharacteristicChangeList = new ArrayList<>();
    }
    public ResourceCharacteristicEntry(ResourceCharacteristic _resChar, Object defaultVal, Object costPerUnit, int ownerID) {
        this.resourceCharacteristicChangeList = new ArrayList<>();
        this.resourceCharacteristicEntryID = ResourceCharacteristicEntry.ResourceCharacteristicEntryID;
        this._curVal = defaultVal;
        this._costPerUnit = costPerUnit;
        this._resChar = _resChar;
        this.resourceCharacteristicOwnerID = ownerID;
        getResourceCharacteristicChangeList().add(new ResourceCharacteristicChange(defaultVal));
        ResourceCharacteristicEntry.ResourceCharacteristicEntryID++;
    }

    public int getResourceCharacteristicEntryID() {
        return resourceCharacteristicEntryID;
    }

    public void setResourceCharacteristicEntryID(int resourceCharacteristicEntryID) {
        this.resourceCharacteristicEntryID = resourceCharacteristicEntryID;
    }

    public ResourceCharacteristic getResChar() {
        return _resChar;
    }

    public void setResChar(ResourceCharacteristic _resChar) {
        this._resChar = _resChar;
    }

    public List<ResourceCharacteristicChange> getResourceCharacteristicChangeList() {
        return resourceCharacteristicChangeList;
    }

    public void setResourceCharacteristicChangeList(List<ResourceCharacteristicChange> resourceCharacteristicChangeList) {
        this.resourceCharacteristicChangeList = resourceCharacteristicChangeList;
    }

    public static ResourceCharacteristic getResourceCharacteristicFroString(String charName) {
        if (charName.trim().length() < 0) {
            return null;
        }
        switch (charName) {
            case "RAM":
                return ResourceCharacteristic.RAM;
            case "CPU":
                return ResourceCharacteristic.CPU;
            case "STORAGE":
                return ResourceCharacteristic.STORAGE;
            case "BANDWIDTH":
                return ResourceCharacteristic.BANDWIDTH;
            case "ENERGY":
                return ResourceCharacteristic.ENERGY;
            case "DATALOC":
                return ResourceCharacteristic.DATALOC;
        }
        return null;
    }

    public Object getLastEntryValue() {
        int lastIndex = resourceCharacteristicChangeList.size() - 1;
        return resourceCharacteristicChangeList.get(lastIndex).getNewVal();

    }

}
