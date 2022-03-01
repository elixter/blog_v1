import { memo } from 'react';
import { Link } from 'react-router-dom';
import { Post } from '../../api/post/types';
import { timeFormat } from '../../utils';
import HashtagList from '../editor/hashtagList';

type Props = {
	post: Post;
};

const PostContentTop = function ({ post }: Props) {
	return (
		<>
			<div className="blog-top post-top">
				<img alt="bg" className="bg-img" src={post.thumbnail} />
			</div>
			<div>
				<div className="top-content">
					<h1>{post.title}</h1>
					<p>{post.category}</p>
					<p>{timeFormat(post.updateAt)}</p>
					<div className="hashtags">
						<HashtagList hashtags={post.hashtags} />
					</div>
				</div>
			</div>
		</>
	);
};

export default memo(PostContentTop);
