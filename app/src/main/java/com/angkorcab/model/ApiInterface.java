package com.angkorcab.model;

import com.angkorcab.constants.ProjectURls;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Epb on 21-07-2016.
 */
public interface ApiInterface {
    @FormUrlEncoded
    @POST(ProjectURls.UPDATE_PROFILE_PIC_URL)
    Call<MyPojo> updateProfilePic(@FieldMap Map<String, String> fields);
}