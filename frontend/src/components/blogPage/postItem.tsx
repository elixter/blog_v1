import { memo } from 'react';
import { Link } from 'react-router-dom';
import { Post } from '../api/post/types';

type Props = {
	post: Post;
};

const PostItem = function ({ post }: Props) {
	const regex = /(<([^>]+)>)/gi;
	const content = post.content.replace(regex, '');

	return (
		<div className="post-list-item">
			<Link to={`blog/posts/${post.id}`}>
				<img alt="thumbnail" src={post.thumbnail} />
			</Link>
			<div className="tit">
				<Link to={`/blog/posts/${post.id}`}>
					<h2>{post.title}</h2>
				</Link>
			</div>
			<div className="post-summary">
				<span>{content.substring(0, Math.min(50, post.content.length))}</span>
			</div>
			<div className="post-item-bt">
				<div className="hashtags">
					{post.hashtags.map((hashtag, i) => {
						return (
							<Link
								key={hashtag + post.id}
								className="hashtag"
								to={`/blog/posts?hashtag=${hashtag}`}
							>{`#${hashtag}`}</Link>
						);
					})}
				</div>
				<Link className="post-more" to={`/blog/posts/${post.id}`}>
					Read more
				</Link>
			</div>
		</div>
	);
};

export default memo(PostItem);
