import React from 'react'
import '../../css/tables.css'
import TopPanel from '../panels/TopPanel'
import RightPanel from '../panels/rightPanel'
import DocumentList  from './DocumentList'
import Document from '../models/Document'
import DocumentsCompare from './documentsCompare'
import { allDocuments, deleteDocumentById } from '../client/RestClient'

class DocumentsPage extends React.Component<any, any> {
  constructor(props: any) {
    super(props)
    
    this.state = {
      loading: true,
      selectedPage: "Список",
      documents: [],
      checkedIds: new Set()
    }
  }

  componentDidMount() {
    allDocuments((res) => {
        this.setState({
          loading: false,
          selectedPage: this.state.selectedPage,
          documents: res
        })
    })
  }

  handleCheckBoxClick = (docId: number) => {
    if (this.state.checkedIds.has(docId)) {
      this.state.checkedIds.delete(docId)
    } else {
      this.state.checkedIds.add(docId);
    }
    console.log(this.state.checkedIds)
    var newState = this.state
    this.setState(newState)
  }

  onListClicked = () => {
    if(this.state.selectedPage === "Список") {
      return
    }
    var newState = {
      ...this.state
    }
    newState.selectedPage = "Список"
    this.setState(newState)
  }

  onCompareClicked = () => {
    if(this.state.selectedPage === "Сравнение") {
      return
    }

    if (this.state.checkedIds.size === 0) {
      alert('Не выбраны документы для сравнения/просмотра')
    } else {
      var newState = {
          ...this.state
        }
      newState.selectedPage = "Сравнение"
      this.setState(newState)
    }
  }

  deleteDocument = (id: number, token: string) => {
      deleteDocumentById(id, token, () => {
        var newState = {
          ...this.state
        }
        newState.documents = this.state.documents.filter((doc: Document) => doc.id !== id)
        this.setState(newState)
    })
  }

  
  render() {
    var rightPanelLinks = [
      {
        title: "Список",
        onClick: this.onListClicked
      },
      {
        title: "Сравнение",
        onClick: this.onCompareClicked
      }
    ]

    var rightPanelProps = {
      links: rightPanelLinks,
      selected: this.state.selectedPage
    }

    var content
    if (this.state.selectedPage === "Список") {
      content = <DocumentList 
        documents={this.state.documents}
        checkedIds={this.state.checkedIds}
        checkAction={this.handleCheckBoxClick}
        deleteAction={this.deleteDocument}
      />
    } else if (this.state.selectedPage === "Сравнение") {
      var contains = (document: Document) => {
        return this.state.checkedIds.has(document.id)
      }

      var selectedDocuments = this.state.documents.filter((document: Document) => contains(document))
      content = <DocumentsCompare documents={selectedDocuments}/>
    }

    return <div className="full_layout" style={{ background: 'rgb(23,93,80)' }}>
    <div className="main_layout">
      <TopPanel pageName="Документы"/>
      <div className="bottom_section">
        <section className="content">
          {(this.state.loading === true) ? "Загрузка..." : ""}
          {content}
        </section>
        <RightPanel value={rightPanelProps} />
      </div>
    </div>
    </div>
  }
  }
  
export default DocumentsPage
