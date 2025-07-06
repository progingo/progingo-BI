package org.progingo.progingobi.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.progingo.progingobi.controller.request.GenChartByAiRequest;
import org.progingo.progingobi.domain.dto.UserDTO;
import org.progingo.progingobi.exception.BusinessException;
import org.progingo.progingobi.exception.ErrorCode;
import org.progingo.progingobi.exception.ThrowUtils;
import org.progingo.progingobi.service.ChartService;
import org.progingo.progingobi.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/chart")
@Slf4j
public class ChartController {

    @Autowired
    private ChartService chartService;

    @PostMapping("/gen")
    @RequiresRoles({"user"})
    public JsonResult addChart(@RequestPart("file") MultipartFile multipartFile,
                               GenChartByAiRequest genChartByAiRequest) {
        if (genChartByAiRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Subject subject = SecurityUtils.getSubject();
        UserDTO user = (UserDTO) subject.getPrincipal();

        JsonResult jsonResult = chartService.analysisFormByAi(user.getId(), genChartByAiRequest, multipartFile);
        ThrowUtils.throwIf(jsonResult.getState() != 200, ErrorCode.OPERATION_ERROR);
        return jsonResult;
    }

    @GetMapping("/analysisList")
    @RequiresRoles({"user"})
    public JsonResult analysisList() {
        Subject subject = SecurityUtils.getSubject();
        UserDTO user = (UserDTO) subject.getPrincipal();

        JsonResult jsonResult = chartService.analysisList(user.getId());
        ThrowUtils.throwIf(jsonResult.getState() != 200, ErrorCode.OPERATION_ERROR);
        return jsonResult;
    }

}
