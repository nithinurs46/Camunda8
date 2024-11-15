package com.camunda.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.camunda.model.Operate;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@RestController
@Slf4j
public class AppController {

	private static final String POST = "POST";
	private static final String ACCEPT = "Accept";
	private static final String AUTHORIZATION = "Authorization";
	private static final String ERROR_RESPONSE = "Error response: ";
	private static final String BEARER = "Bearer ";
	private ZeebeClient zeebeClient;
	private RestClient restClient;

	public AppController(ZeebeClient zeebeClient, RestClient restClient) {
		super();
		this.zeebeClient = zeebeClient;
		this.restClient = restClient;
	}

	private static final String ACCESS_TOKEN = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IlFVVXdPVFpDUTBVM01qZEVRME0wTkRFelJrUkJORFk0T0RZeE1FRTBSa1pFUlVWRVF6bERNZyJ9.eyJodHRwczovL2NhbXVuZGEuY29tL2NsdXN0ZXJJZCI6ImNhZDUxNjBlLTBhZGMtNDE4YS04YWUzLWVhZGVjMDZjZTI3ZSIsImh0dHBzOi8vY2FtdW5kYS5jb20vb3JnSWQiOiJlYzlmZDJkZS1mOTZlLTQwMGYtODllZi04YzA2NWRmZWZlYmMiLCJodHRwczovL2NhbXVuZGEuY29tL2NsaWVudElkIjoibWguN0xrdlBFUX5hVDd-Y2cxVUg0ZHpGcXZROURyWG8iLCJpc3MiOiJodHRwczovL3dlYmxvZ2luLmNsb3VkLmNhbXVuZGEuaW8vIiwic3ViIjoibXdvOTB0MnIzMTYwN3ozNkJOSDY5dFdGS0JYNTVqMVdAY2xpZW50cyIsImF1ZCI6Im9wZXJhdGUuY2FtdW5kYS5pbyIsImlhdCI6MTczMTQ5NzcyOSwiZXhwIjoxNzMxNTg0MTI5LCJzY29wZSI6ImNhZDUxNjBlLTBhZGMtNDE4YS04YWUzLWVhZGVjMDZjZTI3ZSIsImd0eSI6ImNsaWVudC1jcmVkZW50aWFscyIsImF6cCI6Im13bzkwdDJyMzE2MDd6MzZCTkg2OXRXRktCWDU1ajFXIn0.P8YIFaIeN0BQDdWUBMhC4aZIz8PQLIHf8Vs_72744vYdobzJPywyxNOwpuG2TfaP4IINClIeGBcX8gvVMFkjjbRsP2G8y3kNYXDv2kNqnDvEBnHrsYMZKXonlCNl7qvWnps2cduKK7BgJdwKXvS91TSxxSC-VH9t3MkQPu4z480AA35UoI55HOO4hNS6BDVji2XPs0ZxzLocKS45-G-GgJzvGd7TvECeJJeFkdYILiD4NchEAhK9bsgu5UnSa7kP3YsS7vHucftrYZnDiNEdcZ_YOmFKxTK_2nshAKwmM2dGBjWXbxV0xP1Jf2cZrdNM-u1PtnJ8igghhklAFhgMVQ";

	@GetMapping("start/{bpmnProcessId}")
	public String startProcess(@PathVariable String bpmnProcessId) {
		Map<String, String> variables = new HashMap<>();
		variables.put("name", "ABD");
		variables.put("gender", "Female");
		variables.put("age", "31");
		// process-1
		ProcessInstanceEvent processInstanceResult = zeebeClient.newCreateInstanceCommand().bpmnProcessId(bpmnProcessId)
				.latestVersion().variables(variables).send().join();
		return "Process triggered with instance id: " + processInstanceResult.getProcessInstanceKey();
	}

	@PostMapping("process-definitions")
	public String getProcessDefinition() {
		String url = "https://lhr-1.operate.camunda.io:443/cad5160e-0adc-418a-8ae3-eadec06ce27e/v1/process-definitions/search";
		try {
			String response = restClient.post().uri(url).header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
					.header(HttpHeaders.AUTHORIZATION, BEARER + ACCESS_TOKEN).retrieve().body(String.class);
			log.info(response);
			return response;
		} catch (RestClientResponseException e) {
			log.error(ERROR_RESPONSE + e.getResponseBodyAsString());
		}
		return "";
	}

	@PostMapping("process-definitions-custom")
	public String getProcessDefinitionCustom(@org.springframework.web.bind.annotation.RequestBody Operate reqObj) {
		String url = "https://lhr-1.operate.camunda.io:443/cad5160e-0adc-418a-8ae3-eadec06ce27e/v1/process-definitions/search";
		String result = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			String reqBody = objectMapper.writeValueAsString(reqObj);
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse(MediaType.APPLICATION_JSON_VALUE);
			RequestBody body = RequestBody.create(reqBody, mediaType);
			Request request = new Request.Builder().url(url).method(POST, body)
					.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE).addHeader(ACCEPT, "application/json")
					.addHeader(AUTHORIZATION, BEARER + ACCESS_TOKEN).build();
			Response response = client.newCall(request).execute();

