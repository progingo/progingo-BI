package org.progingo.progingobi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.progingo.progingobi.domain.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

@Mapper
public interface ChartMapper {
    void createChartDataTable(@Param("createSql") String createSql);

    void insertChartData(@Param("insertSql") String insertSql);

    Chart queryChartData(@Param("querySql") String querySql);

    void updateChartData(@Param("tableName")String tableName, @Param("genChart")String genChart, @Param("genResult")String genResult, @Param("status")String status, @Param("execMessage")String execMessage);

    void updateStatus(@Param("tableName") String tableName, @Param("status") String status);
}
