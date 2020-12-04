package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.CompanyDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface CompanyApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<CompanyDto>> getAll(@Path("url") String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<CompanyDto> getById(@Path("url") String url, @Path("id") String id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<CompanyDto> create(@Path("url") String url, @Body CompanyDto companyDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<CompanyDto> update(@Path("url") String url, @Body CompanyDto companyDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<CompanyDto> deleteById(@Path("url") String url, @Path("id") String id);
}