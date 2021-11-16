/*
  Set model resources: computation ans storage
  
 */
package phdproject.mcc.mccproject.resource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import org.springframework.core.io.ClassPathResource;
import phdproject.mcc.mccproject.resource.resourcecharacteristics.ResourceCharacteristic;
import phdproject.mcc.mccproject.resource.resourcecharacteristics.ResourceCharacteristicChange;
import phdproject.mcc.mccproject.resource.resourcecharacteristics.ResourceCharacteristicEntry;

/**
 *
 * @author 90930034
 */
public class Resource implements IResource {

    static List<Resource> resourcesList = new ArrayList<>();
    static int RESOURCEID = 0;
    RescourceLocation resLoc;
    String resLocURL;
    List<ResourceCharacteristicEntry> resourceCharacteristicEntryList; // memory, cpu, bandwidth, ....
    String resName;
    int resID;

    public Resource() {

    }

    // initlize resource and get data from properties file / in resources folder / note filename
    public Resource(String name, String resourcePropFileName, RescourceLocation resLoc) throws FileNotFoundException, IOException {
        //System.out.println("Call Resoucr..");
        resourceCharacteristicEntryList = new ArrayList<>();
        resName = name;
        resID = Resource.RESOURCEID;
        this.resLoc = resLoc;
        Resource.RESOURCEID++;
        //String path = resourcePropFileName + ".properties";
        //File file = new ClassPathResource("countries.xml").getFile();
        //String path = "http://34.252.16.85:8080/mccproject/resources/" + resourcePropFileName + ".properties";
        String path = "src\\resources\\" + resourcePropFileName + ".properties";
        //URL oracle = new URL(path);
        ClassPathResource resource = new ClassPathResource(resourcePropFileName + ".properties");
        File file = resource.getFile();
        FileReader reader = new FileReader(file);
        Properties props = new Properties();
        props.load(reader);
        Enumeration<String> elements = (Enumeration<String>) props.propertyNames();
        while (elements.hasMoreElements()) {
            String nextElement = elements.nextElement();
            ResourceCharacteristic rescchar = ResourceCharacteristicEntry.getResourceCharacteristicFroString(nextElement);
            if (rescchar != null) {
                try {
                    double erescEntryVal = Double.parseDouble(props.getProperty(nextElement));
                    resourceCharacteristicEntryList.add(new ResourceCharacteristicEntry(rescchar, erescEntryVal, erescEntryVal, getResID()));
                } catch (Exception ex) {
                    resourceCharacteristicEntryList.add(new ResourceCharacteristicEntry(rescchar, props.getProperty(nextElement), props.getProperty(nextElement), getResID()));
                }

            }
        }
        this.resLocURL = resLocURL = (String) getResourceCharacteristicEntryCurValue(ResourceCharacteristic.DATALOC);
        Resource.getResourcesList().add(this); // add the resource to the model resources 
    }

    @Override
    public String toString() {
        return "Resource{" + "resLoc=" + resLoc + ", resLocURL=" + resLocURL + ", resName=" + resName + ", resID=" + resID + '}';
    }

    public Resource(String name, RescourceLocation resLoc, String resLocURL) {
        resourceCharacteristicEntryList = new ArrayList<>();
        this.resLoc = resLoc;
        resID = Resource.RESOURCEID;
        this.resLocURL = resLocURL;
        Resource.RESOURCEID++;
        Resource.getResourcesList().add(this);
    }

    public static List<Resource> getResourcesList() {
        return resourcesList;
    }

    public static void setResourcesList(List<Resource> resourcesList) {
        Resource.resourcesList = resourcesList;
    }

    public static Resource getRescourceByName(String resName) {
        try {
            return Resource.getResourcesList().stream().filter(r -> r.getResName().equalsIgnoreCase(resName)).findFirst().get();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Helper functions to get a resource / ID, URL, Location, ...
     */

    public static Resource getRescourceByID(int resID) {
        try {
            return Resource.getResourcesList().stream().filter(r -> r.getResID() == resID).findFirst().get();
        } catch (Exception ex) {
            return null;
        }
    }

    public static Resource getRescourceByLocationURL(String resLocURL) {
        try {
            return Resource.getResourcesList().stream().filter(r -> r.getResLocURL().equals(resLocURL)).findFirst().get();
        } catch (Exception ex) {
            return null;
        }
    }

    public static List<Resource> getRescourceByLocation(RescourceLocation loc) {
        try {
            return Resource.getResourcesList().stream().filter(r -> r.getResLoc() == loc).collect(Collectors.toList());
        } catch (Exception ex) {
            return null;
        }
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public RescourceLocation getResLoc() {
        return resLoc;
    }

    public void setResLoc(RescourceLocation resLoc) {
        this.resLoc = resLoc;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public static RescourceLocation getLocationFromID(int locID) {
        switch (locID) {
            case 1:
                return RescourceLocation.MOBILE;
            case 2:
                return RescourceLocation.PUBLICCLOUD;
            case 3:
                return RescourceLocation.CLOUDLET;
            default:
                return RescourceLocation.OTHER;
        }
    }

    public List<ResourceCharacteristicEntry> getResourceCharacteristicEntryList() {
        return resourceCharacteristicEntryList;
    }

    public Object getResourceCharacteristicEntryCurValue(ResourceCharacteristic resChar) {

        return getResourceCharacteristicEntryList().stream().filter(c -> c.getResChar() == resChar).findFirst().get().getLastEntryValue();
    }

    public void setResourceCharacteristicEntryCurValue(ResourceCharacteristic resChar, Object curVal) {

        getResourceCharacteristicEntryList().stream().filter(c -> c.getResChar() == resChar).findFirst().get().getResourceCharacteristicChangeList().add(
                new ResourceCharacteristicChange(curVal));

    }

    public static int getRESOURCEID() {
        return RESOURCEID;
    }

    public static void setRESOURCEID(int RESOURCEID) {
        Resource.RESOURCEID = RESOURCEID;
    }

    public String getResLocURL() {
        return resLocURL;
    }

    public void setResLocURL(String resLocURL) {
        this.resLocURL = resLocURL;
    }

}
