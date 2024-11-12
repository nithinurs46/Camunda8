package com.camunda.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VoipService {

	@JobWorker(type = "startProcess", autoComplete = true)
	public void startProcess(JobClient jobClient, ActivatedJob job) {
		log.info("startProcess");
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (Exception e) {
			e.printStackTrace();
			e.printStackTrace();
			jobClient.newThrowErrorCommand(job.getKey()).errorCode("CustomErrorCode")
					.errorMessage("An error occurred in the service task: " + e.getMessage()).send().join();
		}
	}

	@JobWorker(type = "processStart")
	public void processStart(JobClient jobClient, ActivatedJob job) {
		log.info("processStart");
	}

	@JobWorker(type = "createTaskInitialize")
	public void createTaskInitialize(JobClient jobClient, ActivatedJob job) {
		log.info("createTaskInitialize");
	}

	@JobWorker(type = "taskNotCompleted")
	public Map<String, Object> taskNotCompleted(JobClient jobClient, ActivatedJob job) {
		boolean taskCompleted = false;
		int counter = (int) job.getVariablesAsMap().getOrDefault("counter", 0);
		log.info("counter: " + counter);
		Map<String, Object> variablesMap = new HashMap<>();
		counter++;
		if (counter == 17 || counter == 20) {
			taskCompleted = true;
		}
		variablesMap.put("taskCompleted", taskCompleted);
		variablesMap.put("counter", counter);
		log.info("taskNotCompleted");
		return variablesMap;
	}

	@JobWorker(type = "notifyProcessOnTaskCompletion")
	public void notifyProcessOnTaskCompletion(JobClient jobClient, ActivatedJob job) {
		log.info("notifyProcessOnTaskCompletion");
	}

	@JobWorker(type = "waitForEokReceived")
	public void waitForEokReceived(JobClient jobClient, ActivatedJob job, @Variable String counter) {
		log.info("waitForEokReceived: " + counter);
	}

	@JobWorker
	public Map<String, Object> isWaitForRelatedServiceOrderClosure(JobClient jobClient, ActivatedJob job) {
		Map<String, Object> variablesMap = job.getVariablesAsMap();
		variablesMap.put("relatedClosed", true);
		variablesMap.put("counter", 0);
		log.info("isWaitForRelatedServiceOrderClosure");
		return variablesMap;
	}

	@JobWorker
	public Map<String, Object> sendOcc(JobClient jobClient, ActivatedJob job) {
		Map<String, Object> variablesMap = job.getVariablesAsMap();
		variablesMap.put("relatedClosed", true);
		variablesMap.put("counter", 0);
		log.info("sendOcc");
		return variablesMap;
	}

	@JobWorker
	public Map<String, Object> processComplete(JobClient jobClient, ActivatedJob job) {
		Map<String, Object> variablesMap = job.getVariablesAsMap();
		log.info("processComplete" + job.toJson());
		return variablesMap;
	}

}
