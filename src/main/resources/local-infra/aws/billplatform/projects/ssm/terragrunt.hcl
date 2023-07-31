inputs = {

  project_parameters = {
    gtv-adapter = {
        "sqs/fetch-size"                 = "5"
        "sqs/adapter-isp-data-ready-url" = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-isp-data-ready"
        "sqs/adapter-update-data-url"    = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-update-data"
        "sqs/adapter-data-updated-url"   = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-data-updated"
        "sqs/adapter-store-data-url"     = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-store-data"
        "sqs/adapter-data-stored-url"    = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-data-stored"
        "sqs/adapter-gtv-request-url"    = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-gtv-request"
        "sqs/adapter-gtv-response-url"   = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-gtv-response"
        "gtv/host"                       = "http://localhost:8820"
    }
  }

  insecure_project_parameters = {
    gtv-adapter = {

    }
  }

  namespace_parameters = {
    "graceful-shutdown-timeout"          = "30s"
    "sqs/message-visibility-timeout-sec" = "300"
    "sqs/message-wait-time-sec"          = "0"
  }

  durable_project_parameters_local_values = {
    gtv-adapter = {
        "gtv/api-x-key" = "changeme"
    }
  }
}