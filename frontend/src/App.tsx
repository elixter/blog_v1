import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import './static/css/common.css';
import { StaticPath } from './components/pagePath/pagePath';
import IndexPage from './pages/indexPage';
import BlogPage from './pages/blogPage';

const App = function () {
	return (
		<BrowserRouter>
			<Switch>
				<Route exact path={StaticPath.MAIN} component={IndexPage} />
				<Route path={StaticPath.MAIN} component={BlogPage} />
			</Switch>
		</BrowserRouter>
	);
};

export default App;
