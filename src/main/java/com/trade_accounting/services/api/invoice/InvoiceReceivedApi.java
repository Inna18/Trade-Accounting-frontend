package com.trade_accounting.services.api.invoice;

import com.trade_accounting.models.dto.invoice.InvoiceDto;
import com.trade_accounting.models.dto.invoice.InvoiceReceivedDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

import java.util.List;
import java.util.Map;

public interface InvoiceReceivedApi {

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<InvoiceReceivedDto>> getAll(@Path(value = "url", encoded = true) String url);

    @Headers("Accept: application/json")
    @GET("{url}")
    Call<List<InvoiceReceivedDto>> getAll(@Path(value = "url", encoded = true) String url, @Query("typeOfInvoice") String typeOfInvoice);

    @Headers("Accept: application/json")
    @GET("{url}/queryInvoiceReceived")
    Call<List<InvoiceReceivedDto>> searchByFilter(@Path(value = "url", encoded = true) String url,
                                           @QueryMap Map<String, String> queryInvoiceReceived);


    @Headers("Accept: application/json")
    @GET("{url}/search/{search}")
    Call<List<InvoiceReceivedDto>> searchByString(@Path(value = "url", encoded = true) String url,
                                                  @Path(value = "search", encoded = true) String search);

    @Headers("Accept: application/json")
    @GET("{url}/{id}")
    Call<InvoiceReceivedDto> getById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

    @Headers("Accept: application/json")
    @POST("{url}")
    Call<Void> create(@Path(value = "url", encoded = true) String url, @Body InvoiceReceivedDto invoiceReceivedDto);

    @Headers("Accept: application/json")
    @PUT("{url}")
    Call<Void> update(@Path(value = "url", encoded = true) String url, @Body InvoiceReceivedDto invoiceReceivedDto);

    @Headers("Accept: application/json")
    @DELETE("{url}/{id}")
    Call<Void> deleteById(@Path(value = "url", encoded = true) String url, @Path("id") Long id);

}
