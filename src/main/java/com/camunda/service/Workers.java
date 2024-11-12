package com.camunda.service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.client.api.worker.JobClient;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import io.camunda.zeebe.spring.client.exception.ZeebeBpmnError;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class Workers {

	//fetchVariables - fetch only the reqd variables. If not specified it will get all variables. improves performance when only reqd variables are fetched
	@JobWorker(type = "job1", autoComplete = true, fetchVariables={"name", "gender"})
	public void executeJob1(final JobClient jobClient, ActivatedJob job) {
		log.info("Starting Job-1(): " + job.getKey());
		try {
			String name = (String) job.getVariablesAsMap().get("name");
			log.info(name);
			TimeUnit.SECONDS.sleep(5);
			boolean isCompleted = false;
			if (!isCompleted) {
				throw new ZeebeBpmnError("Error occured", "Task not completed, error thrown");
			}
		} catch (Exception e) {
			e.printStackTrace();
			jobClient.newThrowErrorCommand(job.getKey()).errorCode("CustomErrorCode")
					.errorMessage("An error occurred in the service task: " + e.getMessage()).send().join();
			/*jobClient.newFailCommand(job).retries(0) // job.getRetries() - 1 <1>: Decrement retries
					.errorMessage("Exception in executing the job: " + ex.getMessage()) // <2>
					.send();*/
			// .exceptionally(t -> {throw new RuntimeException("Could not fail job: " +
			// t.getMessage(), t);});
		}
		log.info("Executed Job-1(): " + job.getKey());
	}

	//autocomplete is true by default and spring will take care of job completion. we can set it to false and handle completion manually as well.
	@JobWorker(type = "job2", autoComplete = false)
	public Map<String, Object> executeJob2(final JobClient jobClient, ActivatedJob job, @Variable(name = "age") String age) {
		log.info("Starting Job-2(): " + job.getKey());
		log.info("Age: " + age);
		jobClient.newCompleteCommand(job.getKey())
	     .send()
	     .exceptionally( throwable -> { throw new RuntimeException("Could not complete job " + job, throwable); });
		log.info("Executed Job-2(): " + job.getKey());
		return Map.of();
	}
}
