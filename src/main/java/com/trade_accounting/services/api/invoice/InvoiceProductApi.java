package com.trade_accounting.services.api.invoice;

import com.trade_accounting.models.dto.invoice.InvoiceProductDto;
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

public interface InvoiceProductApi {
        @Headers("Accept: application/json")
        @GET("{url}")
        Call<List<InvoiceProductDto>> getAll(@Path(value = "url", encoded = true) String url);

        @Headers("Accept: application/json")
        @GET("{url}/{id}")
        Call<InvoiceProductDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

        @Headers("Accept: application/json")
        @GET("{url}/{id}")
        Call<List<InvoiceProductDto>> getByInvoiceId(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

        @Headers("Accept: application/json")
        @POST("{url}")
        Call<Void> create(@Path(value = "url", encoded = true) String url, @Body InvoiceProductDto invoiceProductDto);

        @Headers("Accept: application/json")
        @PUT("{url}")
        Call<Void> update(@Path(value = "url", encoded = true) String url, @Body InvoiceProductDto invoiceProductDto);

        @Headers("Accept: application/json")
        @DELETE("{url}/{id}")
        Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

        @Headers("Accept: application/json")
        @GET("{url}/search")
        Call<List<InvoiceProductDto>> search(@Path(value = "url", encoded = true) String url,
                                             @QueryMap Map<String, String> query);

        @Headers("Accept: application/json")
        @GET("{url}/product/{id}")
        Call<List<InvoiceProductDto>> getByProductId(@Path(value = "url", encoded = true) String url, @Path("id") Long id);


}
