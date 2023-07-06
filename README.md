
# Running

In order to run the example app, you'll need to have Docker running in your machine and the following system variable
```
  ENVIRONMENT=local
```
This will trigger the springboot local profile with all the necessary configuration to run the application with the local-infra.
Once the application is up and running you can send a message to the processor queue using the following command.

This command is formatted for windows 
```
aws --endpoint-url=http://localhost:8810 --region us-west-2 sqs send-message `
--queue-url http://localhost:8810/000000000000/local-gtv-adapter-api-event `
--message-body file://src/main/resources/local-infra/aws/gtv-adapter/projects/sqs/test-data/usage-events-bulk.json `
--message-attributes file://src/main/resources/local-infra/aws/gtv-adapter/projects/sqs/test-data/message-attributes.json
```