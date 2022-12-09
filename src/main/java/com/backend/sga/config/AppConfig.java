package com.backend.sga.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.backend.sga.interceptor.AppInterceptor;


@Configuration
public class AppConfig implements WebMvcConfigurer  {
	
	/*
	@Autowired
	private AppInterceptor interceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// adiciona o Interceptor na aplicação
		registry.addInterceptor(interceptor);
	}
	*/
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
        
        registry.addMapping("/**");// LIBERA O CORS, PARA DE DAR ERRO DE CORS
    }
    
    // ESTRUTURA DO BANCO, dataSource e jpaVendorAdapter, se for migrar para outro bd esse dois método já cria a estrutura, claro que mudando os parametros passados

            // COMO SE FOSSE UM ONJ INSTÂNCIADO, FAZ COK QUE A APLICAÇÃO INSTANCIE O DATASOURCE
            @Bean
            public DataSource dataSource() {
                
                DriverManagerDataSource drivManargerDataSource = new DriverManagerDataSource();
                drivManargerDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");// DRIVER DE CONEXÃO COM O BD
                drivManargerDataSource.setUrl("jdbc:mysql://localhost:3307/sga");
                drivManargerDataSource.setUsername("root");
                drivManargerDataSource.setPassword("root");
                
                return drivManargerDataSource;
            }


            // BIBLIOTECA DE PERSISTENCIA DO JAVA, JPA
            @Bean
            public JpaVendorAdapter jpaVendorAdapter() {
                
                HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
                adapter.setDatabase(Database.MYSQL);// TIPO DE DATABASE
                adapter.setDatabasePlatform("org.hibernate.dialect.MySQL8Dialect");// ESPECIFICAR O DIALETO QUE SERÁ USADO PARA CONVERSAR COM A DATABASE, MySQL8Dialect LINGUAGEM QUE VAI SER USADA
                adapter.setPrepareConnection(true);// PREPARAR A CONEXÃO PARA MANDAR INSTRUÇÕES
                adapter.setGenerateDdl(true);// GERAR O DDL, SERIA FALSO SE A TABELA  JÁ EXISTISSE
                adapter.setShowSql(true);// TODAS AS APLICAÇÕES SQL APARECERA NO CONSOLE, PARA CONTROLAR O QUE ESTÁ ACONTECENDO, UM INFORMATIVO
                
                return adapter;
            }

}
