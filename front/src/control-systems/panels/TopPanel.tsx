import {TypedUseSelectorHook, useSelector} from 'react-redux'
import { Link } from 'react-router-dom'
import {LoginState} from '../login/Login'

interface Props {
    pageName: string
}

const TopPanel = (props: Props) => {
    const useAppSelector: TypedUseSelectorHook<LoginState> = useSelector

    let stateMapFunc = (state: LoginState) => state?.login

    let loginState = useAppSelector(stateMapFunc)

    let loginInfo: JSX.Element | undefined = undefined
    if (loginState !== undefined) {
        loginInfo = <>Пользователь: {loginState?.username}</>
    }


    return <aside className="d-flex fluid">
        <span className="page_name">
            {props.pageName}
        </span>
        <span className="button_links">
          <span className='me-2'>
             {loginInfo}
          </span>
          <Link to="/login" ><img className="button_link_img" src="/button/GroupButton_00_Default_TE.png" alt="Вход"></img></Link>
          <img className="button_link_img" src="/button/GroupButton_01_Default_TE.png" alt="Информация о версиях документов"></img>
          <img className="button_link_img" src="/button/GroupButton_02_Default_TE.png" alt="Сервис"></img>
          <img className="button_link_img" src="/button/GroupButton_03_Default_TE.png" alt=""></img>
          <Link to="/parameters">
            <img className="button_link_img"
             src={props.pageName === "Параметры" ? "/button/GroupButton_04_Pressed_TE.png" : "/button/GroupButton_04_Default_TE.png"}
              alt="Параметры"></img>
          </Link>
          <Link to="/characteristics">
            <img className="button_link_img"
             src={props.pageName === "Характеристики" ? "/button/GroupButton_05_Pressed_TE.png" : "/button/GroupButton_05_Default_TE.png"}
              alt="Графики/Характеристики"></img>
          </Link>
          <Link to="/documents">
            <img className="button_link_img" 
                src={props.pageName === "Документы" ? "/button/GroupButton_06_Pressed_TE.png" : "/button/GroupButton_06_Default_TE.png"}
                alt="Сравнение параметров"></img>
          </Link>
          <img className="button_link_img" src="/button/GroupButton_07_Default_TE.png" alt=""></img>
          <img className="button_link_img" src="/button/GroupButton_08_Default_TE.png" alt=""></img>
          <Link to="/load">
            <img className="button_link_img" 
            src={props.pageName === "Загрузка" ? "/button/GroupButton_09_Pressed_TE.png" : "/button/GroupButton_09_Default_TE.png"} 
            alt="Загрузка"></img>
          </Link>
        </span>
      </aside>
}

export default TopPanel
