import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import logo from './logo.svg';
import './App.css';
import { StaticPath } from './components/pagePath/pagePath';

const test = () => {
	return (
		<div className="App">
			<header className="App-header">
				<img src={logo} className="App-logo" alt="logo" />
				<p>
					Edit <code>src/App.tsx</code> and save to reload.
				</p>
				<a className="App-link" href="https://reactjs.org" target="_blank" rel="noopener noreferrer">
					Test
				</a>
			</header>
		</div>
	);
};

const App = function () {
	return (
		<BrowserRouter>
			<Switch>
				<Route exact path={StaticPath.MAIN} component={test} />
			</Switch>
		</BrowserRouter>
	);
};

export default App;
