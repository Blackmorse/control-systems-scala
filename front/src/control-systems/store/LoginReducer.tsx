import { LoginState, LoginAction } from '../login/Login'

let logoutState: LoginState = {
    login: {
    token: undefined,
    username: undefined
}}

const reducer = (_state: LoginState = logoutState, action: LoginAction): LoginState => {
    switch (action.type) {
        case 'LOGIN':
            return {
                login: action.login
            }
        case 'LOGOUT':
            return logoutState
    }
}

export default reducer
