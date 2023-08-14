inputs = {
  ddb_tables = {
    "${ccsi-local.aws.env}_gtv_adapter_account_mapping" = {
      billing_mode   = "PAY_PER_REQUEST"
      read_capacity  = 1
      write_capacity = 1
      hash_key = {
        name = "isp_customer_key"
        type = "N"
      }
      range_key                = null
      global_secondary_indexes = null
      ttl                      = null
    }

    "${ccsi-local.aws.env}_gtv_adapter_service_resource_mapping" = {
      billing_mode   = "PAY_PER_REQUEST"
      read_capacity  = 1
      write_capacity = 1
      hash_key = {
        name = "isp_service_key"
        type = "N"
      }
      range_key = {
        name = "isp_customer_key"
        type = "N"
      }
      global_secondary_indexes = null
      ttl                      = null
    }

    "${ccsi-local.aws.env}_gtv_adapter_data_mapping" = {
      billing_mode   = "PAY_PER_REQUEST"
      read_capacity  = 1
      write_capacity = 1
      hash_key = {
        name = "correlation_id"
        type = "S"
      }
      range_key                = null
      global_secondary_indexes = null
      ttl                      = null
    }

    "${ccsi-local.aws.env}_gtv_adapter_gtv_api_calls" = {
      billing_mode   = "PAY_PER_REQUEST"
      read_capacity  = 1
      write_capacity = 1
      hash_key = {
        name = "correlation_id"
        type = "S"
      }
      range_key                = null
      global_secondary_indexes = null
      ttl                      = null
    }
  }
}


