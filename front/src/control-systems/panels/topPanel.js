import React from 'react'
import { Link } from 'react-router-dom'

class TopPanel extends React.Component {
    constructor(props) {
        super(props);
        this.state={pageName: props.pageName};
    }

    render() {
        return <aside className="top_panel">
        <span className="page_name">
            {this.state.pageName}
        </span>
        <span className="button_links">
          <img className="button_link_img" src="/button/GroupButton_00_Default_TE.png" alt="Вход"></img>
          <img className="button_link_img" src="/button/GroupButton_01_Default_TE.png" alt="Информация о версиях документов"></img>
          <img className="button_link_img" src="/button/GroupButton_02_Default_TE.png" alt="Сервис"></img>
          <img className="button_link_img" src="/button/GroupButton_03_Default_TE.png" alt=""></img>
          <Link to="/parameters">
            <img className="button_link_img"
             src={this.state.pageName === "Параметры" ? "/button/GroupButton_04_Pressed_TE.png" : "/button/GroupButton_04_Default_TE.png"}
              alt="Параметры"></img>
          </Link>
          <Link to="/characteristics">
            <img className="button_link_img"
             src={this.state.pageName === "Характеристики" ? "/button/GroupButton_05_Pressed_TE.png" : "/button/GroupButton_05_Default_TE.png"}
              alt="Графики/Характеристики"></img>
          </Link>
          <Link to="/documents">
            <img className="button_link_img" 
                src={this.state.pageName === "Документы" ? "/button/GroupButton_06_Pressed_TE.png" : "/button/GroupButton_06_Default_TE.png"}
                alt="Сравнение параметров"></img>
          </Link>
          <img className="button_link_img" src="/button/GroupButton_07_Default_TE.png" alt=""></img>
          <img className="button_link_img" src="/button/GroupButton_08_Default_TE.png" alt=""></img>
          <Link to="/">
            <img className="button_link_img" 
            src={this.state.pageName === "Загрузка" ? "/button/GroupButton_09_Pressed_TE.png" : "/button/GroupButton_09_Default_TE.png"} 
            alt="Загрузка"></img>
          </Link>
        </span>
      </aside>
    }
}

export default TopPanel