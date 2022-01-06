import {useCallback, useState} from 'react'
import TopPanel from '../panels/TopPanel'
import RightPanel from '../panels/rightPanel'
import { logIn, logOut } from '../store/LoginActions'
import { Dispatch } from "redux"
import { TypedUseSelectorHook, useDispatch, useSelector } from "react-redux"
import {LoginState} from './Login'
import Form from 'react-bootstrap/Form'
import {Button} from 'react-bootstrap'

const Login = () => {
    const dispatch: Dispatch<any> = useDispatch()

    const [errorMessage, setErrorMessage] = useState('')

    const login = useCallback((username: string, password: string) => {
        setErrorMessage('')
        dispatch(logIn(username, password, msg => setErrorMessage(msg)))
    }, [dispatch])

    const logout = useCallback(() => {
        dispatch(logOut())
    }, [dispatch])

    const [[username, password], setCreds] = useState(['', ''])

    var rightPanelProps = {
      links: [{
        title: "Логин",
        onClick: function(){}
      }],
      selected: "Логин"
    }


    return <div className="full_layout" style={{ background: 'rgb(169,169,169)' }}>
        <div className="main_layout">
          <TopPanel pageName="Логин"/>
          <div className="bottom_section">
            <section className="content">
                <Form>
                    <Form.Group className='m-2' controlId={'usernameControl'}>
                        <Form.Label>Логин:</Form.Label>
                        <Form.Control onChange={e => setCreds([e.target.value, password])} type="login" />
                        <Form.Text>{errorMessage}</Form.Text>
                    </Form.Group>
                    <Form.Group className='m-2'>
                        <Form.Label>Пароль:</Form.Label>
                        <Form.Control type="password" onChange={e => setCreds([username, e.target.value])}/>
                    </Form.Group>
                </Form>
                <Button className='btn-secondary m-2' onClick={_e => login(username, password)}> Log In </Button>
                <Button className='btn-secondary m-2' onClick={_e => logout()}> Log Out </Button>
            </section>
            <RightPanel value={rightPanelProps}/>
          </div> 
        </div>
        </div>
}

export default Login
