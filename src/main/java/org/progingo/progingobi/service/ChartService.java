package org.progingo.progingobi.service;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.progingo.progingobi.controller.request.GenChartByAiRequest;
import org.progingo.progingobi.domain.entity.Chart;
import org.progingo.progingobi.domain.entity.UserChart;
import org.progingo.progingobi.exception.ErrorCode;
import org.progingo.progingobi.exception.ThrowUtils;
import org.progingo.progingobi.manager.RedisLimiterManager;
import org.progingo.progingobi.mapper.UserChartMapper;
import org.progingo.progingobi.mq.BiMessageProducer;
import org.progingo.progingobi.repository.ChartRepository;
import org.progingo.progingobi.util.ExcelUtils;
import org.progingo.progingobi.util.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartService {

    @Autowired
    private ChartRepository chartRepository;
    @Autowired
    private RedisLimiterManager redisLimiterManager;
    @Autowired
    private BiMessageProducer biMessageProducer;
    @Autowired
    private UserChartMapper userChartMapper;

    public JsonResult analysisFormByAi(Integer userId, GenChartByAiRequest genChartByAiRequest, MultipartFile multipartFile) {
        String name = genChartByAiRequest.getName();//名称
        String goal = genChartByAiRequest.getGoal();//目标
        String chartType = genChartByAiRequest.getChartType();//类型

        // 校验
        ThrowUtils.throwIf(StringUtils.isBlank(goal), ErrorCode.PARAMS_ERROR, "目标为空");
        ThrowUtils.throwIf(StringUtils.isNotBlank(name) && name.length() > 100, ErrorCode.PARAMS_ERROR, "名称过长");
        // 校验文件
        long size = multipartFile.getSize();
        String originalFilename = multipartFile.getOriginalFilename();
        // 校验文件大小
        final long ONE_MB = 10 * 1024 * 1024L;
        ThrowUtils.throwIf(size > ONE_MB, ErrorCode.PARAMS_ERROR, "文件超过 10M");
        // 校验文件后缀 aaa.png
        String suffix = FileUtil.getSuffix(originalFilename);
        final List<String> validFileSuffixList = Arrays.asList("xlsx", "xls");
        ThrowUtils.throwIf(!validFileSuffixList.contains(suffix), ErrorCode.PARAMS_ERROR, "文件后缀非法");

        // 限流判断，每个用户一个限流器
        redisLimiterManager.doRateLimit("genChartByAi_" + userId);

        // 压缩后的数据
        String csvData = ExcelUtils.excelToCsv(multipartFile);

        // 插入到数据库
        Chart chart = new Chart();
        chart.setName(name);
        chart.setGoal(goal);
        chart.setChartData(csvData);
        chart.setChartType(chartType);
        chart.setStatus("0");
        chart.setUserId(userId);
        chart.setCreateTime(LocalDateTime.now());

        String chartId = chartRepository.insertChart(chart);
        ThrowUtils.throwIf(chartId == null, ErrorCode.SYSTEM_ERROR, "图表保存失败");
        //提交给消息队列异步处理
        biMessageProducer.sendMessage(chartId);

        return JsonResult.ok(chartId);
    }

    public JsonResult analysisList(Integer userId) {
        List<UserChart> userCharts = userChartMapper.selectList(new LambdaQueryWrapper<UserChart>()
                .eq(UserChart::getUserId, userId)
                .eq(UserChart::getIsDelete, false));

        List<Chart> charts = userCharts.stream()
                .map(x -> chartRepository.queryChart(x.getChartId()))
                .collect(Collectors.toList());
        return JsonResult.ok(charts);
    }
}
