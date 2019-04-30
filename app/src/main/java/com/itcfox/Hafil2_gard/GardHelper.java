package com.itcfox.Hafil2_gard;

import java.io.Serializable;

/**
 * Created by heshe_000 on 9/5/2014.
 */

public class GardHelper  implements Serializable {
private static final long serialVersionUID = 1L;
private  String Date;
private String PlateNumber;


public GardHelper(String Date) {
    this.Date = Date;
}



public  String getDate() {
    return Date;
}

public void setDate(String Date) {
    this.Date = Date;
}

public String getPlateNumber() {
    return PlateNumber;
}

public void setSelected(String PlateNumber) {
    this.PlateNumber = PlateNumber;
}


}