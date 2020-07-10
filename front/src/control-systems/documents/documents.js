import React from 'react'
import './documents.css'
import TopPanel from '../panels/topPanel'
import RightPanel from '../panels/rightPanel'
import axios from 'axios';
import DocumentList from './documentList'
import DocumentsCompare from './documentsCompare'

class Documents extends React.Component {
  constructor(props) {
    super(props)
    
    this.state = {
      loading: true,
      selectedPage: "Список",
      documents: [],
      checkedIds: new Set()
    }
  }

  componentDidMount() {
    axios.get(process.env.REACT_APP_BASE_SERVER_URL + '/allDocumentsRest')
      .then(res => {
        
        this.setState({
          loading: false,
          selectedPage: this.state.selectedPage,
          documents: res.data
        })
      })
  }

  handleCheckBoxClick = docId => {
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
    var newState = this.state
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
      var newState = this.state
      newState.selectedPage = "Сравнение"
      this.setState(newState)
    }
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

    var documentProps = {
      documents: this.state.documents,
      checkAction: this.handleCheckBoxClick,
      checkedIds: this.state.checkedIds
    }

    var content
    if (this.state.selectedPage === "Список") {
      content = <DocumentList value={documentProps} />
    } else if (this.state.selectedPage === "Сравнение") {
      var contains = (document) => {
        return this.state.checkedIds.has(document.id)
      }

      var selectedDocuments = this.state.documents.filter(document => contains(document))
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
  
export default Documents