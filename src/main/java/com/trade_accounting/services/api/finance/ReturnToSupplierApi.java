package com.trade_accounting.services.api.finance;

import com.trade_accounting.models.dto.finance.ReturnToSupplierDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface ReturnToSupplierApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<ReturnToSupplierDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<ReturnToSupplierDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/getByProjectId{id}")
    Call<List<ReturnToSupplierDto>> getByProjectId(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<ReturnToSupplierDto> create(@Path(value = "url", encoded = true) String url, @Body ReturnToSupplierDto modelDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body ReturnToSupplierDto modelDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/search/{nameFilter}")
    Call<List<ReturnToSupplierDto>> searchByString(@Path(value = "url", encoded = true) String url,
                                                   @Path(value = "nameFilter", encoded = true) String nameFilter);

    @Headers("Accept: application/json")
    @GET("{url}/queryReturnToSupplier")
    Call<List<ReturnToSupplierDto>> searchByFilter(@Path(value = "url", encoded = true) String url,
                                                   @QueryMap Map<String, String> querySupplier);

}
