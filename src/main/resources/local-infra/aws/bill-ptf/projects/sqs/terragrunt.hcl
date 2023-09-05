inputs = {

  queue_list = {
    "adapter-isp-data-ready" = {
      fifo                 = true
      dedup                = true
      delay_seconds        = 0
      visibility_timeout   = 60
      receive_wait_time    = 0
      redrive_maxreceive   = 3    }

    "adapter-data-ready-to-update" = {
      fifo                 = true
      dedup                = true
      delay_seconds        = 0
      visibility_timeout   = 60
      receive_wait_time    = 0
      redrive_maxreceive   = 3    }

    "adapter-data-updated" = {
      fifo                 = true
      dedup                = true
      delay_seconds        = 0
      visibility_timeout   = 60
      receive_wait_time    = 0
      redrive_maxreceive   = 3    }

    "adapter-data-ready-to-store" = {
      fifo                 = true
      dedup                = true
      delay_seconds        = 0
      visibility_timeout   = 60
      receive_wait_time    = 0
      redrive_maxreceive   = 3    }

    "adapter-data-stored" = {
      fifo                 = true
      dedup                = true
      delay_seconds        = 0
      visibility_timeout   = 60
      receive_wait_time    = 0
      redrive_maxreceive   = 3    }

    "adapter-gtv-data-ready" = {
      fifo                 = true
      dedup                = true
      delay_seconds        = 0
      visibility_timeout   = 60
      receive_wait_time    = 0
      redrive_maxreceive   = 3    }

    "adapter-gtv-response-received" = {
      fifo                 = true
      dedup                = true
      delay_seconds        = 0
      visibility_timeout   = 60
      receive_wait_time    = 0
      redrive_maxreceive   = 3    }
  }
}