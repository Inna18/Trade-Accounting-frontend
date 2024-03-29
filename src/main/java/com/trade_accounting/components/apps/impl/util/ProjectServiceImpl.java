package com.trade_accounting.components.apps.impl.util;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.util.ProjectDto;
import com.trade_accounting.services.interfaces.util.ProjectService;
import com.trade_accounting.services.api.util.ProjectApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectApi projectApi;

    private final String projectUrl;

    private final CallExecuteService<ProjectDto> dtoCallExecuteService;

    public ProjectServiceImpl(@Value("${project_url}") String projectUrl, Retrofit retrofit, CallExecuteService<ProjectDto> dtoCallExecuteService) {
        projectApi = retrofit.create(ProjectApi.class);
        this.projectUrl = projectUrl;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<ProjectDto> getAll() {
        Call<List<ProjectDto>> projectDtoListCall = projectApi.getAll(projectUrl);
        return dtoCallExecuteService.callExecuteBodyList(projectDtoListCall, ProjectDto.class);
    }

    @Override
    public ProjectDto getById(Long id) {
        ProjectDto projectDto = null;
        Call<ProjectDto> projectDtoCall = projectApi.getById(projectUrl, id);

        try {
            projectDto = projectDtoCall.execute().body();
            log.info("Успешно выполнен запрос на получение экземпляра ProjectDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на получение экземпляра ProjectDto по id= {} - {}", id, e);
        }
        return projectDto;
    }

    @Override
    public void create(ProjectDto projectDto) {

        Call<Void> projectDtoCall = projectApi.create(projectUrl, projectDto);

        try {
            projectDtoCall.execute();
            log.info("Успешно выполнен запрос на создание экземпляра ProjectDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на создание экземпляра ProjectDto - {}", e);
        }
    }

    @Override
    public void update(ProjectDto projectDto) {

        Call<Void> projectDtoCall = projectApi.update(projectUrl, projectDto);

        try {
            projectDtoCall.execute();
            log.info("Успешно выполнен запрос на обновление экземпляра ProjectDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на обновление экземпляра ProjectDto - {}", e);
        }
    }

    @Override
    public void deleteById(Long id) {

        Call<Void> projectDtoCall = projectApi.deleteById(projectUrl, id);

        try {
            projectDtoCall.execute();
            log.info("Успешно выполнен запрос на удаление экземпляра ProjectDto с id = {}", id);
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на удаление экземпляра ProjectDto по id= {} - {}", id, e);
        }
    }

    @Override
    public List<ProjectDto> findBySearch(String search) {
        List<ProjectDto> projectDtoList = new ArrayList<>();
        Call<List<ProjectDto>> projectDtoListCall = projectApi.searchByString(projectUrl, search.toLowerCase());

        try {
            projectDtoList = projectDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка ProjectDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка ProjectDto - ", e);
        }
        return projectDtoList;
    }

    @Override
    public List<ProjectDto> search(Map<String, String> query) {
        List<ProjectDto> projectDtoList = new ArrayList<>();
        Call<List<ProjectDto>> projectDtoListCall = projectApi.search(projectUrl, query);
        try {
            projectDtoList = projectDtoListCall.execute().body();
            log.info("Успешно выполнен запрос на поиск и получение списка ProjectDto");
        } catch (IOException e) {
            log.error("Произошла ошибка при выполнении запроса на поиск и получение списка ProjectDto - ", e);
        }
        return projectDtoList;
    }
}
