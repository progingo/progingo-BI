package org.progingo.progingobi.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

//用户和商家的DTO合为一体

@Data
@ToString
@Builder
public class UserDTO {
    private Integer id;

    private String username;

    private String phone;

}
