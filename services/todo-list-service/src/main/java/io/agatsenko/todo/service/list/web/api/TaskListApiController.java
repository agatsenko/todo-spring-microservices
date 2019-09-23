/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.web.api;

import java.security.Principal;
import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.common.log.LogError;
import io.agatsenko.todo.service.common.log.LogInfo;
import io.agatsenko.todo.service.common.web.api.dto.DtoAssemblers;
import io.agatsenko.todo.service.common.web.api.error.BadRequestException;
import io.agatsenko.todo.service.common.web.api.error.NotFoundException;
import io.agatsenko.todo.service.list.model.TaskList;
import io.agatsenko.todo.service.list.service.TaskListService;
import io.agatsenko.todo.service.list.service.UserIdResolver;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RequestMapping("/api/task-list")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class TaskListApiController {
    @NonNull
    private final UserIdResolver userIdResolver;

    @NonNull
    private final TaskListService listService;

    @NonNull
    private final DtoAssemblers assemblers;

    @LogInfo
    @LogError
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public TaskListResponse addList(Principal principal, @RequestBody @Valid NewTaskListRequest request) {
        return assemblers.assembly(TaskListResponse.class, listService.addTaskList(resolveUserId(principal), request));
    }

    @LogInfo
    @LogError
    @GetMapping(produces = APPLICATION_JSON_UTF8_VALUE)
    public Collection<TaskListResponse> getAllLists(Principal principal) {
        return assemblers.assembly(
                TaskList.class,
                TaskListResponse.class,
                listService.getTaskLists(resolveUserId(principal))
        );
    }

    @LogInfo
    @LogError
    @GetMapping(value = "/{listId}", produces = APPLICATION_JSON_UTF8_VALUE)
    public TaskListResponse getList(Principal principal, @PathVariable("listId") UUID listId) {
        return assemblers.assembly(
                TaskListResponse.class,
                listService
                        .getTaskList(resolveUserId(principal), listId)
                        .orElseThrow(() -> new NotFoundException(
                                String.format("task list with id '%s' is not found", listId)
                        ))
        );
    }

    @LogInfo
    @LogError
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    public TaskListResponse updateList(Principal principal, @RequestBody @Valid UpdateTaskListRequest request) {
        return assemblers.assembly(
                TaskListResponse.class,
                listService
                        .updateTaskList(resolveUserId(principal), request)
                        .orElseThrow(() -> new NotFoundException(
                                String.format(
                                        "task list with id '%s' and version %s is not found",
                                        request.id,
                                        request.version
                                )
                        ))
        );
    }

    @LogInfo
    @LogError
    @DeleteMapping("/{listId}/{version}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteList(
            Principal principal,
            @PathVariable("listId") UUID listId,
            @PathVariable("version") long version) {
        if (!listService.removeTaskList(resolveUserId(principal), listId, version)) {
            throw new NotFoundException(
                    String.format("task list with id '%s' and version %s is not found", listId, version)
            );
        }
    }

    private UUID resolveUserId(Principal principal) {
        return userIdResolver
                .resolve(principal)
                .orElseThrow(() -> new BadRequestException("unable to resolve a user id"));
    }
}
