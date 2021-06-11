import request from '@/utils/request'

export function get(id) {
  return request({
    url: 'api/person/' + id,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: 'api/person',
    method: 'post',
    data
  })
}

export function del(id) {
  return request({
    url: 'api/person/' + id,
    method: 'delete'
  })
}

export function edit(data) {
  return request({
    url: 'api/person',
    method: 'put',
    data
  })
}

export function downloadPerson(params) {
  return request({
    url: 'api/person/download',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

export function getPersonListTree() {
  return request({
    url: 'api/person/tree',
    method: 'get'
  })
}

export function getPersonListDisplay() {
  return request({
    url: 'api/person/display',
    method: 'get'
  })
}
