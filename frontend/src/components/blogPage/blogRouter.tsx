import { Switch, Route } from 'react-router-dom';
import PostPage from '../../pages/postPage';
import BlogPage from '../../pages/blogPage';
import { DynamicPath, StaticPath } from '../pagePath/pagePath';

const BlogRouter = function () {
	return (
		<Switch>
			<Route exact path={StaticPath.BLOG} component={BlogPage} />
			<Route path={DynamicPath.BLOG_POST_DETAIL} component={PostPage} />
			{/* <Route path="/posts" component={IndexPage} /> */}
		</Switch>
	);
};

export default BlogRouter;
