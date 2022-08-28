import {useState} from 'react'
import {TypedUseSelectorHook, useSelector} from 'react-redux'
import {LoginState} from '../login/Login'
import Document from '../models/Document'

interface Props {
    documents: Array<Document>,
    checkedIds: Set<number>,
    checkAction: (id: number) => void,
    deleteAction: (id: number, token: string) => void
}

const DocumentList = (props: Props) =>{

        const useAppSelector: TypedUseSelectorHook<LoginState> = useSelector

        let stateMapFunc = (state: LoginState) => state?.login

        let token = useAppSelector(stateMapFunc)?.token
        let documents = props.documents 

        type Sorting = 'engine' | undefined
        let [ sortingField, setSortingFieldFunc ] = useState(undefined as Sorting)

        function sortDocumentsByEngineNumber() {
            setSortingFieldFunc('engine')
        }

        let sortedDocuments: Array<Document> 
        if (sortingField === 'engine') {
            sortedDocuments = documents.sort((doc1, doc2) => doc1.fileNameParameters.engineNumber - doc2.fileNameParameters.engineNumber)
        } else {
            sortedDocuments = documents
        }

        return <table className="green_table">
        <thead>
          <tr>
            <th></th>
            <th className='text-center'>Тип двигателя</th>
            <th className='text-center'>Число цилиндров</th>
            <th className='text-center'><a className='link-light' href='#' onClick={_e => sortDocumentsByEngineNumber()}>№ двигателя</a></th>
            <th className='text-center'>Название объекта</th>
            <th className='text-center'>№ двигателя на объекте</th>
            <th className='text-center'>Язык</th>
            <th className='text-center'>Дата</th>
            <th className='text-center'>Ревизия</th>
            <th className='text-center'>К сравнению</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {sortedDocuments.map((document, index) => {
	  var cylindersArr = document.parameters.filter(parameter => parameter[0].code === 10003)[0]
          var cylinders = (cylindersArr !== undefined && cylindersArr.length > 1) ? cylindersArr[1] : ''
          return <tr key={"document_" + document.id}>
            <td>{index + 1}</td>
            <td className='text-center'>{document.parameters.filter(parameter => parameter[0].code===10001)[0][1]}</td>
            <td className='text-center'>{cylinders}</td>
            <td className='text-center'>{document.fileNameParameters.engineNumber}</td>
            <td className='text-center'>{document.fileNameParameters.objectName}</td>
            <td className='text-center'>{document.fileNameParameters.objectEngineNumber}</td>
            <td className='text-center'>{document.fileNameParameters.lang}</td>
            <td className='text-center'>{document.fileNameParameters.date}</td>
            <td className='text-center'>{document.fileNameParameters.revision}</td>
            
            <td><input type="checkbox"
                      defaultChecked={props.checkedIds.has(document.id)} 
                      onClick={() => props.checkAction(document.id)}/></td>
            <td>
                {(token === undefined) ? <></> :
                <a href="#" onClick={() => props.deleteAction(document.id, token!)}>Удалить</a>}
            </td>
          </tr>
            
          })}
        </tbody>
      </table>
}

export default DocumentList
