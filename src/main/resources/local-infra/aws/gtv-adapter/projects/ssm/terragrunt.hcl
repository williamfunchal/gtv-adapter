inputs = {

  project_parameters = {
    api = {
        "sqs/fetch-size"          = "5"
        "sqs/api-ready-url"       = "${ccsi-local.aws.endpoint}/000000000000/local-gtv-adapter-api-ready"
        "sqs/ready-to-update-url" = "${ccsi-local.aws.endpoint}/000000000000/local-gtv-adapter-ready-to-update"
        "sqs/ready-to-add-url"    = "${ccsi-local.aws.endpoint}/000000000000/local-gtv-adapter-ready-to-add"
        "sqs/isp-data-ready-url"  = "${ccsi-local.aws.endpoint}/000000000000/local-gtv-adapter-isp-data-ready"
        "sqs/updated-url"         = "${ccsi-local.aws.endpoint}/000000000000/local-gtv-adapter-updated"
        "sqs/stored-url"          = "${ccsi-local.aws.endpoint}/000000000000/local-gtv-adapter-stored"
        "gtv/host"                = "http://localhost:8820"
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