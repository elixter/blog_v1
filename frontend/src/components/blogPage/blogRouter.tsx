import { Switch, Route } from 'react-router-dom';
import PostPage from '../../pages/post/postPage';
import BlogPage from '../../pages/blogPage';
import { DynamicPath, QueryPath, StaticPath } from '../pagePath/pagePath';
import NewPostPage from '../../pages/post/newPostPage';
import PostListMain from './postListPage';
import PostListPage from '../../pages/post/postListPage';

const BlogRouter = function () {
	return (
		<Switch>
			<Route exact path={StaticPath.BLOG} component={BlogPage} />
			<Route path={StaticPath.BLOG_NEW_POST} component={NewPostPage} />
			<Route path={DynamicPath.BLOG_POST_DETAIL} component={PostPage} />
			<Route path={QueryPath.BLOG_POST_LIST} component={PostListPage} />
			{/* <Route path="/posts" component={IndexPage} /> */}
		</Switch>
	);
};

export default BlogRouter;
