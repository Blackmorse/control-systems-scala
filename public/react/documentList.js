'use strict';

const e = React.createElement;
var a = document.getElementById("documentData").getAttribute("pr")
var documents = JSON.parse(a)

class DocumentTable extends React.Component {
    constructor(props) {
        super(props);
        this.state= {
            checkedIds: new Set(),
        }
    }

    handleCheckBoxClick(docId) {
        if (this.state.checkedIds.has(docId)) {
            this.state.checkedIds.delete(docId)
        } else {
            this.state.checkedIds.add(docId);
        }
        this.setState ( {
            checkedIds: this.state.checkedIds,
        })
    }



    render() {
        var hrefParameters = '?' + Array.from(this.state.checkedIds).map(id => 'ids=' + id).join("&")
        const tbl = (
        <div className="documentDivList">
            <table className="documentList">
            <thead>
                <tr>
                    <th></th>
                    <th>№ документа</th>
                    <th>№ двигателя</th>
                    <th>Название объекта</th>
                    <th>№ двигателя на объекте</th>
                    <th>Язык</th>
                    <th>Дата</th>
                    <th>Ревизия</th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                {documents.map(doc => (
                    <tr key={'document_row_' + doc.id}>
                        <td><a href={'/deleteDocumentById'}></a></td>
                        <td>{doc.number}</td>
                        <td>{doc.fileNameParameters.engineNumber}</td>
                        <td>{doc.fileNameParameters.objectName}</td>
                        <td>{doc.fileNameParameters.objectEngineNumber}</td>
                        <td>{doc.fileNameParameters.lang}</td>
                        <td>{doc.fileNameParameters.date}</td>
                        <td>{doc.fileNameParameters.revision}</td>
                        <td><a href={'/documentByIds?ids=' + doc.id}>Открыть</a></td>
                        <td><input type="checkbox" onClick={() => this.handleCheckBoxClick(doc.id)}/></td>
                    </tr>
                ))
                }
            </tbody>
            </table>
            {this.state.checkedIds.size != 0 ? <a href={"/documentByIds" + hrefParameters}>Сравнить</a> : null}
        </div>);

        return tbl
    }
}
const domContainer = document.querySelector('#documentsListReact');
ReactDOM.render(e(DocumentTable), domContainer);