package org.progingo.progingobi.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.progingo.progingobi.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author progingo
 * @since 2025-07-03
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
