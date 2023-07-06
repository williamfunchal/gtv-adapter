inputs = {

  project_parameters = {
    api = {
        "sqs/fetch-size" = "5"
        "sqs/url"        = "${ccsi-local.aws.endpoint}/000000000000/local-gtv-adapter-api-event"
        "gtv/host"       = "http://localhost:8820"
    }
  }

  insecure_project_parameters = {
    api = {

    }
  }

  namespace_parameters = {
    "graceful-shutdown-timeout"          = "30s"
    "sqs/message-visibility-timeout-sec" = "300"
    "sqs/message-wait-time-sec"          = "0"
  }

  durable_project_parameters_local_values = {
    api = {
        "gtv/api-x-key" = "changeme"
    }
  }
}