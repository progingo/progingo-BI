/*
package org.progingo.progingobi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
 
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                //是否启用swagger接口信息 (true 开启  false隐藏。生产环境建议隐藏)
//                .enable(false)
                .select()
                //扫描包路径，捕获注解声明的接口
                .apis(RequestHandlerSelectors.basePackage("com.mh"))
                .paths(PathSelectors.any())
                .build();
    }
 
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //设置文档标题
                .title("我是接口文档标题")
                //设置文档描述
                .description("我是接口文档说明")
                //服务条款URL连接
                .termsOfServiceUrl("http://localhost:9090/")
                //声明版本
                .version("1.0.0")
                .build();
    }
}
*/
