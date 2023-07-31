inputs = {

  queue_list = {
    "adapter-isp-data-ready" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "adapter-update-data" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "adapter-data-updated" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "adapter-store-data" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "adapter-data-stored" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "adapter-gtv-request" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "adapter-gtv-response" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }
  }
}