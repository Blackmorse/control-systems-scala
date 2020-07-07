import React from 'react'
import './characteristics.css'
import TopPanel from './panels/topPanel'
import RightPanel from './panels/rightPanel'

class Characteristic extends React.Component {
  render() {
    return <div className="full_layout" style={{ background: 'rgb(23,93,80)' }}>
    <div className="main_layout">
      <TopPanel pageName="Характеристики"/>
      <div className="bottom_section">
        <section className="content">
          Характеристики
        </section>
        <RightPanel links={[]}/>
      </div>
    </div>
    </div>
  }
  }
  
export default Characteristic