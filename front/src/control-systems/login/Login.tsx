interface Login {
    username: string | undefined
    token: string | undefined
}

export type LoginState = {
    login: Login
}

export type LoginAction = {
    type: 'LOGIN' | 'LOGOUT'
    login: Login
}


export type DispathType = (args: LoginAction) => LoginAction

export default Login
