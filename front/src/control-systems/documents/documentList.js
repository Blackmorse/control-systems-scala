import React from 'react'

class DocumentList extends React.Component {

    render() {
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
          {this.props.value.documents.map(document => {
          return <tr key={"document_" + document.id}>
            <td>{document.parameters.filter(parameter => parameter[0].code===10001)[0][1]}</td>
            <td>{document.parameters.filter(parameter => parameter[0].code===10003)[0][1]}</td>
            <td>{document.fileNameParameters.engineNumber}</td>
            <td>{document.fileNameParameters.objectName}</td>
            <td>{document.fileNameParameters.objectEngineNumber}</td>
            <td>{document.fileNameParameters.lang}</td>
            <td>{document.fileNameParameters.date}</td>
            <td>{document.fileNameParameters.revision}</td>
            
            <td><input type="checkbox"
                      defaultChecked={this.props.value.checkedIds.has(document.id)} 
                      onClick={() => this.props.value.checkAction(document.id)}/></td>
            <td><a href="#" onClick={() => this.props.value.deleteAction(document.id)}>Удалить</a></td>
          </tr>
            
          })}
        </tbody>
      </table>
    }
}

export default DocumentList