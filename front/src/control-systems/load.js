import React from 'react'
import './load.css'
import TopPanel from './panels/TopPanel'
import RightPanel from './panels/rightPanel'
import {uploadDocument} from './client/RestClient';


class Load extends React.Component {
    constructor(props) {
      super(props)
      this.state = {
        msg: '',
        selectedPage: "Загрузка"
      }
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
     
  }

  onLoad= ()=> {

    uploadDocument(this.state.file, 
        (res) => {
            this.setState({
                file: this.state.file,
                msg: res.msg
            })
        }, 
        () => {
            this.setState({
                file: this.state.file,
                msg: 'Ошибка загрузки'
            })
        })
  }

    render() {
      var rightPanelProps = {
        links: this.rightPanelLinks,
        selected: this.state.selectedPage
      }

      return <div className="full_layout" style={{ background: 'rgb(55,70,132)' }}>
      <div className="main_layout">
        <TopPanel pageName="Загрузка" />
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
          <RightPanel value={rightPanelProps} />
        </div>
      </div>
      </div>
    }
  }
  
export default Load
