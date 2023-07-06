inputs = {

  queue_list = {

    "api-event" = {
      fifo               = false
      dedup              = false
      delay_seconds      = 0
      visibility_timeout = 30
      receive_wait_time  = 0
      redrive_maxreceive = 1
    }
  }
}