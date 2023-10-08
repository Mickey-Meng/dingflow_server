package com.snow.flowable.convert;

import com.snow.flowable.domain.response.HistoricTaskInstanceResp;
import com.snow.flowable.domain.response.TaskResp;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author qimingjin
 * @date 2022-12-20 11:03
 * @Description:
 */
@Mapper
public interface TaskConvert {
    TaskConvert INSTANCE = Mappers.getMapper(TaskConvert.class);

    @Mappings(value = {
            @Mapping(source = "id", target = "taskId"),
            @Mapping(source = "name", target = "taskName")
    })
    TaskResp convert(Task task);


    @Mappings(value = {
            @Mapping(source = "id", target = "taskId"),
            @Mapping(source = "name", target = "taskName"),
            @Mapping(source = "createTime", target = "startTime"),
            @Mapping(source = "endTime", target = "completeTime")
    })
    HistoricTaskInstanceResp convert(HistoricTaskInstance historicTaskInstance);



    @Mappings(value = {
            @Mapping(source = "id", target = "taskId"),
            @Mapping(source = "name", target = "taskName"),
            @Mapping(source = "time", target = "startTime"),
            @Mapping(source = "endTime", target = "completeTime")
    })
    List<HistoricTaskInstanceResp> convert(List<HistoricTaskInstance> historicTaskInstanceList);
}
