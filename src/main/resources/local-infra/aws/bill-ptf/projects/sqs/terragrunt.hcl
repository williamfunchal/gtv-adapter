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

    "adapter-data-ready-to-update" = {
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

    "adapter-data-ready-to-store" = {
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

    "adapter-gtv-data-ready" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "adapter-gtv-response-received" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }
  }
}