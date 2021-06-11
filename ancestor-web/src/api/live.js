import request from '@/utils/request'

export function add(data) {
  return request({
    url: 'api/live',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'api/live/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'api/live',
    method: 'put',
    data
  })
}

export function downloadLive(params) {
  return request({
    url: 'api/live/download',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

export function queryAll(data) {
  return request({
    url: 'api/live',
    method: 'get',
    data
  })
}
