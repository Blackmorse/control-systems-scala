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
                    <th>Номер</th>
                    <th>Имя</th>
                    <th>Дата</th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                {documents.map(doc => (
                    <tr key={'document_row_' + doc.id}>
                        <td>{doc.number}</td>
                        <td>{doc.name}</td>
                        <td>{doc.date}</td>
                        <td><a href={'/document?id=' + doc.id}>Открыть</a></td>
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