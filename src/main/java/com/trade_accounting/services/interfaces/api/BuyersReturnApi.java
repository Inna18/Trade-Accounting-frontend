package com.trade_accounting.services.interfaces.api;

import com.trade_accounting.models.dto.BuyersReturnDto;
import com.trade_accounting.models.dto.InvoiceDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface BuyersReturnApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<BuyersReturnDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<BuyersReturnDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<BuyersReturnDto> create(@Path(value = "url", encoded = true) String url, @Body BuyersReturnDto buyersReturnDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body BuyersReturnDto buyersReturnDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @GET("{url}/searchByString")
    Call<List<BuyersReturnDto>> search(@Path(value = "url", encoded = true) String url,
                                  @Query("search") String text);
}
