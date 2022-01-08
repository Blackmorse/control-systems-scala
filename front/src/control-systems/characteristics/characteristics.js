import React from 'react'
import './characteristics.css'
import TopPanel from '../panels/TopPanel'
import RightPanel from '../panels/rightPanel'
import CharacteristicsChart from './characteristicsChart'
import SearchSection from './SearchSection'
import { documentsWithChart } from '../client/RestClient'

class Characteristic extends React.Component {
  constructor(props) {
    super(props)
    this.state = ({
      selectedPage: "Поиск"
    })
  }

  onSearchClicked = (ids, values) => {
    
    documentsWithChart(ids, values, (res) => {
        var newState = this.state
        newState.selectedPage = "График"
        newState.documentCharts = res
        this.setState(newState)
    })
  }

  render() {
    var rightPanelProps = {
      links: [{
        title: "Поиск",
        onClick: () => {var newState=this.state; newState.selectedPage="Поиск"; this.setState(newState)}
      },
      {
        title: "График",
        onClick: () => {}
      }
      ],
      selected: this.state.selectedPage
    }

    var content = (this.state.selectedPage === "Поиск") ? 
      <SearchSection onSearchClicked={(ids, values) => this.onSearchClicked(ids, values)}/> :
      <CharacteristicsChart documentCharts={this.state.documentCharts}/> 
      // <span>adasdas</span>

    return <div className="full_layout" style={{ background: 'rgb(23,93,80)' }}>
    <div className="main_layout">
      <TopPanel pageName="Характеристики"/>
      <div className="bottom_section">
        <section className="content">
        {content}
        </section>
        <RightPanel value={rightPanelProps}/>
      </div> 
    </div>
    </div>
  }
  }
  
export default Characteristic
