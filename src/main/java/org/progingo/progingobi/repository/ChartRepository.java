package org.progingo.progingobi.repository;

import org.apache.ibatis.annotations.Param;
import org.progingo.progingobi.domain.entity.Chart;

public interface ChartRepository {

    String insertChart(Chart chart);

    Chart queryChart(String chartId);

    void updateChartResult(String chartId, String genChart,String genResult, String status, String execMessage);

    void updateStatus(String tableName, String status);

}
