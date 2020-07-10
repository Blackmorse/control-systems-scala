import React from 'react';
import ReactDOM from 'react-dom';
import './css/index.css';
import {  Route, BrowserRouter as Router } from 'react-router-dom'
import Characteristic from './control-systems/characteristics/characteristics'
import Documents from './control-systems/documents/documents'
import Load from './control-systems/load'
import Parameters from './control-systems/parameters'
import * as serviceWorker from './serviceWorker';


const routing = (
  <Router>
      <Route exact path="/" component={Load} />
      <Route exact path="/documents" component={Documents} />
      <Route exact path="/characteristics" component={Characteristic} />
      <Route exact path="/parameters" component={Parameters} />
  </Router>
)

ReactDOM.render(
  routing,
  document.getElementById('root')
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
