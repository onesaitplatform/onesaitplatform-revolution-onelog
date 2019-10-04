/**
 * Copyright Indra Soluciones Tecnologías de la Información, S.L.U.
 * 2013-2019 SPAIN
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.minsait.onesait.platform.onelog.config;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import lombok.Getter;
import lombok.Setter;

public class LogProperties {
	
	private static LogProperties instance;
	
	public static LogProperties getInstance(){
		
		if(instance == null){
			try {
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
				instance = mapper.readValue(getFileFromResources("LogProperties.yml"), LogProperties.class);
			} catch (IOException e) {
				System.err.println("Error in GrayLog configuration: "+e.getMessage());
				return new LogProperties();
			}
		}
		
		return instance;
	}
	
    private static File getFileFromResources(String fileName) {

        URL resource = LogProperties.class.getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file "+fileName+" is not found!");
        } else {
            return new File(resource.getFile());
        }

    }

    @Getter
    @Setter
	private String graylog_host;
    
    @Getter
    @Setter
	private Integer graylog_port;
    
    @Getter
    @Setter
	private Integer connect_timeout;
    
    @Getter
    @Setter
	private Integer reconnect_interval;
    
    @Getter
    @Setter
	private Integer max_retries;
    
    @Getter
    @Setter
	private Integer retry_delay;
    
    @Getter
    @Setter
	private Integer pool_size;
    
    @Getter
    @Setter
	private Integer pool_max_wait_time;
    
    @Getter
    @Setter
	private String origin_host;
    
    @Getter
    @Setter
	private Boolean include_raw_message;
    
    @Getter
    @Setter
	private Boolean include_marker;
    
    @Getter
    @Setter
	private Boolean include_mdc_data;
    
    @Getter
    @Setter
	private Boolean include_caller_data;
    
    @Getter
    @Setter
	private Boolean include_root_cause_data;
    
    @Getter
    @Setter
	private Boolean include_level_name;
    
    @Getter
    @Setter
	private String short_pattern_layout_str;
    
    @Getter
    @Setter
	private String full_pattern_layout_str;
    
    @Getter
    @Setter
	private Boolean numbers_as_string;
	
}