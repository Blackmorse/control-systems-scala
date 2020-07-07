import React from 'react'
import './parameters.css'
import TopPanel from './panels/topPanel'
import RightPanel from './panels/rightPanel'

class Parameters extends React.Component {
  render() {
    return <div className="full_layout" style={{ background: 'rgb(130,50,32)' }}>
    <div className="main_layout">
      <TopPanel pageName="Параметры"/>
      <div className="bottom_section">
        <section className="content">
          Параметры
        </section>
        <RightPanel links={[]}/>
      </div>
    </div>
    </div>
  }
  }
  
export default Parameters