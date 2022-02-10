import { memo } from 'react';
import { Link } from 'react-router-dom';
import { Post } from '../api/post/types';

type Props = {
	post: Post;
};

const PostItem = function ({ post }: Props) {
	return (
		<div className="post-list-item">
			<img alt="thumbnail" src={post.thumbnail} />
			<div className="tit">
				<h2>Title</h2>
			</div>
			<div className="post-summary">
				<span>{post.content.substring(0, post.content.length % 10)}</span>
			</div>
			<div className="post-item-bt">
				<div className="hashtags">
					{post.hashtags.map((hashtag, i) => {
						return <Link className="hashtag" to="">{`#${hashtag}`}</Link>;
					})}
				</div>
				<Link className="post-more" to="">
					Read more
				</Link>
			</div>
		</div>
	);
};

export default memo(PostItem);
