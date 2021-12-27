import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import './static/css/common.css';
import { StaticPath } from './components/pagePath/pagePath';
import IndexPage from './pages/indexPage';

const App = function () {
	return (
		<BrowserRouter>
			<Switch>
				<Route exact path={StaticPath.MAIN} component={IndexPage} />
			</Switch>
		</BrowserRouter>
	);
};

export default App;
