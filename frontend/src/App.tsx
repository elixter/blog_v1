import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import './static/css/common.css';
import { StaticPath } from './components/pagePath/pagePath';
import IndexPage from './pages/indexPage';
import BlogRouter from './components/blogPage/blogRouter';
import SignupPage from './pages/signupPage';

const App = function () {
	return (
		<BrowserRouter>
			<Switch>
				<Route exact path={StaticPath.MAIN} component={IndexPage} />
				<Route path={StaticPath.SIGNUP} component={SignupPage} />
				<Route path={StaticPath.BLOG} component={BlogRouter} />
			</Switch>
		</BrowserRouter>
	);
};

export default App;
