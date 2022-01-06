
interface ControlKey {
    code: number, 
    name: string
}

interface FileNameParameters {
    engineNumber: number,
    objectName: string,
    objectEngineNumber: number,
    lang: string, 
    date: string, 
    revision: number
}

export interface Document {
    id: number, 
    number: number, 
    parameters: Array<[ControlKey, string]>,
    fileNameParameters: FileNameParameters
}

export default Document