			result = response.body().string();
		} catch (RestClientResponseException e) {
			log.error(ERROR_RESPONSE + e.getResponseBodyAsString());
		} catch (IOException e) {
			log.error(ERROR_RESPONSE + e.getMessage());
		}
		return result;
	}

	@PostMapping("access-token")
	public String getAccessToken() {
		String url = "https://login.cloud.camunda.io/oauth/token";
		String clientId = "mh.7LkvPEQ~aT7~cg1UH4dzFqvQ9DrXo";
		String clientSecret = "ZDTO6gz2qlwMdDVHh_-TsNkEMHuq2~l8k04WY53~j9k4LNBA-7xDn5B9K1-oUp7I";
		String body = "grant_type=client_credentials" + "&audience=operate.camunda.io" + "&client_id=" + clientId
				+ "&client_secret=" + clientSecret;
		try {
			String restClientResponse = restClient.post().uri(url)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).body(body).retrieve()
					.body(String.class);
			log.info(restClientResponse);

			OkHttpClient client = new OkHttpClient().newBuilder().build();
			okhttp3.MediaType mediaType = okhttp3.MediaType.parse(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
			RequestBody reqbody = RequestBody.create(body, mediaType);
			Request request1 = new Request.Builder().url(url).method(POST, reqbody).build();
			Response response = client.newCall(request1).execute();
			String msg = response.body().string();
			log.info(msg);
			return msg;

		} catch (RestClientResponseException e) {
			log.error(ERROR_RESPONSE + e.getResponseBodyAsString());
		} catch (IOException e) {
			log.error(ERROR_RESPONSE + e.getMessage());
		}
		return "";
	}
	
	@PostMapping("access-token-rest-client")
	public String getAccessTokenRestClient() {
		String url = "https://login.cloud.camunda.io/oauth/token";
		String clientId = "mh.7LkvPEQ~aT7~cg1UH4dzFqvQ9DrXo";
		String clientSecret = "ZDTO6gz2qlwMdDVHh_-TsNkEMHuq2~l8k04WY53~j9k4LNBA-7xDn5B9K1-oUp7I";
		String body = "grant_type=client_credentials" + "&audience=operate.camunda.io" + "&client_id=" + clientId
				+ "&client_secret=" + clientSecret;
		try {
			String restClientResponse = restClient.post().uri(url)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE).body(body).retrieve()
					.body(String.class);
			log.info(restClientResponse);
			return restClientResponse;

		} catch (RestClientResponseException e) {
			log.error(ERROR_RESPONSE + e.getResponseBodyAsString());
		}
		return "";
	}

	@PostMapping("/search-incidents")
	public String searchIncidents(@org.springframework.web.bind.annotation.RequestBody Operate reqObj)
			throws IOException {
		String url = "https://lhr-1.operate.camunda.io:443/cad5160e-0adc-418a-8ae3-eadec06ce27e/v1/incidents/search";
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		okhttp3.MediaType mediaType = okhttp3.MediaType.parse(MediaType.APPLICATION_JSON_VALUE);
		ObjectMapper objectMapper = new ObjectMapper();
		String reqBody = objectMapper.writeValueAsString(reqObj);
		RequestBody body = RequestBody.create(reqBody, mediaType);
		Request request = new Request.Builder().url(url).addHeader(AUTHORIZATION, BEARER + ACCESS_TOKEN)
				.addHeader(ACCEPT, MediaType.APPLICATION_JSON_VALUE).method(POST, body).build();
		Response response = client.newCall(request).execute();
		String msg = response.body().string();
		log.info("response: " + msg);
		return msg;
	}

	@GetMapping("/incident/{key}")
	public String searchIncidents(@PathVariable String key) throws IOException {
		String url = "https://lhr-1.operate.camunda.io:443/cad5160e-0adc-418a-8ae3-eadec06ce27e/v1/incidents/" + key;
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url(url).method("GET", null)
				.addHeader(ACCEPT, MediaType.APPLICATION_JSON_VALUE).addHeader(AUTHORIZATION, BEARER + ACCESS_TOKEN).build();
		Response response = client.newCall(request).execute();
		log.info("response: " + response);
		return response.body().string();
	}

	@PostMapping("/resolve-incident/{key}")
	public String resolveIncident(@PathVariable String key) throws IOException {
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		okhttp3.MediaType mediaType = okhttp3.MediaType.parse(MediaType.APPLICATION_JSON_VALUE);
		RequestBody body = RequestBody.create("", mediaType);
		Request request = new Request.Builder()
				.url("https://lhr-1.operate.camunda.io:443/cad5160e-0adc-418a-8ae3-eadec06ce27e/v2/incidents/" + key
						+ "/resolution")
				.addHeader(AUTHORIZATION, BEARER + ACCESS_TOKEN).addHeader(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.method(POST, body).build();
		Response response = client.newCall(request).execute();
		String msg = response.body().string();
		log.info(msg);
		return msg;
	}
	
	@PostMapping("/process-instances")
	public String processInstances(@org.springframework.web.bind.annotation.RequestBody Operate reqObj) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		String reqBody = objectMapper.writeValueAsString(reqObj);
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		okhttp3.MediaType mediaType = okhttp3.MediaType.parse(MediaType.APPLICATION_JSON_VALUE);
		RequestBody body = RequestBody.create(reqBody, mediaType);
		Request request = new Request.Builder()
				.url("https://lhr-1.operate.camunda.io:443/cad5160e-0adc-418a-8ae3-eadec06ce27e/v1/process-instances/search")
				.addHeader(AUTHORIZATION, BEARER + ACCESS_TOKEN).addHeader(ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.method(POST, body).build();
		Response response = client.newCall(request).execute();
		String msg = response.body().string();
		log.info(msg);
		return msg;
	}

}
