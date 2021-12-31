import { Link } from 'react-router-dom';
import { memo } from 'react';
import PostItem, { Post } from './postItem';

type Props = {
	categoryName: string;
	posts: Post[];
};

const PostList = function ({ categoryName, posts }: Props) {
	return (
		<div className="post-list">
			<div className="post-list-hd">
				<h1>{categoryName}</h1>
				<Link className="more" to={`/blog/${categoryName}`}>
					All Articles
				</Link>
			</div>
			<div className="post-list-items">
				{posts.map((post, i) => {
					return <PostItem post={post} />;
				})}
			</div>
		</div>
	);
};

export default memo(PostList);
