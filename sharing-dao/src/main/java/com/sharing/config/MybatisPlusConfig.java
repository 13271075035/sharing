
package com.sharing.config;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @author: Techmile tecsmile@outlook.com
* @date: 2019/7/15
* @description: mybatis-plus配置
*/


@Configuration
public class MybatisPlusConfig {

   /**
    * 配置分页
    */
   @Bean
   public PaginationInterceptor paginationInterceptor() {
       return new PaginationInterceptor();
   }

    /**
     * 配置逻辑删除
     * @return
     */
   @Bean
   public ISqlInjector sqlInjector(){
       return new LogicSqlInjector();
   }

}
