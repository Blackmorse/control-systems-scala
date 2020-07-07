import React from 'react'
import axios from 'axios';
import './load.css'
import TopPanel from './panels/topPanel'
import RightPanel from './panels/rightPanel'


class Load extends React.Component {
    constructor(props) {
      super(props)
      this.state = {msg: ''}
      this.rightPanelLinks = [
      {
        title: "Загрузка",
        onClick: this.rightPanelLoadOnClick
      }
     ]
    }

    onChangeHandler=event=>{
      this.setState({file: event.target.files[0]})      
  }

  rightPanelLoadOnClick = () => {
    console.log('Нажали Загрузку')    
  }

  onLoad= ()=> {
    const data = new FormData() 
    data.append('file', this.state.file)
    axios.post("http://localhost:9002/upload", data, { // receive two parameter endpoint url ,form data 
      })
      .then(res => { // then print response status
        this.setState({file: this.state.file,
          msg: res.data.msg})
      })
      .catch(error => {
        this.setState({file: this.state.file,
          msg: 'Ошибка загрузки'})
      })
  }

    render() {
      return <div className="full_layout" style={{ background: 'rgb(130,50,32)' }}>
      <div className="main_layout">
        <TopPanel pageName="Загрузка"/>
        <div className="bottom_section">
          <section className="content">
            <section className="load_section"> 
              <label htmlFor="upload_file" id="upload_file_label">Выбрать файл</label>
              <input id="upload_file" type="file" onChange={this.onChangeHandler}/>
              <label>{this.state.file ? this.state.file.name : ""}</label><br/>
              <button className="load_button"  onClick={this.onLoad}>Загрузить</button><br />
              <label>{this.state.msg}</label>
            </section>
          </section>
          <RightPanel links={this.rightPanelLinks}/>
        </div>
      </div>
      </div>
    }
  }
  
export default Load