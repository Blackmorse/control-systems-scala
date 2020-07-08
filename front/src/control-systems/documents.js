import React from 'react'
import './documents.css'
import TopPanel from './panels/topPanel'
import RightPanel from './panels/rightPanel'
import axios from 'axios';

class Documents extends React.Component {
  constructor(props) {
    super(props)
    this.rightPanelLinks = [
      {
        title: "Список",
        onClick: () => console.log()
      },
      {
        title: "Сравнение",
        onClick: () => console.log()
      }
    ]
    this.state = {
      loading: true,
      selectedPage: "Список",
      documents: []
    }
  }

  componentDidMount(){
    axios.get(process.env.REACT_APP_BASE_SERVER_URL + '/allDocumentsRest')
      .then(res => {
        console.log(res.data)
        this.setState({
          loading: false,
          selectedPage: "Список",
          documents: res.data
        })
      })
  }

  
  render() {
    var rightPanelProps = {
      links: this.rightPanelLinks,
      selected: this.state.selectedPage
    }

    return <div className="full_layout" style={{ background: 'rgb(23,93,80)' }}>
    <div className="main_layout">
      <TopPanel pageName="Документы"/>
      <div className="bottom_section">
        <section className="content">
          <table className="documents_list_table">
            <thead>
              <tr>
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
              {this.state.documents.map(document => {
              return <tr key={"document_" + document.id}>
                <td>{document.number}</td>
                <td>{document.fileNameParameters.engineNumber}</td>
                <td>{document.fileNameParameters.objectName}</td>
                <td>{document.fileNameParameters.objectEngineNumber}</td>
                <td>{document.fileNameParameters.lang}</td>
                <td>{document.fileNameParameters.date}</td>
                <td>{document.fileNameParameters.revision}</td>
                <td></td>
                <td></td>
              </tr>
              })}
            </tbody>
          </table>
        </section>
        <RightPanel value={rightPanelProps} />
      </div>
    </div>
    </div>
  }
  }
  
export default Documents