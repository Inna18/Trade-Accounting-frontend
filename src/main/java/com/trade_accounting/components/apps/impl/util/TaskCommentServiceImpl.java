package com.trade_accounting.components.apps.impl.util;

import com.trade_accounting.components.apps.impl.CallExecuteService;
import com.trade_accounting.models.dto.util.TaskCommentDto;
import com.trade_accounting.services.interfaces.util.TaskCommentService;
import com.trade_accounting.services.interfaces.util.TaskService;
import com.trade_accounting.services.api.util.TaskCommentApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.List;

@Service
@Slf4j
public class TaskCommentServiceImpl implements TaskCommentService {

    private final TaskCommentApi taskCommentApi;

    private final String taskCommentUrl;

    private final TaskService taskService;

    private final CallExecuteService<TaskCommentDto> dtoCallExecuteService;

    @Autowired
    public TaskCommentServiceImpl(@Value("${task_comment_url}") String taskCommentUrl, Retrofit retrofit, TaskService taskService, CallExecuteService<TaskCommentDto> dtoCallExecuteService) {
        taskCommentApi = retrofit.create(TaskCommentApi.class);
        this.taskCommentUrl = taskCommentUrl;
        this.taskService = taskService;
        this.dtoCallExecuteService = dtoCallExecuteService;
    }

    @Override
    public List<TaskCommentDto> getAll() {
        Call<List<TaskCommentDto>> taskCommentDtoListCall = taskCommentApi.getAll(taskCommentUrl);
        return dtoCallExecuteService.callExecuteBodyList(taskCommentDtoListCall, TaskCommentDto.class);
    }

    @Override
    public TaskCommentDto getById(Long id) {
        Call<TaskCommentDto> taskCommentDtoCall = taskCommentApi.getById(taskCommentUrl, id);
        return dtoCallExecuteService.callExecuteBodyById(taskCommentDtoCall, TaskCommentDto.class, id);
    }

    @Override
    public void create(TaskCommentDto taskCommentDto) {
        Call<Void> taskCommentDtoCall = taskCommentApi.create(taskCommentUrl, taskCommentDto);
        dtoCallExecuteService.callExecuteBodyCreate(taskCommentDtoCall, TaskCommentDto.class);
    }

    @Override
    public void update(TaskCommentDto taskCommentDto) {
        Call<Void> taskCommentDtoCall = taskCommentApi.update(taskCommentUrl, taskCommentDto);
        dtoCallExecuteService.callExecuteBodyUpdate(taskCommentDtoCall, TaskCommentDto.class);
    }

    @Override
    public void deleteById(Long id) {
        Call<Void> taskCommentDtoCall = taskCommentApi.deleteById(taskCommentUrl, id);
        dtoCallExecuteService.callExecuteBodyDelete(taskCommentDtoCall, TaskCommentDto.class, id);
    }
}