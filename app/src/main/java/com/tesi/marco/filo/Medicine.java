package com.tesi.marco.filo;

/**
 * Created by Marco on 07/02/2018.
 */

public class Medicine {

    private String drug;
    private String consumption;

    public Medicine(String drug, String consumption) {
        this.drug = drug;
        this.consumption = consumption;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }
}
