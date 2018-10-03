package iiot.sample;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
@EnableJpaAuditing
//@ImportResource({"classpath:watch-security-config.xml"})
@ComponentScan(
	value={
		"iiot.sample"
	},
	excludeFilters = {
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.ge.stuf.doc.*"),
		@ComponentScan.Filter(type = FilterType.REGEX, pattern = "iiot.sample.tinfoil.*")
	}
)
public class Application extends SpringBootServletInitializer {
	@Value("${spring.datasource.driver-class-name:org.postgresql.Driver}")
	private String dataSourceDriverClassName;

	@Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.uri}")
	private String dataSourceUrl;

	@Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.username}")
	private String dataSourceUsername;

	@Value("${vcap.services.${iiot_sample_postgres_name:iiot-sample-postgres}.credentials.password}")
	private String dataSourcePassword;

	/*@Value("${vcap.services.${alarms_redis_name:apm-redis}.credentials.host}")
	private  String redisHost;

	@Value("${vcap.services.${alarms_redis_name:apm-redis}.credentials.port}")
	private int redisPort;

	@Value("${vcap.services.${alarms_redis_name:apm-redis}.credentials.password}")
	private String redisPassword;*/


	@Bean
	public MappingJackson2HttpMessageConverter jackson2Converter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
		converter.getObjectMapper().configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		return converter;
	}


	@Bean
	public Decoder decoder(){
		return new JacksonDecoder(new ObjectMapper()
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true));
	}

	@Bean
	public Encoder encoder() {
		return new JacksonEncoder(new ObjectMapper()
				.setSerializationInclusion(JsonInclude.Include.NON_NULL)
				.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true));
	}


	public static void main(String[] args) {
		new SpringApplicationBuilder(Application.class)
			.run(args);
	}

}
