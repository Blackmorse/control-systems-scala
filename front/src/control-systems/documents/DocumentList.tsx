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
        
        let documents: Array<Document> = props.documents as Array<Document>
        return <table className="green_table">
        <thead>
          <tr>
            <th>Тип двигателя</th>
            <th>Число цилиндров</th>
            <th>№ двигателя</th>
            <th>Название объекта</th>
            <th>№ двигателя на объекте</th>
            <th>Язык</th>
            <th>Дата</th>
            <th>Ревизия</th>
            <th>К сравнению</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {documents.map(document => {
	  var cylindersArr = document.parameters.filter(parameter => parameter[0].code === 10003)[0]
          var cylinders = (cylindersArr !== undefined && cylindersArr.length > 1) ? cylindersArr[1] : ''
          return <tr key={"document_" + document.id}>
            <td>{document.parameters.filter(parameter => parameter[0].code===10001)[0][1]}</td>
            <td>{cylinders}</td>
            <td>{document.fileNameParameters.engineNumber}</td>
            <td>{document.fileNameParameters.objectName}</td>
            <td>{document.fileNameParameters.objectEngineNumber}</td>
            <td>{document.fileNameParameters.lang}</td>
            <td>{document.fileNameParameters.date}</td>
            <td>{document.fileNameParameters.revision}</td>
            
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
