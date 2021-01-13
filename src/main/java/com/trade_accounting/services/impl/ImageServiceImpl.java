package com.trade_accounting.services.impl;

import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.services.interfaces.ImageService;
import com.trade_accounting.services.interfaces.api.ImageApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.List;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    private final ImageApi imageApi;
    private final String imageUrl;
    private List<ImageDto> imageDtoList;
    private ImageDto imageDto;

    public ImageServiceImpl(@Value("${image_url}") String imageUrl, Retrofit retrofit) {
        imageApi = retrofit.create(ImageApi.class);
        this.imageUrl = imageUrl;
    }


    @Override
    public List<ImageDto> getAll() {
        Call<List<ImageDto>> imageDtoListCall = imageApi.getAll(imageUrl);

        imageDtoListCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<ImageDto>> call, Response<List<ImageDto>> response) {
                if(response.isSuccessful()){
                    imageDtoList = response.body();
                    log.info("Успешно выполнен запрос на получение списка ImageDto");
                } else {
                    log.error("Произошла ошибка при отправке запроса на получение списка ImageDto: {}", response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<ImageDto>> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на получение списка ImageDto: ", throwable);
            }
        });
        return imageDtoList;
    }

    @Override
    public ImageDto getById(Long id) {
        Call<ImageDto> imageDtoCall = imageApi.getById(imageUrl, id);

        imageDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ImageDto> call, Response<ImageDto> response) {
                if(response.isSuccessful()){
                    imageDto = response.body();
                    log.info("Успешно выполнен запрос на получение экземпляра ImageDto с id = {}", id);
                } else {
                    log.error("Произошла ошибка при отправке запроса на получение ImageDto с id = {}: {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ImageDto> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на получение ImageDto c id = {}: {}",
                        id, throwable);
            }
        });
        return imageDto;
    }

    @Override
    public void create(ImageDto imageDto) {
    Call<Void> imageDtoCall = imageApi.create(imageUrl, imageDto);

    imageDtoCall.enqueue(new Callback<>() {
        @Override
        public void onResponse(Call<Void> call, Response<Void> response) {
            if(response.isSuccessful()){
                log.info("Успешно выполнен запрос на создание нового экземпляра ImageDto {}", imageDto);
            } else {
                log.error("Произошла ошибка при отправке запроса на создание нового экземпляра ImageDto {}: {}",
                        imageDto, response.errorBody());
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable throwable) {
            log.error("Произошла ошибка при отправке запроса на создание нового экземпляра ImageDto {}: {}",
                    imageDto, throwable);
        }
    });
    }

    @Override
    public void update(ImageDto imageDto) {
        Call<Void> imageDtoCall = imageApi.update(imageUrl, imageDto);

        imageDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    log.info("Успешно выполнен запрос на обновление ImageDto {}", imageDto);
                } else {
                    log.error("Произошла ошибка при отправке запроса на обновление ImageDto {}: {}",
                            imageDto, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на обновление ImageDto {}: {}",
                        imageDto, throwable);
            }
        });
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> imageDtoCall = imageApi.deleteById(imageUrl, id);

        imageDtoCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    log.info("Успешно выполнен запрос на удаление ImageDto c id = {}", id);
                } else {
                    log.error("Произошла ошибка при отправке запроса на удаление ImageDto c id = {}: {}",
                            id, response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                log.error("Произошла ошибка при отправке запроса на удаление ImageDto c id = {}: {}",
                        id, throwable);
            }
        });
    }
}