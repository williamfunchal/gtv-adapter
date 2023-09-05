inputs = {
  ddb_tables = {
    "${ccsi-local.aws.env}_${ccsi-local.aws.namespaces}_usage_events" = {
      billing_mode   = "PAY_PER_REQUEST"
      read_capacity  = 1
      write_capacity = 1
      hash_key = {
        name = "event_id"
        type = "S"
      }
      range_key                = null
      global_secondary_indexes = {
        correlation_id = {
          hash_key = {
            name = "correlation_id"
            type = "S"
          }
          range_key       = null
          projection_type = "KEYS_ONLY"
        }
      }
      ttl                      = null
    }

    "${ccsi-local.aws.env}_${ccsi-local.aws.namespaces}_customer_events" = {
      billing_mode   = "PAY_PER_REQUEST"
      read_capacity  = 1
      write_capacity = 1
      hash_key = {
        name = "event_id"
        type = "S"
      }
      range_key                = null
      global_secondary_indexes = {
        correlation_id = {
          hash_key = {
            name = "correlation_id"
            type = "S"
          }
          range_key       = null
          projection_type = "KEYS_ONLY"
        }
      }
      ttl                      = null
    }

    "${ccsi-local.aws.env}_${ccsi-local.aws.namespaces}_service_events" = {
      billing_mode   = "PAY_PER_REQUEST"
      read_capacity  = 1
      write_capacity = 1
      hash_key = {
        name = "event_id"
        type = "S"
      }
      range_key                = null
      global_secondary_indexes = {
        correlation_id = {
          hash_key = {
            name = "correlation_id"
            type = "S"
          }
          range_key       = null
          projection_type = "KEYS_ONLY"
        }
      }
      ttl                      = null
    }

    "${ccsi-local.aws.env}_${ccsi-local.aws.namespaces}_service_resource_events" = {
      billing_mode   = "PAY_PER_REQUEST"
      read_capacity  = 1
      write_capacity = 1
      hash_key = {
        name = "event_id"
        type = "S"
      }
      range_key                = null
      global_secondary_indexes = {
        correlation_id = {
          hash_key = {
            name = "correlation_id"
            type = "S"
          }
          range_key       = null
          projection_type = "KEYS_ONLY"
        }
      }
      ttl                      = null
    }

    "${ccsi-local.aws.env}_${ccsi-local.aws.namespaces}_account_mapping" = {
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

    "${ccsi-local.aws.env}_${ccsi-local.aws.namespaces}_service_resource_mapping" = {
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

    "${ccsi-local.aws.env}_${ccsi-local.aws.namespaces}_gtv_api_calls" = {
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


