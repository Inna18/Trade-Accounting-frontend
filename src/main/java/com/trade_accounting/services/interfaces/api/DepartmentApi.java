package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.DepartmentDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import java.util.List;

public interface DepartmentApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<DepartmentDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<DepartmentDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<DepartmentDto> create(@Path(value = "url", encoded = true) String url, @Body DepartmentDto departmentDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<DepartmentDto> update(@Path(value = "url", encoded = true) String url, @Body DepartmentDto departmentDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<DepartmentDto> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);
}
