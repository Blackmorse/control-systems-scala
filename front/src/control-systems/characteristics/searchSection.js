import React from 'react'
import axios from 'axios';

class CharacteristicsSearchSection extends React.Component {

    constructor(props) {
        super(props)
        this.state = {
            loading: true
        }
        this.rowsNumber = 2
    }

    componentDidMount() {
        axios.get(process.env.REACT_APP_BASE_SERVER_URL + '/allParameters')
            .then(res => {
                this.setState({
                  parameters: res.data,
                  loading: false,
                  selectedParameters: Array(2).fill(res.data[0].id),
                  textValues: Array(2).fill('')
                })
            })
      }

    onSearchClick = () => {
        this.props.searchClicked(this.state.selectedParameters, this.state.textValues)
    }

    handleSelectChange = (i, e) => {
        var newState = this.state
        newState.selectedParameters[i] = e.target.value
        this.setState(newState)
    }

    handleTextChange = (i, e) => {
        var newState = this.state
        newState.textValues[i] = e.target.value
    }

   render() {
        var content
        if(this.state.loading) {
            content = <span>Загрузка...</span>
        } else {
            content = <section className="search_section">
        {Array.from(Array(this.rowsNumber).keys()).map(i => {
          return <div key={'search_row_' + i} className="search_section_row">
            <select id={'search_parameter_' + i} onChange={e => this.handleSelectChange(i, e)}> 
              {this.state.parameters.map(parameter => {
                return <option value={parameter.id} key={'search_param_' + i + '_option' + parameter.id}>{parameter.name}</option>
              })}
            </select>
            <input type="text" id={'search_text_' + i} onChange={e => this.handleTextChange(i, e)}></input>
          </div>
    })}

      <button className="green_button" onClick={this.onSearchClick}>
        Поиск
      </button>
      </section>
        }

        return content
    }
}

export default CharacteristicsSearchSection