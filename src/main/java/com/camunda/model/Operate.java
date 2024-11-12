package com.camunda.model;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Operate {

	private Filter filter;
	private int size;
	private List<Map<String, Object>> searchAfter;
	private List<Sort> sort;

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	public static class Filter {
		private long key;
		private String name;
		private int version;
		private String bpmnProcessId;
		private String tenantId;
		private String processDefinitionKey;
		private String processInstanceKey;
		private String message;
		private String creationTime;
		private String state;
		private int jobKey; 

	}

	@AllArgsConstructor
	@NoArgsConstructor
	@Getter
	@Setter
	public static class Sort {
		private String field;
		private String order;

	}
}
