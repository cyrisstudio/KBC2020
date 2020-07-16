package com.Cyris.kbc2020.topscore;

import java.util.Date;

public class FirebaseVariable {

    public FirebaseVariable()
    {}

    public TopEntity getEntity() {
        return entity;
    }

    public void setEntity(TopEntity entity) {
        this.entity = entity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    TopEntity entity;
    Date date;
}
