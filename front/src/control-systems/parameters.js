import React from 'react'
import './parameters.css'
import TopPanel from './panels/topPanel'
import RightPanel from './panels/rightPanel'
import axios from 'axios';

class Parameters extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      loading: true
    }
  }

  componentDidMount() {
    axios.get(process.env.REACT_APP_BASE_SERVER_URL + '/allParameters')
        .then(res => {
            this.setState({
              parameters: res.data,
              loading: false
            })
        })
  }

  render() {
    var rightPanelProps = {
      links: [{
        title: "Все параметры",
        onClick: function(){}
      }],
      selected: "Все параметры"
    }

    var content 
    if (this.state.loading) {
      content = <span>Загрузка</span>
    } else {
      content = <table className="red_table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Имя характеристики</th>
            <th>Единицы</th>
            <th>Значение по умолчанию</th>
          </tr>
        </thead>
        <tbody>
          {this.state.parameters.map(parameter => {
            return <tr key={'parameter_row_' + parameter.id}>
              <td>{parameter.id}</td>
              <td>{parameter.name}</td>
              <td>{parameter.unit}</td>
              <td>{parameter.defaultValue}</td>
            </tr>
          })}
        </tbody>
      </table>
    }

    return <div className="full_layout" style={{ background: 'rgb(130,50,32)' }}>
    <div className="main_layout">
      <TopPanel pageName="Параметры"/>
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
  
export default Parameters