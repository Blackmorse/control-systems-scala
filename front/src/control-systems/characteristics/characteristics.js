import React from 'react'
import './characteristics.css'
import TopPanel from '../panels/topPanel'
import RightPanel from '../panels/rightPanel'
import axios from 'axios';
import CharacteristicsSearchSection from './searchSection'
import CharacteristicsChart from './characteristicsChart'

class Characteristic extends React.Component {
  constructor(props) {
    super(props)
    this.state = ({
      selectedPage: "Поиск"
    })
  }

  onSearchClicked = (ids, values) => {
    var requestUrl = process.env.REACT_APP_BASE_SERVER_URL + '/searchDocumentsWithChart?'
    for (var i = 0; i < ids.length; i++) {     
        requestUrl = requestUrl + 'searchParams=' + encodeURIComponent(ids[i] + '=' + values[i]) + '&'
    }
    axios.get(requestUrl)
      .then(res => {
        console.log(res.data)
        var newState = this.state
        newState.selectedPage = "График"
        newState.documentCharts = res.data
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
      <CharacteristicsSearchSection searchClicked={this.onSearchClicked}/> :
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