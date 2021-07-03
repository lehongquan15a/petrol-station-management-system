package com.gasstation.managementsystem.controller;

import com.gasstation.managementsystem.entity.Api;
import com.gasstation.managementsystem.entity.UserType;
import com.gasstation.managementsystem.exception.custom.CustomDuplicateFieldException;
import com.gasstation.managementsystem.exception.custom.CustomNotFoundException;
import com.gasstation.managementsystem.model.dto.api.ApiDTO;
import com.gasstation.managementsystem.model.dto.api.ApiDTOCreate;
import com.gasstation.managementsystem.model.dto.api.ApiDTOUpdate;
import com.gasstation.managementsystem.repository.ApiRepository;
import com.gasstation.managementsystem.service.ApiService;
import com.gasstation.managementsystem.utils.AccountHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = Api.PREFIX)
@CrossOrigin
@Tag(name = "Api", description = "API for api")
@RequiredArgsConstructor
public class ApiController {
    private final ApiService apiService;
    private final ApiRepository apiRepository;
    private final AccountHelper accountHelper;
    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Operation(summary = "View All api")
    @GetMapping("/apis")
    public HashMap<String, Object> getAll(@RequestParam(name = "pageIndex", defaultValue = "1") Integer pageIndex,
                                          @RequestParam(name = "pageSize", required = false) Integer pageSize) {

        if (pageSize != null) {
            return apiService.findAll(PageRequest.of(pageIndex - 1, pageSize));
        }
        UserType userType = accountHelper.getUserTypeOfUserLogin();
        if (userType.getId() == UserType.ADMIN) {
            return apiService.findAll();
        }
        return apiService.findAllByUserTypeId(userType.getId());
    }

    @Operation(summary = "Find api by id")
    @GetMapping("/apis/{id}")
    public ApiDTO getOne(@PathVariable(name = "id") Integer id) throws CustomNotFoundException {
        return apiService.findById(id);
    }

    @Operation(summary = "Update api by id")
    @PutMapping("/apis/{id}")
    public ApiDTO update(@PathVariable(name = "id") Integer id, @Valid @RequestBody ApiDTOUpdate apiDTOUpdate) throws CustomNotFoundException, CustomDuplicateFieldException {
        return apiService.update(id, apiDTOUpdate);
    }


    private boolean isRouteIgnore(String route) {
        List<String> listRouteIgnore = Arrays.asList("refresh-token", "swagger-ui.html", "user-types", "api", "endpoints", "login", "profile");
        return listRouteIgnore.stream().anyMatch(route::startsWith);
    }

    private boolean isMethodIgnore(String method) {
        return method.equalsIgnoreCase("getOne");
    }

    @PostMapping("endpoints")
    public void updateEndPoints() {
        apiService.deleteAll();
        List<ApiDTOCreate> apiDTOCreateList = getAllEndPoints();
        apiService.saveAll(apiDTOCreateList);
    }

    @PutMapping("endpoints")
    public void resetEndPoints() {
        List<ApiDTOCreate> apiDTOCreateList = getAllEndPoints();
        List<Api> apiList = apiRepository.findAll();
        List<ApiDTOCreate> listInsert = new ArrayList<>();
        for (ApiDTOCreate create : apiDTOCreateList) {
            boolean insert = true;
            for (Api api : apiList) {
                if (create.getMethod().equalsIgnoreCase(api.getMethod()) && (Api.PREFIX + create.getPath()).equalsIgnoreCase(api.getPath())) {
                    insert = false;
                    break;
                }
            }
            if (insert) {
                listInsert.add(create);
            }
        }
        apiService.saveAll(listInsert);
    }

    private List<ApiDTOCreate> getAllEndPoints() {
        List<ApiDTOCreate> apiDTOCreateList = new ArrayList<>();
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping
                .getHandlerMethods();
        map.forEach((key, value) -> {
            String all = key.toString();
            String[] parts = all.split("\\s+");
            String method = parts[0].replace("{", "").trim();
            String path = parts[1].replace("[", "").replace("]}", "").replace("/{id}", "").trim();
            String name = value.getMethod().getName();
            name += path.substring(path.lastIndexOf("/") + 1);
            String route = path.substring(path.lastIndexOf("/") + 1);
            if (method.equalsIgnoreCase("GET")) {
                name = "View " + route;
            } else if (method.equalsIgnoreCase("POST")) {
                name = "Create new " + route;
            } else if (method.equalsIgnoreCase("PUT")) {
                name = "Update " + route;
            } else if (method.equalsIgnoreCase("DELETE")) {
                name = "Delete " + route;
            }

            if (!method.equals("")) {
                String methodName = value.getMethod().getName(); //vd getOne
                if (!isMethodIgnore(methodName) && !isRouteIgnore(route)) {
                    apiDTOCreateList.add(ApiDTOCreate.builder()
                            .name(name).method(method).path("/" + route)
                            .build());
                }
            }
        });

        return apiDTOCreateList.stream().sorted((o1, o2) -> {
            if (o1.getPath().compareToIgnoreCase(o2.getPath()) > 0) {
                return 1;
            } else if (o1.getPath().compareToIgnoreCase(o2.getPath()) < 0) {
                return -1;
            } else {
                return o1.getMethod().compareToIgnoreCase(o2.getMethod());
            }
        }).collect(Collectors.toList());
    }
}
