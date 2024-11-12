# Camunda8
Spring boot project using Camunda-8

Goto https://console.cloud.camunda.io/ , create a new cluster and client. Add all the required information in application.properties.

Invoke below URLs to start the process - 

1. http://localhost:8080/start/VoipProcess
2. http://localhost:8080/start/process-2

Other camunda operate rest endpoints - 

POST: http://localhost:8080/access-token-rest-client

GET: http://localhost:8080/incident/2251799813961990

POST: http://localhost:8080/process-definitions-custom

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

POST: http://localhost:8080/access-token

POST: http://localhost:8080/search-incidents

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

POST: http://localhost:8080/resolve-incident/2251799813961990

POST: http://localhost:8080/process-definitions

To get the access token from camunda cloud - 

GET: https://login.cloud.camunda.io/oauth/token

Set body as Form URL Encoded in postman/Insomnia and add below fields - 

client_secret: <client_secret>

client_id: <client_id>

audience: operate.camunda.io

grant_type: client_credentials



More information about Camunda-8 can be found here : https://docs.camunda.io/docs/guides/
