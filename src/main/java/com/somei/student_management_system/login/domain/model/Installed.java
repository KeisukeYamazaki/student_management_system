package com.somei.student_management_system.login.domain.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Installed {

    @SerializedName("client_id")
    @Expose
    public String clientId;
    @SerializedName("project_id")
    @Expose
    public String projectId;
    @SerializedName("auth_uri")
    @Expose
    public String authUri;
    @SerializedName("token_uri")
    @Expose
    public String tokenUri;
    @SerializedName("auth_provider_x509_cert_url")
    @Expose
    public String authProviderX509CertUrl;
    @SerializedName("client_secret")
    @Expose
    public String clientSecret;
    @SerializedName("redirect_uris")
    @Expose
    public List<String> redirectUris = null;

}
