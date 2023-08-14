inputs = {

  project_parameters = {
    gtv-adapter = {
        "sqs/fetch-size"                       = "5"
        "sqs/adapter-isp-data-ready-url"       = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-isp-data-ready"
        "sqs/adapter-data-ready-to-update-url" = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-data-ready-to-update"
        "sqs/adapter-data-updated-url"         = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-data-updated"
        "sqs/adapter-data-ready-to-store-url"  = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-data-ready-to-store"
        "sqs/adapter-data-stored-url"          = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-data-stored"
        "sqs/adapter-gtv-data-ready-url"       = "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-gtv-data-ready"
        "sqs/adapter-gtv-response-received-url"= "${ccsi-local.aws.endpoint}/000000000000/local-billplatform-adapter-gtv-response-received"
        "gtv/host"                             = "http://localhost:8820"
        "ddb/data-mapping-table"               = "local_gtv_adapter_data_mapping"
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