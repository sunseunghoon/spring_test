package com.example.myapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.awt.*;

/**
 * Configuration + EnableAutoConfiguration + ComponentScan
 */
@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		//SpringApplication.run(DemoApplication.class, args);
/*		SpringApplication application = new SpringApplication(DemoApplication.class);
		application.setBanner(new Banner() {
			@Override
			public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
				out.print("\n\n\t 나만의 멋진 배너랍니다. !\n\n".toUpperCase());
			}
		});
		application.run(args);
		*/
		Logger log = LoggerFactory.getLogger(DemoApplication.class);

		//Builder타입으로 변경
		new SpringApplicationBuilder()
				.listeners(new ApplicationListener<ApplicationEvent>() {
					@Override
					public void onApplicationEvent(ApplicationEvent applicationEvent) {
						log.info("#### >" + applicationEvent.getClass().getCanonicalName());
					}
				})
				.bannerMode(Banner.Mode.OFF)
				.sources(DemoApplication.class)
				//.web(false)  //exclude
				.run(args);

	}

}
