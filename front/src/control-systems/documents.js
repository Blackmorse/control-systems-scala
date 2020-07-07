import React from 'react'
import './documents.css'
import TopPanel from './panels/topPanel'
import RightPanel from './panels/rightPanel'

class Documents extends React.Component {
  render() {
    return <div className="full_layout" style={{ background: 'rgb(23,93,80)' }}>
    <div className="main_layout">
      <TopPanel pageName="Документы"/>
      <div className="bottom_section">
        <section className="content">
          Документы
        </section>
        <RightPanel links={[]}/>
      </div>
    </div>
    </div>
  }
  }
  
export default Documents