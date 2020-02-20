package com.somei.student_management_system.login.domain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GoogleSheetJson {

    @SerializedName("installed")
    @Expose
    public Installed installed;
}
