# Camunda8
Spring boot project using Camunda-8

Goto https://console.cloud.camunda.io/ , create a new cluster and client. Add all the required information in application.properties.

Invoke below URLs to start the process - 

1. http://localhost:8080/start/VoipProcess
2. http://localhost:8080/start/process-2

More information about Camunda-8 can be found here : https://docs.camunda.io/docs/guides/

Sample request body - 

Process-definitions-custom

{
  "filter": {
    "key": 2251799813706288,
    "name": "process-1",
    "version": 2,
    "bpmnProcessId": "process-1",
    "tenantId": "<default>"
  },
  "size": 1,
  "searchAfter": [
    {}
  ],
  "sort": [
    {
      "field": "key",
      "order": "ASC"
    }
  ]
}

Search incidents - 

{
  "filter": {
    "key": 4503599627672468,
    "name": "process-1",
    "version": 2,
    "bpmnProcessId": "process-1",
    "tenantId": "<default>"
  },
  "size": 1,
  "searchAfter": [
    {}
  ],
  "sort": [
    {
      "field": "key",
      "order": "ASC"
    }
  ]
}


