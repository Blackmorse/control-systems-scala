import { login } from '../client/RestClient'
import { DispathType, LoginAction } from '../login/Login'

export function logIn(username: string, password: string, errorCallback:(msg: string) => void) {
    return (dispatch: DispathType) => login(username, password, token => {
        let action: LoginAction = {
            type: 'LOGIN',
            login: {
                username: username,
                token: token
            }
        }
        dispatch(action)

    }, errorCallback)
}

export function logOut() {
    return (dispatch: DispathType) => {
        let action: LoginAction = {
            type: 'LOGIN',
            login: {username: undefined, token: undefined}
        }
        dispatch(action)
    }
}
