import { Switch, Route } from 'react-router-dom';
import PostPage from '../../pages/postPage';
import BlogPage from '../../pages/blogPage';
import { DynamicPath, StaticPath } from '../pagePath/pagePath';
import NewPostPage from '../../pages/newPostPage';

const BlogRouter = function () {
	return (
		<Switch>
			<Route exact path={StaticPath.BLOG} component={BlogPage} />
			<Route path={StaticPath.BLOG_NEW_POST} component={NewPostPage} />
			<Route path={DynamicPath.BLOG_POST_DETAIL} component={PostPage} />
			{/* <Route path="/posts" component={IndexPage} /> */}
		</Switch>
	);
};

export default BlogRouter;
