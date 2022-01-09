import React from 'react';
import ReactDOM from 'react-dom';
import './css/index.css';
import {  Route, BrowserRouter as Router } from 'react-router-dom'
import Characteristic from './control-systems/characteristics/characteristics'
import DocumentsPage from './control-systems/documents/DocumentsPage'
import Load from './control-systems/load'
import Parameters from './control-systems/ParametersPage'
import * as serviceWorker from './serviceWorker';
import LoginPage from './control-systems/login/LoginPage'
import { Store, createStore, applyMiddleware } from 'redux'
import thunk from 'redux-thunk'
import { LoginState, LoginAction, DispathType } from './control-systems/login/Login'
import reducer from './control-systems/store/LoginReducer'
import { Provider } from 'react-redux'


const store: Store<LoginState, LoginAction> & { dispatch: DispathType } = 
    createStore(reducer, applyMiddleware(thunk))

const routing = (
  <Router>
      <Route exact path="/" component={LoginPage} />
      <Route exact path="/load" component={Load} />
      <Route exact path="/documents" component={DocumentsPage} />
      <Route exact path="/characteristics" component={Characteristic} />
      <Route exact path="/parameters" component={Parameters} />
      <Route exact path="/login" component={LoginPage} />
  </Router>
)

ReactDOM.render(
  <Provider store={store}>
     {routing}
  </Provider>,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
