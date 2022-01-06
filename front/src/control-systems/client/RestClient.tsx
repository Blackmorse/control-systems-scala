import ax from 'axios';
import ParameterEntity from '../models/ParameterEntity'

const axios = ax.create({ baseURL: process.env.REACT_APP_BASE_SERVER_URL })

export function getAllParameters(callback: (res: Array<ParameterEntity>) => void) {
  axios.get<Array<ParameterEntity>>('/allParameters')
    .then(response => response.data)
    .then(callback)
}

export function deleteDocumentById(id: number, token: string, callback: () => void) {
  axios.delete('/deleteDocumentById?id=' + id, {
    headers: {
        'lg-token': token
    }
  })
    .then(_res => callback())
}

export function login(username: string, password: string, callback: (token: string) => void, errorCallback: (msg: string) => void) {
  axios.post<string>('/login', {
    username: username,
    password: password
  })
    .then(response => response.data)
    .then(data => callback(data))
    .catch(e => {
      errorCallback(e.response.data)
    })
}
