package com.trade_accounting.services.api.warehouse;

import com.trade_accounting.models.dto.warehouse.SalesSubGoodsForSaleDto;
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

public interface SalesSubGoodsForSaleApi {
    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<SalesSubGoodsForSaleDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<SalesSubGoodsForSaleDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<SalesSubGoodsForSaleDto> create(@Path(value = "url", encoded = true) String url, @Body SalesSubGoodsForSaleDto salesSubGoodsForSaleDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body SalesSubGoodsForSaleDto salesSubGoodsForSaleDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/searchByFilter")
    Call<List<SalesSubGoodsForSaleDto>> searchByFilter(@Path(value = "url", encoded = true) String url, @QueryMap Map<String, String> filterData);
}
