inputs = {

  queue_list = {

    "api-ready" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "ready-to-add" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "ready-to-update" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "isp-data-ready" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "updated" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }

    "stored" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }
  }
}