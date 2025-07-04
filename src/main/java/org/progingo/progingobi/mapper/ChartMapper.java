package org.progingo.progingobi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.progingo.progingobi.domain.entity.Chart;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 图表信息表 Mapper 接口
 * </p>
 *
 * @author progingo
 * @since 2025-07-03
 */

@Mapper
public interface ChartMapper extends BaseMapper<Chart> {

}
