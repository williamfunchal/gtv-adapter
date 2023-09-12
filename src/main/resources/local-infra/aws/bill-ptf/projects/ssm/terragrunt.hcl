inputs = {

  project_parameters = {
    gtv-adapter = {
        "gtv/host"                             = "http://localhost:${ccsi-local.wiremock.port}"
    }
  }

  insecure_project_parameters = {

  }

  namespace_parameters = {
    "graceful-shutdown-timeout"            = "30s"
    "sqs/adapter-isp-data-ready-url"       = "${ccsi-local.aws.endpoint}/000000000000/local-bill-ptf-adapter-isp-data-ready.fifo"
    "sqs/adapter-data-ready-to-update-url" = "${ccsi-local.aws.endpoint}/000000000000/local-bill-ptf-adapter-data-ready-to-update.fifo"
    "sqs/adapter-data-updated-url"         = "${ccsi-local.aws.endpoint}/000000000000/local-bill-ptf-adapter-data-updated.fifo"
    "sqs/adapter-data-ready-to-store-url"  = "${ccsi-local.aws.endpoint}/000000000000/local-bill-ptf-adapter-data-ready-to-store.fifo"
    "sqs/adapter-data-stored-url"          = "${ccsi-local.aws.endpoint}/000000000000/local-bill-ptf-adapter-data-stored.fifo"
    "sqs/adapter-gtv-data-ready-url"       = "${ccsi-local.aws.endpoint}/000000000000/local-bill-ptf-adapter-gtv-data-ready.fifo"
    "sqs/adapter-gtv-response-received-url"= "${ccsi-local.aws.endpoint}/000000000000/local-bill-ptf-adapter-gtv-response-received.fifo"
    "sqs/message-visibility-timeout-sec"   = "60"
    "sqs/message-wait-time-sec"            = "20"
    "sqs/fetch-size"                       = "10"
    "sqs/empty-timeout-mills"              = "0"
    "coredb/url"                           = "jdbc:oracle:thin:@coredev1-serv-usw2.czoqxvus1z1f.us-west-2.rds.amazonaws.com:1521:SERV"
    "coredb/username"                      = "gtvadapter"
  }

  durable_project_parameters_local_values = {
    gtv-adapter = {
        "gtv/api-x-key"   = "changeme"
        "coredb/password" = "changeme"
    }
  }
}