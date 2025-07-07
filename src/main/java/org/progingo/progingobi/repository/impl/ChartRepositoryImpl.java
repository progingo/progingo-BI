package org.progingo.progingobi.repository.impl;

import org.progingo.progingobi.domain.entity.Chart;
import org.progingo.progingobi.domain.entity.UserChart;
import org.progingo.progingobi.mapper.ChartMapper;
import org.progingo.progingobi.mapper.UserChartMapper;
import org.progingo.progingobi.repository.ChartRepository;
import org.progingo.progingobi.util.MyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ChartRepositoryImpl implements ChartRepository {

    @Autowired
    private MyUtil myUtil;
    @Autowired
    private ChartMapper chartMapper;
    @Autowired
    private UserChartMapper userChartMapper;
    @Override
    public String insertChart(Chart chart) {
        String chartId = myUtil.nextId("chart");

        UserChart userChart = new UserChart();
        userChart.setChartId(chartId);
        userChart.setUserId(chart.getUserId());
        userChart.setIsDelete(false);
        userChartMapper.insert(userChart);

        //创建分表
        chart.setChartId(chartId);
        String createTableSQL = String.format("CREATE TABLE `chart_%s`  (" +
                "  `goal` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL," +
                "  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL," +
                "  `chart_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL," +
                "  `chart_type` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL," +
                "  `gen_chart` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL," +
                "  `gen_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL," +
                "  `status` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'wait'," +
                "  `exec_message` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL" +
                ") ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;", chartId);

        chartMapper.createChartDataTable(createTableSQL);
        //插入数据
        String insertDataSQL =
                String.format("INSERT INTO `chart_%s`(`name`,`goal`,`chart_data`,`chart_type`,`status`) VALUES ('%s','%s','%s','%s','%s');",
                        chartId,
                        chart.getName(),
                        chart.getGoal(),
                        chart.getChartData(),
                        chart.getChartType(),
                        "0");
        chartMapper.insertChartData(insertDataSQL);

        return chartId;
    }

    @Override
    public Chart queryChart(String chartId) {
        String querySql = String.format("select `goal`,`name`,`chart_data`,`chart_type`,`gen_chart`,`gen_result`,`status`,`exec_message` from `chart_%s`",  chartId);
        Chart chart = chartMapper.queryChartData(querySql);
        return chart;
    }


    @Override
        public void updateChartResult(String chartId, String genChart, String genResult, String status, String execMessage) {
        chartMapper.updateChartData(String.format("chart_%s", chartId),genChart, genResult,status, execMessage);
    }

    @Override
    public void updateStatus(String tableName, String status) {
        chartMapper.updateStatus(tableName, status);
    }
}
