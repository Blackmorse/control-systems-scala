import ax from 'axios';
import ParameterEntity from '../models/ParameterEntity'
import DocumentIdResponse from '../models/DocumentIdResponse'

const axios = ax.create({ baseURL: process.env.REACT_APP_BASE_SERVER_URL + '/api' })

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
 
export function updateParameter(token: string,
        id: number, name: string, unit: string, defaultValue: string,
        callback: () => void, errorCallback: () => void) {
    axios.put('/updateParameter?id=' + id + '&unit=' + unit + '&defaultValue=' + defaultValue + '&name=' + name, {}, {
    headers: { 
        'lg-token': token
      }}
    ).then(_res => callback())
    .catch(_e => errorCallback())
}

export function addParameter(token: string,
        id: string, name: string, unit: string, defaultValue: string,
        callback: () => void, errorCallback: () => void) {
    axios.post('/addParameter?id=' + id + '&unit=' + unit + '&defaultValue=' + defaultValue + '&name=' + name, {}, {
    headers: { 
        'lg-token': token
      }}
    ).then(_res => callback())
    .catch(_e => errorCallback())
}

export function documentsWithChart(ids: Array<number>, values: Array<string>,
        callback: (res: Array<Document>) => void) {
    let queryParams = ''
    for (var i = 0; i < ids.length; i++) {     
        if (ids[i] !== undefined && values[i] !== '') {
            queryParams = queryParams + 'searchParams=' + encodeURIComponent(ids[i] + '=' + values[i]) + '&'
        }
    }

    axios.get('/searchDocumentsWithChart?' + queryParams)
        .then(response => response.data)
        .then(res => callback(res))
}

export function uploadDocument(file: any, callback: (res: DocumentIdResponse) => void,
        errorCallback: () => void) {

    let data = new FormData()
    data.append('file', file)
    axios.post<DocumentIdResponse>('upload', data, {})
        .then(response => response.data)
        .then(res => callback(res))
        .catch(_e => errorCallback())
}

export function allDocuments(callback: (res: Array<Document>) => void) {
    axios.get<Array<Document>>('/allDocumentsRest')
        .then(response => response.data)
        .then(res => callback(res))
}
